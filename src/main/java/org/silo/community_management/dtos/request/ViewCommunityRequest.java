package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewCommunityRequest {

    private String token;

    private String communityId;

}
