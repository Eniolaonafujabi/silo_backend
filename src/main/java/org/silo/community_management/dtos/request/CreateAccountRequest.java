package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateAccountRequest {

    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    private String bio;

}
