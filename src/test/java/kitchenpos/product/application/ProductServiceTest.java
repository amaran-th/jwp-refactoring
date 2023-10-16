package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.helper.ServiceIntegrateTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.persistence.ProductDao;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceIntegrateTest {

  @Autowired
  private ProductService productService;
  @Autowired
  private ProductDao productDao;

  @Test
  @DisplayName("상품을 등록할 수 있다.")
  void create_success() {
    //given, when
    final String name = "아마란스 무침";
    final Product savedProduct = productService.create(
        new Product(name, BigDecimal.valueOf(10000)));
    final Product actual = productDao.findById(savedProduct.getId()).get();

    //then
    Assertions.assertAll(
        () -> assertThat(actual).isNotNull(),
        () -> assertThat(actual.getName()).isEqualTo(name)
    );
  }

  @Test
  @DisplayName("상품을 등록할 때 상품의 가격이 0보다 작을 경우 예외를 반환한다.")
  void create_fail_negative_price() {
    //given
    final Product product = productService.create(new Product("아마란스 샐러드", BigDecimal.valueOf(-1)));

    //when
    final ThrowingCallable actual = () -> productService.create(product);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("상품을 등록할 때 상품의 가격이 null일 경우 예외를 반환한다.")
  void create_fail_null_price() {
    //given
    final Product product = new Product("진달래떡", BigDecimal.valueOf(-1));

    //when
    final ThrowingCallable actual = () -> productService.create(product);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }


  @Test
  @DisplayName("등록된 상품 목록을 조회할 수 있다.")
  void list_success() {
    // given
    final String name = "화전";
    final int beforeSize = productService.list().size();
    productService.create(new Product(name, BigDecimal.valueOf(10000)));

    // when
    final List<Product> actual = productService.list();
    final List<String> actualNames = actual.stream()
        .map(Product::getName)
        .collect(Collectors.toList());

    //then
    Assertions.assertAll(
        () -> assertThat(actual).hasSize(beforeSize + 1),
        () -> assertThat(actualNames).contains(name)
    );

  }
}