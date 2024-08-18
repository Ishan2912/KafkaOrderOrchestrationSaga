package com.united.orders.saga;


import com.united.core.records.commands.ApproveOrderCommand;
import com.united.core.records.commands.CancelProductReservationCommand;
import com.united.core.records.commands.ProcessPaymentCommand;
import com.united.core.records.commands.RejectOrderCommand;
import com.united.core.records.commands.ReserveProductCommand;
import com.united.core.records.events.OrderApprovedEvent;
import com.united.core.records.events.OrderCreatedEvent;
import com.united.core.records.events.PaymentFailedEvent;
import com.united.core.records.events.PaymentProcessedEvent;
import com.united.core.records.events.ProductReservationCancelledEvent;
import com.united.core.records.events.ProductReservedEvent;
import com.united.core.types.OrderStatus;
import com.united.orders.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {
        "${orders.events.topic.name}",
        "${products.events.topic.name}",
        "${payments.events.topic.name}"
})
public class OrderSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productsCommandsTopicName;
    private final OrderHistoryService orderHistoryService;
    private final String paymentsCommandsTopicName;
    private final String ordersCommandsTopicName;

    public OrderSaga(KafkaTemplate<String, Object> kafkaTemplate,
                     @Value("${products.commands.topic.name}") String productsCommandsTopicName,
                     OrderHistoryService orderHistoryService,
                     @Value("${payments.commands.topic.name}") String paymentsCommandsTopicName,
                     @Value("${orders.commands.topic.name}") String ordersCommandsTopicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.productsCommandsTopicName = productsCommandsTopicName;
        this.orderHistoryService = orderHistoryService;
        this.paymentsCommandsTopicName = paymentsCommandsTopicName;
        this.ordersCommandsTopicName = ordersCommandsTopicName;
    }

    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvent event) {

        var command = new ReserveProductCommand(
                event.productId(),
                event.productQuantity(),
                event.orderId()
        );

        kafkaTemplate.send(productsCommandsTopicName, command);
        orderHistoryService.add(event.orderId(), OrderStatus.CREATED);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservedEvent event) {

        var processPaymentCommand = new ProcessPaymentCommand(event.orderId(),
                event.productId(), event.productPrice(), event.productQuantity());
        kafkaTemplate.send(paymentsCommandsTopicName, processPaymentCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentProcessedEvent event) {

        var approveOrderCommand = new ApproveOrderCommand(event.orderId());
        kafkaTemplate.send(ordersCommandsTopicName, approveOrderCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload OrderApprovedEvent event) {
        orderHistoryService.add(event.orderId(), OrderStatus.APPROVED);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentFailedEvent event) {
        var cancelProductReservationCommand =
                new CancelProductReservationCommand(event.productId(),
                        event.orderId(),
                        event.productQuantity());
        kafkaTemplate.send(productsCommandsTopicName, cancelProductReservationCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservationCancelledEvent event) {

        var rejectOrderCommand = new RejectOrderCommand(event.orderId());
        kafkaTemplate.send(ordersCommandsTopicName, rejectOrderCommand);
        orderHistoryService.add(event.orderId(), OrderStatus.REJECTED);
    }
}
