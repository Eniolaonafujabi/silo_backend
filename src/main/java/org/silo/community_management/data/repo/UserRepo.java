package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, String> {
    boolean findByPhoneNumber(String phoneNumber);

    boolean findByEmail(String email);

    User findUserByPhoneNumber(String phoneNumber);

    User findUserByEmail(String email);
}
