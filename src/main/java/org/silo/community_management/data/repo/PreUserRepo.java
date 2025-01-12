package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.PreUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PreUserRepo extends MongoRepository<PreUser, String> {

    Optional<PreUser> findPreUserByEmail(String email);

    Boolean findByEmail(String email);

    void deletePreUserByEmail(String email);
}
