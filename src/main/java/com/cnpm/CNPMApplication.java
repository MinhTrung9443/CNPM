package com.cnpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class CNPMApplication {

    public static void main(String[] args) {
        SpringApplication.run(CNPMApplication.class, args);
    }

}
