package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddSubGroupMemberRequest {

    private String groupId;

    private String memberId;

}
