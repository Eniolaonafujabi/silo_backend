package org.silo.community_management.service.interfaces;

import org.silo.community_management.data.model.User;
import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;

import java.io.IOException;

public interface CommunityInterface {

    CreateCommunityResponse createCommunity(CreateCommunityRequest request, String founderName) throws IOException;

    EditCommunityResponse editCommunity(EditCommunityRequest request) throws IOException;

    ViewCommunityResponse viewCommunity(ViewCommunityRequest request) throws IOException;

    AddMemberResponse addMemberToCommunity(AddMemberRequest request, User user);

    AddMemberResponse addAdminToCommunity(AddMemberRequest request, User user);

    Boolean validateMemberShip(String id, String communityId);

    Boolean validateMemberShipRole(String id, String communityId);



//    DeleteCommunityResponse DeleteCommunity(DeleteCommunityRequest request) throws IOException;
}
