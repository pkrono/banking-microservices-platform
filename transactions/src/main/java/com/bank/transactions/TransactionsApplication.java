package com.bank.transactions;

import com.bank.transactions.configs.ActiveMq;
import com.bank.transactions.configs.TransactionConfigs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({TransactionConfigs.class, ActiveMq.class})
public class TransactionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionsApplication.class, args);
    }

}
