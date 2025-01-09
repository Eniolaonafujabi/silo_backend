package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogInRequest {

    private String name;

    private String password;
}
