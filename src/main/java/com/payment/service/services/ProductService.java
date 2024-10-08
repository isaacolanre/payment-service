package com.payment.service.services;

import com.payment.service.enumerations.ServiceProviderStatus;
import com.payment.service.exceptions.ProductNotFoundException;
import com.payment.service.exceptions.ServiceProviderNotFoundException;
import com.payment.service.models.Product;
import com.payment.service.models.ServiceProvider;
import com.payment.service.repository.ProductRepository;
import com.payment.service.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ServiceProvider getActiveServiceProviderForProduct(Long productId, String productName) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            return product.getServiceProviders().stream()
                    .filter((s-> s.getProductType().name().equals(productName) && (s.getStatus()
                            .name().equals(ServiceProviderStatus.ACTIVE.name()))
                    ))
                    .findFirst()
                    .orElseThrow(() -> new ServiceProviderNotFoundException("No active service provider found for this product."));
        }
        throw new ProductNotFoundException("Product not found.");
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}

