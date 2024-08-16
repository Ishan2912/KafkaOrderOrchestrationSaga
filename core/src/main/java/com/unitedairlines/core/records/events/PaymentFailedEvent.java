package com.unitedairlines.core.records.events;

import java.util.UUID;

public record PaymentFailedEvent(
    UUID orderId,
    UUID productId,
    Integer productQuantity) {
}

