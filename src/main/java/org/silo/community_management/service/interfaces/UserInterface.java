package org.silo.community_management.service.interfaces;

import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;

import java.io.IOException;

public interface UserInterface {

    String sendOtp(String email) throws IOException;

    boolean verifyOtp(String email, String otp);

    CreateAccountResponse createAccount(CreateAccountRequest request) throws IOException;

    LogInResponse logInAccount(LogInRequest request) throws IOException;

    AllUserDataResponse getAllUserInfo(AllUserDataRequest allUserDataRequest) throws IOException;

    CreateCommunityResponse createCommunity(CreateCommunityRequest request) throws IOException;

    AddPostResponse addPost(AddPostRequest request) throws IOException;

    AddMemberResponse addMember(AddMemberRequest request) throws IOException;

    AddMemberResponse addAdmin(AddMemberRequest request);

    ViewCommunityResponse viewCommunity(ViewCommunityRequest request) throws IOException;

    CreateSubGroupResponse createSubGroup(CreateSubGroupRequest request);

    AddMemberResponse addMemberToSubGroup(AddSubGroupMemberRequest request);

    AddMemberResponse addAdminToSubGroup(AddSubGroupMemberRequest request);

    GetAllSubGroupResponse getAllSubGroupInACommunity(String communityId, String token);

}
