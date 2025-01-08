package org.silo.community_management.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateCommunityResponse {

    private String communityName;

    private String communityDescription;

    private String id;

    private String message;

}
