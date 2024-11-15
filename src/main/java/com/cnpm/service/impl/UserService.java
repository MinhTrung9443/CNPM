package com.cnpm.service.impl;

import com.cnpm.entity.User;
import com.cnpm.repository.UserRepository;
import com.cnpm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User getUser(String name) {
        return userRepository.findByAccount_Username(name).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
