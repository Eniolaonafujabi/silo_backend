package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogInRequest {

    private String phoneNumber;

    private String email;

    private String password;
}
