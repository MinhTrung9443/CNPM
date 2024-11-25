package com.cnpm.controller;

import com.cnpm.model.ChatMessage;
import com.cnpm.dto.SessionInfoDTO;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller("/employee")
public class ChatController {

	@GetMapping("/chat/{SessionInfoname}")
	public String showChatPage(@PathVariable String SessionInfoname, Model model) {
		model.addAttribute("SessionInfoname", SessionInfoname); // Truyền tên người dùng vào model
		return "chat/index"; // Đảm bảo trả về đúng template
	}

	// Phương thức gửi SessionInfoname vào WebSocket session
	@MessageMapping("/chat.addSessionInfo")
	@SendTo("/topic/public")
	public ChatMessage addSessionInfo(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		// Tạo đối tượng SessionInfoDTO thay vì sử dụng HttpSession
		SessionInfoDTO SessionInfoDTO = (SessionInfoDTO) headerAccessor.getSessionAttributes().get("SessionInfoDTO");

		if (SessionInfoDTO == null) {
			// Nếu không có SessionInfoDTO trong session, có thể là người dùng chưa đăng
			// nhập
			SessionInfoDTO = new SessionInfoDTO("anonymousSessionInfo");
		}

		// Thêm SessionInfoname vào WebSocket session
		headerAccessor.getSessionAttributes().put("SessionInfoDTO", SessionInfoDTO);
		chatMessage.setSender(SessionInfoDTO.getUserName()); // Gửi SessionInfoname trong tin nhắn

		return chatMessage;
	}

	// Phương thức gửi tin nhắn
	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		return chatMessage;
	}

	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage) {
		chatMessage.setType(ChatMessage.MessageType.JOIN);
		return chatMessage;
	}

	@MessageMapping("/chat.removeUser")
	@SendTo("/topic/public")
	public ChatMessage removeUser(@Payload ChatMessage chatMessage) {
		chatMessage.setType(ChatMessage.MessageType.LEAVE); // Đảm bảo set type là LEAVE
		return chatMessage;
	}

}
