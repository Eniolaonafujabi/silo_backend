package org.silo.community_management.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Setter
@Getter
public class Community {

    @Id
    private String id;

    private String communityName;

    private String communityDescription;

    private String imageVideoUrl;

    private ArrayList<String> adminId = new ArrayList<>();

    private ArrayList<String> memberId =  new ArrayList<>();

//    private ArrayList<String> postId = new ArrayList<>();

}
