package com.united.core.records.dto;

import java.util.UUID;

public record Shipment(
        UUID id,
        UUID orderId,
        UUID paymentId) {
}

