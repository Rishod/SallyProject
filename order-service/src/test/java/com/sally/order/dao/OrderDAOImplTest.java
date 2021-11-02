package com.sally.order.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sally.api.OrderItem;
import com.sally.api.OrderStatus;
import com.sally.order.DaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.UUID;

public class OrderDAOImplTest extends DaoTest {

    @Autowired
    private OrderDAOImpl orderDAO;

    @Test
    void testSaveOrder() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID orderItemId = UUID.randomUUID();
        final OrderItem orderItem = OrderItem.builder()
                .productId(orderItemId)
                .count(3)
                .build();

        orderDAO.create(orderId, customerId, Collections.singletonList(orderItem), OrderStatus.VERIFYING);

        assertNotNull(testEntityManager.find(OrderEntity.class, orderId));
    }
}