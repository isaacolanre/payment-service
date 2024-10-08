package com.payment.service.services;
import com.payment.service.enumerations.ProductType;
import com.payment.service.models.ServiceProvider;
import com.payment.service.repository.ServiceProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ServiceProviderServiceTest {

    @Mock
    private ServiceProviderRepository serviceProviderRepository;

    @InjectMocks
    private ServiceProviderService serviceProviderService;

    private List<ServiceProvider> serviceProviders;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ServiceProvider provider1 = new ServiceProvider();
        provider1.setName("Provider 1");

        ServiceProvider provider2 = new ServiceProvider();
        provider2.setName("Provider 2");

        serviceProviders = new ArrayList<>();
        serviceProviders.add(provider1);
        serviceProviders.add(provider2);
    }

    @Test
    void testGetServiceProvidersByProductType() {
        ProductType productType = ProductType.AIRTIME;
        when(serviceProviderRepository.findByProductType(productType)).thenReturn(serviceProviders);

        List<ServiceProvider> result = serviceProviderService.getServiceProvidersByProductType(productType);


        assertEquals(2, result.size());
        assertEquals("Provider 1", result.get(0).getName());
        assertEquals("Provider 2", result.get(1).getName());
    }
}

