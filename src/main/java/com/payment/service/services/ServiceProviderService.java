package com.payment.service.services;

import com.payment.service.enumerations.ProductType;
import com.payment.service.models.ServiceProvider;
import com.payment.service.repository.ServiceProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;

    public ServiceProviderService(ServiceProviderRepository serviceProviderRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
    }

    public List<ServiceProvider> getServiceProvidersByProductType(ProductType productType) {
        return serviceProviderRepository.findByProductType(productType);
    }
}

