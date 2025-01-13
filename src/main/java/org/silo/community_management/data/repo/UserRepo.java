package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    User findUserByPhoneNumber(String phoneNumber);

    User findUserByEmail(String email);

    User findUserById(String userId);
}
