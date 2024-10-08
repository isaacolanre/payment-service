package com.payment.service.services;

import com.payment.service.dto.AppUserBasicProjectionDto;
import com.payment.service.dto.request.UserSignupRequest;
import com.payment.service.enumerations.*;
import com.payment.service.exceptions.UserAlreadyExistsException;
import com.payment.service.exceptions.UserNotFoundException;
import com.payment.service.models.Account;
import com.payment.service.models.AppUser;
import com.payment.service.models.Role;
import com.payment.service.repository.AccountRepository;
import com.payment.service.repository.AppUserRepository;
import com.payment.service.repository.AuthorizationTokenRepository;
import com.payment.service.repository.RoleRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.payment.service.enumerations.InternalExceptionCode.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthorizationTokenRepository authorizationTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PermissionService permissionService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private UserService userService;

    private AppUser user;
    private Role role;
    private Account account;
    private UUID userPublicId;
    private Long userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userPublicId = UUID.randomUUID();
        userId = 1L;
        user = AppUser.builder()
                .publicId(userPublicId)
                .username("user1")
                .email("user1@example.com")
                .password("encodedPassword")
                .loginTries(0)
                .build();

        role = Role.builder().roleName(RoleName.CUSTOMER).build();

        account = new Account();
        account.setUser(user);
        account.setAccountNumber(RandomStringUtils.randomNumeric(10));
        account.setBalance(BigDecimal.ZERO);
    }

    @Test
    void testGetUserDetailsById_Success() {
        when(userRepository.findByPublicId(userPublicId)).thenReturn(Optional.of(user));

        AppUser result = userService.getUserDetailsById(userPublicId);

        assertNotNull(result);
        assertEquals("user1", result.getUsername());
    }

    @Test
    void testGetUserDetailsById_UserNotFound() {
        when(userRepository.findByPublicId(userPublicId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserDetailsById(userPublicId));
        assertEquals(USER_NOT_FOUND.name(), exception.getMessage());
    }

    @Test
    void testRegisterUser_Success() {
        UserSignupRequest signupRequest = new UserSignupRequest("newuser", "newuser@example.com", "password123");
        when(userRepository.getUserByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByRoleName(RoleName.CUSTOMER)).thenReturn(Optional.of(role));
        when(permissionService.getTransactionPermissions()).thenReturn(new HashSet<>());

        AppUserBasicProjectionDto result = userService.registerUser(signupRequest);

        assertNotNull(result);
        assertEquals("newuser@example.com", result.email());
        verify(userRepository, times(1)).save(any(AppUser.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        UserSignupRequest signupRequest = new UserSignupRequest("existinguser", "existinguser@example.com", "password123");
        when(userRepository.getUserByEmail("existinguser@example.com")).thenReturn(Optional.of(user));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(signupRequest));
        assertEquals("Email is already in use!", exception.getMessage());
        verify(userRepository, never()).save(any(AppUser.class));
    }


    @Test
    void testGetAuthTokenByUserId_UserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getAuthTokenByUserId("unknown@example.com"));
        assertTrue(exception.getMessage().contains("Contact admin for account creation"));
    }

    @Test
    void testIncrementFailedLogins_UserBlocked() {
        user.setLoginTries(4);
        when(userRepository.findByPublicId(userPublicId)).thenReturn(Optional.of(user));

        userService.incrementFailedLogins(userPublicId, user.getLoginTries());

        verify(userRepository, times(1)).incrementFailedLoginCount(userPublicId);
        verify(userRepository, times(1)).updateUserBlockedUntilTime(any(LocalDateTime.class), eq(userPublicId));
    }

    @Test
    void testResetFailedLoginCount_Success() {
        userService.resetFailedLoginCount(userPublicId);
        verify(userRepository, times(1)).updateLoginAttempts(userId, 0);
    }
}
