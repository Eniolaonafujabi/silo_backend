package org.silo.community_management.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddPostRequest {

    private String token;

    private String title;

    private String content;

    private MultipartFile file;

    private String communityId;

}
