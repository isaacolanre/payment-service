package com.payment.service.config.security.filters;


import com.payment.service.config.security.AppUserUsernamePasswordAuthenticationToken;
import com.payment.service.config.security.JwtToken;
import com.payment.service.config.security.JwtTokenUtil;
import com.payment.service.enumerations.Namespace;
import com.payment.service.exceptions.AuthenticationException;
import com.payment.service.exceptions.GlobalExceptionHandler;
import com.payment.service.services.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;

import static com.payment.service.dto.response.ServletResponse.getErrorResponse;


@Slf4j
@Service
@DependsOn("rsaPublicKey")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtUserAuthTokenUtil;
    private final RSAPublicKey rsaPublicKey;

    private final GlobalExceptionHandler exceptionHandler;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtUserAuthTokenUtil, RSAPublicKey rsaPublicKey,
                                   GlobalExceptionHandler exceptionHandler, UserService userService) {
        this.jwtUserAuthTokenUtil = jwtUserAuthTokenUtil;
        this.rsaPublicKey = rsaPublicKey;
        this.exceptionHandler = exceptionHandler;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();

        try {
            JwtToken parsedToken = jwtUserAuthTokenUtil.parseToken(token);

            var persistedToken = userService.getAuthTokenByUserId(parsedToken.getSub()).accessToken();
            if (!token.equalsIgnoreCase(persistedToken)) {
                throw new BadCredentialsException("Invalid jwt token provided, please login again");
            }
            if (!jwtUserAuthTokenUtil.validateToken(token, rsaPublicKey)) {
                filterChain.doFilter(request, response);
                return;
            }
            var authentication = new AppUserUsernamePasswordAuthenticationToken(parsedToken.getSub(),
                    token, parsedToken
                    .getAuthorities()
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList(),
                    Namespace.valueOf(parsedToken.getNamespace()));

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            getErrorResponse(HttpStatus.UNAUTHORIZED.value(), exceptionHandler.handleAuthenticationException(e).getBody(), response);
        }
    }

}
