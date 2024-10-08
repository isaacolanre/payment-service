package com.payment.service.config.security;

import com.payment.service.enumerations.Namespace;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;

@Getter
@Slf4j
public class AppUserUsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 600L;
    private final Object principal;
    private Object credentials;

    private final Namespace namespace;

    public AppUserUsernamePasswordAuthenticationToken(Object principal, Object credentials, Namespace namespace) {
        super(List.of());
        this.principal = principal;
        this.credentials = credentials;
        this.namespace = namespace;
        this.setAuthenticated(false);
        log.info("Login Credentials principal credentials namespace {} {} {} ", principal, credentials, namespace);
    }

    public AppUserUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Namespace namespace) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.namespace = namespace;
        super.setAuthenticated(true);
    }

    public static AppUserUsernamePasswordAuthenticationToken unauthenticated(Object principal, Object credentials, Namespace namespace) {
        return new AppUserUsernamePasswordAuthenticationToken(principal, credentials, namespace);
    }

    public static AppUserUsernamePasswordAuthenticationToken authenticated(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Namespace namespace) {
        return new AppUserUsernamePasswordAuthenticationToken(principal, credentials, authorities, namespace);
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
