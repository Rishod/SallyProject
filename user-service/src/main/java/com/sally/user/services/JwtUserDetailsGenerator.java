package com.sally.user.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtUserDetailsGenerator {
    String generateToken (UserDetails userDetails);
}
