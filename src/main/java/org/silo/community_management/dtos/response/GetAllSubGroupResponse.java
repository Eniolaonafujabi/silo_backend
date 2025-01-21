package org.silo.community_management.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.silo.community_management.data.model.SubGroup;

import java.util.ArrayList;

@Setter
@Getter
public class GetAllSubGroupResponse {

    private ArrayList<SubGroup> subGroups;
}
