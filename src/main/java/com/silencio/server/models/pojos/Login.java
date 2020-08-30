package com.silencio.server.models.pojos;

import lombok.Value;

@Value
public class Login {
    private String username;
    private String password;
}
