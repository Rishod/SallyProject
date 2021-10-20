package com.sally.shop.service;

import com.sally.api.Shipping;
import com.sally.api.commands.CreateShippingCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ShippingServiceImpl {

    public List<Shipping> getShopShippings(UUID shopId) {
        // todo
        return null;
    }

    public Shipping markShippingAsDelivered(UUID shippingId) {
        // todo
        return null;
    }

    public Shipping markShippingAsCanceled(UUID shippingId) {
        // todo
        return null;
    }

    @CommandHandler
    public void on(CreateShippingCommand createShippingCommand) {
        // todo
    }
}
