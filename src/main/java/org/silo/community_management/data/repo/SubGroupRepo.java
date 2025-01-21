package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.SubGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubGroupRepo extends MongoRepository<SubGroup,String> {
}
