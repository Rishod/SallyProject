package com.sally.user.controllers;

import static com.sally.user.models.UserEndpoints.AUTHENTICATE;
import static com.sally.user.models.UserEndpoints.CUSTOMER;
import static com.sally.user.models.UserEndpoints.SHOP_OWNER;
import static com.sally.user.models.UserEndpoints.USER;
import static com.sally.user.models.UserEndpoints.V1;

import com.sally.auth.SalyUserDetails;
import com.sally.exceptions.BadRequestException;
import com.sally.exceptions.ErrorCode;
import com.sally.user.models.AuthenticationRequest;
import com.sally.user.models.AuthenticationResponse;
import com.sally.user.models.CustomerCreateRequest;
import com.sally.user.models.ShopOwnerCreateRequest;
import com.sally.user.models.User;
import com.sally.user.services.JwtUserDetailsGenerator;
import com.sally.user.services.SalyUserDetailsService;
import com.sally.user.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(V1)
public class UserControllerV1 {
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsGenerator jwtUserDetailsGenerator;
    private final SalyUserDetailsService userDetailsService;
    private final UserService userService;

    public UserControllerV1(AuthenticationManager authenticationManager, JwtUserDetailsGenerator jwtUserDetailsGenerator,
                            SalyUserDetailsService userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsGenerator = jwtUserDetailsGenerator;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping(AUTHENTICATE)
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final SalyUserDetails userDetails = userDetailsService.loadUserByUsernameWithoutPassword(authenticationRequest.getUsername());

        final String token = jwtUserDetailsGenerator.generateToken(userDetails);
        return new AuthenticationResponse(token);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new RuntimeException("Not handle disabled users");
        } catch (BadCredentialsException e) {
            throw new BadRequestException(ErrorCode.BAD_CREDENTIALS);
        }
    }

    @PostMapping(CUSTOMER)
    public User createCustomer(@RequestBody CustomerCreateRequest createRequest) {
        return userService.createCustomer(createRequest);
    }

    @PostMapping(SHOP_OWNER)
    @PreAuthorize("hasRole('ROLE_SUPER_USER')")
    public User createShopOwner(@RequestBody ShopOwnerCreateRequest createRequest) {
        return userService.createShopOwner(createRequest);
    }

    @GetMapping(USER)
    @PreAuthorize("isAuthenticated()")
    public User getUserContext(final Principal principal) {
        return userService.getUserByUsername(principal.getName());
    }
}
