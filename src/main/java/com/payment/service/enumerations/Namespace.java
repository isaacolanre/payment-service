package com.payment.service.enumerations;

import lombok.Getter;

@Getter
public enum Namespace {
    ADMIN(3, AccountType.NA),
    CUSTOMER(1, AccountType.CUSTOMER),
    SYSTEM(10, AccountType.NA);

    private final int id;

    private final AccountType primaryAccountType;

    Namespace(int id, AccountType primaryAccountType) {
        this.id = id;
        this.primaryAccountType = primaryAccountType;
    }
}
