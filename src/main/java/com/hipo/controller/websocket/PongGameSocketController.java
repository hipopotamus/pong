package com.hipo.controller.websocket;

import com.hipo.dataobjcet.dto.BarCond;
import com.hipo.domain.game.PongGameFrame;
import com.hipo.domain.game.PongGameManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PongGameSocketController {

    private final PongGameManager pongGameManager;

    @MessageMapping("/pongGame/{gameRoomId}/ready")
    public void ready(@DestinationVariable("gameRoomId") Long gameRoomId) {
        PongGameFrame pongGameFrame = pongGameManager.findPongGameFrame(gameRoomId);
        pongGameFrame.setReady(true);
    }

    @MessageMapping("/pongGame/{gameRoomId}/start")
    public void start(@DestinationVariable("gameRoomId") Long gameRoomId) {
        PongGameFrame pongGameFrame = pongGameManager.findPongGameFrame(gameRoomId);
        pongGameFrame.start();
    }

    @MessageMapping("/pongGame/{gameRoomId}/bar")
    public void barControl(@DestinationVariable("gameRoomId") Long gameRoomId, BarCond barCond) {
        PongGameFrame pongGameFrame = pongGameManager.findPongGameFrame(gameRoomId);
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

}
