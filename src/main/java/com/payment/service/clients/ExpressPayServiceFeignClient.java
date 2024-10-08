package com.payment.service.clients;

import com.payment.service.dto.request.ExpressPayPaymentRequest;
import com.payment.service.dto.request.PurchaseRequestDto;
import com.payment.service.dto.response.PurchaseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "billerServiceClient", url = "${external.service.url}")

public interface ExpressPayServiceFeignClient {

    @PostMapping("/airtime/fulfil")
    PurchaseResponseDto purchaseAirtime(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("PaymentHash") String paymentHash,
            @RequestHeader("channel") String channel,
            @RequestBody ExpressPayPaymentRequest purchaseRequest);
}
