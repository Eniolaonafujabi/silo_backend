package org.silo.community_management.dtos.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
public class CreateCommunityRequest {

    private String token;

    private String communityName;

    private String description;
}
