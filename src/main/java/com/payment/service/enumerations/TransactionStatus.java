package com.payment.service.enumerations;

public enum TransactionStatus {
    PENDING,         // Transaction created but not yet processed.
    PROCESSING,      // Transaction is currently being processed.
    SUCCESS,         // Transaction completed successfully.
    FAILED,          // Transaction failed to complete.
    CANCELLED,       // Transaction was cancelled before processing.
    REFUNDED,        // Transaction was successful but later refunded.
    REVERSED,        // Transaction was reversed due to an error or correction.
    EXPIRED,         // Transaction expired without being completed.
    ON_HOLD,         // Transaction is temporarily on hold, awaiting further action.
    DECLINED,        // Transaction was declined by the system or provider.
    RETRYING,        // Transaction is being retried due to a failure.
    CHARGEBACK,      // Transaction was disputed and reversed (e.g., through a bank or card provider).
    AUTHORIZING      // Transaction is in the process of being authorized.
}
