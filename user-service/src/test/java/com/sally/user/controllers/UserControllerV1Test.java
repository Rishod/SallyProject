package com.sally.user.controllers;

import static com.sally.user.models.UserEndpoints.CUSTOMER;
import static com.sally.user.models.UserEndpoints.V1;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sally.auth.JwtUserDetailsFetcher;
import com.sally.auth.UserRole;
import com.sally.user.models.CustomerCreateRequest;
import com.sally.user.models.User;
import com.sally.user.services.JwtUserDetailsGenerator;
import com.sally.user.services.SalyUserDetailsService;
import com.sally.user.services.UserService;
import com.sally.utils.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(UserControllerV1.class)
class UserControllerV1Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUserDetailsGenerator jwtUserDetailsGenerator;
    @MockBean
    private SalyUserDetailsService userDetailsService;
    @MockBean
    private UserService userService;
    @MockBean
    protected JwtUserDetailsFetcher jwtTokenResolver;


    @Test
    void testCreateCustomer() throws Exception {
        CustomerCreateRequest customerCreateRequest = CustomerCreateRequest.builder()
                .username("username")
                .password("password")
                .build();

        User user = User.builder()
                .username("username")
                .roles(Sets.of(UserRole.CUSTOMER))
                .build();

        when(userService.createCustomer(customerCreateRequest)).thenReturn(user);

        final MockHttpServletRequestBuilder requestBuilder = post(V1 + CUSTOMER)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(customerCreateRequest));

        // ACT
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }
}