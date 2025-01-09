package org.silo.community_management.service;

import org.silo.community_management.data.model.User;
import org.silo.community_management.data.repo.UserRepo;
import org.silo.community_management.dtos.exceptions.UserException;
import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;
import org.springframework.stereotype.Service;

@Service
public class UserServices implements UserInterface{
    private final UserRepo userRepo;

    private final CommunityService communityService;

    private final PostServices postServices;

    public UserServices(UserRepo userRepo, CommunityService communityService, PostServices postServices) {
        this.userRepo = userRepo;
        this.communityService = communityService;
        this.postServices = postServices;
    }

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        CreateAccountResponse response = new CreateAccountResponse();
        validateRequest(request);
        User user = new User();
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBio(request.getBio());
        return null;
    }

    private void validateRequest(CreateAccountRequest request) {
        if (request.getName() == null || request.getBio().isEmpty() || request.getPhoneNumber().isEmpty() || request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
            throw new UserException("Request Can,t Be Null");
        }
    }

    @Override
    public LogInResponse LogInAccount(LogInRequest request) {
        return null;
    }

    @Override
    public CreateCommunityResponse createCommunity(CreateCommunityRequest request) {
        return null;
    }

    @Override
    public AddPostResponse addPost(AddPostRequest request) {
        return null;
    }

    @Override
    public AddMemberResponse addMember(AddMemberRequest request) {
        return null;
    }

    @Override
    public AddMemberResponse addAdmin(AddMemberRequest request) {
        return null;
    }

    @Override
    public ViewCommunityResponse viewCommunity(ViewCommunityRequest request) {
        return null;
    }
}
