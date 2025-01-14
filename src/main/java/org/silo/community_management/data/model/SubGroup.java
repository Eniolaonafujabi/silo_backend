package org.silo.community_management.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Document
@Setter
@Getter
public class SubGroup {

    private String id;

    private String profileId;

    private String name;

    private String description;

    private LocalDateTime localDateTime;

    private ArrayList<String> memberId = new ArrayList<>();

}
