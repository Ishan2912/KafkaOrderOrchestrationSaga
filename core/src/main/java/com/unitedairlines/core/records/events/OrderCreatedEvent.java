package com.unitedairlines.core.records.events;

import java.util.UUID;

public record OrderCreatedEvent(
    UUID orderId,
    UUID customerId,
    UUID productId,
    Integer productQuantity) {

    public OrderCreatedEvent(){
        this(null, null, null, 0);
    }
}

