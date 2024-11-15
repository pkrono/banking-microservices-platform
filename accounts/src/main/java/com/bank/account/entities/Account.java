package com.bank.account.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerId;
    @Column(nullable = false, unique = true)
    private String accountNumber;
    private String accountType;
    private double balance=0.0;
    private String accountStatus;
    private String currencyCode;
}
