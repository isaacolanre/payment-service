package com.payment.service.config.security;

import com.payment.service.dto.AppUserBasicProjectionDto;
import com.payment.service.enumerations.Namespace;
import com.payment.service.exceptions.AuthenticationException;
import com.payment.service.repository.AppUserRepository;
import com.payment.service.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordService {

    public static final String CANNOT_COMPLETE_THE_ACTION_PLEASE_START_FROM_THE_SCRATCH_AGAIN = "Cannot complete the action, please start from the scratch again";
    private static final int LOGIN_LIMIT = 5;

    private final UserService userService;


    private final PasswordEncoder passwordEncoder;

    private final PasswordValidator passwordValidator;

//    private final UserEventService userEventService;

    private final AppUserRepository userRepository;


    public AppUserBasicProjectionDto getUserDetails(String credential, Namespace namespace) {

        return userRepository.getUserBasicProjectionsByEmailNumberAndNamespace(credential, namespace)
                .orElseThrow(() -> new AuthenticationException("An OTP will be sent to the provided email address if it exists.", false));
    }




    public void verifyPassword(String password, String userPassword, UUID userId, int loginTries) {
        if (!(passwordEncoder.matches(password, userPassword))) {
            userService.incrementFailedLogins(userId, loginTries);
            throw new AuthenticationException(String.format("Phone Number or Password Mismatch. Your account would be blocked after %s Invalid Login tries.",
                    LOGIN_LIMIT - loginTries % LOGIN_LIMIT), false);
        }

    }

}
