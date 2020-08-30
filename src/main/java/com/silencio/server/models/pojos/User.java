package com.silencio.server.models.pojos;

import com.silencio.server.models.enums.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String personId;
    private String username;
    private Role role;
    private String password;
    private String email;
}
