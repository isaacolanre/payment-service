package com.payment.service.dto.response;



import com.payment.service.enumerations.KycLevel;
import com.payment.service.enumerations.Namespace;
import com.payment.service.enumerations.RoleName;

import java.util.Set;
import java.util.UUID;

public record UserDetailsDTO(UUID id,
                             String mobile,
                             String firstName,
                             String lastName,
                             Namespace role,
                             Set<RoleName> roles,
                             KycLevel kycLevel,

                             UUID accountPublicId, String email) {
}
