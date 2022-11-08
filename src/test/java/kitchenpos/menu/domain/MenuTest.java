package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

class MenuTest {

    @DisplayName("모든 메뉴 상품이 추가된 메뉴를 생성한다")
    @Test
    void addMenuProducts() {
        BigDecimal price = new BigDecimal(22_000);
        MenuProduct menuProduct1 = new MenuProduct(new Product("치킨", BigDecimal.valueOf(1_9000)), 1);
        MenuProduct menuProduct2 = new MenuProduct(new Product("치즈볼", BigDecimal.valueOf(5_000)), 1);
        List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);

        Menu menu = new Menu("치킨 세트", price, new MenuGroup("한마리 치킨"), menuProducts);

        assertThat(menu.getMenuProducts()).hasSize(2);
    }

    @DisplayName("모든 메뉴를 생성할 때, 메뉴 가격이 상품가격의 합보다 클 경우 예외가 발생한다")
    @Test
    void validatePriceIsCheaperThanSum() {
        BigDecimal price = new BigDecimal(25_000);
        MenuProduct menuProduct1 = new MenuProduct(new Product("치킨", BigDecimal.valueOf(1_9000)), 1);
        MenuProduct menuProduct2 = new MenuProduct(new Product("치즈볼", BigDecimal.valueOf(5_000)), 1);
        List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);

        assertThatThrownBy(() -> new Menu("치킨 세트", price, new MenuGroup("한마리 치킨"), menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격은 상품 가격의 합보다 적어야 합니다.");
    }
}