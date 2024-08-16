package orders.records;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateOrderRequest(
    @NotNull
    UUID customerId,
    @NotNull
    UUID productId,
    @NotNull
    @Positive
    Integer productQuantity){
}

