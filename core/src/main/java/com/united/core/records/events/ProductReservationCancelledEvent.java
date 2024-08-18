package com.united.core.records.events;

import java.util.UUID;

public record ProductReservationCancelledEvent(
        UUID productId,
        UUID orderId) {

    public ProductReservationCancelledEvent() {
        this(null, null);
    }
}

