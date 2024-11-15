package com.bank.transactions.services;

import com.bank.transactions.configs.ActiveMq;
import com.bank.transactions.configs.TransactionConfigs;
import com.bank.transactions.dto.AccountDto;
import com.bank.transactions.dto.WithdrawFunds;
import com.bank.transactions.entities.Transaction;
import com.bank.transactions.repo.TransactionRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class TransactionService {
    static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    final TransactionRepo transactionRepo;
    final TransactionConfigs transactionConfigs;
    final ObjectMapper objectMapper;
    final JmsTemplate jmsTemplate;
    final ActiveMq activeMq;

    public TransactionService(TransactionRepo transactionRepo, TransactionConfigs transactionConfigs, ObjectMapper objectMapper, JmsTemplate jmsTemplate, ActiveMq activeMq) {
        this.transactionRepo = transactionRepo;
        this.transactionConfigs = transactionConfigs;
        this.objectMapper = objectMapper;
        this.jmsTemplate = jmsTemplate;
        this.activeMq = activeMq;
    }

    public Transaction cashDeposit(Transaction transaction) throws JsonProcessingException {
        LOGGER.info("cash deposit called");
        //Check if account exists
        AccountDto accountDetails = fetchAccountDetails(transaction.getCreditAccount());
        LOGGER.info("Account name: {}-> A/C balance: {} {}", accountDetails.customerId(), accountDetails.currencyCode(),accountDetails.balance());
        if(!accountDetails.currencyCode().equals(transaction.getTransactionCurrency())) {
            LOGGER.info("Account currency code is different");
            transaction.setStatusCode("4");
            transaction.setStatusMessage("Transaction failed: CURRENCY MISMATCH");
            transaction.setTransactionDate(LocalDateTime.now());
        }else if(!Objects.equals(accountDetails.accountNumber(), transaction.getCreditAccount())){
            LOGGER.info("account number mismatch");
            transaction.setStatusCode("5");
            transaction.setStatusMessage("Transaction failed: ACCOUNT NOT FOUND");
            transaction.setTransactionDate(LocalDateTime.now());
        }else {
            LOGGER.info("Processing cash deposit");
            transaction.setTransactionReference("TT" + generateRandomString(4));
            transaction = transactionRepo.save(transaction);
            double balance = accountDetails.balance() + transaction.getAmount();

            AccountDto updatedAccount = new AccountDto(
                    null, accountDetails.accountNumber(),accountDetails.accountType(),
                    balance,accountDetails.accountStatus(),accountDetails.currencyCode()
            );
            //TODO: construct request for account details to be updated
            transaction.setStatusMessage("Transaction completed successfully");
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setStatusCode("0");

            LOGGER.info("Transaction completed successfully");
            updateAccountDetails(updatedAccount);
        }
        transaction = transactionRepo.save(transaction);
        LOGGER.info("End of cash deposit called");
        // send  notification to ACTIVEMQ
        String message = objectMapper.writeValueAsString(transaction);
        sendMessage(message, activeMq.cashDeposit());
        return transaction;
    }
    public Transaction fundsTransfer(Transaction transaction) throws JsonProcessingException {
        LOGGER.info("funds transfer called");
        if(transaction.getCreditAccount() == null){
            LOGGER.info("Credit account missing");
            transaction.setStatusCode("05");
            transaction.setStatusMessage("CREDIT A/C Missing");
        } else if (transaction.getDebitAccount() == null) {
            LOGGER.info("Debit account missing");
            transaction.setStatusCode("05");
            transaction.setStatusMessage("DEBIT A/C Missing");
        }
        AccountDto originator = fetchAccountDetails(transaction.getDebitAccount());
        AccountDto beneficiary = fetchAccountDetails(transaction.getCreditAccount());
        if(originator.accountNumber() == null){
            LOGGER.info("Originator details could not be found");
            transaction.setStatusMessage("Originator ACCOUNT details Missing");
            transaction.setStatusCode("06");
        }
        if(beneficiary.accountNumber() == null){
            LOGGER.info("Originator details could not be found");
            transaction.setStatusMessage("Beneficiary ACCOUNT details Missing");
            transaction.setStatusCode("06");
        }
        // calculate tax
        double chargeAmount = transaction.getAmount() * 0.005; // charge can configurabele
        double tax = chargeAmount * 0.20; // Tax percentage can be configurable
        double totalAmtDeducted = chargeAmount+tax+transaction.getAmount();
        if(beneficiary.accountNumber() != null && originator.accountNumber() != null){
            if(Objects.equals(beneficiary.currencyCode(), originator.currencyCode()) && originator.balance() >= totalAmtDeducted ){
                // Deduct amount from originator and credit beneficiary
                double originatorBalance = originator.balance() - totalAmtDeducted;
                double beneficiaryBalance = beneficiary.balance() + totalAmtDeducted;
                // Tax will credited tax collection account
                // Bank charge will be credited to charge

                // update both BENEFICIARY & ORIGINATOR balances and query ACCOUNT service with the updates
                AccountDto ORIGINATOR = new AccountDto(null, originator.accountNumber(),originator.accountType(),
                        originatorBalance,originator.accountStatus(),originator.currencyCode());

                updateAccountDetails(ORIGINATOR);

                AccountDto BENEFICIARY = new AccountDto(
                        null, beneficiary.accountNumber(),beneficiary.accountType(),
                        beneficiaryBalance,beneficiary.accountStatus(),beneficiary.currencyCode()
                );
                updateAccountDetails(BENEFICIARY);
            }else{
                LOGGER.info("currency do not matching");
                // TODO: Handle CROSS-CURRENCY
            }
        }
        if(transaction.getCreditAccount() == null && transaction.getDebitAccount()==null){
            LOGGER.info("For funds transfer Debit and credit account cannot be null");
            return null;
        }
        transaction.setTransactionType("FT"+generateRandomString(8));
        transaction = transactionRepo.save(transaction);
        String message = objectMapper.writeValueAsString(transaction);
        sendMessage(message, activeMq.fundsTransfer());
        // send  notification to ACTIVEMQ
        return transaction;
    }

    public boolean withdrawFunds(WithdrawFunds withdrawFunds) throws JsonProcessingException {
        LOGGER.info("withdraw funds called");
        try {
            AccountDto customerAccount = fetchAccountDetails(withdrawFunds.accountNumber());
            double chargeAmount = withdrawFunds.amount() * 0.05;
            double tax = chargeAmount * 0.20;
            double totAmtDeducted = chargeAmount + tax + withdrawFunds.amount();
            if (customerAccount.accountNumber() != null && customerAccount.balance() >= totAmtDeducted) {
                double balance = customerAccount.balance() - totAmtDeducted;
                AccountDto updatedCuctomerAccount = new AccountDto(
                        null, customerAccount.accountNumber(), customerAccount.accountType(),
                        balance, customerAccount.accountStatus(), customerAccount.currencyCode()
                );
                updateAccountDetails(updatedCuctomerAccount);
                // Send notification to ACTIVEMQ
                String message = objectMapper.writeValueAsString(withdrawFunds);
                sendMessage(message, activeMq.withdrawals());
                return true;
            }
        }catch (Exception e){
            LOGGER.error("Error occurred: {}", e.getMessage());
            return false;
        }
        return false;
    }
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        String s = localDateTime.toString().substring(0, 7).replace("-","");
        return s+result.toString().toUpperCase();
    }

    AccountDto fetchAccountDetails(String accountNumber) throws JsonProcessingException {
        String response = accountRestClient()
                .get()
                .uri("/api/v1/account/balance?accountNumber="+accountNumber)
                .retrieve().body(String.class);
        LOGGER.info("Account response: {}", response);
        AccountDto accountDto = objectMapper.readValue(response, AccountDto.class);
        LOGGER.info("Account map: {}", accountDto);
        return accountDto;
    }
    void updateAccountDetails(AccountDto accountDto) throws JsonProcessingException {
        String res = accountRestClient()
                .put()
                .uri("/api/v1/account/update?accountNumber="+accountDto.accountNumber())
                .body(accountDto)
                .retrieve().body(String.class);
        LOGGER.info("Balance update: {}", res);
    }

    RestClient accountRestClient(){
        return RestClient.builder().baseUrl(transactionConfigs.baseUrl()).build();
    }
    public void sendMessage(String message, String topic) throws JsonProcessingException {
        try {
            LOGGER.info("Sending notification to active MQ-> topic: {}",topic);
            jmsTemplate.convertAndSend(topic,message);
        }catch (Exception e){
            LOGGER.error("Sending notification to active MQ failed: {}", e.getMessage());
        }
    }

}
