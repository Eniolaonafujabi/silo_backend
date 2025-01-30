package org.silo.community_management.service.implementations;

import org.jetbrains.annotations.NotNull;
import org.silo.community_management.data.model.Community;
import org.silo.community_management.data.model.User;
import org.silo.community_management.data.repo.CommunityRepo;
import org.silo.community_management.dtos.exceptions.CommunityException;
import org.silo.community_management.dtos.exceptions.ImageVideoException;
import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;
import org.silo.community_management.dtos.util.Mapper;
import org.silo.community_management.service.interfaces.CommunityInterface;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CommunityService implements CommunityInterface {

    private final CloudinaryService cloudinaryService;

    private final CommunityRepo communityRepo;

    public CommunityService(CloudinaryService cloudinaryService, CommunityRepo communityRepo) {
        this.cloudinaryService = cloudinaryService;
        this.communityRepo = communityRepo;
    }

    @Override
    public CreateCommunityResponse createCommunity(CreateCommunityRequest request, String founderName) throws IOException {
        Community community = mapCommunity(request, founderName);
        communityRepo.save(community);
        CreateCommunityResponse response = new CreateCommunityResponse();
        if (!community.getImageVideoUrl().isEmpty()){
            response.setImageVideoUrl(cloudinaryService.fetchImage(community.getImageVideoUrl()));
        }
        Mapper.map(response,community);
        return response;
    }

    @NotNull
    private Community mapCommunity(CreateCommunityRequest request, String founderName) {
        Community community = new Community();
        Mapper.map(community,request, founderName);
        return community;
    }

    private void checkImage(MultipartFile imageVideo) {
        if (imageVideo.isEmpty())throw new ImageVideoException("Invalid Image");
    }

    @Override
    public EditCommunityResponse editCommunity(EditCommunityRequest request) throws IOException {
        Community community = findCommunity(request.getCommunityId());
        Mapper.map(community,request);
        communityRepo.save(community);
        EditCommunityResponse response = new EditCommunityResponse();
        Mapper.map(response,community);
        return response;
    }

    @Override
    public ViewCommunityResponse viewCommunity(ViewCommunityRequest request) throws IOException {
        Community community = findCommunity(request.getCommunityId());
        byte[] imageVideo = cloudinaryService.fetchImage(community.getImageVideoUrl());
        ViewCommunityResponse response = new ViewCommunityResponse();
        Mapper.map(imageVideo,response,community);
        return response;
    }

    @Override
    public AddMemberResponse addMemberToCommunity(AddMemberRequest request, User user) {
        Community community = findCommunity(request.getCommunityId());
        if(community.getMemberId().contains(user.getId()) || community.getAdminId().contains(request.getToken()))throw new CommunityException("Member already exists");
        community.getMemberId().add(user.getId());
        communityRepo.save(community);
        AddMemberResponse response = new AddMemberResponse();
        response.setId(community.getId());
        response.setMessage("Added Successfully");
        return response;
    }

    @Override
    public AddMemberResponse addAdminToCommunity(AddMemberRequest request, User user) {
        AddMemberResponse response = new AddMemberResponse();

        Community community = findCommunity(request.getCommunityId());
        if(community.getMemberId().contains(user.getId())){
            community.getMemberId().remove(user.getId());
            community.getAdminId().add(user.getId());
            response.setMessage("Added Successfully");

        }else {
            if (community.getAdminId().contains(request.getToken()))throw new CommunityException("Admin already exists");
            community.getAdminId().add(user.getId());
            response.setMessage("Added Successfully");
        }
        return response;
    }

    @Override
    public Boolean validateMemberShip(String id, String communityId) {
        Community community = findCommunity(communityId);
        return community.getAdminId().contains(id) || community.getMemberId().contains(id);
    }

    @Override
    public Boolean validateMemberShipRole(String id, String communityId) {
        Community community = findCommunity(communityId);
        return community.getAdminId().contains(id);
    }


    private Community findCommunity(String request) {
        return communityRepo.findById(request)
                .orElseThrow(() -> new CommunityException("Community not found"));
    }
}
