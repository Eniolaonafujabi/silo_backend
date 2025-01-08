package org.silo.community_management.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

@Document
@Setter
@Getter
public class Post {

    @Id
    private String id;

    private String title;

    private String content;

    private String file;

    private String communityId;

    private String memberId;

}
