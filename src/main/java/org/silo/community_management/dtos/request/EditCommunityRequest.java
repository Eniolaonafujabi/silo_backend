package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class EditCommunityRequest {

    private String communityId;

    private String communityName;

    private String description;

    private MultipartFile imageVideo;
}
