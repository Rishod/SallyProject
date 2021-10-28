package com.sally.order.controllers;

import static com.sally.order.OrderServiceEndpoints.V1;

import com.sally.order.services.OrderAggregate;
import com.sally.order.services.OrderSaga;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.modelling.command.Repository;
import org.axonframework.modelling.saga.AssociationValue;
import org.axonframework.modelling.saga.SagaRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

//@RestController
//@RequestMapping(V1)
public class OrderSagaControllerV1 {
    private final Repository<OrderAggregate> orderAggregateRepository;
    private final SagaRepository<OrderSaga> orderSagaRepository;
    private final EventStore orderAggregateEventStore;

    public OrderSagaControllerV1(Repository<OrderAggregate> orderAggregateRepository, SagaRepository<OrderSaga> orderSagaRepository, EventStore orderAggregateEventStore) {
        this.orderAggregateRepository = orderAggregateRepository;
        this.orderSagaRepository = orderSagaRepository;
        this.orderAggregateEventStore = orderAggregateEventStore;
    }

    public List<OrderSaga> getSagas(String key, String value) {
        return orderSagaRepository.find(new AssociationValue(key, value))
                .stream()
                .map(orderSagaRepository::load)
                .map(saga -> saga.invoke(Function.identity()))
                .collect(Collectors.toList());
    }

    public OrderAggregate getOrderAggregate(UUID orderId) {
        return orderAggregateRepository.load(orderId.toString())
                .invoke(Function.identity());
    }

    public List<Object> getOrderEvents(UUID orderId) {
        return orderAggregateEventStore.readEvents(orderId.toString())
                .asStream()
                .map(Message::getPayload)
                .collect(Collectors.toList());
    }

}
