package com.payment.service.models;

import com.payment.service.enumerations.ProductType;
import com.payment.service.enumerations.ServiceProviderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
public class ServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String apiUrl;

    @Column(nullable = false)
    private String apiKey;

    @Column(nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    private ServiceProviderStatus status;

    @Column(nullable = true)
    private Integer timeout;

    @ElementCollection
    private List<String> supportedCurrencies;

    @Column(nullable = true)
    private String baseCurrency;

    @Column(nullable = true)
    private String supportContact;

    @ManyToMany(mappedBy = "serviceProviders")
    private List<Product> products;

    @OneToMany(mappedBy = "serviceProvider")
    private List<Transaction> transactions;

    @ElementCollection
    @CollectionTable(name = "service_provider_metadata", joinColumns = @JoinColumn(name = "service_provider_id"))
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, String> metadata;

    @Column(nullable = true)

    private LocalDateTime lastUpdated;
}
