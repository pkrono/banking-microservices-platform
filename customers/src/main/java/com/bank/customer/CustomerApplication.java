package com.bank.customer;

import com.bank.customer.auth.UsersRepo;
import com.bank.customer.configs.RsaKeyProperties;
import com.bank.customer.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties({RsaKeyProperties.class})
public class CustomerApplication {
    @Autowired
    UsersRepo usersRepo;
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(ApplicationContext context){
//
//        return args -> {
//            Customer customer = new Customer();
//            customer.setActive(true);
//            customer.setEmail("johndoe@test.com");
//            customer.setPassword(new BCryptPasswordEncoder(10).encode("test@123456"));
//            customer.setFirstName("John");
//            customer.setLastName("Doe");
//            System.out.println("Customer: "+ customer);
//
//            Customer customer1 = usersRepo.save(customer);
//            System.out.println("Customer created: "+ customer1);
//        };
//    }

}
