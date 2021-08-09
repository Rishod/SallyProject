package com.sally.shop.configs;

import com.sally.exceptions.CommonExceptionAdvisor;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Order
@ControllerAdvice
public class ShopServiceAdvisor extends CommonExceptionAdvisor {
}
