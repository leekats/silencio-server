package com.silencio.server.models.pojos;

import com.silencio.server.models.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Auth {
    private String personId;
    private Role role;
}
