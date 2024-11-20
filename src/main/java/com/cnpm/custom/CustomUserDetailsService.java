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
//        Spring Security gọi loadUserByUsername để nạp thông tin người dùng từ cơ sở dữ liệu.
//                Phương thức kiểm tra:
//        Nếu tìm thấy người dùng, trả về một đối tượng UserDetails.
//                Nếu không, ném UsernameNotFoundException.
//                Spring Security sẽ so sánh:
//        Mật khẩu nhập vào (sau khi mã hóa) với mật khẩu trong cơ sở dữ liệu.
//        Nếu khớp, xác thực thành công và cấp quyền dựa trên vai trò (roles).
        User user = userRepository.findByAccount_Username(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        Logger.log(user.getAccount());
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getAccount().getUsername())
                .password(user.getAccount().getPassword())
                .roles(user.getAccount().getRole().getRoleName())
                .build();
    }
}