package org.silo.community_management.data.repo;

import org.silo.community_management.data.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepo extends MongoRepository<Post, String> {
}
