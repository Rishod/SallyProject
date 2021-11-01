package com.sally.shop.service;

import com.sally.api.OrderItem;
import com.sally.api.Product;
import com.sally.api.SearchedProducts;
import com.sally.api.commands.CreateShippingCommand;
import com.sally.api.commands.VerifyProductsCommand;
import com.sally.api.events.ProductsVerificationFailedEvent;
import com.sally.api.events.ProductsVerificationSuccessEvent;
import com.sally.api.query.FindProductsByIdsQuery;
import com.sally.shop.dao.ProductDAO;
import com.sally.shop.dao.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderSagaMessageHandler {
    private final ProductDAO productDAO;
    private final ShippingService shippingService;
    private final EventGateway eventGateway;
    private final QueryGateway queryGateway;

    public OrderSagaMessageHandler(ProductDAO productDAO, ShippingService shippingService, EventGateway eventGateway,
                                   QueryGateway queryGateway) {
        this.productDAO = productDAO;
        this.shippingService = shippingService;
        this.eventGateway = eventGateway;
        this.queryGateway = queryGateway;
    }

    @Transactional
    @CommandHandler
    public void on(final VerifyProductsCommand command) {
        log.info("Handle VerifyProductsCommand [orderId: {}]", command.getOrderId());

        try {
            BigDecimal total = new BigDecimal("0.00");
            final List<OrderItem> items = new ArrayList<>();

            UUID shopId = null;
            for (VerifyProductsCommand.Product product : command.getProducts()) {
                ProductEntity productEntity = productDAO.getProductById(product.getProductId()).orElse(null);

                if (productEntity == null) {
                    eventGateway.publish(new ProductsVerificationFailedEvent(command.getOrderId().toString(), "Not found product by id " + product.getProductId()));
                    return;
                }

                if (shopId == null) {
                    shopId = productEntity.getShopId();
                } else if (shopId != productEntity.getShopId()) {
                    eventGateway.publish(new ProductsVerificationFailedEvent(command.getOrderId().toString(), "Products should be from same shop"));
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

            eventGateway.publish(new ProductsVerificationSuccessEvent(shopId, command.getOrderId().toString(), items, total));
        } catch (Exception e) {
            eventGateway.publish(new ProductsVerificationFailedEvent(command.getOrderId().toString(), "Something went wrong when verifying products"));
        }
    }

    //@CommandHandler
    public void validate(final VerifyProductsCommand command) {
        log.info("Handle VerifyProductsCommand(V2) [orderId: {}]", command.getOrderId());

        final List<UUID> productIds = command.getProducts().stream().map(VerifyProductsCommand.Product::getProductId)
                .collect(Collectors.toList());

        queryGateway.query(new FindProductsByIdsQuery(productIds), SearchedProducts.class)
                .thenAccept(searchedProducts -> validate(command, searchedProducts));
    }

    private void validate(VerifyProductsCommand command, SearchedProducts products) {
        final List<Product> storedProducts = products.getProducts();
        final Set<UUID> shopIds = storedProducts.stream().map(Product::getShopId).collect(Collectors.toSet());

        if (shopIds.size() != 1) {
            eventGateway.publish(new ProductsVerificationFailedEvent(command.getOrderId().toString(), "Products should be from same shop"));
            return;
        }

        final Map<UUID, Product> storedProductById = storedProducts.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        BigDecimal total = new BigDecimal("0.00");
        final List<OrderItem> items = new ArrayList<>();

        for (VerifyProductsCommand.Product orderedProduct : command.getProducts()) {
            final Product storedProduct = storedProductById.get(orderedProduct.getProductId());

            if (storedProduct == null) {
                eventGateway.publish(new ProductsVerificationFailedEvent(command.getOrderId().toString(),
                        "Not found product by id " + orderedProduct.getProductId()));
                return;
            }

            total = total.add(storedProduct.getPrice().multiply(new BigDecimal(orderedProduct.getCount())));
            items.add(OrderItem.builder()
                    .productId(storedProduct.getId())
                    .productName(storedProduct.getName())
                    .price(storedProduct.getPrice())
                    .count(orderedProduct.getCount())
                    .build());
        }

        eventGateway.publish(new ProductsVerificationSuccessEvent(shopIds.stream().findFirst().get(), command.getOrderId().toString(), items, total));
    }

    @CommandHandler
    public void on(final CreateShippingCommand command) {
        log.info("Handle CreateShippingCommand [orderId: {}, shopId: {}]", command.getOrderId(), command.getShopId());

        shippingService.createShipping(command.getShopId(), command.getOrderId(), command.getCustomerId(),
                command.getCustomerName(), command.getOrderItems());
    }
}
