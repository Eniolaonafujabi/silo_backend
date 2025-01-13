package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.Community;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepo extends MongoRepository<Community, String> {
}
