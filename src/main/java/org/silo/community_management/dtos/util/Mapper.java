package org.silo.community_management.dtos.util;

import org.silo.community_management.data.model.*;
import org.silo.community_management.dtos.request.*;
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
        response.setBudgets(community.getBudgets());
        response.setAdminId(community.getAdminId());
        response.setMemberId(community.getMemberId());
        response.setEvents(community.getEvents());
        response.setDateFounded(community.getDateFounded());
        response.setDateTimeOnboarded(community.getDateTimeOnboarded());
        response.setBudgetFunds(community.getBudgetFunds());
    }

    public static void map(Community community, CreateCommunityRequest request, String founderName) {
        ArrayList<String> adminId = community.getAdminId();
        adminId.add(request.getToken());
        community.setAdminId(adminId);
        community.setDateFounded(request.getDateFounded());
        community.setCommunityName(request.getCommunityName());
        community.setFounderName(founderName);
        community.setCommunityDescription(request.getDescription());
        community.setDateTimeOnboarded(LocalDateTime.now());
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

    public static void map(SubGroup subGroup, CreateSubGroupRequest request) {
        subGroup.setName(request.getName());
        subGroup.setDescription(request.getDescription());
        subGroup.setFounderId(request.getToken());
        subGroup.setCommunityId(request.getCommunityId());
    }

    public static void map(CreateSubGroupResponse response, SubGroup subGroup) {
        response.setName(subGroup.getName());
        response.setDescription(subGroup.getDescription());
        response.setFounderId(subGroup.getFounderId());
        response.setDateAndTimeCrated(subGroup.getDateAndTimeCrated());
    }
}
