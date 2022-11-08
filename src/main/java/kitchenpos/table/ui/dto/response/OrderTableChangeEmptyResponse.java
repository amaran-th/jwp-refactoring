package kitchenpos.table.ui.dto.response;

import kitchenpos.table.domain.OrderTable;

public class OrderTableChangeEmptyResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableChangeEmptyResponse() {
    }

    public OrderTableChangeEmptyResponse(final Long id, final Long tableGroupId, final int numberOfGuests,
                                         final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableChangeEmptyResponse from(final OrderTable orderTable) {
        return new OrderTableChangeEmptyResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
