package com.cnpm.custom;

import com.cnpm.entity.User;
import com.cnpm.repository.UserRepository;
import com.cnpm.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccount_Username(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        Logger.log(user.getAccount());
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getAccount().getUsername())
                .password(user.getAccount().getPassword())
                .roles(user.getAccount().getRole().getRoleName())
                .build();
    }
}