package com.hipo.web.websocket;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.MessageType;
import com.hipo.service.AccountService;
import com.hipo.service.MessageService;
import com.hipo.web.form.ChatMessageForm;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SimpMessagingTemplate template;
    private final MessageService messageService;
    private final AccountService accountService;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageForm chatMessageForm) {
        chatMessageForm.setMessageType(MessageType.ENTER);
        chatMessageForm.setMessage(chatMessageForm.getWriter() + "님이 채팅방에 입장하셨습니다.");

        template.convertAndSend("/sub/chat/room/" + chatMessageForm.getRoomId(), chatMessageForm);

        Account account = accountService.findByNickname(chatMessageForm.getWriter());
        messageService.createMessage(chatMessageForm.getMessage(), chatMessageForm.getMessageType(),
                account.getId(), chatMessageForm.getRoomId());
    }

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageForm chatMessageForm) {
        chatMessageForm.setMessageType(MessageType.MESSAGE);

        template.convertAndSend("/sub/chat/room/" + chatMessageForm.getRoomId(), chatMessageForm);

        Account account = accountService.findByNickname(chatMessageForm.getWriter());
        messageService.createMessage(chatMessageForm.getMessage(), chatMessageForm.getMessageType(),
                account.getId(), chatMessageForm.getRoomId());
    }
}
