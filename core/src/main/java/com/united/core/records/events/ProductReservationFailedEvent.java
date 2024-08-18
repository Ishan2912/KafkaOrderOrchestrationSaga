package com.united.core.records.events;

import java.util.UUID;

public record ProductReservationFailedEvent(
        UUID productId,
        UUID orderId,
        Integer productQuantity) {
}

