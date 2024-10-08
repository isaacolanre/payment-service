package com.payment.service.enumerations;

import lombok.Getter;

@Getter
public enum MessageChannel {
    SMS("sms"),
    EMAIL("email"),
    PUSH("firebase"),
    IN_APP("inapp"),
    WHATSAPP("whatsapp"),
    TELEGRAM("telegram");

    private final String description;

    MessageChannel(String description) {
        this.description = description;
    }
}
