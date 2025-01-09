package org.silo.community_management.dtos.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
public class CreateCommunityRequest {

    private String founderName;

    private String founderId;

    private String communityName;

    private String description;

    private MultipartFile imageVideo;

    public CreateCommunityRequest(MultipartFile file, String communityName, String communityDescription, String founderId) {
        this.imageVideo = file;
        this.communityName = communityName;
        this.description = communityDescription;
        this.founderId = founderId;
    }
}
