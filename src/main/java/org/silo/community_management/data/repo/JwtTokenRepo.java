package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.JwtToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepo extends MongoRepository<JwtToken, String> {
    JwtToken findByUserName(String userId);
}
