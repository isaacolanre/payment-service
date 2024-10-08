package com.payment.service.config.security;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "authorization_token")
@NoArgsConstructor
public class AuthorizationToken implements Serializable {

  public AuthorizationToken(Long userId, String accessToken, String refreshToken, Instant expiryDate) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiryDate = expiryDate;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private Long userId;

  private UUID publicId;

  @Column(nullable = false, unique = true, length = 2048)
  private String accessToken;

  @Column(nullable = false, unique = true, length = 2048)
  private String refreshToken;

  @Column(name = "expiry_date", nullable = false)
  private Instant expiryDate;

  public AuthorizationToken(Long userId, UUID publicId, String accessToken, String refreshToken, Instant expiryDate) {
    this.userId = userId;
    this.publicId = publicId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiryDate = expiryDate;
  }

  public String toString() {
    return "AuthorizationToken{" +
            "publicId=" + publicId +
            ", accessToken='" + accessToken + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            ", expiryDate=" + expiryDate +
            '}';
  }
}
