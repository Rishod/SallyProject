package com.sally.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sally.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class JwtUserDetailsFetcherImpl implements JwtUserDetailsFetcher {
    private final String jwtAuthSecret;
    private final ObjectMapper objectMapper;

    public JwtUserDetailsFetcherImpl(String jwtAuthSecret, ObjectMapper objectMapper) {
        this.jwtAuthSecret = jwtAuthSecret;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<SalyUserDetails> fetchUserDetails(String token) {
        return JwtUtil.fetchSubject(token, jwtAuthSecret)
                .flatMap(this::mapUserDetailsFromString);
    }

    private Optional<SalyUserDetails> mapUserDetailsFromString(final String userDetails) {
        try {
            return Optional.ofNullable(objectMapper.readValue(userDetails, SalyUserDetails.class));
        } catch (Exception e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }
}
