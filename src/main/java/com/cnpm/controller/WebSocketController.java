package com.cnpm.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

@RestController
public class WebSocketController {

    @MessageMapping("/session")
    @SendTo("/topic/response")
    public String handleSessionInfo(HttpSession session) {
        // Lấy thông tin từ HttpSession
        String sessionId = session.getId();
        String userName = (String) session.getAttribute("username"); // Giả sử bạn lưu trữ thông tin userName trong session

        // Trả về thông tin cần thiết mà bạn muốn gửi qua WebSocket
        return "Session ID: " + sessionId + ", User Name: " + userName;
    }
}
