package kitchenpos.menu.application.entity;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Price;

public class MenuEntity {

  private Long id;
  private String name;
  private BigDecimal price;
  private Long menuGroupId;

  public MenuEntity(final Long id, final String name, final BigDecimal price,
      final Long menuGroupId) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.menuGroupId = menuGroupId;
  }

  public MenuEntity() {
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public Long getMenuGroupId() {
    return menuGroupId;
  }

  public static MenuEntity from(final Menu menu) {
    return new MenuEntity(menu.getId(), menu.getName(), menu.getPrice().getValue(),
        menu.getMenuGroupId());
  }

  public Menu toMenu() {
    return new Menu(id, name, new Price(price), menuGroupId);
  }

  public Menu toMenu(final List<MenuProduct> menuProducts) {
    return new Menu(id, name, new Price(price), menuGroupId, menuProducts);
  }
}