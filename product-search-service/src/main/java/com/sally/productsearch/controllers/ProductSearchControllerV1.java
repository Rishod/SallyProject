package com.sally.productsearch.controllers;

import static com.sally.productsearch.ProductSearchEndpoints.PRODUCTS;
import static com.sally.productsearch.ProductSearchEndpoints.V1;

import com.sally.productsearch.entities.ProductEntity;
import com.sally.productsearch.repositories.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(V1)
public class ProductSearchControllerV1 {

    private final ProductRepository productRepository;

    public ProductSearchControllerV1(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping(PRODUCTS)
    public List<ProductEntity> getProducts(@RequestParam("searchText") String searchText) {
        return productRepository.defaultSearch(searchText, Pageable.unpaged()).getContent();
    }

}
