'use strict';

const chatPage = document.querySelector('#chat-page');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const connectingElement = document.querySelector('.connecting');

let stompClient = null;

// Lấy tên người dùng từ URL path (phần cuối cùng của đường dẫn)
const pathParts = window.location.pathname.split('/');
const username = pathParts[pathParts.length - 1] || "Guest"; // Nếu không tìm thấy, mặc định là "Guest"

const colors = [
	'#2196F3', '#32c787', '#00BCD4', '#ff5652',
	'#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// Kết nối WebSocket
function connect() {
	console.log("Connecting as " + username);

	const socket = new SockJS('/ws');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, onConnected, onError);
}

function onConnected() {
	// Subscribe to the Public Topic
	stompClient.subscribe('/topic/public', onMessageReceived);

	// Thông báo đã tham gia phòng chat
	stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));

	connectingElement.classList.add('hidden');
}

function onError(error) {
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
	// Tạo một khoảng thời gian cố gắng lại sau 5 giây nếu không kết nối được
	setTimeout(() => {
		connect(); // Tự động kết nối lại
	}, 5000);
}

// Gửi tin nhắn
function sendMessage(event) {
	event.preventDefault(); // Ngăn ngừa hành động mặc định

	const messageContent = messageInput.value.trim();

	if (messageContent && stompClient) {
		const chatMessage = {
			sender: username,
			content: messageContent,
			type: 'CHAT'
		};

		stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
		messageInput.value = ''; // Clear message input
	}
}

// Nhận tin nhắn
function onMessageReceived(payload) {
	const message = JSON.parse(payload.body);
	console.log(message);  // Xem thông tin tin nhắn nhận được

	const messageElement = document.createElement('li');

	// Xử lý khi có người tham gia (JOIN)
	if (message.type === 'JOIN') {
		messageElement.classList.add('event-message');
		message.content = message.sender + ' đã tham gia phòng chat!';
		messageElement.style.backgroundColor = '#DFF2BF'; // Màu nền cho sự kiện JOIN
	} 
	// Xử lý khi có người rời (LEAVE)
	else if (message.type === 'LEAVE') {
		messageElement.classList.add('event-message');
		message.content = message.sender + ' đã rời khỏi phòng chat!';
		messageElement.style.backgroundColor = '#FFB6B9'; // Màu nền cho sự kiện LEAVE
	} 
	// Xử lý tin nhắn bình thường (CHAT)
	else {
		messageElement.classList.add('chat-message');
		messageElement.style.backgroundColor = '#F1F1F1'; // Màu nền cho tin nhắn bình thường

		// Tạo avatar cho người gửi
		const avatarElement = document.createElement('i');
		const avatarText = document.createTextNode(message.sender[0].toUpperCase()); // Chữ đầu tiên in hoa
		avatarElement.appendChild(avatarText);
		avatarElement.style['background-color'] = getAvatarColor(message.sender);
		avatarElement.classList.add('avatar'); // Thêm lớp avatar để CSS tùy chỉnh
		messageElement.appendChild(avatarElement);

		// Hiển thị tên người gửi
		const usernameElement = document.createElement('span');
		const usernameText = document.createTextNode(message.sender);
		usernameElement.appendChild(usernameText);
		messageElement.appendChild(usernameElement);
	}

	// Hiển thị nội dung tin nhắn
	const textElement = document.createElement('p');
	const messageText = document.createTextNode(message.content);
	textElement.appendChild(messageText);
	messageElement.appendChild(textElement);

	// Thêm tin nhắn vào vùng hiển thị
	messageArea.appendChild(messageElement);

	// Cuộn xuống dưới cùng
	messageArea.scrollTop = messageArea.scrollHeight;
}

// Lấy màu sắc cho avatar
function getAvatarColor(messageSender) {
	let hash = 0;
	for (let i = 0; i < messageSender.length; i++) {
		hash = 31 * hash + messageSender.charCodeAt(i);
	}

	const index = Math.abs(hash % colors.length);
	return colors[index];
}

// Nghe sự kiện submit để gửi tin nhắn
messageForm.addEventListener('submit', sendMessage, true);

// Kết nối khi trang tải xong
window.onload = connect;
window.onbeforeunload = function () {
    if (stompClient) {
        stompClient.send("/app/chat.removeUser", {}, JSON.stringify({ sender: username, type: 'LEAVE' }));
    }
};
// Gửi tin nhắn khi nhấn Enter
messageInput.addEventListener('keydown', function(event) {
	if (event.key === "Enter" && !event.shiftKey) { // Chỉ gửi tin nhắn khi nhấn Enter, không phải Shift+Enter
		sendMessage(event);
	}
});
