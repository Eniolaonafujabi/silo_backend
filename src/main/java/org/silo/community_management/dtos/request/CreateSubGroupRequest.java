package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CreateSubGroupRequest {

    private String name;

    private String description;

    private String founderId;

    private String communityId;

}
