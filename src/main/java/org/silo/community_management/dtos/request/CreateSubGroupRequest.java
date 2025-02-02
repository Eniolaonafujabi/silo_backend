package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateSubGroupRequest {

    private String name;

    private String description;

    private String token;

    private String communityId;

}
