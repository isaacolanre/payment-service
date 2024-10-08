package com.payment.service.enumerations;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum KycLevel {
    TIER_ZERO(0, KycStatus.NOT_STARTED),
    TIER_ONE(1, KycStatus.PENDING),
    TIER_TWO(2, KycStatus.PENDING),
    TIER_THREE(3, KycStatus.VERIFIED),

    TIER_ONE_PENDING(0, KycStatus.NOT_STARTED),
    TIER_TWO_PENDING(1, KycStatus.PENDING),
    SUSPENSE_ACCOUNT(7, KycStatus.VERIFIED),

    TIER_THREE_PENDING(2, KycStatus.PENDING),

    TIER_THREE_PREMIUM(3, KycStatus.VERIFIED),
    BILLER_ACCOUNT(8, KycStatus.VERIFIED);

    private final int level;

    private final KycStatus status;

    KycLevel(int i, KycStatus status) {
        level = i;
        this.status = status;
    }

    public static KycLevel findByLevel(final int kycLevel) {
        return Arrays.stream(values())
                .filter(value -> value.level == kycLevel)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid enum constant: " + kycLevel));
    }
}
