package com.sally.api.requests;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
