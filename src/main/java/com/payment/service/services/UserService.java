package com.payment.service.services;

import com.payment.service.dto.AppUserBasicProjectionDto;
import com.payment.service.dto.AuthTokenDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Currency;

import static com.payment.service.enumerations.InternalExceptionCode.USER_NOT_FOUND;


@Service
public class UserService implements UserDetailsService{

    private final AppUserRepository userRepository;

    private final RoleRepository roleRepository;
    private final AuthorizationTokenRepository authorizationTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final PermissionService permissionService;

    private final AccountRepository accountRepository;
    public static final int LOGIN_LIMIT = 5;

    public UserService(AppUserRepository userRepository, RoleRepository roleRepository, AuthorizationTokenRepository authorizationTokenRepository, PasswordEncoder passwordEncoder, PermissionService permissionService, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorizationTokenRepository = authorizationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionService = permissionService;
        this.accountRepository = accountRepository;
    }

    public void incrementFailedLogins(UUID userId, int loginTries) {

        userRepository.incrementFailedLoginCount(userId);

        if ((loginTries + 1) % LOGIN_LIMIT == 0)
            userRepository.updateUserBlockedUntilTime(LocalDateTime.now().plusMinutes(15), userId);
    }

    public AppUser getUserDetailsById(UUID id) {
        return userRepository.findByPublicId(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.name(), false));
    }


    public AuthTokenDTO getAuthTokenByUserId(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Contact admin for account creation" + email, false));
        return getAuthTokenByUserId(user.getPublicId());
    }

    @Transactional
    public void resetFailedLoginCount(UUID userId) {
        AppUser user = userRepository.findByPublicId(userId).orElseThrow(()->new UserNotFoundException("User not found for the login attempts operation" + userId, false));
        userRepository.updateLoginAttempts(user.getId(), 0);
    }

    public Set<Role> getAllUserRoles(UUID userId) {
        AppUser user = userRepository.findByPublicId(userId).orElseThrow(()->new UserNotFoundException("User not found for the login attempts operation" + userId, false));
        return roleRepository.findAllUserRolesById(user.getId());
    }

    public AppUserBasicProjectionDto getUserBasicProjections(UUID userId) {

        return userRepository.getUserBasicProjectionsByPublicId(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.name(), false));

    }

    public Set<RoleName> getUserRoleNames(UUID userPublicId) {
        AppUser user = userRepository.findByPublicId(userPublicId).orElseThrow(()->new UserNotFoundException("User not found for the login attempts operation" + userPublicId, false));
        return userRepository.getUserRoleNames(user.getId());
    }

    public Namespace getNamespace(String username, Namespace namespace) {

        var user = userRepository.findByEmailAndNamespace(username, namespace)
                .orElseThrow(() -> new UserNotFoundException("Can't authenticate successfully. Login again.", false));

        return user.namespace();
    }

    public AuthTokenDTO getAuthTokenByUserId(UUID userId) {

        var authToken = authorizationTokenRepository.findFirstByPublicIdOrderByIdDesc(userId)
                .orElseThrow(() -> new UserNotFoundException("Authorization token not found", false));

        return new AuthTokenDTO(authToken.getPublicId(), authToken.getAccessToken(), authToken.getRefreshToken());
    }

    @Transactional
    public AppUserBasicProjectionDto registerUser(UserSignupRequest userSignupRequest) {
        userRepository.getUserByEmail(userSignupRequest.getUsername())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("Email is already in use!", false);
                });

        AppUser user = AppUser.builder()
                .publicId(UUID.randomUUID())
                .username(userSignupRequest.getUsername())
                .email(userSignupRequest.getEmail())
                .password(passwordEncoder.encode(userSignupRequest.getPassword()))
                .firstName(RandomStringUtils.randomAlphabetic(5))
                .lastName(RandomStringUtils.randomAlphabetic(5))
                .dateOfBirth(null)
                .gender(Gender.M)
                .bvn(RandomStringUtils.randomNumeric(11))
                .mobile(RandomStringUtils.randomNumeric(10))
                .address(null)
                .lga(null)
                .state(null)
                .zipCode(null)
                .registeredBy(null)
                .namespace(Namespace.CUSTOMER)
                .emailValidated(false)
                .accountValidated(false)
                .kycLevel(KycLevel.TIER_ZERO)
                .build();


        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(RandomStringUtils.randomNumeric(10));
        account.setFirstname(user.getFirstName());
        account.setLastname(user.getLastName());
        account.setBalance(BigDecimal.ZERO);
        account.setAccountType(AccountType.CUSTOMER);
        account.setAccountKycLevel(KycLevel.TIER_ONE);
        account.setCurrency(Currency.getInstance("NGN"));
        account.setOverdraftLimit(BigDecimal.ZERO);
        account.setPan(RandomStringUtils.randomNumeric(10));
        account.setPublicId(UUID.randomUUID());
        account.setPrimaryAccount(true);
        account.setAccountStatus(AccountStatus.ACTIVE);

        Role defaultRole = roleRepository.findByRoleName(RoleName.valueOf("CUSTOMER"))
                .orElseThrow(() -> new UserNotFoundException("Default role not found"));
        user.setUserRoles(Set.of(defaultRole));

        user.setPermissions(permissionService.getTransactionPermissions());

        userRepository.save(user);
        accountRepository.save(account);
        return new AppUserBasicProjectionDto(
                user.getPublicId(),
                user.getFirstName(),
                user.getLastName(),
                user.getKycLevel(),
                user.getNamespace(),
                account.getPublicId(),
                user.getMobile(),
                user.getEmail(),
                user.isEmailValidated(),
                user.getStatus(),
                user.getGender(),
                user.getLoginTries(),
                user.getUsername(),
                user.isCredentialExpired()
        );


    }

    public Optional<AppUserBasicProjectionDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::convertToDto);
    }

    private AppUserBasicProjectionDto convertToDto(AppUser user) {
        Account userAccount = null;
        if (Objects.nonNull(user.getPrimaryAccountPublicId())) {
            userAccount = accountRepository.findByUserId(user.getId()).orElse(null);
        }
        return new AppUserBasicProjectionDto(
                user.getPublicId(),
                user.getFirstName(),
                user.getLastName(),
                user.getKycLevel(),
                user.getNamespace(),
                userAccount != null ? userAccount.getPublicId() : null,
                user.getMobile(),
                user.getEmail(),
                user.isEmailValidated(),
                user.getStatus(),
                user.getGender(),
                user.getLoginTries(),
                user.getEmail(),
                user.isCredentialExpired()
        );
    }
}
