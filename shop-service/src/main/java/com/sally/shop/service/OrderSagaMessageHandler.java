package com.sally.shop.service;

import com.sally.api.OrderItem;
import com.sally.api.commands.CreateShippingCommand;
import com.sally.api.commands.VerifyProductsCommand;
import com.sally.api.events.ProductsVerificationFailedEvent;
import com.sally.api.events.ProductsVerificationSuccessEvent;
import com.sally.shop.dao.ProductDAO;
import com.sally.shop.dao.entity.ProductEntity;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderSagaMessageHandler {
    private final ProductDAO productDAO;
    private final ShippingService shippingService;
    private final EventGateway eventGateway;

    public OrderSagaMessageHandler(ProductDAO productDAO, ShippingService shippingService, EventGateway eventGateway) {
        this.productDAO = productDAO;
        this.shippingService = shippingService;
        this.eventGateway = eventGateway;
    }

    @Transactional
    @CommandHandler
    public void on(final VerifyProductsCommand command) {
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

    @CommandHandler
    public void on(final CreateShippingCommand command) {
        shippingService.createShipping(command.getShopId(), command.getOrderId(), command.getCustomerId(),
                command.getCustomerName(), command.getOrderItems());
    }
}
