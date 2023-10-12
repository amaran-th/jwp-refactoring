package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceIntegrateTest {

  @Autowired
  private OrderTableDao orderTableDao;
  @Autowired
  private OrderService orderService;
  @Autowired
  private TableGroupService tableGroupService;

  private OrderTable table1;
  private OrderTable table2;
  private OrderTable table3;

  @BeforeEach
  void init() {
    table1 = orderTableDao.findById(1L).get();
    table2 = orderTableDao.findById(2L).get();
    table3 = orderTableDao.findById(3L).get();
  }

  @Test
  @DisplayName("단체 테이블을 등록할 수 있다.")
  void create_success() {
    //given
    final TableGroup tableGroup = new TableGroup();
    tableGroup.setOrderTables(List.of(table1, table2));

    //when
    final TableGroup actual = tableGroupService.create(tableGroup);
    final Long emptyTableCount = actual.getOrderTables()
        .stream()
        .filter(OrderTable::isEmpty)
        .count();

    //then
    Assertions.assertAll(
        () -> assertThat(actual).isNotNull(),
        () -> assertThat(emptyTableCount).isZero()
    );

  }

  @Test
  @DisplayName("단체 테이블을 등록할 때 주문 테이블의 수가 2 미만이면 예외를 반환한다.")
  void create_fail_not_multiple_orderTable() {
    //given
    final TableGroup tableGroup = new TableGroup();
    tableGroup.setOrderTables(List.of(table1));

    //when
    final ThrowingCallable actual = () -> tableGroupService.create(tableGroup);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("단체 테이블을 등록할 때 존재하지 않는 테이블이 포함되어 있다면 예외를 반환한다.")
  void create_fail_not_exist_orderTable() {
    //given
    final TableGroup tableGroup = new TableGroup();
    table2.setId(999L);

    tableGroup.setOrderTables(List.of(table1, table2));

    //when
    final ThrowingCallable actual = () -> tableGroupService.create(tableGroup);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("단체 테이블을 등록할 때 비어있지 않은 테이블을 포함하고 있으면 예외를 반환한다.")
  void create_fail_not_empty_table() {
    //given
    final TableGroup tableGroup = new TableGroup();
    table1.setEmpty(false);
    final OrderTable notEmptyTable1 = orderTableDao.save(table1);

    tableGroup.setOrderTables(List.of(notEmptyTable1, table2));

    //when
    final ThrowingCallable actual = () -> tableGroupService.create(tableGroup);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("단체 테이블을 등록할 때 이미 다른 단체 테이블에 포함된 테이블이 포함되어 있으면 예외를 반환한다.")
  void create_fail_already_in_other_tableGroup() {
    //given
    final TableGroup tableGroup1 = new TableGroup();
    final TableGroup tableGroup2 = new TableGroup();
    tableGroup1.setOrderTables(List.of(table1, table2));
    tableGroup2.setOrderTables(List.of(table2, table3));
    tableGroupService.create(tableGroup1);
    //when
    final ThrowingCallable actual = () -> tableGroupService.create(tableGroup2);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("단체 테이블을 삭제할 수 있다.")
  void ungroup_success() {
    //given
    final TableGroup tableGroup = new TableGroup();
    tableGroup.setOrderTables(List.of(table1, table2));
    final Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

    //when
    final Executable actual = () -> tableGroupService.ungroup(savedTableGroupId);

    //then
    Assertions.assertDoesNotThrow(actual);
    Assertions.assertAll(
        () -> assertThat(orderTableDao.findById(1L).get().isEmpty()).isFalse(),
        () -> assertThat(orderTableDao.findById(2L).get().isEmpty()).isFalse()
    );
  }

  @Test
  @DisplayName("단체 테이블을 삭제할 때 테이블들의 주문들 중 계산이 완료되지 않은 주문이 있으면 예외를 반환한다.")
  void ungroup_fail_not_COMPLETION_order() {
    //given
    final TableGroup tableGroup = new TableGroup();
    tableGroup.setOrderTables(List.of(table1, table2));

    final TableGroup savedTableGroup = tableGroupService.create(tableGroup);
    final Long savedTableGroupId = savedTableGroup.getId();

    final OrderTable savedTable = savedTableGroup.getOrderTables().get(0);
    savedTable.setEmpty(false);
    orderTableDao.save(savedTable);

    orderService.create(주문());

    //when
    final ThrowingCallable actual = () -> tableGroupService.ungroup(savedTableGroupId);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }
}
