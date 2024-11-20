package com.cnpm.service.impl;

import com.cnpm.entity.Account;
import com.cnpm.entity.Customer;
import com.cnpm.entity.User;
import com.cnpm.repository.UserRepository;
import com.cnpm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUser(String name) {
        return userRepository.findByAccount_Username(name).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void register(String username, String password) {
        if (userRepository.findByAccount_Username(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        Customer customer = new Customer();
        Account account = new Account();
        account.setUsername(username);
        String encodedPassword = passwordEncoder.encode("123456");
        account.setPassword(encodedPassword);
        account.setUser(customer);
        customer.setAccount(account);
        userRepository.save(customer);
    }
}
