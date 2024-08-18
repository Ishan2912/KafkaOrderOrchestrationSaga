package com.united.core.records.dto;


import com.united.core.types.OrderStatus;

import java.util.UUID;

public record Order(
        UUID orderId,
        UUID customerId,
        UUID productId,
        Integer productQuantity,
        OrderStatus status) {

    public Order() {
        this(null, null, null, 0, null);
    }
}
