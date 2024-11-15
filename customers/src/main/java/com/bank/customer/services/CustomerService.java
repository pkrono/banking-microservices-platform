package com.bank.customer.services;

import com.bank.customer.auth.UsersRepo;
import com.bank.customer.entities.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final UsersRepo useRepo;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(UsersRepo useRepo, PasswordEncoder passwordEncoder) {
        this.useRepo = useRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public Customer createCustomer(Customer customer) {
        LOGGER.info("Registering customer: {} ",customer.toString());
        Customer newCustomer = new Customer();

        newCustomer.setFirstName(customer.getFirstName());
        newCustomer.setLastName(customer.getLastName());
        newCustomer.setEmail(customer.getEmail());
        newCustomer.setPassword(passwordEncoder.encode(customer.getPassword()));
        useRepo.save(newCustomer);
        return newCustomer;
    }

    public Customer updateCustomerProfile(Customer customer) {
        LOGGER.info("Updating customer: {} ",customer.toString());
        Customer cust = (Customer) useRepo.findByEmail(customer.getEmail());

        cust.setFirstName(customer.getFirstName());
        cust.setLastName(customer.getLastName());
        cust.setEmail(customer.getEmail());
        cust.setPassword(passwordEncoder.encode(customer.getPassword()));
        useRepo.save(cust);
        return cust;
    }
}
