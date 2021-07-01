package com.sally.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CommonSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private JwtUserDetailsFetcher jwtUserDetailsFetcher;
//
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public JwtUserDetailsFetcher jwtUserDetailsFetcher() {
        return new JwtUserDetailsFetcherImpl(jwtSecret, objectMapper);
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(jwtUserDetailsFetcher());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new SalyAuthenticationEntryPoint();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
