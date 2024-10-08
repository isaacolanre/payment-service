package com.payment.service.config.security.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.payment.service.config.security.AuthorizationToken;
import com.payment.service.config.security.AuthorizationTokenService;
import com.payment.service.config.security.JwtTokenUtil;
import com.payment.service.dto.AppUserBasicProjectionDto;
import com.payment.service.dto.response.LoginResponse;
import com.payment.service.dto.response.UserDetailsDTO;
import com.payment.service.services.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import static com.payment.service.config.security.Authorities.TOKEN_REFRESH;


@Component
public class AppUserRefreshTokenFilter extends OncePerRequestFilter {

    private final AuthorizationTokenService authorizationTokenService;

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    private final JwtTokenUtil jwtTokenUtil;

    private final RSAPublicKey rsaPublicKey;

    private final UserService userService;

    public AppUserRefreshTokenFilter(AuthorizationTokenService authorizationTokenService, JwtTokenUtil jwtTokenUtil, RSAPublicKey rsaPublicKey, @Lazy UserService userService) {
        this.authorizationTokenService = authorizationTokenService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.rsaPublicKey = rsaPublicKey;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        if (!request.getRequestURL().toString().endsWith("/v2/oauth/refresh")) {
            chain.doFilter(request, response);
            return;
        }

        final String requestRefreshToken = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validateToken(requestRefreshToken, rsaPublicKey)) {
            chain.doFilter(request, response);
            return;
        }

        if (jwtTokenUtil.parseToken(requestRefreshToken).getAuthorities().contains(TOKEN_REFRESH))
            throw new AccessDeniedException("Invalid refresh token provided");

        UUID userPublicId = authorizationTokenService.findByRefreshToken(requestRefreshToken)
                .map(authorizationTokenService::verifyExpiration)
                .map(AuthorizationToken::getPublicId)
                .orElseThrow(() -> new AuthenticationException("Refresh token not found") {
                });

        AppUserBasicProjectionDto  userDetails = userService.getUserBasicProjections(userPublicId);

        var roleNames = userService.getUserRoleNames(userDetails.publicId());

        var authorizationToken = authorizationTokenService.createAuthorizationToken(userPublicId, userDetails.namespace());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final ServletResponseWrapper responseWrapper = (ServletResponseWrapper) response;
        responseWrapper.getResponse().getOutputStream().write(ow.writeValueAsString(
                LoginResponse.builder()
                        .accessToken(authorizationToken.getAccessToken())
                        .refreshToken(authorizationToken.getRefreshToken())
                        .user(new UserDetailsDTO(userDetails.publicId(),
                                userDetails.mobile(), userDetails.firstName(), userDetails.lastName(), userDetails.namespace(), roleNames,
                                userDetails.kycLevel(), userDetails.primaryAccountId(), userDetails.email()))
                        .build()
        ).getBytes());
    }

}
