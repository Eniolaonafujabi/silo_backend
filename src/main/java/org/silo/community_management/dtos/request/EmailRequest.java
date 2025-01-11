package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailRequest {

    private String toEmail;
    private String subject;
    private String body;
}
