package kitchenpos.table_group.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderDao;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.persistence.OrderTableDao;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.persistence.TableGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

  private final OrderDao orderDao;
  private final OrderTableDao orderTableDao;
  private final TableGroupDao tableGroupDao;

  public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
      final TableGroupDao tableGroupDao) {
    this.orderDao = orderDao;
    this.orderTableDao = orderTableDao;
    this.tableGroupDao = tableGroupDao;
  }

  @Transactional
  public TableGroup create(final TableGroup tableGroup) {
    final List<OrderTable> orderTables = tableGroup.getOrderTables();

    if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
      throw new IllegalArgumentException();
    }

    final List<Long> orderTableIds = orderTables.stream()
        .map(OrderTable::getId)
        .collect(Collectors.toList());

    final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

    if (orderTables.size() != savedOrderTables.size()) {
      throw new IllegalArgumentException();
    }

    for (final OrderTable savedOrderTable : savedOrderTables) {
      if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
        throw new IllegalArgumentException();
      }
    }

    tableGroup.setCreatedDate(LocalDateTime.now());

    final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

    final Long tableGroupId = savedTableGroup.getId();
    for (final OrderTable savedOrderTable : savedOrderTables) {
      savedOrderTable.setTableGroupId(tableGroupId);
      savedOrderTable.setEmpty(false);
      orderTableDao.save(savedOrderTable);
    }
    savedTableGroup.setOrderTables(savedOrderTables);

    return savedTableGroup;
  }

  @Transactional
  public void ungroup(final Long tableGroupId) {
    final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

    final List<Long> orderTableIds = orderTables.stream()
        .map(OrderTable::getId)
        .collect(Collectors.toList());

    if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
        orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
      throw new IllegalArgumentException();
    }

    for (final OrderTable orderTable : orderTables) {
      orderTable.setTableGroupId(null);
      orderTable.setEmpty(false);
      orderTableDao.save(orderTable);
    }
  }
}