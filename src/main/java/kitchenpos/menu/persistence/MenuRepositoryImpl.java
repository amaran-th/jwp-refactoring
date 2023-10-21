package kitchenpos.menu.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.entity.MenuEntity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepositoryImpl implements MenuRepository {

  private final MenuDao menuDao;
  private final MenuProductDao menuProductDao;

  public MenuRepositoryImpl(final MenuDao menuDao, final MenuProductDao menuProductDao) {
    this.menuDao = menuDao;
    this.menuProductDao = menuProductDao;
  }

  @Override
  public Menu save(final Menu entity) {
    final MenuEntity savedMenu = menuDao.save(MenuEntity.from(entity));

    final List<MenuProduct> savedMenuProducts = saveMenuProducts(entity, savedMenu);
    return savedMenu.toMenu(savedMenuProducts);
  }

  private List<MenuProduct> saveMenuProducts(final Menu entity, final MenuEntity savedMenu) {
    final List<MenuProduct> savedMenuProducts = new ArrayList<>();
    for (final MenuProduct menuProduct : entity.getMenuProducts()) {
      final MenuProduct savedMenuProduct = new MenuProduct(savedMenu.getId(),
          menuProduct.getProductId(), menuProduct.getQuantity());

      savedMenuProducts.add(menuProductDao.save(savedMenuProduct));
    }
    return savedMenuProducts;
  }

  @Override
  public List<Menu> findAll() {
    return menuDao.findAll()
        .stream()
        .map(menu -> menu.toMenu(menuProductDao.findAllByMenuId(menu.getId())))
        .collect(Collectors.toList());
  }

  @Override
  public long countByIdIn(final List<Long> ids) {
    return menuDao.countByIdIn(ids);
  }

}