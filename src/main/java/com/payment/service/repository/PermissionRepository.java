package com.payment.service.repository;

import com.payment.service.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // Custom query to find specific permissions
    @Query("SELECT p FROM Permission p WHERE p.description IN (:descriptions)")
    Set<Permission> findPermissionsByDescriptions(@Param("descriptions") List<String> descriptions);
}
