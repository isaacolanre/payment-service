package com.payment.service.api;

import com.payment.service.models.Product;
import com.payment.service.models.ServiceProvider;
import com.payment.service.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}/service-provider")
    public ResponseEntity<ServiceProvider> getActiveServiceProvider(
            @PathVariable Long productId,
            @RequestParam String productName) {
        ServiceProvider serviceProvider = productService.getActiveServiceProviderForProduct(productId, productName);
        return ResponseEntity.ok(serviceProvider);
    }
}
