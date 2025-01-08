package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.Community;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommunityRepo extends MongoRepository<Community, String> {
}
