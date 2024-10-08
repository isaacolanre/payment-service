package com.payment.service.api;

import com.payment.service.enumerations.ProductType;
import com.payment.service.models.ServiceProvider;
import com.payment.service.services.ServiceProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service-providers")
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    public ServiceProviderController(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceProvider>> getServiceProvidersByProductType(@RequestParam ProductType productType) {
        List<ServiceProvider> serviceProviders = serviceProviderService.getServiceProvidersByProductType(productType);
        return ResponseEntity.ok(serviceProviders);
    }
}

