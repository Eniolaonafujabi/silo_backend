package org.silo.community_management.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

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

    private String imageVideo;

    private ArrayList<String> communityManagerId = new ArrayList<>();

    private ArrayList<String> communityMemberId = new ArrayList<>();

}
