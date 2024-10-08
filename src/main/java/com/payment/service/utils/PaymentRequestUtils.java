package com.payment.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.service.dto.request.ExpressPayPaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class PaymentRequestUtils {


    @Value("${api.private.key}")
    private  String privateKey;
    @Autowired
    private  ObjectMapper objectMapper;

    public  String generateRequestId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = dateFormat.format(new Date());
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "REQ-" + timestamp + "-" + randomPart;
    }


    public  String generateHmacHash(ExpressPayPaymentRequest apiRequest) {

        try {
            String jsonBody = objectMapper.writeValueAsString(apiRequest);


            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            sha512Hmac.init(keySpec);

            byte[] hashBytes = sha512Hmac.doFinal(jsonBody.getBytes(StandardCharsets.UTF_8));

            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }

            return hashString.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC SHA512 hash", e);
        }
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
