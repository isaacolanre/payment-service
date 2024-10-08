package com.payment.service.repository;


import com.payment.service.config.security.AuthorizationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorizationTokenRepository extends JpaRepository<AuthorizationToken, Long> {

  Optional<AuthorizationToken> findByRefreshToken(String token);

  Optional<AuthorizationToken> findFirstByPublicIdOrderByIdDesc(UUID user);

}
