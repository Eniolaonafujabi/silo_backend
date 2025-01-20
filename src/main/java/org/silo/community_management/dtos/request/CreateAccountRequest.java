package org.silo.community_management.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private String phoneNumber;

}
