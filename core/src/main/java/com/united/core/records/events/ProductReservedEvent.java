package com.united.core.records.events;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductReservedEvent(
        UUID orderId,
        UUID productId,
        BigDecimal productPrice,
        Integer productQuantity) {
}

