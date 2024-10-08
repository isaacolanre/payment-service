package com.payment.service.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${app.security.jwt.token.secret-key}")
    private  String secretKey;

    public boolean isValidateToken(String token, RSAPublicKey secret) {

        try {
            buildJWTVerifier(secret).verify(token.replace("Bearer ", ""));
            return false;
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
        var algo = Algorithm.HMAC256(secretKey);
        return JWT.require(algo).build();
    }
}
