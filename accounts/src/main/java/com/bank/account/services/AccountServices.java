package com.bank.account.services;

import com.bank.account.repo.AccountRepo;
import com.bank.account.entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Service
public class AccountServices {
    static final Logger LOGGER = LoggerFactory.getLogger(AccountServices.class);
    final AccountRepo accountRepo;

    public AccountServices(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }
    public Account getAccount(String accountNumber) {
        Account account = accountRepo.findByAccountNumber(accountNumber);
        if (account == null) {
            return null;
        }
        return account;
    }

    public Account createAccount(Account account) {
        LOGGER.info("Creating account : {}" ,account.getAccountType());
        account.setAccountStatus("ACTIVE");

            if (account.getAccountType().equals(null)){
                account.setAccountType("SALARY");
            }
            boolean existsAccount = true;
            while (existsAccount) {
                String accountNumber = generateTimestampID();
                if (accountRepo.findByAccountNumber(accountNumber) == null) {
                    LOGGER.info("Creating new account : {}" ,accountNumber);
                    account.setAccountNumber(accountNumber);
                    existsAccount = false;
                }
            }
            return accountRepo.save(account);
    }

    public void closeAccount(String accountNumber) {
        LOGGER.info("Closing account : {}" ,accountNumber);
        Account account = accountRepo.findByAccountNumber(accountNumber);
        account.setAccountStatus("CLOSED");
        accountRepo.save(account);
        LOGGER.info("Account closed : {}" ,account.getAccountNumber());
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        LOGGER.info("Getting account : {}" ,accountNumber);
        Account account = accountRepo.findByAccountNumber(accountNumber);
        if (account == null) {
            return null;
        }
        LOGGER.info("Account found : {}" ,account.getAccountNumber());
        return account;
    }
    public void updateAccount(Account account){
        LOGGER.info("Update account: {}", account);
        Account account1 = accountRepo.findByAccountNumber(account.getAccountNumber());
        LOGGER.info("DB data: {}", account1.getAccountNumber());
        if(account1.getAccountNumber() != null){
            account1.setBalance(account.getBalance());
            accountRepo.save(account1);
            LOGGER.info("Account balance updated...");
        }
    }

    public static String generateTimestampID() {
        long timestamp = System.currentTimeMillis(); // Current timestamp in milliseconds
        int randomNum = new Random().nextInt(90) + 10; // Two-digit random number
        String id = String.valueOf(timestamp).substring(3) + randomNum; // Use the last 8 digits of timestamp and add two random digits
        return id.substring(0, 10); // Ensure it's 10 digits
    }

    public ArrayList<Account> getAccounts() {
        return accountRepo.findAllByAccountStatus("ACTIVE");
    }
}
