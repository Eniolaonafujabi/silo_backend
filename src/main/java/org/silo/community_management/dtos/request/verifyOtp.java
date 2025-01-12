package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class verifyOtp {

    private String otp;

    private String email;
}
