package com.payment.service.enumerations;

import java.util.Arrays;
import java.util.Optional;

public enum UserStatus {
    ENABLED,
    BLOCKED,
    ARCHIVED,
    LOCKED;

    private static final UserStatus[] copyOfValues = values();

    public static Optional<UserStatus> nameExists(String name) {
        return Arrays.stream(copyOfValues)
                .filter(v -> v.name().equals(name))
                .findFirst();
    }
}
