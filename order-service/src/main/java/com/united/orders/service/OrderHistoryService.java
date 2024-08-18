package com.united.orders.service;


import com.united.core.records.dto.OrderHistory;
import com.united.core.types.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryService {
    void add(UUID orderId, OrderStatus orderStatus);

    List<OrderHistory> findByOrderId(UUID orderId);
}
