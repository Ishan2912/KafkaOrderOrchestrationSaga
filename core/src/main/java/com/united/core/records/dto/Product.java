package com.united.core.records.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record Product(
        UUID id,
        String name,
        BigDecimal price,
        Integer quantity) {
    public Product() {
        this(null, null, null, 0);
    }

    public Product(Integer quantity) {
        this(null, null, null, quantity);
    }

    public Product(UUID productId, Integer quantity) {
        this(productId, null, null, quantity);
    }
}

