package com.sally.productsearch.projections;

import com.sally.api.Product;
import com.sally.api.SearchedProducts;
import com.sally.api.events.ProductCreatedEvent;
import com.sally.api.events.ProductDeletedEvent;
import com.sally.api.events.ProductUpdatedEvent;
import com.sally.api.query.FindProductsByIdsQuery;
import com.sally.api.query.GetAllProductsQuery;
import com.sally.api.query.GetProductByIdQuery;
import com.sally.productsearch.entities.ProductEntity;
import com.sally.productsearch.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductProjection {
    private final ProductRepository productRepository;

    public ProductProjection(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(final ProductCreatedEvent event) {
        log.info("Handle ProductCreatedEvent (productId: {}, shopId: {})", event.getProductId(), event.getShopId());

        final ProductEntity productEntity = ProductEntity.builder()
                .id(event.getProductId())
                .title(event.getName())
                .description(event.getDescription())
                .shopId(event.getShopId())
                .shopName(event.getShopName())
                .price(event.getPrice())
                .build();

        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(final ProductUpdatedEvent event) {
        log.info("Handle ProductUpdatedEvent (productId: {}, shopId: {})", event.getProductId(), event.getShopId());

        final ProductEntity productEntity = ProductEntity.builder()
                .id(event.getProductId())
                .title(event.getName())
                .description(event.getDescription())
                .price(event.getPrice())
                .shopId(event.getShopId())
                .shopName(event.getShopName())
                .build();

        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(final ProductDeletedEvent event) {
        log.info("Handle ProductDeletedEvent (productId: {}, shopId: {})", event.getProductId(), event.getShopId());

        productRepository.findById(event.getProductId()).ifPresent(productRepository::delete);
    }

    @QueryHandler
    public List<Product> query(final GetAllProductsQuery query) {
        final ArrayList<Product> result = new ArrayList<>();
        productRepository.findAll().forEach(productEntity -> result.add(mapProduct(productEntity)));

        return result;
    }

    @QueryHandler
    public Product query(final GetProductByIdQuery query) {
        return productRepository.findById(query.getProductId()).map(this::mapProduct).orElse(null);
    }

    @QueryHandler
    public SearchedProducts query(final FindProductsByIdsQuery query) {
        return new SearchedProducts(productRepository.findByIdIn(query.getProductIds())
                .stream()
                .map(this::mapProduct)
                .collect(Collectors.toList()));
    }

    private Product mapProduct(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getTitle())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .build();
    }
}
