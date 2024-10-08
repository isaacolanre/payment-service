package com.payment.service.config.security.providers;


import com.payment.service.config.security.AppUserUsernamePasswordAuthenticationToken;
import com.payment.service.dto.AppUserAuthProjectionDto;
import com.payment.service.exceptions.AuthenticationException;
import com.payment.service.repository.AppUserRepository;
import com.payment.service.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AppUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;

    private final AppUserRepository appUserRepository;

    private final UserService userService;

    public AppUsernamePasswordAuthenticationProvider(PasswordEncoder passwordEncoder, AppUserRepository appUserRepository, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
        this.userService = userService;
    }


    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        AppUserUsernamePasswordAuthenticationToken auth = (AppUserUsernamePasswordAuthenticationToken) authentication;

        var user = this.appUserRepository.findUserByCredentialAndNamespace(determineUsername(authentication), auth.getNamespace())
                .orElseThrow(() -> new AuthenticationException(String.format("%s not found. Contact admin to create account.", authentication.getPrincipal()), false));

        additionalAuthenticationChecks(user.password(), user.publicId(), user.loginTries(), (AppUserUsernamePasswordAuthenticationToken) authentication);

        userService.resetFailedLoginCount(user.publicId());

        return this.createSuccessAuthentication(user, authentication);
    }

    protected Authentication createSuccessAuthentication(AppUserAuthProjectionDto principal, Authentication authentication) {
        AppUserUsernamePasswordAuthenticationToken result = AppUserUsernamePasswordAuthenticationToken
                .authenticated(principal, authentication.getCredentials(), userService.getAllUserRoles(principal.publicId()), principal.namespace());

        result.setDetails(principal);
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AppUserUsernamePasswordAuthenticationToken.class);
    }

    private String determineUsername(Authentication authentication) {
        return authentication.getPrincipal() == null ? "NONE_PROVIDED" : authentication.getName();
    }

    protected void additionalAuthenticationChecks(String password, UUID userId, int loginTries,
                                                  AppUserUsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            log.debug("Failed to authenticate since no credentials provided");
            throw new AuthenticationException("Bad credentials", false);
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            if (!(passwordEncoder.matches(presentedPassword, password))) {
                userService.incrementFailedLogins(userId, loginTries);
                throw new AuthenticationException(String.format("Email Number or Password Mismatch. Your account would be blocked after %s Invalid Login tries.",
                        UserService.LOGIN_LIMIT - loginTries % UserService.LOGIN_LIMIT), false);
            }
        }
    }

}
