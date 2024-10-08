package com.payment.service.models;

import com.payment.service.enumerations.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID publicId;

    @Column(nullable = false)
    private String transactionType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = true)
    private String transactionReference;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = true)
    private String paymentMethod;

    @Column(nullable = true)
    private String paymentProvider;

    @Column(nullable = true)
    private BigDecimal fee;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private LocalDateTime completedAt;

    @Column(nullable = true)
    private String errorMessage;

    @Column(nullable = true)
    private String errorCode;

    @Column(nullable = true)
    private String externalTransactionId;

    @ElementCollection
    @CollectionTable(name = "transaction_metadata", joinColumns = @JoinColumn(name = "transaction_id"))
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, String> metadata;

    @Column(nullable = true)
    private Long linkedTransactionId;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "geo_location_id", referencedColumnName = "id")
    private GeoLocation geoLocation;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private ServiceProvider serviceProvider;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
