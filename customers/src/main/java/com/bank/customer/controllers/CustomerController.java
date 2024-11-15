package com.bank.customer.controllers;

import com.bank.customer.entities.Customer;
import com.bank.customer.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/customer/")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("update")
    public ResponseEntity updateCustomerProfile(@RequestBody Customer customer) {
        logger.info("Request from: {}",customer.toString());
        Customer res = customerService.updateCustomerProfile(customer);
        logger.info("Response: {}",res.getId());
        if(res.getId() != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
