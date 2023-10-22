package kitchenpos.table_group.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.table_group.application.entity.TableGroupEntity;

public interface TableGroupDao {

  TableGroupEntity save(TableGroupEntity entity);

  Optional<TableGroupEntity> findById(Long id);

  List<TableGroupEntity> findAll();
}
