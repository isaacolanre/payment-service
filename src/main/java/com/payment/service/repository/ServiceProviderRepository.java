package com.payment.service.repository;

import com.payment.service.enumerations.ProductType;
import com.payment.service.models.Product;
import com.payment.service.models.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {


    @Query("SELECT sp FROM ServiceProvider sp WHERE sp.productType = :productType")
    List<ServiceProvider> findByProductType(@Param("productType") ProductType productType);

    @Query("SELECT sp FROM ServiceProvider sp JOIN sp.products p WHERE sp.status = 'ACTIVE' AND p.id = :productId")
    Optional<ServiceProvider> findActiveServiceProviderByProduct(@Param("productId") Long productId);
}
