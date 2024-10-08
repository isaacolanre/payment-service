package com.payment.service.enumerations;

import lombok.Getter;

@Getter
public enum MessageBrokerExchange {

    TRANSACTION("x.transaction"),

    OTP("x.otp"),

    REGISTRATION("x.registration"),

    EMAIL("x.email"),

    NEW_ACCOUNT("x.account"),

    FIREBASE("x.firebase"),

    TRANSACTION_EVENT("x.transaction.event"),

    PAYMENT_REQUEST("x.payment.request"),

    ACCOUNT_CREATED("x.account.created"),

    ACCOUNT_CREATED_DETAILS("x.account.created.details"),

    INAPP("x.inapp"),

    INAPP_REPORTS("x.inapp.reports"),

    GENERIC("x.generic"),

    BILL_PAYMENT_TOKEN("x.bill.payment.token"),

    TRANSACTION_UPDATE("x.transaction.update"),

    ACCOUNT_RECEIVABLE("x.account.receivable"),

    ACCOUNT_PAYABLE("x.account.payable"),

    SELFIE_STATUS("x.selfie.status"),

    BILL_PAYMENT_STATUS("x.bill.payment.status"),

    MONITORING("x.monitoring"),

    AUDIT_EVENTS("x.audit.event"),

    RECONCILIATION_AUDIT_EVENT("x.reconciliation.audit.event"),

    USER_BVN_WATCHLIST_EVENT("x.user.watchlist.event"),
    BILL_PAYMENT_RECONCILIATION("x.bill.payment.reconciliation"),

    TRANSACTION_RECONCILIATION("x.transaction.reconciliation"),

    RECONCILIATION_COMPLETE("x.reconciliation.complete");


    private final String name;

    MessageBrokerExchange(String name) {
        this.name = name;
    }
}
