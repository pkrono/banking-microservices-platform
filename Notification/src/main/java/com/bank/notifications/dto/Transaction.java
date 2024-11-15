package com.bank.notifications.dto;

import java.time.LocalDateTime;

public record Transaction(Long transactionId,
        String accountId,
        double amount,
        String description,
        String transactionType,
        String debitAccount,
        String creditAccount,
        String transactionReference,
        String transactionCurrency,
        LocalDateTime transactionDate,
        String statusCode,
        String statusMessage) {
}
