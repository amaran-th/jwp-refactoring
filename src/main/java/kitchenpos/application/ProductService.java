package kitchenpos.application;

import java.util.List;
import kitchenpos.application.request.ProductCreateRequest;
import kitchenpos.dao.product.ProductDao;
import kitchenpos.domain.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        return productDao.save(new Product(request.getName(), request.getPrice()));
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
