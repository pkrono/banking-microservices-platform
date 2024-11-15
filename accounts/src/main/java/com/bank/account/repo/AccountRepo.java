package com.bank.account.repo;

import com.bank.account.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);

    ArrayList<Account> findAllByAccountStatus(String active);
}
