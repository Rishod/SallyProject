package com.sally.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RouteLocator routes(final RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/user-service/**")
                        .uri("http://user-service:8080/")
                        .id("userServiceModule"))
                .route(p -> p
                        .path("/shop-service/**")
                        .uri("http://shop-service:8080")
                        .id("shopServiceModule"))
                .build();
    }

}
