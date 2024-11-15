package com.cnpm.service;

import com.cnpm.entity.User;
import org.springframework.stereotype.Service;
@Service
public interface IUserService {
    User getUser(String name);
}