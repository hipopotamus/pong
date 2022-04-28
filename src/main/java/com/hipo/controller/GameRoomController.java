package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.dataobjcet.form.GameRoomForm;
import com.hipo.service.GameRoomService;
import com.hipo.service.PongGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GameRoomController {

    private final GameRoomService gameRoomService;
    private final PongGameService pongGameManager;

    @PostMapping("/gameRoom")
    public ResultMessage createGameRoom(@LoginAccountId Long loginAccountId,
                                        @RequestBody GameRoomForm gameRoomForm) {

        gameRoomService.createGameRoom(loginAccountId, gameRoomForm.getName());
        return new ResultMessage("success create GameRoom");
    }

    @PostMapping("/gameRoom/{gameRoomId}/challenger")
    public ResultMessage attendByChallenger(@LoginAccountId Long loginAccountId,
                                            @PathVariable("gameRoomId") Long gameRoomId) {

        pongGameManager.attendRoomByChallenger(gameRoomId, loginAccountId);
        return new ResultMessage("success join GameRoom By Challenger");
    }

    @PostMapping("/gameRoom/{gameRoomId}/spectator")
    public ResultMessage attendBySpectator(@LoginAccountId Long loginAccountId,
                                           @PathVariable("gameRoomId") Long gameRoomId) {
        pongGameManager.attendRoomBySpectator(gameRoomId, loginAccountId);
        return new ResultMessage("success join GameRoom By Spectator");
    }

    @DeleteMapping("/gameRoom/{gameRoomId}")
    public ResultMessage deleteGameRoom(@LoginAccountId Long loginAccountId,
                                        @PathVariable("gameRoomId") Long gameRoomId) {
        gameRoomService.deleteGameRoom(loginAccountId, gameRoomId);
        return new ResultMessage("success delete GameRoom");
    }

}
