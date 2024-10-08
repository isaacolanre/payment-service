package com.payment.service.config.security.filters;


import com.google.common.base.Splitter;
import com.payment.service.config.security.AppUserUsernamePasswordAuthenticationToken;
import com.payment.service.config.security.AuthorizationTokenService;
import com.payment.service.config.security.providers.AppUsernamePasswordAuthenticationProvider;
import com.payment.service.dto.AppUserAuthProjectionDto;
import com.payment.service.enumerations.Namespace;
import com.payment.service.exceptions.AuthenticationException;
import com.payment.service.exceptions.GlobalExceptionHandler;
import com.payment.service.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.payment.service.dto.response.ServletResponse.getErrorResponse;
import static com.payment.service.dto.response.ServletResponse.getLoginResponse;

@Component
public class UsernamePasswordAuthenticationFilter extends OncePerRequestFilter {

    private final AppUsernamePasswordAuthenticationProvider authenticationManager;

    private final AuthorizationTokenService authorizationTokenService;

    private final UserService userService;

    @Qualifier("handlerExceptionResolver")
    private GlobalExceptionHandler resolver = new GlobalExceptionHandler();

    public UsernamePasswordAuthenticationFilter(AppUsernamePasswordAuthenticationProvider appUsernamePasswordAuthenticationProvider, AuthorizationTokenService authorizationTokenService,
                                                 UserService userService) {
        this.authenticationManager = appUsernamePasswordAuthenticationProvider;
        this.authorizationTokenService = authorizationTokenService;
        this.userService = userService;

    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        var urlMappings = List.of("/api/v1/customer/login",  "/api/v1/customer/authenticate/login");

        if (urlMappings.stream().noneMatch(m -> request.getRequestURL().toString().endsWith(m))) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            JSONObject loginJsonObject = new JSONObject(request.getReader().lines().collect(Collectors.joining()));

            var namespace = loginJsonObject.getString("namespace");
            var username = loginJsonObject.getString("email");
            var password = loginJsonObject.getString("password");

            Authentication authentication = authenticationManager.authenticate(
                    new AppUserUsernamePasswordAuthenticationToken(username, password, List.of(), Namespace.valueOf(namespace)));

            AppUserAuthProjectionDto user = (AppUserAuthProjectionDto) authentication.getDetails();

            var authorizationToken = authorizationTokenService.createAuthorizationToken(user.publicId(), user.namespace());

            var userDetails = userService.getUserBasicProjections(user.publicId());
            var roleNames = userService.getUserRoleNames(userDetails.publicId());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            var userNamespace = userService.getNamespace(userDetails.email(), userDetails.namespace());

            getLoginResponse(authorizationToken.getAccessToken(), authorizationToken.getRefreshToken(),
                    response, userDetails, userNamespace, roleNames);

        } catch (AuthenticationException | org.springframework.security.core.AuthenticationException e) {
            getErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                    resolver.handleAuthenticationException(e).getBody(), response);
        }

    }

    private Map<String, String> splitToMap(String in) {
        return Splitter.on(" ").withKeyValueSeparator("=").split(in);
    }

}
