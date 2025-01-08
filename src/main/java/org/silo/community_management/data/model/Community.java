package org.silo.community_management.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
public class Community {

    @Id
    private String id;

    private String communityName;

    private String communityDescription;

    private String imageVideoUrl;
}
