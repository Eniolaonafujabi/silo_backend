package org.silo.community_management.dtos.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class CreateCommunityRequest {

    private String token;

    private String communityName;

    private String description;

    private String dateFounded;
}
