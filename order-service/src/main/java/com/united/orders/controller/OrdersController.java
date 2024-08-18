package com.united.orders.controller;


import com.united.core.records.dto.CreateOrderRequest;
import com.united.core.records.dto.CreateOrderResponse;
import com.united.core.records.dto.Order;
import com.united.core.records.dto.OrderHistoryResponse;
import com.united.core.types.OrderStatus;
import com.united.orders.service.OrderHistoryService;
import com.united.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;
    private final OrderHistoryService orderHistoryService;


    public OrdersController(OrderService orderService, OrderHistoryService orderHistoryService) {
        this.orderService = orderService;
        this.orderHistoryService = orderHistoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateOrderResponse placeOrder(@RequestBody @Valid CreateOrderRequest request) {
        var order = new Order(UUID.randomUUID(), request.customerId(),
                request.productId(), request.productQuantity(), OrderStatus.CREATED);
        Order createdOrder = orderService.placeOrder(order);

        return new CreateOrderResponse(createdOrder.orderId(),
                createdOrder.customerId(),
                createdOrder.productId(),
                createdOrder.productQuantity(), createdOrder.status());
    }

    @GetMapping("/{orderId}/history")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderHistoryResponse> getOrderHistory(@PathVariable UUID orderId) {
        return orderHistoryService.findByOrderId(orderId).stream().map(orderHistory -> {
            OrderHistoryResponse orderHistoryResponse = new OrderHistoryResponse();
            BeanUtils.copyProperties(orderHistory, orderHistoryResponse);
            return orderHistoryResponse;
        }).toList();
    }

    @GetMapping("/")
    public String get() {
        return "OrderService running...";
    }
}
