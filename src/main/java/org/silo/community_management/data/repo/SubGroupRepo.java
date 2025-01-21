package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.SubGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface SubGroupRepo extends MongoRepository<SubGroup,String> {
    ArrayList<SubGroup> findByCommunityId(String communityId);

    boolean existsByNameAndCommunityId(String subGroupName, String communityId);
}
