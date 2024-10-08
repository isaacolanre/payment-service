package com.payment.service.repository;

import com.payment.service.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserId(Long userId);

    List<Account> findByUserPublicId(UUID userPublicId);

    Optional<Account> findAccountByLastname(String name);
}
