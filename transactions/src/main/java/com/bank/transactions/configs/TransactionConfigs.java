package com.bank.transactions.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bank.account")
public record TransactionConfigs(String baseUrl) {
}
