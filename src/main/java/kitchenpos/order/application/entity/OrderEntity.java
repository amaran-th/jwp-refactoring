package kitchenpos.order.application.entity;

import java.time.LocalDateTime;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderEntity {

  private Long id;
  private Long orderTableId;
  private String orderStatus;
  private LocalDateTime orderedTime;


  public OrderEntity(final Long id, final Long orderTableId, final String orderStatus,
      final LocalDateTime orderedTime) {
    this.id = id;
    this.orderTableId = orderTableId;
    this.orderStatus = orderStatus;
    this.orderedTime = orderedTime;
  }

  public OrderEntity() {
  }

  public Long getId() {
    return id;
  }

  public Long getOrderTableId() {
    return orderTableId;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public LocalDateTime getOrderedTime() {
    return orderedTime;
  }

  public static OrderEntity from(final Order order) {
    return new OrderEntity(order.getId(), order.getOrderTableId(),
        order.getOrderStatus().name(), order.getOrderedTime());
  }

  public Order toOrder() {
    return new Order(id, orderTableId, OrderStatus.valueOf(orderStatus), orderedTime);
  }
}