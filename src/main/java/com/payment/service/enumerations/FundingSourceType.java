package com.payment.service.enumerations;

public enum FundingSourceType {
    IN_APP(false),
    BANK(true),
    CARD(true),
    WALLET(true);

    private final boolean skipBalanceCheck;

    FundingSourceType(boolean skipBalanceCheck) {
        this.skipBalanceCheck = skipBalanceCheck;
    }

    public boolean skipBalanceCheck() {
        return skipBalanceCheck;
    }
}
