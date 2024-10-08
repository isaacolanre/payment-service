package com.payment.service.api;


import com.payment.service.dto.request.PurchaseRequestDto;
import com.payment.service.services.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase/airtime")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<String> purchaseProduct(@RequestBody PurchaseRequestDto purchaseRequest) {

        return ResponseEntity.ok(purchaseService.purchaseProduct(purchaseRequest));
    }
}
