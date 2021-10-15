package com.sally.productsearch;

import com.sally.productsearch.entities.ProductEntity;
import com.sally.productsearch.repositories.ProductRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ProductRepository productRepository;

    public TestDataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ProductEntity productEntity = new ProductEntity("id_wrfwef", "test_product title1 title2", "desc, desc1, desc2", "test_shop");
        productRepository.save(productEntity);
    }
}
