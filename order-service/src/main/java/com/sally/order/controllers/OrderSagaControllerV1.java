package com.sally.order.controllers;

import static com.sally.order.OrderServiceEndpoints.AGGREGATE_BY_ID;
import static com.sally.order.OrderServiceEndpoints.AGGREGATE_EVENTS_BY_ID;
import static com.sally.order.OrderServiceEndpoints.V1;

import com.sally.order.services.OrderAggregate;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.modelling.command.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(V1)
public class OrderSagaControllerV1 {

    private final EventStore eventStore;
    private final Repository<OrderAggregate> orderAggregateRepository;

    public OrderSagaControllerV1(EventStore eventStore, Repository<OrderAggregate> orderAggregateRepository) {
        this.eventStore = eventStore;
        this.orderAggregateRepository = orderAggregateRepository;
    }

    @GetMapping(AGGREGATE_EVENTS_BY_ID)
    public Map<String, Object> getOrderEvents(@PathVariable("id") UUID orderId) {
        return eventStore.readEvents(orderId.toString())
                .asStream()
                .collect(Collectors.toMap(m -> m.getPayloadType().getName(), Message::getPayload));
    }

    // TODO: not working
    @GetMapping(AGGREGATE_BY_ID)
    public OrderAggregate findAggregate(@PathVariable("id") UUID orderId) {
        return orderAggregateRepository.load(orderId.toString())
                .invoke(Function.identity());
    }

}
