package kitchenpos.ui.dto.request;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    private ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return Product.of(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
