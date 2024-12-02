package com.cnpm.controller;

import com.cnpm.dto.ChatMessageDTO;

import jakarta.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({ "", "*/" })
public class ChatController {

	// Phương thức hiển thị trang chat cho người dùng thông thường
	@GetMapping("/chat/{SessionInfoname}")
	public String showChatPage(@PathVariable(required = false) String SessionInfoname, Model model,
			HttpSession session) {
		try {
			if (SessionInfoname == null || SessionInfoname.isEmpty()) {
				SessionInfoname = "guest"; // Nếu không có, gán giá trị mặc định
			}
			model.addAttribute("SessionInfoname", SessionInfoname); // Truyền tên người dùng vào model
			return "chat/index"; // Trả về trang chat chính
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/signin"; // Chuyển hướng đến trang đăng nhập nếu có lỗi
		}
	}

	// Phương thức hiển thị trang chat cho người dùng từ /employee
	@GetMapping("/chat/employee/{SessionInfoname}")
	public String showEmployeeChatPage(@PathVariable(required = false) String SessionInfoname, Model model,
			HttpSession session) {
		try {
			if (SessionInfoname == null || SessionInfoname.isEmpty()) {
				SessionInfoname = "guest"; // Nếu không có, gán giá trị mặc định
			}

			// Thêm từ "Nhân viên" vào trước SessionInfoname
			String employeeSessionName = "Nhân viên " + SessionInfoname;

			model.addAttribute("SessionInfoname", employeeSessionName); // Truyền tên người dùng đã thay đổi vào model
			return "chat/index"; // Trả về trang chat chính
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/signin"; // Chuyển hướng đến trang đăng nhập nếu có lỗi
		}
	}

	// Phương thức gửi SessionInfoname vào WebSocket session
	@MessageMapping("/chat.addSessionInfo")
	@SendTo("/topic/public")
	public ChatMessageDTO addSessionInfo(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		SessionInfoDTO SessionInfoDTO = (SessionInfoDTO) headerAccessor.getSessionAttributes().get("SessionInfoDTO");

		if (SessionInfoDTO == null) {
			SessionInfoDTO = new SessionInfoDTO("anonymousSessionInfo"); // Nếu không có SessionInfoDTO, tạo mới
		}

		headerAccessor.getSessionAttributes().put("SessionInfoDTO", SessionInfoDTO);
		chatMessage.setSender(SessionInfoDTO.getFullname()); // Gửi tên người dùng vào tin nhắn

		return chatMessage;
	}

	// Phương thức gửi tin nhắn
	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessage) {
		return chatMessage;
	}

	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessage) {
		chatMessage.setType(ChatMessageDTO.MessageType.JOIN);
		return chatMessage;
	}

	@MessageMapping("/chat.removeUser")
	@SendTo("/topic/public")
	public ChatMessageDTO removeUser(@Payload ChatMessageDTO chatMessage) {
		chatMessage.setType(ChatMessageDTO.MessageType.LEAVE); // Đảm bảo set type là LEAVE
		return chatMessage;
	}
}
