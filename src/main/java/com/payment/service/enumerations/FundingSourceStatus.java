package com.payment.service.enumerations;

import lombok.Getter;

@Getter
public enum FundingSourceStatus {
    ACTIVE,
    INACTIVE,
    DELETED,
    SUSPENDED
}
