package kitchenpos.product.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid final ProductRequest productRequest) {
        final ProductResponse response = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok().body(productService.findAll());
    }
}