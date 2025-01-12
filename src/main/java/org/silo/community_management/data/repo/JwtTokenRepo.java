package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.JwtToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JwtTokenRepo extends MongoRepository<JwtToken, String> {
    JwtToken findByUsername(String username);
}
