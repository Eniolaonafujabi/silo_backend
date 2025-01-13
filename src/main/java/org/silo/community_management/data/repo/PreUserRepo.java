package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.PreUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PreUserRepo extends MongoRepository<PreUser, String> {

    Optional<PreUser> findPreUserByEmail(String email);

    public abstract Optional<Boolean> findByEmail(String email);


    void deletePreUserByEmail(String email);
}
