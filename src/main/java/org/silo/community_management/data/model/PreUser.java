package org.silo.community_management.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@Document
public class PreUser {
    @Id
    private String id;
    private String email;
    private boolean isEmailVerified;
    private String otp;
    private LocalDateTime otpExpiration;

}
