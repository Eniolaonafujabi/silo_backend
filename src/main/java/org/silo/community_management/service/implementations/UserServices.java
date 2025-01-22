package org.silo.community_management.service.implementations;

import org.silo.community_management.data.model.InvitedUser;
import org.silo.community_management.data.model.JwtToken;
import org.silo.community_management.data.model.User;
import org.silo.community_management.data.repo.UserRepo;
import org.silo.community_management.dtos.exceptions.ImageVideoException;
import org.silo.community_management.dtos.exceptions.UserException;
import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;
import org.silo.community_management.dtos.util.JwtUtil;
import org.silo.community_management.dtos.util.Mapper;
import org.silo.community_management.service.interfaces.UserInterface;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    private final SubGroupServices subGroupServices;

    public UserServices(UserRepo userRepo, CommunityService communityService, PostServices postServices, CloudinaryService cloudinaryService, PreUserService preUserService, JwtServices jwtServices, JwtUtil jwtUtil, MailServices mailServices, InvitedUserService invitedUserService, SubGroupServices subGroupServices) {
        this.userRepo = userRepo;
        this.communityService = communityService;
        this.postServices = postServices;
        this.cloudinaryService = cloudinaryService;
        this.preUserService = preUserService;
        this.jwtServices = jwtServices;
        this.jwtUtil = jwtUtil;
        this.mailServices = mailServices;
        this.invitedUserService = invitedUserService;
        this.subGroupServices = subGroupServices;
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
        if(preUserService.checkIfEmailIsVerified(request.getEmail())){
                User user = new User();
                ifWasInvitedToJoinACommunity(request, user);
                Mapper.map(user,request);
                userRepo.save(user);
                preUserService.deletePreUser(request.getEmail());
                response.setMessage("Successfully created user");
                return response;
        }else {
            throw new UserException("Email is not verified");
        }
    }

    private void ifWasInvitedToJoinACommunity(CreateAccountRequest request, User user) {
        if (invitedUserService.invitedUserExists(request.getEmail())){
            InvitedUser user1 =  invitedUserService.getInvitedUser(request.getEmail());
            if (user1.getRole().equalsIgnoreCase("member")) user.getListOfCommunityMemberId().add(user1.getCommunityId());
            if (user1.getRole().equalsIgnoreCase("admin")) user.getListOfCommunityMemberId().add(user1.getCommunityId());
            invitedUserService.deleteInvitedUser(request.getEmail());
        }
        ;
    }


    @Override
    public LogInResponse logInAccount(LogInRequest request) throws IOException {
        LogInResponse response = new LogInResponse();
        validateRequestForLogINOverAll(request);
        if (request.getEmail().isEmpty()){
            validateRequestForLogIN(request);
            User user = userRepo.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(()-> new UserException("User does not exit"));
            if (!user.getPassword().equals(request.getPassword())){
                throw new UserException("Wrong password");
            }else {
                JwtToken jwtToken = jwtServices.generateAndSaveToken(user.getId());
                gettingUserInfo(response, user , jwtToken);
                return response;
            }
        }

        if (request.getPhoneNumber().isEmpty()){
            validateRequestForLogIN2(request);
            User user = userRepo.findByEmail(request.getEmail())
                    .orElseThrow(()-> new UserException("User does not exit"));
            if (!user.getPassword().equals(request.getPassword())){
                throw new UserException("Wrong password");
            }else {
                JwtToken jwtToken = jwtServices.generateAndSaveToken(user.getId());
                gettingUserInfo(response, user , jwtToken);
                return response;
            }
        }
        throw new UserException("All fields are required");
    }

    @Override
    public AllUserDataResponse getAllUserInfo(AllUserDataRequest allUserDataRequest) throws IOException {
        String userId = jwtUtil.extractUsername(allUserDataRequest.getToken());
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserException("User not found"));
        AllUserDataResponse response = new AllUserDataResponse();
        Mapper.mapGetAllUserInfo(user,response);
        if (user.getProfilePicture() != null){
            response.setFile(cloudinaryService.fetchImage(user.getProfilePicture()));
        }
        return response;
    }


    @Override
    public CreateCommunityResponse createCommunity(CreateCommunityRequest request) throws IOException {
        User user = userRepo.findById(jwtUtil.extractUsername(request.getToken()))
                .orElseThrow(() -> new UserException("User not found"));
        request.setFounderName(user.getFirstName() + " " + user.getLastName());
        request.setToken(jwtUtil.extractUsername(request.getToken()));
        CreateCommunityResponse createCommunityResponse = communityService.createCommunity(request);
        user.getListOfCommunityManagerId().add(createCommunityResponse.getId());
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
            emailRequest.setSubject("Add Member To Community By " + userAdmin.getFirstName() + " " + userAdmin.getLastName());
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
            user.getListOfCommunityMemberId().add(response.getId());
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
            emailRequest.setSubject("Add Member To Community By " + userAdmin.getFirstName() + " " + userAdmin.getLastName());
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
            user.getListOfCommunityMemberId().add(response.getId());
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
            throw new UserException("Member ship is not valid Can,t view community.");
        }
        return response;
    }

    @Override
    public CreateSubGroupResponse createSubGroup(CreateSubGroupRequest request) {
        if (communityService.validateMemberShipRole(request.getFounderId(), request.getCommunityId()))
            return subGroupServices.createSubGroup(request);
        else {
            throw new UserException("Member ship is not valid Can,t create sub group");
        }
    }

    @Override
    public AddMemberResponse addMemberToSubGroup(AddSubGroupMemberRequest request) {
        if (communityService.validateMemberShipRole(request.getAdminId(), request.getCommunityId()))
            return subGroupServices.addMemberToSubGroup(request);
        else {
            throw new UserException("Member ship is not valid Can,t create sub group");
        }
    }

    @Override
    public AddMemberResponse addAdminToSubGroup(AddSubGroupMemberRequest request) {
        return null;
    }

    @Override
    public GetAllSubGroupResponse getAllSubGroupInACommunity(String communityId) {
        return null;
    }

    @Override
    public boolean validateIfSubGroupNameExistInACommunity(String subGroupName, String communityId) {
        return false;
    }

    private User existByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(()-> new UserException("User does not exit"));
    }

    private User existByPhoneNumber(String phoneNumber) {
        return userRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(()-> new UserException("User does not exit"));
    }

    private void validateRequestForLogINOverAll(LogInRequest request) {
        if ((request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty())
                && (request.getEmail() == null || request.getEmail().isEmpty())
                || request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new UserException("Request fields cannot be null or empty");
        }
    }

    private void validateRequestForLogIN2(LogInRequest request) {
        if (request.getEmail().isEmpty() && request.getPassword().isEmpty()){
            throw new UserException("Request Can,t Be Null");
        }
    }

    private void validateRequestForLogIN(LogInRequest request) {
        if (request.getPhoneNumber().isEmpty() && request.getPassword().isEmpty()) {
            throw new UserException("Request Can,t Be Null");
        }
    }

    private void validateRequest(CreateAccountRequest request) {
        if (request.getFirstName().isEmpty() && request.getLastName().isEmpty() && request.getPhoneNumber().isEmpty() && request.getEmail().isEmpty() && request.getPassword().isEmpty()) {
            throw new UserException("Request Can,t Be Null");
        }
    }

    private void validateRequest2(String phoneNumber, String email) {
        if(userRepo.findByPhoneNumber(phoneNumber).isPresent())throw new UserException("Phone number is already in use.");
        if (userRepo.findByEmail(email).isPresent())throw new UserException("Email is already in use.");
    }

    private void gettingUserInfo(LogInResponse response, User user, JwtToken jwtToken) throws IOException {
            if (user.getProfilePicture()==null){
                Mapper.map(response, user, jwtToken);
            }else {
                byte[] file = cloudinaryService.fetchImage(user.getProfilePicture());
                Mapper.map(response, user, jwtToken);
                response.setFile(file);
            }
    }



}
