package com.bank.transactions.dto;

public record AccountDto(Long customerId,
                         String accountNumber,
                         String accountType,
                         double balance,
                         String accountStatus,
                         String currencyCode) {
}
