package com.cnpm.config;

import com.cnpm.custom.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        tạm thời làm vậy, để khi đặt hàng lấy username từ principal mới tra ra được mã khách hàng cũng như phục vụ các chức năng khác
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/**").hasAnyRole("SHOP_OWNER", "EMPLOYEE", "CUSTOMER")
//                        .requestMatchers("/user/signup", "/user/signin", "/user/order/**", "/error","/").permitAll()
//                        .requestMatchers("/assets/**","/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
//                tạm thời xài form login và logout mặc định trc, sau này sẽ ấy sau
                .formLogin(form -> form.permitAll())
                .logout(logout -> logout.permitAll());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)  // Sử dụng custom UserDetailsService
                .passwordEncoder(passwordEncoder());  // Sử dụng PasswordEncoder

        return authenticationManagerBuilder.build();  // Trả về AuthenticationManager
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}