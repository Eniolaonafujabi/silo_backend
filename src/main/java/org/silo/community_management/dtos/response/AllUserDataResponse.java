package org.silo.community_management.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AllUserDataResponse {
    private String name;

    private String email;

    private String phoneNumber;

    private String bio;

    private int noOfCommunityAdmin;

    private int noOfCommunityMember;

    private byte[] file;
}
