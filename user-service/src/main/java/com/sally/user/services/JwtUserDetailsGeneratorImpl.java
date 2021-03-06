package com.sally.user.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sally.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsGeneratorImpl implements JwtUserDetailsGenerator {
    private final String jwtAuthSecret;
    private final long tokenValidity;
    private final ObjectMapper objectMapper;

    public JwtUserDetailsGeneratorImpl(@Value("${jwt.secret}") String jwtAuthSecret,
                                       @Value("${jwt.token.validity}") long tokenValidity, ObjectMapper objectMapper) {
        this.jwtAuthSecret = jwtAuthSecret;
        this.tokenValidity = tokenValidity;
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return JwtUtil.generateToken(jwtAuthSecret, tokenValidity, mapUserDetailsToString(userDetails))
                .orElseThrow(() -> new RuntimeException("Can't generate jwt token for user details"));
    }

    private String mapUserDetailsToString(UserDetails userDetails) {
        try {
            return objectMapper.writeValueAsString(userDetails);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't map userDetails to string.", e);
        }
    }
}
