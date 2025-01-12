package org.silo.community_management.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "jwt_tokens")
public class JwtToken {

    @Id
    private String id;
    private String username;
    private String token;
    private Long expirationTime;
}
