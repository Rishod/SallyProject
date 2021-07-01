package com.sally.auth;

import java.util.Optional;

public interface JwtUserDetailsFetcher {
    Optional<SalyUserDetails> fetchUserDetails(String token);
}
