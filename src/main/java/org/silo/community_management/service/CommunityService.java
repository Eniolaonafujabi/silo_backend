package org.silo.community_management.service;

import org.silo.community_management.data.model.Community;
import org.silo.community_management.data.repo.CommunityRepo;
import org.silo.community_management.dtos.exceptions.CommunityException;
import org.silo.community_management.dtos.exceptions.ImageVideoException;
import org.silo.community_management.dtos.request.CreateCommunityRequest;
import org.silo.community_management.dtos.request.EditCommunityRequest;
import org.silo.community_management.dtos.request.ViewCommunityRequest;
import org.silo.community_management.dtos.response.CreateCommunityResponse;
import org.silo.community_management.dtos.response.EditCommunityResponse;
import org.silo.community_management.dtos.response.ViewCommunityResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        String filePublicId = fileResponse.get("public_id").toString();
        Community community = new Community();
        community.setCommunityName(request.getCommunityName());
        community.setCommunityName(request.getCommunityName());
        community.setCommunityDescription(request.getDescription());
        community.setImageVideoUrl(filePublicId);
        communityRepo.save(community);
        CreateCommunityResponse response = new CreateCommunityResponse();
        response.setCommunityName(community.getCommunityName());
        response.setCommunityDescription(community.getCommunityDescription());
        response.setId(community.getId());
        response.setMessage("Created Successfully");
        return response;
    }

    private void checkImage(MultipartFile imageVideo) {
        if (imageVideo.isEmpty())throw new ImageVideoException("Invalid Image");
    }

    @Override
    public EditCommunityResponse editCommunity(EditCommunityRequest request) throws IOException {
        Community community = communityRepo.findById(request.getCommunityId())
                .orElseThrow(() -> new CommunityException("Community not found"));
        checkImage(request.getImageVideo());
        Map<String, Object> fileResponse = cloudinaryService.editFile(community.getImageVideoUrl(), request.getImageVideo());
        String filePublicId = fileResponse.get("public_id").toString();
        community.setImageVideoUrl(filePublicId);
        community.setCommunityName(request.getCommunityName());
        community.setCommunityDescription(request.getDescription());
        communityRepo.save(community);
        EditCommunityResponse response = new EditCommunityResponse();
        response.setCommunityName(community.getCommunityName());
        response.setCommunityDescription(community.getCommunityDescription());
        response.setId(community.getId());
        response.setMessage("Edited Successfully");
        return response;
    }

    @Override
    public ViewCommunityResponse viewCommunity(ViewCommunityRequest request) throws IOException {
        Community community = communityRepo.findById(request.getCommunityId())
                .orElseThrow(() -> new CommunityException("Community not found"));
        byte[] imageVideo = cloudinaryService.fetchImage(cloudinaryService.getImageUrl(community.getImageVideoUrl()));
        ViewCommunityResponse response = new ViewCommunityResponse();
        response.setCommunityName(community.getCommunityName());
        response.setCommunityDescription(community.getCommunityDescription());
        response.setId(community.getId());
        response.setImageVideoUrl(imageVideo);
        response.setMessage("View Community");
        return response;
    }
}
