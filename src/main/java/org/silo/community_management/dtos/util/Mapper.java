package org.silo.community_management.dtos.util;

import org.silo.community_management.data.model.Community;
import org.silo.community_management.data.model.JwtToken;
import org.silo.community_management.data.model.Post;
import org.silo.community_management.data.model.User;
import org.silo.community_management.dtos.request.AddPostRequest;
import org.silo.community_management.dtos.request.CreateAccountRequest;
import org.silo.community_management.dtos.request.CreateCommunityRequest;
import org.silo.community_management.dtos.request.EditCommunityRequest;
import org.silo.community_management.dtos.response.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Mapper {
    public static void mapGetAllUserInfo(User user, AllUserDataResponse response) {
        response.setBio(user.getBio());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setNoOfCommunityMember(user.getListOfCommunityMemberId().size());
        response.setNoOfCommunityAdmin(user.getListOfCommunityManagerId().size());
    }

    public static void map(User user, CreateAccountRequest request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
    }

    public static void map(LogInResponse response, User user, JwtToken jwtToken) {
        response.setBio(user.getBio());
        response.setToken(jwtToken.getToken());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPassword(user.getPassword());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setNoOfCommunityAdmin(user.getListOfCommunityManagerId().size());
        response.setNoOfCommunityMember(user.getListOfCommunityManagerId().size());
    }

    public static void map(Post post, AddPostRequest request) {
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCommunityId(request.getCommunityId());
        post.setMemberId(request.getToken());
    }

    public static void map(CreateCommunityResponse response, Community community) {
        response.setCommunityName(community.getCommunityName());
        response.setCommunityDescription(community.getCommunityDescription());
        response.setId(community.getId());
        response.setMessage("Created Successfully");
    }

    public static void map(Community community, CreateCommunityRequest request) {
        ArrayList<String> adminId = community.getAdminId();
        adminId.add(request.getToken());
        community.setAdminId(adminId);
        community.setCommunityName(request.getCommunityName());
        community.setCommunityName(request.getCommunityName());
        community.setCommunityDescription(request.getDescription());
        community.setLocalDateTime(LocalDateTime.now());
    }

    public static void map(Community community, EditCommunityRequest request) {
        community.setCommunityName(request.getCommunityName());
        community.setCommunityDescription(request.getDescription());
    }

    public static void map(EditCommunityResponse response, Community community) {
        response.setCommunityName(community.getCommunityName());
        response.setCommunityDescription(community.getCommunityDescription());
        response.setId(community.getId());
        response.setMessage("Edited Successfully");
    }

    public static void map(byte[] imageVideo, ViewCommunityResponse response, Community community) {
        response.setCommunityName(community.getCommunityName());
        response.setCommunityDescription(community.getCommunityDescription());
        response.setId(community.getId());
        response.setImageVideoUrl(imageVideo);
        response.setMessage("View Community");
    }
}
