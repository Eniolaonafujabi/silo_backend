package org.silo.community_management.service;

import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;

public interface UserInterface {

    CreateAccountResponse createAccount(CreateAccountRequest request);

    LogInResponse LogInAccount(LogInRequest request);

    CreateCommunityResponse createCommunity(CreateCommunityRequest request);

    AddPostResponse addPost(AddPostRequest request);

    AddMemberResponse addMember(AddMemberRequest request);

    AddMemberResponse addAdmin(AddMemberRequest request);

    ViewCommunityResponse viewCommunity(ViewCommunityRequest request);

}
