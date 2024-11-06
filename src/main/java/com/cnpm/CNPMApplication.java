package com.cnpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement

public class CNPMApplication {

    public static void main(String[] args) {
        SpringApplication.run(CNPMApplication.class, args);
    }

}
