package kitchenpos.table.domain.repository;

import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
