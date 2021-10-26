package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("메뉴 서비스 테스트")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        Menu created = menuService.create(menu());

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getName()).isEqualTo("menu"),
            () -> assertThat(created.getMenuProducts().size()).isEqualTo(1)
        );
    }

    @DisplayName("메뉴 리스트를 불러온다.")
    @Test
    void list() {
        menuService.create(menu());

        List<Menu> menus = menuService.list();
        assertThat(menus.size()).isEqualTo(1);
    }
}