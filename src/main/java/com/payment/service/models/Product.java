package com.payment.service.models;

import com.payment.service.enumerations.ProductType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductType name;

    private String description;

    private BigDecimal value;

    private String uniqueCode;

    @ManyToMany
    @JoinTable(
            name = "product_service_provider",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "service_provider_id")
    )
    private Set<ServiceProvider> serviceProviders;

    @OneToMany(mappedBy = "product")
    private Set<Transaction> transactions;
}
