package com.sally.shop.service;

import com.sally.api.Product;
import com.sally.api.commands.CreateProductCommand;
import com.sally.api.commands.DeleteProductCommand;
import com.sally.api.commands.UpdateProductCommand;
import com.sally.api.query.GetAllProductsQuery;
import com.sally.api.query.GetProductByIdQuery;
import com.sally.api.requests.CreateProductRequest;
import com.sally.api.requests.UpdateProductRequest;
import com.sally.auth.ShopDetails;
import com.sally.exceptions.ErrorCode;
import com.sally.exceptions.NotFoundException;
import com.sally.shop.dao.entity.ProductEntity;
import com.sally.shop.dao.ProductDAO;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public ProductServiceImpl(ProductDAO productDAO, CommandGateway commandGateway, EventGateway eventGateway, QueryGateway queryGateway) {
        this.productDAO = productDAO;
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @Override
    public CompletableFuture<Product> saveProduct(final ShopDetails shop, final CreateProductRequest request) {
        final UUID productId = UUID.randomUUID();
        final CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(productId)
                .shopId(shop.getId())
                .shopName(shop.getName())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        return commandGateway.send(createProductCommand);
    }

    private Product mapProduct(final ProductEntity productEntity) {
        return Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .shopId(productEntity.getShopId())
                .build();
    }

    @Override
    @Transactional
    public CompletableFuture<Product> updateProduct(final ShopDetails shop, final UpdateProductRequest request) {
        final UpdateProductCommand updateProductCommand = UpdateProductCommand.builder()
                .productId(request.getId())
                .shopId(shop.getId())
                .shopName(shop.getName())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        return commandGateway.send(updateProductCommand);
    }

    @Override
    @Transactional
    public Product getProduct(UUID productId) {
        return productDAO.getProductById(productId)
                .map(this::mapProduct)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND_BY_ID));
    }

    @Override
    @Transactional
    public List<Product> getProducts() {
        return productDAO.getAllProducts()
                .stream()
                .map(this::mapProduct)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProduct(UUID shopId, UUID productId) {
        productDAO.getProductByIdAndShopId(productId, shopId).ifPresent(product -> {
            productDAO.delete(shopId, productId);
            commandGateway.sendAndWait(new DeleteProductCommand(productId, shopId));
        });
    }

    @Override
    public CompletableFuture<Product> queryProduct(UUID productId) {
        return queryGateway.query(new GetProductByIdQuery(productId), ResponseTypes.instanceOf(Product.class));
    }

    @Override
    public CompletableFuture<List<Product>> queryProducts() {
        return queryGateway.query(new GetAllProductsQuery(), ResponseTypes.multipleInstancesOf(Product.class));
    }
}
