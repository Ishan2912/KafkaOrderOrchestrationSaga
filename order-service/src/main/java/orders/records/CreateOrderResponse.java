package orders.records;



import com.unitedairlines.core.types.OrderStatus;

import java.util.UUID;

public record CreateOrderResponse(
    UUID orderId,
    UUID customerId,
    UUID productId,
    Integer productQuantity,
    OrderStatus status) {

    public CreateOrderResponse(){
        this(null, null, null, 0,null);
    }
}

