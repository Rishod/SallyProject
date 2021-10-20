package com.sally.order.configs;

import com.sally.exceptions.CommonExceptionAdvisor;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Order
@ControllerAdvice
public class OrderServiceAdvisor extends CommonExceptionAdvisor {
}
