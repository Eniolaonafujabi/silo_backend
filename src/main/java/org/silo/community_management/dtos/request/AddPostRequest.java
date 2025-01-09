package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class AddPostRequest {

    private String memberId;

    private String title;

    private String content;

    private MultipartFile file;

    private String communityId;

}
