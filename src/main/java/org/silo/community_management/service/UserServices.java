package org.silo.community_management.service;

import org.silo.community_management.data.model.JwtToken;
import org.silo.community_management.data.model.User;
import org.silo.community_management.data.repo.UserRepo;
import org.silo.community_management.dtos.exceptions.UserException;
import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class UserServices implements UserInterface {
    private final UserRepo userRepo;

    private final CommunityService communityService;

    private final PostServices postServices;

    private final CloudinaryService cloudinaryService;

    private final PreUserService preUserService;

    private final JwtServices jwtServices;

    public UserServices(UserRepo userRepo, CommunityService communityService, PostServices postServices, CloudinaryService cloudinaryService, PreUserService preUserService, JwtServices jwtServices) {
        this.userRepo = userRepo;
        this.communityService = communityService;
        this.postServices = postServices;
        this.cloudinaryService = cloudinaryService;
        this.preUserService = preUserService;
        this.jwtServices = jwtServices;
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
        CreateAccountResponse response = new CreateAccountResponse();
        validateRequest(request);
        validateRequest2(request.getPhoneNumber(),request.getEmail());
        if(preUserService.checkIfAccountIsVerified(request.getEmail())){
            if (request.getFile()==null){
                User user = new User();
                user.setName(request.getName());
                user.setPassword(request.getPassword());
                user.setEmail(request.getEmail());
                user.setPhoneNumber(request.getPhoneNumber());
                user.setBio(request.getBio());
                userRepo.save(user);
                response.setMessage("Successfully created user");
            }else {
                Map<String, Object> fileResponse = cloudinaryService.uploadImage(request.getFile());
                String filePublicId = fileResponse.get("public_id").toString();
                User user = new User();
                user.setName(request.getName());
                user.setPassword(request.getPassword());
                user.setEmail(request.getEmail());
                user.setPhoneNumber(request.getPhoneNumber());
                user.setBio(request.getBio());
                user.setImageVideo(filePublicId);
                userRepo.save(user);
                response.setMessage("Successfully created user");
            }
            return response;
        }else {
            throw new UserException("Email is not verified");
        }
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
        userRepo.findById(request.getFounderId())
                .orElseThrow(() -> new UserException("User not found"));
        User user = userRepo.findUserById(request.getFounderId());
        request.setFounderName(user.getName());
        return communityService.createCommunity(request);
    }

    @Override
    public AddPostResponse addPost(AddPostRequest request) throws IOException {
        AddPostResponse response = new AddPostResponse();
        if(communityService.validateMemberShip(request.getMemberId(),request.getCommunityId())){
            response = postServices.addPost(request);
        }
        return response;
    }

    @Override
    public AddMemberResponse addMember(AddMemberRequest request) {
        AddMemberResponse response =  new AddMemberResponse();
        if(communityService.validateMemberShipRole(request.getMemberId(), request.getCommunityId())){
            response = communityService.addMemberToCommunity(request);
        }else {
            throw new UserException("Member ship is not valid Can,t Add member");
        }
        return response;
    }

    @Override
    public AddMemberResponse addAdmin(AddMemberRequest request) {
        AddMemberResponse response =  new AddMemberResponse();
        if(communityService.validateMemberShipRole(request.getMemberId(), request.getCommunityId())){
            response = communityService.addAdminToCommunity(request);
        }else {
            throw new UserException("Member ship is not valid Can,t Add member");
        }
        return response;
    }

    @Override
    public ViewCommunityResponse viewCommunity(ViewCommunityRequest request) throws IOException {
        ViewCommunityResponse response =  new ViewCommunityResponse();
        if(communityService.validateMemberShipRole(request.getMemberId(), request.getCommunityId())){
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
        if (request.getPhoneNumber().isEmpty() || request.getPassword().isEmpty()){
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
            if (user.getImageVideo().isEmpty()){
                response.setBio(user.getBio());
                response.setToken(jwtToken.getToken());
                response.setEmail(user.getEmail());
                response.setName(user.getName());
                response.setPhoneNumber(user.getPhoneNumber());
            }else {
                byte[] file = cloudinaryService.fetchImage(user.getImageVideo());
                response.setBio(user.getBio());
                response.setToken(jwtToken.getToken());
                response.setEmail(user.getEmail());
                response.setName(user.getName());
                response.setPhoneNumber(user.getPhoneNumber());
                response.setFile(file);
            }
        }
    }



}
