package org.silo.community_management.service;

import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;

import java.io.IOException;

public interface UserInterface {

    String sendOtp(String email) throws IOException;

    boolean verifyOtp(String email, String otp);

    CreateAccountResponse createAccount(CreateAccountRequest request) throws IOException;

    LogInResponse LogInAccount(LogInRequest request) throws IOException;

    CreateCommunityResponse createCommunity(CreateCommunityRequest request) throws IOException;

    AddPostResponse addPost(AddPostRequest request) throws IOException;

    AddMemberResponse addMember(AddMemberRequest request);

    AddMemberResponse addAdmin(AddMemberRequest request);

    ViewCommunityResponse viewCommunity(ViewCommunityRequest request) throws IOException;

}
