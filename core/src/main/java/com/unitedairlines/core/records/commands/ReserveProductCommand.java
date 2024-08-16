package com.unitedairlines.core.records.commands;

import java.util.UUID;

public record ReserveProductCommand(
    UUID productId,
    Integer productQuantity,
    UUID orderId
){
}

