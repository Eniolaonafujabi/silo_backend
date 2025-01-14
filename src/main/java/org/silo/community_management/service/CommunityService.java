package org.silo.community_management.service;

import org.jetbrains.annotations.NotNull;
import org.silo.community_management.data.model.Community;
import org.silo.community_management.data.model.User;
import org.silo.community_management.data.repo.CommunityRepo;
import org.silo.community_management.dtos.exceptions.CommunityException;
import org.silo.community_management.dtos.exceptions.ImageVideoException;
import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;
import org.silo.community_management.dtos.util.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Service
public class CommunityService implements CommunityInterface {

    private final CloudinaryService cloudinaryService;

    private final CommunityRepo communityRepo;

    public CommunityService(CloudinaryService cloudinaryService, CommunityRepo communityRepo) {
        this.cloudinaryService = cloudinaryService;
        this.communityRepo = communityRepo;
    }

    @Override
    public CreateCommunityResponse createCommunity(CreateCommunityRequest request) throws IOException {
        checkImage(request.getImageVideo());
        Map<String, Object> fileResponse = cloudinaryService.uploadImage(request.getImageVideo());
//        String filePublicId = fileResponse.get("public_id").toString();
        String filePublicId = fileResponse.get("url").toString();
        Community community = mapCommunity(request, filePublicId);
        communityRepo.save(community);
        CreateCommunityResponse response = new CreateCommunityResponse();
        Mapper.map(response,community);
        return response;
    }

    @NotNull
    private Community mapCommunity(CreateCommunityRequest request, String filePublicId) {
        Community community = new Community();
        Mapper.map(community,request,filePublicId);
        return community;
    }

    private void checkImage(MultipartFile imageVideo) {
        if (imageVideo.isEmpty())throw new ImageVideoException("Invalid Image");
    }

    @Override
    public EditCommunityResponse editCommunity(EditCommunityRequest request) throws IOException {
        Community community = findCommunity(request.getCommunityId());
        checkImage(request.getImageVideo());
        Map<String, Object> fileResponse = cloudinaryService.editFile(community.getImageVideoUrl(), request.getImageVideo());
//        String filePublicId = fileResponse.get("public_id").toString();
        String filePublicId = fileResponse.get("url").toString();
        Mapper.map(filePublicId,community,request);
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
