package kitchenpos.menu_group.application.dto;

import kitchenpos.menu_group.domain.MenuGroup;

public class MenuGroupQueryResponse {

  private Long id;
  private String name;


  public MenuGroupQueryResponse(final Long id, final String name) {
    this.id = id;
    this.name = name;
  }

  public MenuGroupQueryResponse() {
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static MenuGroupQueryResponse from(final MenuGroup menuGroup) {
    return new MenuGroupQueryResponse(menuGroup.getId(), menuGroup.getName());
  }
}