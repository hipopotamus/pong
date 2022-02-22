package com.hipo.controller.websocket;

import com.hipo.dataobjcet.dto.BarCond;
import com.hipo.dataobjcet.form.ChatMessageForm;
import com.hipo.domain.entity.enums.MessageType;
import com.hipo.domain.game.PongGameFrame;
import com.hipo.repository.PongGameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PongGameSocketController {

    private final PongGameRepository pongGameRepository;
    private final SimpMessagingTemplate template;

    @MessageMapping("/pongGame/{gameRoomId}/ready")
    public void ready(@DestinationVariable("gameRoomId") Long gameRoomId) {
        PongGameFrame pongGameFrame = pongGameRepository.findPongGameFrame(gameRoomId);
        pongGameFrame.setReady(true);
    }

    @MessageMapping("/pongGame/{gameRoomId}/start")
    public void start(@DestinationVariable("gameRoomId") Long gameRoomId) {
        PongGameFrame pongGameFrame = pongGameRepository.findPongGameFrame(gameRoomId);
        pongGameFrame.start();
    }

    @MessageMapping("/pongGame/{gameRoomId}/bar")
    public void barControl(@DestinationVariable("gameRoomId") Long gameRoomId, BarCond barCond) {
        PongGameFrame pongGameFrame = pongGameRepository.findPongGameFrame(gameRoomId);
        if (barCond.getMaster()) {
            if (barCond.getUp()) {
                pongGameFrame.getMasterBar().up();
            }
            if (!barCond.getUp()) {
                pongGameFrame.getMasterBar().down();
            }
        }

        if (!barCond.getMaster()) {
            if (barCond.getUp()) {
                pongGameFrame.getChallengerBar().up();
            }
            if (!barCond.getUp()) {
                pongGameFrame.getChallengerBar().down();
            }
        }
    }

    @MessageMapping("/pongGame/{gameRoomId}/chat/enter")
    public void enter(ChatMessageForm chatMessageForm, @DestinationVariable("gameRoomId") Long gameRoomId) {
        chatMessageForm.setMessageType(MessageType.ENTER);
        chatMessageForm.setMessage(chatMessageForm.getWriter() + "님이 채팅방에 입장하셨습니다.");

        template.convertAndSend("/sub/pongGame/" + gameRoomId + "/chat", chatMessageForm);
    }

    @MessageMapping("/pongGame/{gameRoomId}/chat/message")
    public void sendMessage(ChatMessageForm chatMessageForm, @DestinationVariable("gameRoomId") Long gameRoomId) {
        chatMessageForm.setMessageType(MessageType.MESSAGE);
        template.convertAndSend("/sub/pongGame/" + gameRoomId + "/chat", chatMessageForm);
    }

}
