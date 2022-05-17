package com.hipo.web.dto;

import com.hipo.domain.entity.Account;
import com.hipo.domain.game.Ball;
import com.hipo.domain.game.Bar;
import com.hipo.domain.game.PongGameFrame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PongGameFrameDto {

    private Bar leftBar;

    private Bar rightBar;

    private Ball ball;

    private int width;

    private int height;

    private Account master;

    private Account challenger;

    private int masterWinNumber;

    private int challengerWinNumber;

    public PongGameFrameDto(PongGameFrame pongGameFrame) {
        leftBar = pongGameFrame.getChallengerBar();
        rightBar = pongGameFrame.getMasterBar();
        ball = pongGameFrame.getBall();
        width = pongGameFrame.getWidth();
        height = pongGameFrame.getHeight();
        master = pongGameFrame.getMaster();
        challenger = pongGameFrame.getChallenger();
        masterWinNumber = pongGameFrame.getMasterWinNumber();
        challengerWinNumber = pongGameFrame.getChallengerWinNumber();
    }
}
