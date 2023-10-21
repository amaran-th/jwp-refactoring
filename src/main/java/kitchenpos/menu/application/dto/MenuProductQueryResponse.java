package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductQueryResponse {

  private Long seq;
  private Long menuId;
  private Long productId;
  private long quantity;

  public MenuProductQueryResponse(final Long seq, final Long menuId, final Long productId,
      final long quantity) {
    this.seq = seq;
    this.menuId = menuId;
    this.productId = productId;
    this.quantity = quantity;
  }

  public MenuProductQueryResponse() {
  }

  public Long getSeq() {
    return seq;
  }

  public Long getMenuId() {
    return menuId;
  }

  public Long getProductId() {
    return productId;
  }

  public long getQuantity() {
    return quantity;
  }

  public static MenuProductQueryResponse from(final MenuProduct menuProduct) {
    return new MenuProductQueryResponse(menuProduct.getSeq(), menuProduct.getMenuId(),
        menuProduct.getProductId(), menuProduct.getQuantity());
  }
}