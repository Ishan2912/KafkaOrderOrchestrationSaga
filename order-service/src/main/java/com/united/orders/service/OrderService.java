package com.united.orders.service;

import com.united.core.records.dto.Order;

import java.util.UUID;

public interface OrderService {
    Order placeOrder(Order order);

    void approveOrder(UUID orderId);

    void rejectOrder(UUID orderId);
}
