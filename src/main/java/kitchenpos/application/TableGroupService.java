package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.TableRepository;
import kitchenpos.repository.order.TableGroupRepository;
import kitchenpos.specification.TableGroupSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupSpecification tableGroupSpecification;

    public TableGroupService(OrderRepository orderRepository,
                             TableRepository tableRepository,
                             TableGroupRepository tableGroupRepository,
                             TableGroupSpecification tableGroupSpecification) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupSpecification = tableGroupSpecification;
    }

    @Transactional
    public TableGroup create(TableGroupRequest request) {

        List<Long> requestOrderTableIds = request.tableIds();

        List<OrderTable> savedTables =
                tableRepository.findAllWithTableGroupByIdIn(requestOrderTableIds);

        tableGroupSpecification.validateCreate(request, savedTables);

        TableGroup tableGroup = new TableGroup();
        tableGroup.mapTables(savedTables);

        tableGroup.initCurrentDateTime();
        tableGroup.changeStatusNotEmpty();

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return savedTableGroup;
    }


    @Transactional
    public void ungroup(final Long tableGroupId) {

        List<OrderTable> orderTables = tableRepository.findAllWithTableGroup(tableGroupId);

        tableGroupSpecification.validateUngroup(orderTables);

        for (OrderTable orderTable : orderTables) {
            orderTable.changeStatusNotEmpty();
            orderTable.ungroup();
        }

        tableRepository.saveAll(orderTables);
    }
}
