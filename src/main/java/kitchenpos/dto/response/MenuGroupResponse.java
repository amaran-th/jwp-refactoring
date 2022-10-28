package kitchenpos.dto.response;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private final Long id;
    private final String name;

    private MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(
                menuGroup.getId(),
                menuGroup.getName()
        );
    }
}