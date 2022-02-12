package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.dataobjcet.form.GameRoomForm;
import com.hipo.domain.game.PongGameManager;
import com.hipo.service.GameRoomService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"8. GameRoom"})
@RestController
@RequiredArgsConstructor
public class GameRoomController {

    private final GameRoomService gameRoomService;
    private final PongGameManager pongGameManager;

    @PostMapping("/gameRoom")
    public ResultMessage createGameRoom(@ApiIgnore @LoginAccountId Long loginAccountId,
                                        @RequestBody GameRoomForm gameRoomForm) {

        gameRoomService.createGameRoom(loginAccountId, gameRoomForm.getName());
        return new ResultMessage("success create GameRoom");
    }

    @PostMapping("/gameRoom/{gameRoomId}/challenger")
    public ResultMessage attendByChallenger(@ApiIgnore @LoginAccountId Long loginAccountId,
                                            @PathVariable("gameRoomId") Long gameRoomId) {

        pongGameManager.attendRoomByChallenger(gameRoomId, loginAccountId);
        return new ResultMessage("success join GameRoom By Challenger");
    }

    @PostMapping("/gameRoom/{gameRoomId}/challenger")
    public ResultMessage deleteGameRoom(@ApiIgnore @LoginAccountId Long loginAccountId,
                                        @PathVariable("gameRoomId") Long gameRoomId) {
        gameRoomService.deleteGameRoom(loginAccountId, gameRoomId);
        return new ResultMessage("success delete GameRoom");
    }

}
