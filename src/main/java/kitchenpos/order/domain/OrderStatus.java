package kitchenpos.order.domain;

import java.util.List;

public enum OrderStatus {
  COOKING, MEAL, COMPLETION;

  public static List<OrderStatus> NOT_COMPLETION_STATUSES = List.of(COOKING, MEAL);
}
