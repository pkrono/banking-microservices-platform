package com.bank.customer.auth;

import com.bank.customer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsersRepo extends JpaRepository<Customer, Long> {
    UserDetails findByEmail(String s);
}
