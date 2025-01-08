package org.silo.community_management.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
@Getter
public class User {

    private String id;

    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    private String bio;
}
