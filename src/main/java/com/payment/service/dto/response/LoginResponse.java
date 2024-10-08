package com.payment.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private UserDetailsDTO user;

}
