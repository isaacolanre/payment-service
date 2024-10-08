package com.payment.service.repository;

import com.payment.service.dto.AppUserAuthProjectionDto;
import com.payment.service.dto.AppUserBasicProjectionDto;
import com.payment.service.enumerations.Namespace;
import com.payment.service.enumerations.RoleName;
import com.payment.service.enumerations.UserStatus;
import com.payment.service.models.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, Long > {

    @Query("SELECT su FROM AppUser su WHERE su.status = 'ACTIVE' ")
    List<AppUser> getActiveUsers();

    @Query("SELECT u FROM AppUser u WHERE u.email = :email")
    Optional<AppUser> getUserByEmail(@Param("email") String email);

    @Query("SELECT su FROM AppUser su WHERE su.publicId = :publicId")
    Optional<AppUser> findByPublicId(UUID publicId);

    @Modifying
    @Query("UPDATE AppUser su SET su.status = ?2 WHERE su.publicId = ?1")
    void updateUserStatus( UUID publicId, boolean enabled);

    @Query("SELECT su FROM AppUser su WHERE CONCAT(su.firstName, ' ', su.lastName, ' ', su.email) LIKE %?1% ")
    Page<AppUser> findAllUserMatching(String keyword, Pageable pageable);

    Optional<AppUser> findByEmail(String email);


    Optional<AppUser> findByUsernameAndNamespace(String username, Namespace type);
    @Query("""
            SELECT new com.payment.service.dto.AppUserAuthProjectionDto(u.password, u.publicId, u.loginTries, u.namespace, u.firstName, u.lastName,u.email,
             u.emailValidated, u.mobile, u.blockedUntil, u.publicId)
            from AppUser u
            where (u.email = :username or u.mobile = :username)
                          and u.namespace = :type
            """)
    Optional<AppUserAuthProjectionDto> findByEmailAndNamespace(String username, Namespace type);
    @Query("""
            SELECT new com.payment.service.dto.AppUserAuthProjectionDto(u.password, u.publicId, u.loginTries, u.namespace, u.firstName, u.lastName,u.email,
             u.emailValidated, u.mobile, u.blockedUntil, u.publicId)
            from AppUser u
            where(u.email = :username or u.mobile = :username)
                         and u.namespace = :namespace
            """)
    Optional<AppUserAuthProjectionDto> findUserByCredentialAndNamespace(String username, Namespace namespace);

    @Modifying
    @Query("UPDATE AppUser u SET u.loginTries = :count WHERE u.id = :userId")
    void updateLoginAttempts(Long userId, int count);

    @Modifying
    @Query("UPDATE AppUser u SET u.loginTries = u.loginTries + 1 WHERE u.id = :userId")
    void incrementFailedLoginCount(UUID userId);

    @Modifying
    @Query("UPDATE AppUser u SET u.pinInputAttempts = u.pinInputAttempts + 1 WHERE u.id = :userId")
    void incrementFailedPinAttempt(UUID userId);

    @Query("""
            SELECT new com.payment.service.dto.AppUserBasicProjectionDto(u.publicId, u.firstName, u.lastName, u.kycLevel,
             u.namespace,
             u.primaryAccountPublicId, u.mobile, u.email, u.emailValidated, u.status, u.gender, u.loginTries, u.username,  u.credentialExpired)
            FROM AppUser u
            where u.email in :email and u.namespace = :namespace
            """)
    Optional<AppUserBasicProjectionDto> getUserBasicProjectionsByPhoneNumberAndNamespace(String email, Namespace namespace);

    @Query("""
            SELECT new com.payment.service.dto.AppUserBasicProjectionDto(u.publicId, u.firstName, u.lastName, u.kycLevel,
             u.namespace,
             u.primaryAccountPublicId, u.mobile, u.email, u.emailValidated, u.status, u.gender, u.loginTries, u.username,  u.credentialExpired)
            FROM AppUser u
            where u.email in :email and u.namespace = :namespace
            """)
    Optional<AppUserBasicProjectionDto> getUserBasicProjectionsByEmailNumberAndNamespace(String email, Namespace namespace);

    @Modifying
    @Query("UPDATE AppUser u SET u.blockedUntil = :blockedUntil WHERE u.id = :id")
    void updateUserBlockedUntilTime(LocalDateTime blockedUntil, UUID id);

    @Modifying
    @Query("UPDATE AppUser u SET u.password = :password WHERE u.id = :userId")
    void updatePassword(UUID userId, String password);

    @Query(nativeQuery = true, value = """
            select r.role_name from billpayment.user_role ur inner join billpayment.role r on r.id = ur.role_id where user_id = :userId
            """)
    Set<RoleName> getUserRoleNames(Long userId);

    @Query("""
            SELECT new com.payment.service.dto.AppUserBasicProjectionDto(u.publicId, u.firstName, u.lastName, u.kycLevel,
             u.namespace,
             u.primaryAccountPublicId, u.mobile, u.email, u.emailValidated, u.status, u.gender, u.loginTries, u.username,  u.credentialExpired)
            FROM AppUser u
            where u.publicId = :publicId
            """)
    Optional<AppUserBasicProjectionDto> getUserBasicProjectionsByPublicId(UUID publicId);


    Optional<AppUser> findByEmailEquals(String email);

    @Modifying
    @Query("UPDATE AppUser u SET u.status = :status, u.blockedAt = :blockedAt, u.blockedUntil = :blockedUntil WHERE u.id = :id")
    void updateUserBlockedStatus(UserStatus status, LocalDateTime blockedAt, LocalDateTime blockedUntil, UUID id);

}
