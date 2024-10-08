package com.payment.service.models;

import com.payment.service.enumerations.AccountStatus;
import com.payment.service.enumerations.AccountType;
import com.payment.service.enumerations.KycLevel;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ZERO;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
@Table(name = "user_account")
public class Account extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter(AccessLevel.NONE)
    private BigDecimal balance = ZERO;

    private String firstname;

    private String lastname;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private KycLevel accountKycLevel;

    private Currency currency = getDefaultCurrency();

    private BigDecimal overdraftLimit = ZERO;


    @Setter(AccessLevel.PRIVATE)
    private BigDecimal lockedBalance = ZERO;

    @Setter(AccessLevel.PRIVATE)
    private BigDecimal lienAmount = ZERO;

    @Column(unique = true)
    private String accountNumber;

    @Column(unique = true)
    private String pan;

    private String bvn;

    private UUID publicId;

    private boolean isPrimaryAccount;

    @Column(name = "parent_account")
    private Long parentAccount;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "account",  cascade = CascadeType.ALL)
    private Set<NotificationDevice> notificationDevices = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "parentAccount",  cascade = CascadeType.ALL)
    private Set<Account> childAccounts = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 32, columnDefinition = "varchar(32) default 'ACTIVE'")
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<FundingSource> fundingSources;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
    public static Currency getDefaultCurrency() {
        return Currency.getInstance("NGN");
    }

    public void setBalance(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

}
