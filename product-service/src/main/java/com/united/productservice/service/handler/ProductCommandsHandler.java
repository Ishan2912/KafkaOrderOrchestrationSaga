package com.united.productservice.service.handler;

import com.united.core.records.commands.CancelProductReservationCommand;
import com.united.core.records.commands.ReserveProductCommand;
import com.united.core.records.dto.Product;
import com.united.core.records.events.ProductReservationCancelledEvent;
import com.united.core.records.events.ProductReservationFailedEvent;
import com.united.core.records.events.ProductReservedEvent;
import com.united.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${products.commands.topic.name}",
        groupId = "${spring.kafka.consumer.group-id}")
public class ProductCommandsHandler {

    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productEventsTopicName;

    public ProductCommandsHandler(ProductService productService,
                                  KafkaTemplate<String, Object> kafkaTemplate,
                                  @Value("${products.events.topic.name}") String productEventsTopicName) {
        this.productService = productService;
        this.kafkaTemplate = kafkaTemplate;
        this.productEventsTopicName = productEventsTopicName;
    }

    @KafkaHandler
    public void handleCommand(@Payload ReserveProductCommand command) {

        try {
            Product desiredProduct = new Product(command.productId(), command.productQuantity());
            Product reservedProduct = productService.reserve(desiredProduct, command.orderId());
            ProductReservedEvent productReservedEvent = new ProductReservedEvent(command.orderId(),
                    command.productId(),
                    reservedProduct.price(),
                    command.productQuantity());
            kafkaTemplate.send(productEventsTopicName, productReservedEvent);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            ProductReservationFailedEvent productReservationFailedEvent =
                    new ProductReservationFailedEvent(command.productId(),
                            command.orderId(),
                            command.productQuantity());
            kafkaTemplate.send(productEventsTopicName, productReservationFailedEvent);
        }
    }

    @KafkaHandler
    public void handleCommand(@Payload CancelProductReservationCommand command) {
        Product productToCancel = new Product(command.productId(), command.productQuantity());
        productService.cancelReservation(productToCancel, command.orderId());

        ProductReservationCancelledEvent productReservationCancelledEvent =
                new ProductReservationCancelledEvent(command.productId(), command.orderId());
        kafkaTemplate.send(productEventsTopicName, productReservationCancelledEvent);
    }
}
