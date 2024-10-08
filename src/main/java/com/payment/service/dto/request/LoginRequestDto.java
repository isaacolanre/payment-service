package com.payment.service.dto.request;

import com.payment.service.enumerations.Namespace;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequestDto {
    private String email;
    private String password;
    private Namespace namespace;
}

