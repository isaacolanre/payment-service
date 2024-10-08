package com.payment.service.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.payment.service.enumerations.Namespace;
import com.payment.service.models.Role;
import com.payment.service.services.UserDetailsService;
import com.payment.service.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static com.payment.service.config.security.Authorities.TOKEN_REFRESH;


@Slf4j
@Service
public class JwtUserAuthTokenUtil {

    private  String secretKey;
    @Autowired
    private UserDetailsService userService;

    public JwtUserAuthTokenUtil(UserService userService, @Value("${app.security.jwt.token.secret-key}") String secretKey) {
        this.secretKey = secretKey;
        this.userService = userService;
    }

    public String generateAuthenticationToken(UUID userId, Namespace namespace){
        var currentDate = new Date();
        var validity = new Date(currentDate.getTime() + 3_600_000);
        var authorities = new ArrayList<>();
        authorities.add(namespace.name());

        var user = userService.getUserDetailsById(userId);
        var roles = user.getUserRoles().stream()
                .map(role -> role.getRoleName().name())
                .toList();
        log.info("User {} has roles {}", userId, roles);
        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuer(user.getEmail())
                .withIssuedAt(currentDate)
                .withExpiresAt(validity)
                .withClaim("firstName", user.getFirstName())
                .withClaim("lastName", user.getLastName())
                .withClaim("email", user.getEmail())
                .withClaim("namespace", user.getNamespace().name())
                .withClaim("roles", roles)
                .withClaim("minKycLevel", user.getKycLevel().name())
                .withClaim("authorities", authorities)
                .sign(Algorithm.HMAC256(secretKey));
    }
    public String generateRefreshToken(UUID userId) {
        var authorities = new ArrayList<>();
        authorities.add(TOKEN_REFRESH.name());
        var user = userService.getUserDetailsById(userId);
        return JWT
                .create()
                .withSubject(String.valueOf(userId))
                .withIssuer(user.getEmail())
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plusMillis(12000000)))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public boolean validateToken(String token, RSAPublicKey secret) {

        try {
            buildJWTVerifier(secret).verify(token.replace("Bearer ", ""));
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new BadCredentialsException("Invalid jwt token provided");
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new CredentialsExpiredException("Your session has expired, please login again");
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new BadCredentialsException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new BadCredentialsException("JWT claims are empty");
        } catch (TokenExpiredException e){
            throw new CredentialsExpiredException("Your session has expired, please login again");
        } catch (Exception e){
            log.error("General exception: {}", e.getMessage());
            throw new BadCredentialsException("An error has occurred, we are working to resolve it");
        }
    }

    public JwtToken parseToken(String token){

        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));

        try {
            return new ObjectMapper().readValue(payload, JwtToken.class);
        } catch (IOException e) {
            throw new AccessDeniedException(e.getMessage(), e);
        }
    }

    private JWTVerifier buildJWTVerifier(RSAPublicKey rsaPublicKey) {
        var algo = Algorithm.RSA256(rsaPublicKey, null);
        return JWT.require(algo).build();
    }
}
