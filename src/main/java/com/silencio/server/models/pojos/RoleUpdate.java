package com.silencio.server.models.pojos;

import com.silencio.server.models.enums.Role;
import lombok.Value;

@Value
public class RoleUpdate {
    private Role role;
}
