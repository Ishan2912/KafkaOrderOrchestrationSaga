package com.united.core.records.dto;

import com.united.core.types.OrderStatus;

import java.sql.Timestamp;
import java.util.UUID;

public record OrderHistory(
        UUID id,
        UUID orderId,
        OrderStatus status,
        Timestamp createdAt) {

    public OrderHistory() {
        this(null, null, null, null);
    }
}

