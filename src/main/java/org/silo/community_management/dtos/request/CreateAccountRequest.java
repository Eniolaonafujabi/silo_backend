package org.silo.community_management.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class CreateAccountRequest {

    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    private String bio;

    MultipartFile file;

}
