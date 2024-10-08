package com.payment.service.services;


import com.payment.service.enumerations.ProductType;
import com.payment.service.enumerations.ServiceProviderStatus;
import com.payment.service.exceptions.ProductNotFoundException;
import com.payment.service.exceptions.ServiceProviderNotFoundException;
import com.payment.service.models.Product;
import com.payment.service.models.ServiceProvider;
import com.payment.service.repository.ProductRepository;
import com.payment.service.repository.ServiceProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ServiceProviderRepository serviceProviderRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ServiceProvider serviceProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setName(ProductType.AIRTIME);

        serviceProvider = new ServiceProvider();
        serviceProvider.setName("Test Service Provider");
        serviceProvider.setProductType(product.getName());
        serviceProvider.setStatus(ServiceProviderStatus.ACTIVE);

        Set<ServiceProvider> serviceProviders = new HashSet<>();
        serviceProviders.add(serviceProvider);
        product.setServiceProviders(serviceProviders);
    }

    @Test
    void testGetActiveServiceProviderForProduct_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        ServiceProvider result = productService.getActiveServiceProviderForProduct(1L, product.getName().name());

        assertNotNull(result);
        assertEquals(serviceProvider.getName(), result.getName());
        assertEquals(ServiceProviderStatus.ACTIVE, result.getStatus());
    }

    @Test
    void testGetActiveServiceProviderForProduct_ProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ProductNotFoundException thrown = assertThrows(ProductNotFoundException.class, () -> {
            productService.getActiveServiceProviderForProduct(1L, "Test Product");
        });

        assertEquals("Product not found.", thrown.getMessage());
    }

    @Test
    void testGetActiveServiceProviderForProduct_ServiceProviderNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        product.getServiceProviders().clear();  // No active service provider

        ServiceProviderNotFoundException thrown = assertThrows(ServiceProviderNotFoundException.class, () -> {
            productService.getActiveServiceProviderForProduct(1L, "Test Product");
        });

        assertEquals("No active service provider found for this product.", thrown.getMessage());
    }

    @Test
    void testGetAllProducts() {
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(product);

        when(productRepository.findAll()).thenReturn(mockProducts);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals(product.getName(), result.get(0).getName());
    }
}
