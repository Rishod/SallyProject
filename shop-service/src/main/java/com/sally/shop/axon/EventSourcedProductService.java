package com.sally.shop.axon;

import com.sally.api.Product;
import com.sally.api.commands.CreateProductCommand;
import com.sally.api.commands.DeleteProductCommand;
import com.sally.api.commands.UpdateProductCommand;

import com.sally.api.query.GetAllProductsQuery;
import com.sally.api.query.GetProductByIdQuery;
import com.sally.api.requests.CreateProductRequest;
import com.sally.api.requests.UpdateProductRequest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcedAggregate;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.command.LockAwareAggregate;
import org.axonframework.modelling.command.Repository;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This service implementation representing Event Source pattern which used Axon framework as engine
 */
@Service
@Deprecated
public class EventSourcedProductService  {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public EventSourcedProductService(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    public CompletableFuture<UUID> saveProduct(UUID shopId, CreateProductRequest request) {
        final UUID productId = UUID.randomUUID();
        final CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(productId)
                .shopId(shopId)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        return commandGateway.send(createProductCommand)
                .thenApply(result -> productId);
    }

    public CompletableFuture<UUID> updateProduct(final UUID shopId, UpdateProductRequest request) {
        UpdateProductCommand command = UpdateProductCommand.builder()
                .productId(request.getId())
                .shopId(shopId)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();


        return commandGateway.send(command)
                .thenApply(result -> request.getId());
    }

    public CompletableFuture<Product> getProduct(UUID productId) {
        return queryGateway.query(new GetProductByIdQuery(productId), ResponseTypes.instanceOf(Product.class));
    }

    public CompletableFuture<List<Product>> getProducts() {
        return queryGateway.query(new GetAllProductsQuery(), ResponseTypes.multipleInstancesOf(Product.class));
    }

    public void deleteProduct(UUID shopId, UUID productId) {
        commandGateway.send(new DeleteProductCommand(productId, shopId));
    }
}
