package com.unitedairlines.core.records.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessPaymentCommand(
    UUID orderId,
    UUID productId,
    BigDecimal productPrice,
    Integer productQuantity){
}


