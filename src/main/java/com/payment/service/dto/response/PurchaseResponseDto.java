package com.payment.service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PurchaseResponseDto {

    private String requestId;
    private String referenceId;
    private String responseCode;
    private String responseMessage;
    private String data;
}
