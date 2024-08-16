package orders.service;

import com.unitedairlines.core.records.Order;
import com.unitedairlines.core.records.events.OrderApprovedEvent;
import com.unitedairlines.core.records.events.OrderCreatedEvent;
import com.unitedairlines.core.types.OrderStatus;
import orders.dao.jpa.entity.OrderEntity;
import orders.dao.jpa.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String ordersEventsTopicName;

    public OrderServiceImpl(OrderRepository orderRepository,
                            KafkaTemplate<String, Object> kafkaTemplate,
                            @Value("${orders.events.topic.name}") String ordersEventsTopicName) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.ordersEventsTopicName = ordersEventsTopicName;
    }

    @Override
    public Order placeOrder(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setCustomerId(order.customerId());
        entity.setProductId(order.productId());
        entity.setProductQuantity(order.productQuantity());
        entity.setStatus(OrderStatus.CREATED);
        orderRepository.save(entity);

        OrderCreatedEvent placedOrder = new OrderCreatedEvent(
                entity.getId(),
                entity.getCustomerId(),
                order.productId(),
                order.productQuantity()
        );
        kafkaTemplate.send(ordersEventsTopicName, placedOrder);

        return new Order(
                entity.getId(),
                entity.getCustomerId(),
                entity.getProductId(),
                entity.getProductQuantity(),
                entity.getStatus());
    }

    @Override
    public void approveOrder(UUID orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        Assert.notNull(orderEntity, "No order is found with id " + orderId + " in the database table");
        orderEntity.setStatus(OrderStatus.APPROVED);
        orderRepository.save(orderEntity);
        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(orderId);
        kafkaTemplate.send(ordersEventsTopicName, orderApprovedEvent);
    }

}
