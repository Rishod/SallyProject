package com.sally.shop.configs;

import com.sally.auth.CommonSecurityConfig;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ShopServiceSecurityConfig extends CommonSecurityConfig {
}
