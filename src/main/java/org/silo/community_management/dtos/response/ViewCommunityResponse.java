package org.silo.community_management.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewCommunityResponse {

    private String id;

    private String communityName;

    private String communityDescription;

    private byte[] imageVideoUrl;

    private String message;

}
