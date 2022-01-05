package com.hipo.controller.websocket;

import com.hipo.dataobjcet.form.ChatMessageForm;
import com.hipo.domain.entity.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageForm chatMessageForm) {
        chatMessageForm.setMessageType(MessageType.ENTER);
        chatMessageForm.setMessage(chatMessageForm.getWriter() + "님이 채팅방에 입장하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + chatMessageForm.getRoomId(), chatMessageForm);
    }

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageForm chatMessageForm) {
        chatMessageForm.setMessageType(MessageType.MESSAGE);
        template.convertAndSend("/sub/chat/room/" + chatMessageForm.getRoomId(), chatMessageForm);
    }
}
