package com.sally.productsearch.controllers;

import com.sally.productsearch.entities.ProductEntity;
import com.sally.productsearch.repositories.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductSearchControllerV1 {

    private final ProductRepository productRepository;

    public ProductSearchControllerV1(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public List<ProductEntity> getProducts(@RequestParam("title") String title) {
        return productRepository.findByTitle(title, Pageable.unpaged()).getContent();
    }

}
