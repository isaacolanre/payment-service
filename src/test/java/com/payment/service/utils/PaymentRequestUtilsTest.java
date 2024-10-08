package com.payment.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.service.dto.request.ExpressPayPaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class PaymentRequestUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PaymentRequestUtils paymentRequestUtils;

    private String privateKey = "testPrivateKey";

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        MockitoAnnotations.openMocks(this);

        Field privateKeyField = PaymentRequestUtils.class.getDeclaredField("privateKey");
        privateKeyField.setAccessible(true);
        privateKeyField.set(paymentRequestUtils, privateKey);
    }

    @Test
    void testGenerateRequestId() {
        String requestId = paymentRequestUtils.generateRequestId();

        assertNotNull(requestId);
        assertTrue(requestId.startsWith("REQ-"));
        assertEquals(30, requestId.length());
    }

    @Test
    void testGenerateHmacHash_Success() throws Exception {
        ExpressPayPaymentRequest apiRequest = new ExpressPayPaymentRequest();
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"sample\":\"data\"}");

        String hmacHash = paymentRequestUtils.generateHmacHash(apiRequest);

        assertNotNull(hmacHash);
        assertEquals(128, hmacHash.length());
    }

    @Test
    void testGenerateHmacHash_Exception() throws Exception {
        ExpressPayPaymentRequest apiRequest = new ExpressPayPaymentRequest();
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentRequestUtils.generateHmacHash(apiRequest));
        assertTrue(exception.getMessage().contains("Error generating HMAC SHA512 hash"));
    }
}
