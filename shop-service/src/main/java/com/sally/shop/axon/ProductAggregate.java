package com.sally.shop.axon;

import com.sally.api.commands.CreateProductCommand;
import com.sally.api.commands.DeleteProductCommand;
import com.sally.api.commands.UpdateProductCommand;
import com.sally.api.events.ProductCreatedEvent;
import com.sally.api.events.ProductDeletedEvent;
import com.sally.api.events.ProductUpdatedEvent;
import com.sally.exceptions.ErrorCode;
import com.sally.exceptions.NotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Slf4j
@Aggregate
public class ProductAggregate {
    @AggregateIdentifier
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private UUID shopId;
    private String shopName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected ProductAggregate() {
        // for Axon instantiation
    }

    @CommandHandler
    public ProductAggregate(final CreateProductCommand command) {
        // todo validation for properties

        final ProductCreatedEvent event = ProductCreatedEvent.builder()
                .productId(command.getProductId())
                .shopId(command.getShopId())
                .shopName(command.getShopName())
                .name(command.getName())
                .description(command.getDescription())
                .price(command.getPrice())
                .createdAt(LocalDateTime.now())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void on(final UpdateProductCommand command) {
        if (this.shopId != command.getShopId()) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND_BY_ID);
        }

        final ProductUpdatedEvent event = ProductUpdatedEvent.builder()
                .productId(this.id)
                .shopId(this.shopId)
                .name(command.getName())
                .description(command.getDescription())
                .price(command.getPrice())
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void on(DeleteProductCommand command) {
        if (this.shopId != command.getShopId()) {
            throw new IllegalArgumentException("Shop Id is not match");
        }

        AggregateLifecycle.apply(new ProductDeletedEvent(command.getProductId(), command.getShopId()));
    }

    @EventSourcingHandler
    public void on(final ProductCreatedEvent event) {
        log.info("Handle ProductCreatedEvent (productId: {}, shopId: {})", event.getProductId(), event.getShopId());

        this.id = event.getProductId();
        this.shopId = event.getShopId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.createdAt = event.getCreatedAt();
        this.updatedAt = event.getCreatedAt();
    }

    @EventSourcingHandler
    public void on(ProductUpdatedEvent event) {
        log.info("Handle ProductUpdatedEvent (productId: {}, shopId: {})", event.getProductId(), event.getShopId());

        this.name = event.getName();
        this.description = event.getDescription();
        this.price = event.getPrice();
        this.updatedAt = event.getUpdatedAt();
    }

    @EventSourcingHandler
    public void on(ProductDeletedEvent event) {
        log.info("Handle ProductDeletedEvent (productId: {}, shopId: {})", event.getProductId(), event.getShopId());

        AggregateLifecycle.markDeleted();
    }

}
