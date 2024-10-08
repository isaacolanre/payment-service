package com.payment.service.repository;

import com.payment.service.enumerations.ProductType;
import com.payment.service.models.Product;
import com.payment.service.models.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


}
