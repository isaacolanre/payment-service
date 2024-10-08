package com.payment.service.config.security;


import com.payment.service.dto.AuthTokenDTO;
import com.payment.service.enumerations.Namespace;
import com.payment.service.exceptions.UserNotFoundException;
import com.payment.service.exceptions.TokenRefreshException;
import com.payment.service.repository.AuthorizationTokenRepository;
import com.payment.service.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorizationTokenService {

    private final Long refreshTokenDurationMs;

    private final AuthorizationTokenRepository authorizationTokenRepository;

    private final JwtUserAuthTokenUtil jwtUserAuthTokenUtil;
    @Autowired
    private UserDetailsService userService;

    public AuthorizationTokenService(@Value("${app.security.jwt.expiration}") long refreshTokenDurationMs,
                                     AuthorizationTokenRepository authorizationTokenRepository,
                                     JwtUserAuthTokenUtil jwtUserAuthTokenUtil) {
        this.refreshTokenDurationMs = refreshTokenDurationMs;
        this.authorizationTokenRepository = authorizationTokenRepository;
        this.jwtUserAuthTokenUtil = jwtUserAuthTokenUtil;
    }


    public Optional<AuthorizationToken> findByRefreshToken(String token) {
        return authorizationTokenRepository.findByRefreshToken(token);
    }

    public AuthorizationToken createAuthorizationToken(UUID userId, Namespace namespace) {
        var user = userService.getUserDetailsById(userId);

        AuthorizationToken authorizationToken = new AuthorizationToken(user.getId(), userId, jwtUserAuthTokenUtil.generateAuthenticationToken(userId, namespace),
                jwtUserAuthTokenUtil.generateRefreshToken(userId), Instant.now().plusMillis(refreshTokenDurationMs));

        return saveAuthorizationToken(authorizationToken);
    }

    public AuthorizationToken verifyExpiration(AuthorizationToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            authorizationTokenRepository.delete(token);
            throw new TokenRefreshException(token.getRefreshToken(), "Refresh token was expired. Please make a new signin request", true);
        }

        return token;
    }

    public AuthTokenDTO getAuthTokenByUserId(UUID userId) {

        var authToken = authorizationTokenRepository.findFirstByPublicIdOrderByIdDesc(userId)
                .orElseThrow(() -> new UserNotFoundException("Authorization token not found", false));

        return new AuthTokenDTO(authToken.getPublicId(), authToken.getAccessToken(), authToken.getRefreshToken());
    }

    public AuthorizationToken saveAuthorizationToken(AuthorizationToken token) {
        return authorizationTokenRepository.save(token);
    }

    public void invalidateAuthToken(UUID userId) {

        var authToken = authorizationTokenRepository.findFirstByPublicIdOrderByIdDesc(userId)
                .orElseThrow(() -> new UserNotFoundException("Authorization token not found", false));

        authToken.setAccessToken(null);

        authorizationTokenRepository.save(authToken);
    }

}
