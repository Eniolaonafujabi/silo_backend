package org.silo.community_management.service;

import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;

import java.io.IOException;

public interface CommunityInterface {

    CreateCommunityResponse createCommunity(CreateCommunityRequest request) throws IOException;

    EditCommunityResponse editCommunity(EditCommunityRequest request) throws IOException;

    ViewCommunityResponse viewCommunity(ViewCommunityRequest request) throws IOException;

    AddMemberResponse addMemberToCommunity(AddMemberRequest request);

    AddMemberResponse addAdminToCommunity(AddMemberRequest request);

    Boolean validateMemberShip(String id, String communityId);

    Boolean validateMemberShipRole(String id, String communityId);

//    DeleteCommunityResponse DeleteCommunity(DeleteCommunityRequest request) throws IOException;
}
