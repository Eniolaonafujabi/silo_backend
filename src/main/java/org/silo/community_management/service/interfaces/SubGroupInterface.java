package org.silo.community_management.service.interfaces;

import org.silo.community_management.dtos.request.AddSubGroupMemberRequest;
import org.silo.community_management.dtos.request.CreateSubGroupRequest;
import org.silo.community_management.dtos.response.AddMemberResponse;
import org.silo.community_management.dtos.response.CreateSubGroupResponse;
import org.silo.community_management.dtos.response.GetAllSubGroupResponse;

public interface SubGroupInterface {

    CreateSubGroupResponse createSubGroup(CreateSubGroupRequest request);

    AddMemberResponse addMemberToSubGroup(AddSubGroupMemberRequest request);

    AddMemberResponse addAdminToSubGroup(AddSubGroupMemberRequest request);

    GetAllSubGroupResponse getAllSubGroupInACommunity(String communityId);

    boolean validateIfSubGroupNameExistInACommunity(String subGroupName, String communityId);

}
