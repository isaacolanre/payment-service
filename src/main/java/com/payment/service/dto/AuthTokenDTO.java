package com.payment.service.dto;

import java.util.UUID;

public record AuthTokenDTO(UUID userId, String accessToken, String refreshToken){

}
