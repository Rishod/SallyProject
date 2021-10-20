package com.sally.shop.service;

import static java.util.function.Function.identity;

import com.sally.api.OrderItem;
import com.sally.api.Product;
import com.sally.api.commands.CreateProductCommand;
import com.sally.api.commands.DeleteProductCommand;
import com.sally.api.commands.UpdateProductCommand;
import com.sally.api.commands.VerifyProductsCommand;
import com.sally.api.events.ProductsVerificationFailedEvent;
import com.sally.api.events.ProductsVerificationSuccessEvent;
import com.sally.api.requests.CreateProductRequest;
import com.sally.api.requests.UpdateProductRequest;
import com.sally.exceptions.ErrorCode;
import com.sally.exceptions.NotFoundException;
import com.sally.shop.dao.entity.ProductEntity;
import com.sally.shop.dao.ProductDAO;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;
    private final CommandGateway commandGateway;
    private final EventGateway eventGateway;

    public ProductServiceImpl(ProductDAO productDAO, CommandGateway commandGateway, EventGateway eventGateway) {
        this.productDAO = productDAO;
        this.commandGateway = commandGateway;
        this.eventGateway = eventGateway;
    }

    @Override
    @Transactional
    public Product saveProduct(final UUID shopId, final CreateProductRequest request) {
        final ProductEntity productEntity = productDAO.saveProduct(shopId, request.getName(), request.getDescription(),
                request.getPrice());

        final CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(productEntity.getId())
                .shopId(productEntity.getShopId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .build();

        commandGateway.sendAndWait(createProductCommand);

        return mapProduct(productEntity);
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
    public Product updateProduct(final UUID shopId, final UpdateProductRequest request) {
        final ProductEntity productEntity = productDAO.updateProduct(request.getId(), shopId, request.getName(),
                request.getDescription(), request.getPrice());

        final UpdateProductCommand updateProductCommand = UpdateProductCommand.builder()
                .productId(productEntity.getId())
                .shopId(productEntity.getShopId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .build();

        commandGateway.sendAndWait(updateProductCommand);

        return mapProduct(productEntity);
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

    @CommandHandler
    public void on(final VerifyProductsCommand command) {
        BigDecimal total = new BigDecimal("0.00");
        final List<OrderItem> items = new ArrayList<>();

        for (VerifyProductsCommand.Product product : command.getProducts()) {
            ProductEntity productEntity = productDAO.getProductById(product.getProductId()).orElse(null);

            if (productEntity == null) {
                eventGateway.publish(new ProductsVerificationFailedEvent(command.getOrderId().toString(), "Not found product by id " + product.getProductId()));
                return;
            }

            total = total.add(productEntity.getPrice().multiply(new BigDecimal(product.getCount())));
            items.add(OrderItem.builder()
                    .productId(productEntity.getId())
                    .productName(productEntity.getName())
                    .price(productEntity.getPrice())
                    .count(product.getCount())
                    .build());
        }

        eventGateway.publish(new ProductsVerificationSuccessEvent(command.getOrderId().toString(), items, total));
    }
}
