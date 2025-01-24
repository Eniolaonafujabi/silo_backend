package org.silo.community_management.service.implementations;

import org.silo.community_management.data.model.SubGroup;
import org.silo.community_management.data.repo.SubGroupRepo;
import org.silo.community_management.dtos.exceptions.SubGroupException;
import org.silo.community_management.dtos.request.AddSubGroupMemberRequest;
import org.silo.community_management.dtos.request.CreateSubGroupRequest;
import org.silo.community_management.dtos.response.AddMemberResponse;
import org.silo.community_management.dtos.response.CreateSubGroupResponse;
import org.silo.community_management.dtos.response.GetAllSubGroupResponse;
import org.silo.community_management.dtos.util.Mapper;
import org.silo.community_management.service.interfaces.SubGroupInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class SubGroupServices implements SubGroupInterface {

    private final SubGroupRepo subGroupRepo;

    public SubGroupServices(SubGroupRepo subGroupRepo) {
        this.subGroupRepo = subGroupRepo;
    }

    @Override
    public CreateSubGroupResponse createSubGroup(CreateSubGroupRequest request) {
        if (validateIfSubGroupNameExistInACommunity(request.getName(),request.getCommunityId()))throw new SubGroupException("Name already in existence in community");
        SubGroup subGroup = new SubGroup();
        Mapper.map(subGroup,request);
        subGroupRepo.save(subGroup);
        CreateSubGroupResponse response = new CreateSubGroupResponse();
        Mapper.map(response,subGroup);
        return response;
    }

    @Override
    public AddMemberResponse addMemberToSubGroup(AddSubGroupMemberRequest request) {
        SubGroup subGroup = subGroupRepo.findById(request.getGroupId())
                .orElseThrow(() -> new SubGroupException("Group not found"));
        subGroup.getMemberId().add(request.getMemberId());
        AddMemberResponse response = new AddMemberResponse();
        response.setMessage("Added Successfully");
        return response;
    }

    @Override
    public AddMemberResponse addAdminToSubGroup(AddSubGroupMemberRequest request) {
        SubGroup subGroup = subGroupRepo.findById(request.getGroupId())
                .orElseThrow(() -> new SubGroupException("Group not found"));
        subGroup.getAdminId().add(request.getMemberId());
        AddMemberResponse response = new AddMemberResponse();
        response.setMessage("Added Successfully");
        return response;
    }

    @Override
    public GetAllSubGroupResponse getAllSubGroupInACommunity(String communityId) {
        ArrayList<SubGroup> allSubGroupOfACommunity = subGroupRepo.findByCommunityId(communityId);
        GetAllSubGroupResponse response = new GetAllSubGroupResponse();
        response.setSubGroups(allSubGroupOfACommunity);
        return response;
    }

    @Override
    public boolean validateIfSubGroupNameExistInACommunity(String subGroupName, String communityId) {
        return (subGroupRepo.existsByNameAndCommunityId(subGroupName,communityId));
    }

    @Override
    public boolean checkIfMemberExist(String memberId, String communityId) {
        SubGroup subGroup = subGroupRepo.findById(communityId)
                .orElseThrow(()-> new SubGroupException(""));
        return subGroup.getMemberId().contains(memberId) || subGroup.getAdminId().contains(memberId);
    }
}
