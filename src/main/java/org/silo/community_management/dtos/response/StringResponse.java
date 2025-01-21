package org.silo.community_management.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StringResponse {
    private String otp;
    private boolean status;
}
