package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class CreateCommunityRequest {

    private String founderName;

    private String communityName;

    private String description;

    private MultipartFile imageVideo;
}
