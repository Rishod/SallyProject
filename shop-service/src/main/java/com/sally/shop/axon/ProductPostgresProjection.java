package com.sally.shop.axon;

import com.sally.api.events.ProductCreatedEvent;
import com.sally.api.events.ProductDeletedEvent;
import com.sally.api.events.ProductUpdatedEvent;
import com.sally.shop.dao.ProductDAO;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ProductPostgresProjection {

    private final ProductDAO productDAO;

    public ProductPostgresProjection(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @EventHandler
    @Transactional
    public void on(ProductCreatedEvent event) {
        log.info("Projection ProductCreatedEvent (productId: {}, shopId: {})", event.getProductId(), event.getShopId());

        productDAO.getProductByIdAndShopId(event.getProductId(), event.getShopId())
                .orElseGet(() -> productDAO.saveProduct(event.getProductId(), event.getShopId(),
                        event.getName(), event.getDescription(), event.getPrice()));
    }

    @EventHandler
    @Transactional
    public void on(ProductUpdatedEvent event) {
        log.info("Projection ProductUpdatedEvent (productId: {}, shopId: {})", event.getProductId(), event.getShopId());

        productDAO.getProductByIdAndShopId(event.getProductId(), event.getShopId()).ifPresent(product -> {
            if (product.getUpdatedAt().isAfter(event.getUpdatedAt())) {
                log.info("ProductUpdatedEvent is older for product [id: {}]", product.getId());
                return;
            }

            productDAO.updateProduct(event.getProductId(), event.getShopId(), event.getName(),
                    event.getDescription(), event.getPrice());
        });
    }

    @EventHandler
    @Transactional
    public void on(ProductDeletedEvent event) {
        log.info("Projection ProductDeletedEvent (productId: {}, shopId: {})", event.getProductId(), event.getShopId());

        productDAO.getProductByIdAndShopId(event.getProductId(), event.getShopId())
                .ifPresent(product -> productDAO.delete(event.getProductId(), event.getShopId()));
    }
}
