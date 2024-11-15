package com.bank.account.controllers;

import com.bank.account.entities.Account;
import com.bank.account.services.AccountServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {
    static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    final AccountServices accountServices;

    public AccountController(AccountServices accountServices) {
        this.accountServices = accountServices;
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody  Account account) {
        try {
            LOGGER.info("Creating account {}", account);
            Account account1 = accountServices.createAccount(account);
            return new ResponseEntity<>(account1, HttpStatus.CREATED);
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }
    @GetMapping("/balance")
    public ResponseEntity<Account> getAccountDetails(@RequestParam String accountNumber) {
        try {
            LOGGER.info("Retrieving account {}", accountNumber);
            Account account = accountServices.getAccountByAccountNumber(accountNumber);
            return new ResponseEntity<>(account, HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/close")
    public ResponseEntity closeAccount(@RequestParam String accountNumber) {
        try {
            LOGGER.info("Closing account {}", accountNumber);
            accountServices.closeAccount(accountNumber);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/update")
    public ResponseEntity<Account> updateAccount(@RequestParam String accountNumber, @RequestBody Account account) {
        LOGGER.info("Updating account {}", accountNumber);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<ArrayList<Account>> getAccounts(){
        LOGGER.info("Fetching all the accounts");
        return new ResponseEntity<>(accountServices.getAccounts(), HttpStatus.OK);
    }
}

