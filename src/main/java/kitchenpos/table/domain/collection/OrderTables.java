package kitchenpos.table.domain.collection;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.table.domain.entity.TableGroup;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private static final int GROUPING_CONDITION = 2;
    private List<OrderTable> elements;

    public OrderTables(List<OrderTable> elements) {
        validate(elements);
        this.elements = elements;
    }

    private void validate(List<OrderTable> elements) {
        if (CollectionUtils.isEmpty(elements) || elements.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getElements() {
        return elements;
    }

    public List<Long> getOrderTableIds() {
        return elements.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public boolean isReadyToGroup(OrderTables unsavedOrderTables) {
        if (elements.size() != unsavedOrderTables.elements.size()) {
            return false;
        }

        for (OrderTable savedOrderTable : elements) {
            if (!savedOrderTable.isReadyToGroup()) {
                return false;
            }
        }
        return true;
    }

    public void group(TableGroup tableGroup) {
        for (OrderTable orderTable : elements) {
            orderTable.joinGroup(tableGroup);
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : elements) {
            orderTable.exitFromGroup();
        }
    }

    public boolean isAbleToGroup() {
        return elements.size() >= GROUPING_CONDITION;
    }
}