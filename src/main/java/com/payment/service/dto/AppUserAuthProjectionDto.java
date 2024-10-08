package com.payment.service.dto;



import com.payment.service.enumerations.Namespace;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppUserAuthProjectionDto(String password, UUID publicId, int loginTries, Namespace namespace, String firstName,
                                       String lastName, String email, boolean emailValidated, String phoneNumber,
                                       LocalDateTime blockedUntil, UUID accountPublicId) {
}
