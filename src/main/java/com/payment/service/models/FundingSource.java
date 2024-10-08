package com.payment.service.models;

import com.payment.service.enumerations.FundingSourceStatus;
import com.payment.service.enumerations.FundingSourceType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Table(name = "funding_source")
public class FundingSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @Enumerated(EnumType.STRING)
    private FundingSourceType type;

    private UUID accountPublicId;

    private UUID publicId;

    private String institutionCode;

    private boolean supportsDebit;

    private String accountIdentifier;

    private String fundingSourceProvider;

    @Enumerated(EnumType.STRING)
    private FundingSourceStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    public  FundingSource(){}
    public FundingSource(UUID accountPublicId, String institutionCode, FundingSourceType type, boolean supportsDebit) {
        this.accountPublicId = accountPublicId;
        this.publicId = UUID.randomUUID();
        this.institutionCode = institutionCode;
        this.type = type;
        this.supportsDebit = supportsDebit;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FundingSource that)) return false;
        return type == that.type && Objects.equals(accountPublicId, that.accountPublicId) && Objects.equals(publicId, that.publicId) && Objects.equals(institutionCode, that.institutionCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, accountPublicId, publicId, institutionCode);
    }
}
