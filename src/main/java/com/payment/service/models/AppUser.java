package com.payment.service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.service.enumerations.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"password", "resetToken", "tries", "deletedAt"})
@Table(name = "users")
public class AppUser implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID publicId = UUID.randomUUID();

    @Column(nullable = false, length = 64)
    private String firstName;


    @Column(nullable = false, length = 64)
    private String lastName;

    @Column(nullable = false, length = 64)
    private String username;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 15)
    private String bvn;

    @JsonIgnore
    private int loginTries;

    private int pinInputAttempts;

    @Column(length = 64, unique = true)
    private String nickname;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String pin;

    @JsonIgnore
    private LocalDateTime pinCreatedAt;

    @Column(length = 56)
    private String email;

    @Column(length = 16)
    private String mobile;

    private UUID registeredBy;

    private String registeredByName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Namespace namespace;

    private String address;
    private String lga;
    private String state;
    private String zipCode;

    private UUID primaryAccountPublicId;
    private boolean accountValidated;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private KycLevel kycLevel = KycLevel.TIER_ZERO;

    @JsonIgnore
    @CreationTimestamp
    @Column(updatable = false, precision = 3, nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(insertable = false)
    private LocalDateTime activatedAt;

    @JsonIgnore
    @Column(insertable = false)
    private LocalDateTime blockedAt;

    private LocalDateTime blockedUntil;

    private String referralCode;
    private UUID referredBy;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ENABLED;

    private boolean credentialExpired;

    @JsonIgnore
    private String ussdPin;

    @JsonIgnore
    private LocalDateTime ussdPinCreatedAt;

    private boolean emailValidated;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> userRoles = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_permissions", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "permissions_id", referencedColumnName = "id")})
    private Set<Permission> permissions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<FundingSource> fundingSources;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Transaction> transactions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications;
    public Set<Permission> getPermissions() {
        return Optional.ofNullable(permissions).orElse(Set.of());
    }

    @Override
    public boolean isAccountNonExpired() {
        return getStatus() != UserStatus.ARCHIVED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getStatus() != UserStatus.BLOCKED || getBlockedAt() == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.isCredentialExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ENABLED == this.getStatus() || (this.getActivatedAt() != null &&
                this.getBlockedAt() == null);
    }

    public String getFullName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }
    public boolean hasRole(RoleName roleName) {
        return userRoles.stream().anyMatch(u -> u.getRoleName() == roleName);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(getNamespace().name()));
        getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getDescription())));
        return authorities;
    }
}

