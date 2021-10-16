package com.sally.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sally.api.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalyUserDetails implements UserDetails {
    protected UUID userId;
    protected String username;
    protected Set<UserRole> roles;
    protected ShopDetails shopDetails;

    public SalyUserDetails(UUID userId, String username, Set<UserRole> roles) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }

    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        return roles.stream().map(this::mapUserRole).collect(Collectors.toList());
    }

    private GrantedAuthority mapUserRole(UserRole userRole) {
        return new AuthorityImpl(userRole);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
