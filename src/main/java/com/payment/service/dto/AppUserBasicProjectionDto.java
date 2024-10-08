package com.payment.service.dto;

import com.payment.service.enumerations.*;
import com.payment.service.models.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppUserBasicProjectionDto(UUID publicId, String firstName, String lastName, KycLevel kycLevel,
                                        Namespace namespace, UUID primaryAccountId, String mobile,
                                        String email, boolean emailValidated, UserStatus status, Gender gender, int loginTries,
                                        String userName,  boolean credentialExpired
                                       ) {
}
