package com.payment.service.models;

import com.payment.service.enumerations.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;


    @CreationTimestamp
    @Column(updatable = false, precision = 3, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private List<Permission> permissions = new ArrayList<>();

    @ManyToMany(mappedBy = "userRoles")
    private Set<AppUser> users = new HashSet<>();

    public List<Permission> getPermissions() {
        return permissions;
    }

    public Role(RoleName roleName, LocalDateTime createdAt) {
        this.roleName = roleName;
        this.createdAt = createdAt;
    }

    @Override
    public String getAuthority() {
        return null;
    }

    public void setUser(AppUser user) {
        getUsers().add(user);
    }
}
