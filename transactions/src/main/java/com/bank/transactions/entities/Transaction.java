package com.bank.transactions.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;
    String accountId;
    double amount;
    String description;
    String transactionType;
    String debitAccount;
    String creditAccount;
    String transactionReference;
    String transactionCurrency;
    LocalDateTime transactionDate;
    String statusCode;
    String statusMessage;

}
