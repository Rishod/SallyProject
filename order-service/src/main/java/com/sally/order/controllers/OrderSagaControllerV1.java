package com.sally.order.controllers;

import static com.sally.order.OrderServiceEndpoints.AGGREGATE_BY_ID;
import static com.sally.order.OrderServiceEndpoints.AGGREGATE_EVENTS_BY_ID;
import static com.sally.order.OrderServiceEndpoints.V1;

import com.sally.order.services.OrderAggregate;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.DomainEventStream;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.modelling.command.Repository;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.Tuple;

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
    public List<Pair> getOrderEvents(@PathVariable("id") UUID orderId) {
        return eventStore.readEvents(orderId.toString())
                .asStream()
                .sorted(Comparator.comparing(DomainEventMessage::getSequenceNumber))
                .map(m -> Pair.of(m.getPayloadType(), m.getPayload()))
                .collect(Collectors.toList());
    }

    // TODO: not working
    @GetMapping(AGGREGATE_BY_ID)
    public OrderAggregate findAggregate(@PathVariable("id") UUID orderId) {
        return orderAggregateRepository.load(orderId.toString())
                .invoke(Function.identity());
    }

}
