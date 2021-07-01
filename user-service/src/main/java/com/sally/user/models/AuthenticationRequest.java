package com.sally.user.models;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
