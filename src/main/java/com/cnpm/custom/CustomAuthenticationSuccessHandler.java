//package com.cnpm.custom;
//
//import com.cnpm.util.Logger;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Collection;
//
//@Component
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//
//    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        // Lấy vai trò của người dùng
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        Logger.log("Authorities: " + authorities);
//        // Mặc định chuyển đến trang chủ
//        String targetUrl = "/shop";
//
//        // Phân quyền chuyển hướng theo role
//        for (GrantedAuthority grantedAuthority : authorities) {
//            String role = grantedAuthority.getAuthority();
//            targetUrl = switch (role) {
//                case "ROLE_SHOP_OWNER" -> "/admin/dashboard";
//                case "ROLE_EMPLOYEE" -> "/employee/dashboard";
//                case "ROLE_CUSTOMER" -> "/";
//                default -> "/";
//            };
//        }
//
////        // Lưu thông tin người dùng vào session nếu cần
////        HttpSession session = request.getSession();
////        // Lưu ý: Không nên lưu trực tiếp đối tượng authentication vào session
////        session.setAttribute("username", authentication.getName());
//
//        // Thực hiện chuyển hướng
////        response.sendRedirect(request.getContextPath() + targetUrl);
//        redirectStrategy.sendRedirect(request, response, targetUrl);
//    }
//}