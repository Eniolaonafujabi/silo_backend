package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetAllSubGroupRequest {
    private String communityId;
    private String token;
}
