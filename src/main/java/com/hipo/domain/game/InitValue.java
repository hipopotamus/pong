package com.hipo.domain.game;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InitValue {

    private Bar masterBar;

    private Bar challengerBar;

    private Ball ball;

    private int width;

    private int height;

    public InitValue(Bar masterBar, Bar challengerBar, Ball ball, int width, int height) {
        this.masterBar = new Bar(masterBar.getX(), masterBar.getY(), masterBar.getWidth(), masterBar.getHeight(), masterBar.getSpeed());
        this.challengerBar = new Bar(challengerBar.getX(), challengerBar.getY(), challengerBar.getWidth(), challengerBar.getHeight(), challengerBar.getSpeed());
        this.ball = new Ball(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight(), ball.getSpeed(), ball.getDir(), width, height);
        this.width = width;
        this.height = height;
    }
}
