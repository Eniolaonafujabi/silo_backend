package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.InvitedUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvitedUserRepo extends MongoRepository<InvitedUser, String> {
    InvitedUser findInvitedUserByEmail(String email);

    void deleteByEmail(String email);
}
