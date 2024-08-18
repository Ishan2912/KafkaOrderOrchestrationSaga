package com.united.core.records.events;

import java.util.UUID;

public record PaymentProcessedEvent(
        UUID orderId,
        UUID paymentId) {
}

