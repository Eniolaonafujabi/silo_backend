package org.silo.community_management.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CreateSubGroupResponse {

    private String name;

    private String description;

    private LocalDateTime dateAndTimeCrated;

    private String founderId;

    private String message;
}
