package org.silo.community_management.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document
public class InvitedUser {

    @Id
    private int id;

    private String email;

    private String communityId;

    private String role;
}
