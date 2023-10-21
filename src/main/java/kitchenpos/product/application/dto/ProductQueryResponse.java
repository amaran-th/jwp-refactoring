package kitchenpos.product.application.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductQueryResponse {

  private Long id;
  private String name;
  private BigDecimal price;

  public ProductQueryResponse(final Long id, final String name, final BigDecimal price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public ProductQueryResponse() {
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

  public static ProductQueryResponse of(final Product product) {
    return new ProductQueryResponse(product.getId(), product.getName(),
        product.getPrice().getValue());
  }
}