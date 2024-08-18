package com.united.core.records.commands;

import java.util.UUID;

public record CancelProductReservationCommand(
        UUID productId,
        UUID orderId,
        Integer productQuantity) {

    public CancelProductReservationCommand() {
        this(null, null, 0);
    }
}

