package com.united.core.records.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductCreationResponse(
        UUID id,
        String name,
        BigDecimal price,
        Integer quantity) {

    public ProductCreationResponse() {
        this(null, null, null, 0);
    }
}
