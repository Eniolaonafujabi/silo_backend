package org.silo.community_management.service;

import org.silo.community_management.dtos.request.AddPostRequest;
import org.silo.community_management.dtos.response.AddPostResponse;

import java.io.IOException;

public interface PostInterface {

    AddPostResponse addPost(AddPostRequest request) throws IOException;

}
