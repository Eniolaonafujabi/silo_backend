package org.silo.community_management.service;

import org.silo.community_management.dtos.request.CreateCommunityRequest;
import org.silo.community_management.dtos.request.EditCommunityRequest;
import org.silo.community_management.dtos.request.ViewCommunityRequest;
import org.silo.community_management.dtos.response.CreateCommunityResponse;
import org.silo.community_management.dtos.response.EditCommunityResponse;
import org.silo.community_management.dtos.response.ViewCommunityResponse;

import java.io.IOException;

public interface CommunityInterface {

    CreateCommunityResponse createCommunity(CreateCommunityRequest request) throws IOException;

    EditCommunityResponse editCommunity(EditCommunityRequest request) throws IOException;

    ViewCommunityResponse viewCommunity(ViewCommunityRequest request) throws IOException;
}
