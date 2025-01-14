package org.silo.community_management.service;

import org.silo.community_management.data.model.InvitedUser;
import org.silo.community_management.data.model.JwtToken;
import org.silo.community_management.data.model.User;
import org.silo.community_management.data.repo.UserRepo;
import org.silo.community_management.dtos.exceptions.ImageVideoException;
import org.silo.community_management.dtos.exceptions.UserException;
import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;
import org.silo.community_management.dtos.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Service
public class UserServices implements UserInterface {
    private final UserRepo userRepo;

    private final CommunityService communityService;

    private final PostServices postServices;

    private final CloudinaryService cloudinaryService;

    private final PreUserService preUserService;

    private final JwtServices jwtServices;

    private final JwtUtil jwtUtil;

    private final MailServices mailServices;

    private final InvitedUserService invitedUserService;

    public UserServices(UserRepo userRepo, CommunityService communityService, PostServices postServices, CloudinaryService cloudinaryService, PreUserService preUserService, JwtServices jwtServices, JwtUtil jwtUtil, MailServices mailServices, InvitedUserService invitedUserService) {
        this.userRepo = userRepo;
        this.communityService = communityService;
        this.postServices = postServices;
        this.cloudinaryService = cloudinaryService;
        this.preUserService = preUserService;
        this.jwtServices = jwtServices;
        this.jwtUtil = jwtUtil;
        this.mailServices = mailServices;
        this.invitedUserService = invitedUserService;
    }

    @Override
    public String sendOtp(String email) throws IOException {
        return preUserService.preSignup(email);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        return preUserService.verifyOtp(email, otp);
    }

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest request) throws IOException {
        String contentType = request.getFile().getContentType();
        if (contentType != null && contentType.startsWith("image/"))throw new ImageVideoException("Invalid file type. Only images are supported.");
        CreateAccountResponse response = new CreateAccountResponse();
        validateRequest(request);
        validateRequest2(request.getPhoneNumber(),request.getEmail());
        if(preUserService.checkIfAccountIsVerified(request.getEmail())){
            if (request.getFile()==null){
                User user = new User();
                ifWasInvitedToJoinACommunity(request, user);
                user.setName(request.getName());
                user.setPassword(request.getPassword());
                user.setEmail(request.getEmail());
                user.setPhoneNumber(request.getPhoneNumber());
                user.setBio(request.getBio());
                userRepo.save(user);
            }else {
                Map<String, Object> fileResponse = cloudinaryService.uploadImage(request.getFile());
//                String filePublicId = fileResponse.get("public_id").toString();
                String filePublicId = fileResponse.get("url").toString();
                User user = new User();
                ifWasInvitedToJoinACommunity(request, user);
                user.setName(request.getName());
                user.setPassword(request.getPassword());
                user.setEmail(request.getEmail());
                user.setPhoneNumber(request.getPhoneNumber());
                user.setBio(request.getBio());
                user.setImageVideo(filePublicId);
                userRepo.save(user);
            }
            response.setMessage("Successfully created user");
            return response;
        }else {
            throw new UserException("Email is not verified");
        }
    }

    private void ifWasInvitedToJoinACommunity(CreateAccountRequest request, User user) {
        if (invitedUserService.invitedUserExists(request.getEmail())){
            InvitedUser user1 =  invitedUserService.getInvitedUser(request.getEmail());
            if (user1.getRole().equalsIgnoreCase("member")) user.getCommunityMemberId().add(user1.getCommunityId());
            if (user1.getRole().equalsIgnoreCase("admin")) user.getCommunityMemberId().add(user1.getCommunityId());
            invitedUserService.deleteInvitedUser(request.getEmail());
        }
        ;
    }


    @Override
    public LogInResponse LogInAccount(LogInRequest request) throws IOException {
        LogInResponse response = new LogInResponse();
        validateRequestForLogINOverAll(request);
        if (request.getEmail().isEmpty()){
            validateRequestForLogIN(request);
            User user = existByPhoneNumber(request.getPhoneNumber());
            JwtToken jwtToken = jwtServices.generateAndSaveToken(user.getId());
            gettingUserInfo(request, response, user , jwtToken);
        }

        if (request.getPhoneNumber().isEmpty()){
            validateRequestForLogIN2(request);
            User user = existByEmail(request.getEmail());
            JwtToken jwtToken = jwtServices.generateAndSaveToken(user.getId());
            gettingUserInfo(request, response, user , jwtToken);
        }
        return response;
    }


    @Override
    public CreateCommunityResponse createCommunity(CreateCommunityRequest request) throws IOException {
        String contentType = request.getImageVideo().getContentType();
        if (contentType != null && contentType.startsWith("image/"))throw new ImageVideoException("Invalid file type. Only images are supported.");
        User user = userRepo.findById(jwtUtil.extractUsername(request.getToken()))
                .orElseThrow(() -> new UserException("User not found"));
        request.setFounderName(user.getName());
        request.setToken(jwtUtil.extractUsername(request.getToken()));
        CreateCommunityResponse createCommunityResponse = communityService.createCommunity(request);
        ArrayList<String> userCommunity = user.getCommunityManagerId();
        userCommunity.add(createCommunityResponse.getId());
        user.setCommunityManagerId(userCommunity);
        return createCommunityResponse;
    }

    @Override
    public AddPostResponse addPost(AddPostRequest request) throws IOException {
        String contentType = request.getFile().getContentType();
        if (contentType != null && contentType.startsWith("image/") || contentType.startsWith("video/"))throw new ImageVideoException("Invalid file type. Only images and video are supported.");
        AddPostResponse response = new AddPostResponse();
        String extractedtedToken = jwtUtil.extractUsername(request.getToken());
        if(communityService.validateMemberShip(extractedtedToken, request.getCommunityId())){
            request.setToken(extractedtedToken);
            response = postServices.addPost(request);
        }
        return response;
    }

    @Override
    public AddMemberResponse addMember(AddMemberRequest request) throws IOException {
        AddMemberResponse response =  new AddMemberResponse();
        User userAdmin = userRepo.findById(jwtUtil.extractUsername(request.getToken()))
                .orElseThrow(() -> new UserException("User not found"));
        request.setToken(jwtUtil.extractUsername(request.getToken()));
        if(communityService.validateMemberShipRole(request.getToken(), request.getCommunityId())){
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setToEmail(request.getEmail());
            emailRequest.setSubject("Add Member To Community By " + userAdmin.getName());
            emailRequest.setBody("You are required to download Silo application to be added has member of ");
            User user = userRepo.findByEmail(request.getEmail())
                    .orElseGet(()-> {
                        try {
                            mailServices.sendEmail(emailRequest);
                            invitedUserService.saveInvitedUser(request.getEmail(),request.getCommunityId(), "member");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    });
            if (user == null){
                response.setMessage("Email has been sent");
                return response;
            }
            response = communityService.addMemberToCommunity(request,user);
            user.getCommunityMemberId().add(response.getId());
            userRepo.save(user);
        }else {
            throw new UserException("Member ship is not valid Can,t Add member");
        }
        return response;
    }

    @Override
    public AddMemberResponse addAdmin(AddMemberRequest request) {
        AddMemberResponse response =  new AddMemberResponse();
        User userAdmin = userRepo.findById(jwtUtil.extractUsername(request.getToken()))
                .orElseThrow(() -> new UserException("User not found"));
        if(communityService.validateMemberShipRole(jwtUtil.extractUsername(request.getToken()), request.getCommunityId())){
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setToEmail(request.getEmail());
            emailRequest.setSubject("Add Member To Community By " + userAdmin.getName());
            emailRequest.setBody("You are required to download Silo application to be added has member of ");
            User user = userRepo.findByEmail(request.getEmail())
                    .orElseGet(()-> {
                        try {
                            mailServices.sendEmail(emailRequest);
                            invitedUserService.saveInvitedUser(request.getEmail(),request.getCommunityId(), "admin");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    });
            if (user == null){
                response.setMessage("Email has been sent");
                return response;
            }
            response = communityService.addAdminToCommunity(request,user);
            user.getCommunityMemberId().add(response.getId());
            userRepo.save(user);
        }else {
            throw new UserException("Member ship is not valid Can,t Add member");
        }
        return response;
    }

    @Override
    public ViewCommunityResponse viewCommunity(ViewCommunityRequest request) throws IOException {
        ViewCommunityResponse response =  new ViewCommunityResponse();
        if(communityService.validateMemberShip(jwtUtil.extractUsername(request.getToken()), request.getCommunityId())){
            response = communityService.viewCommunity(request);
        }else {
            throw new UserException("Member ship is not valid Can,t view community");
        }
        return response;
    }

    private User existByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    private User existByPhoneNumber(String phoneNumber) {
        return userRepo.findUserByPhoneNumber(phoneNumber);
    }

    private void validateRequestForLogINOverAll(LogInRequest request) {
        if ((request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty())
                && (request.getEmail() == null || request.getEmail().isEmpty())
                || request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new UserException("Request fields cannot be null or empty");
        }
    }

    private void validateRequestForLogIN2(LogInRequest request) {
        if (request.getEmail().isEmpty() || request.getPassword().isEmpty()){
            throw new UserException("Request Can,t Be Null");
        }
    }

    private void validateRequestForLogIN(LogInRequest request) {
        if (request.getPhoneNumber().isEmpty() || request.getPassword().isEmpty()) {
            throw new UserException("Request Can,t Be Null");
        }
    }

    private void validateRequest(CreateAccountRequest request) {
        if (request.getName() == null || request.getBio().isEmpty() || request.getPhoneNumber().isEmpty() || request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
            throw new UserException("Request Can,t Be Null");
        }
    }

    private void validateRequest2(String phoneNumber, String email) {
        if(userRepo.findByPhoneNumber(phoneNumber).isPresent())throw new UserException("Phone number is already in use");
        if (userRepo.findByEmail(email).isPresent())throw new UserException("Email is already in use");
    }

    private void gettingUserInfo(LogInRequest request, LogInResponse response, User user, JwtToken jwtToken) throws IOException {
        if (user.getPassword().equals(request.getPassword())){
            if (user.getImageVideo()==null){
                response.setBio(user.getBio());
                response.setToken(jwtToken.getToken());
                response.setEmail(user.getEmail());
                response.setName(user.getName());
                response.setPassword(user.getPassword());
                response.setPhoneNumber(user.getPhoneNumber());
            }else {
                byte[] file = cloudinaryService.fetchImage(user.getImageVideo());
                response.setBio(user.getBio());
                response.setToken(jwtToken.getToken());
                response.setEmail(user.getEmail());
                response.setName(user.getName());
                response.setPhoneNumber(user.getPhoneNumber());
                response.setPassword(user.getPassword());
                response.setFile(file);
            }
        }
    }



}
