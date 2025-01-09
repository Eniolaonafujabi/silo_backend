package org.silo.community_management.service;

import org.silo.community_management.data.model.Post;
import org.silo.community_management.data.repo.PostRepo;
import org.silo.community_management.dtos.request.AddPostRequest;
import org.silo.community_management.dtos.response.AddPostResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class PostServices implements PostInterface{

    private final PostRepo postRepo;

    private final CommunityService communityService;

    private final CloudinaryService cloudinaryService;

    public PostServices(PostRepo postRepo, CommunityService communityService, CloudinaryService cloudinaryService) {
        this.postRepo = postRepo;
        this.communityService = communityService;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public AddPostResponse addPost(AddPostRequest request) throws IOException {
        AddPostResponse response = new AddPostResponse();
        if (communityService.validateMemberShip(request.getMemberId(),request.getCommunityId())){
            Post post = new Post();
            if(request.getFile().isEmpty()){
                post.setTitle(request.getTitle());
                post.setContent(request.getContent());
                post.setCommunityId(request.getCommunityId());
                post.setMemberId(request.getMemberId());
                postRepo.save(post);
                response.setMessage("Successfully added post");
            }else {
                Map<String, Object> fileResponse = cloudinaryService.uploadImage(request.getFile());
                String filePublicId = fileResponse.get("public_id").toString();
                post.setTitle(request.getTitle());
                post.setContent(request.getContent());
                post.setCommunityId(request.getCommunityId());
                post.setMemberId(request.getMemberId());
                post.setFile(filePublicId);
                postRepo.save(post);
                response.setMessage("Successfully added post");
            }
        }
        return response;
    }
}
