package com.sally.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Value("${user-service.host}")
    private String userServiceHost;
    @Value("${order-service.host}")
    private String orderServiceHost;
    @Value("${shop-service.host}")
    private String shopServiceHost;
    @Value("${product-search-service.host}")
    private String productSearchServiceHost;
    @Value("${axon-gui.host}")
    private String axonGuiHost;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RouteLocator routes(final RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/user-service/**")
                        .uri(userServiceHost)
                        .id("userServiceModule"))
                .route(p -> p
                        .path("/shop-service/**")
                        .uri(shopServiceHost)
                        .id("shopServiceModule"))
                .route(p -> p
                        .path("/product-search-service/**")
                        .uri(productSearchServiceHost)
                        .id("productSearchServiceModule"))
                .route(p -> p
                        .path("/order-service/**")
                        .uri(orderServiceHost)
                        .id("orderServiceModule"))
                .route(p -> p
                        .path("/axon/**")
                        .uri(axonGuiHost)
                        .id("axonGuiModule"))
                .build();
    }

}
