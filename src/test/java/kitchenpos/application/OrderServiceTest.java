package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderFixture.주문_잘못된_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceIntegrateTest {

  @Autowired
  private OrderTableDao orderTableDao;
  @Autowired
  private OrderService orderService;

  private Long orderTableId;

  @BeforeEach
  void init() {
    orderTableId = 1L;
    final OrderTable orderTable = orderTableDao.findById(orderTableId).get();
    orderTable.setEmpty(false);
    orderTableDao.save(orderTable);
  }

  @Test
  @DisplayName("주문을 등록할 수 있다.")
  void create_success() {
    //given, when
    final Order actual = orderService.create(주문());

    //then
    Assertions.assertAll(
        () -> assertThat(actual).isNotNull(),
        () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
    );

  }

  @Test
  @DisplayName("주문을 등록할 때 메뉴가 1개도 포함되어 있지 않다면 예외를 반환한다.")
  void create_fail_empty_orderLineItem() {
    //given
    final Order order = 주문();
    order.setOrderLineItems(List.of());

    //when
    final ThrowingCallable actual = () -> orderService.create(order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("주문을 등록할 때 존재하지 않는 메뉴가 포함되어 있으면 예외를 반환한다.")
  void create_fail_not_exist_menu() {
    //given, when
    final ThrowingCallable actual = () -> orderService.create(주문_잘못된_메뉴());

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("주문을 등록할 때 존재하지 않는 테이블의 주문이면 예외를 반환한다.")
  void create_fail_not_exist_table() {
    //given
    final Order order = 주문();
    order.setOrderTableId(999L);

    //when
    final ThrowingCallable actual = () -> orderService.create(order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("주문을 등록할 때 주문한 테이블이 비어있으면 예외를 반환한다.")
  void create_fail_empty_table() {
    //given
    final Order order = 주문();
    order.setOrderTableId(2L);

    //when
    final ThrowingCallable actual = () -> orderService.create(order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("등록된 주문 목록을 조회할 수 있다.")
  void list_success() {
    //given
    final OrderLineItem orderLineItem = new OrderLineItem();
    orderLineItem.setMenuId(1L);
    orderLineItem.setQuantity(1);

    final Order order = new Order();
    order.setOrderTableId(1L);
    order.setOrderLineItems(List.of(orderLineItem));
    orderService.create(order);

    // when
    final List<Order> actual = orderService.list();

    //then
    assertThat(actual).hasSize(1);
  }

  @Test
  @DisplayName("등록된 주문 상태를 수정할 수 있다.")
  void changeOrderStatus_success() {
    //given
    final String newStatus = OrderStatus.MEAL.name();
    final Order order = 주문();
    final Long savedOrderId = orderService.create(order).getId();
    order.setOrderStatus(newStatus);

    // when
    final Order actual = orderService.changeOrderStatus(savedOrderId, order);

    //then
    assertThat(actual.getOrderStatus()).isEqualTo(newStatus);
  }

  @Test
  @DisplayName("등록된 주문 상태를 수정할 때 존재하지 않는 주문이라면 예외를 반환한다.")
  void changeOrderStatus_fail_not_exist_order() {
    //given
    final String newStatus = OrderStatus.MEAL.name();
    final Order order = 주문();
    order.setOrderStatus(newStatus);

    // when
    final ThrowingCallable actual = () -> orderService.changeOrderStatus(999L, order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("등록된 주문 상태를 수정할 때 이미 계산이 완료된 주문이라면 예외를 반환한다.")
  void changeOrderStatus_fail_already_COMPLETEION() {
    //given
    final String newStatus = OrderStatus.COMPLETION.name();
    final Order order = 주문();
    final Long savedOrderId = orderService.create(order).getId();

    order.setOrderStatus(newStatus);
    orderService.changeOrderStatus(savedOrderId, order);

    // when
    final ThrowingCallable actual = () -> orderService.changeOrderStatus(savedOrderId, order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

}