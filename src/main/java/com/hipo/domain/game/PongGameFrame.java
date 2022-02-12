package com.hipo.domain.game;

import com.hipo.domain.entity.Account;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@Slf4j
@Data
@Transactional
public class PongGameFrame {

    private Bar masterBar;

    private Bar challengerBar;

    private Ball ball;

    private int width;

    private int height;

    private int masterWinNumber = 0;

    private int challengerWinNumber = 0;

    private boolean started = false;

    private boolean ready = false;

    private boolean end = false;

    private Account master;

    private Account challenger;

    private List<String> spectator;

    private InitValue initValue;

    Timer timer = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ball.move();
            checkCollision();
            match();
            log.info("ball x : {}, ball y : {}", ball.getX(), ball.getY());
            log.info("master : {}, challenger : {}", masterWinNumber, challengerWinNumber);
            end();
        }
    });

    public void start() {
        end = false;
        started = true;
        timer.start();
    }

    public void end() {
        if (end) {
            timer.stop();
        }
    }

    private void checkCollision() {
        checkBallCollision();
        checkBarCollision();
    }

    private void match() {
        if (ball.getX() >= width - ball.getWidth()) {
            challengerWinNumber += 1;
            initialization();

            if (challengerWinNumber == 3) {
                end = true;
            }
        } else if (ball.getX() <= 0) {
            masterWinNumber += 1;
            initialization();

            if (masterWinNumber == 3) {
                end = true;
            }
        }
    }

    public void pause() {

    }

    private void initialization() {
        masterBar.setX(initValue.getMasterBar().getX());
        masterBar.setY(initValue.getMasterBar().getY());
        challengerBar.setX(initValue.getChallengerBar().getX());
        challengerBar.setY(initValue.getChallengerBar().getY());
        ball.setX(initValue.getBall().getX());
        ball.setY(initValue.getBall().getY());
    }

    private void checkBarCollision() {
        if (masterBar.getY() >= height) {
            masterBar.setY(height);
        }
        if (challengerBar.getY() >= height) {
            challengerBar.setY(height);
        }
        if (masterBar.getY() - masterBar.getHeight() <= 0) {
            masterBar.setY(masterBar.getHeight());
        }
        if (challengerBar.getY() - challengerBar.getHeight() <= 0) {
            challengerBar.setY(challengerBar.getHeight());
        }
    }

    private void checkBallCollision() {
        if (ball.getDir() == 0) {
            if (ball.getY() >= height) {
                ball.setDir(1);
            }

            if (ball.getX() + ball.getWidth() > masterBar.getX() &&
                    (ball.getY() + (ball.getHeight() / 3) <= masterBar.getY() &&
                            ball.getY() - (ball.getHeight() / 3) >= masterBar.getY() - masterBar.getHeight())) {
                ball.setDir(3);
            }
        } else if (ball.getDir() == 1) {
            if (ball.getY() <= ball.getHeight()) {
                ball.setDir(0);
            }

            if (ball.getX() + ball.getWidth() > masterBar.getX() &&
                    (ball.getY() + (ball.getHeight() / 3) <= masterBar.getY() &&
                            ball.getY() - (ball.getHeight() / 3) >= masterBar.getY() - masterBar.getHeight())) {
                ball.setDir(2);
            }
        } else if (ball.getDir() == 2) {
            if (ball.getY() <= ball.getHeight()) {
                ball.setDir(3);
            }

            if (ball.getX() < challengerBar.getX() + challengerBar.getWidth() &&
                    (ball.getY() + (ball.getHeight() / 3) <= challengerBar.getY() &&
                            ball.getY() - (ball.getHeight() / 3) >= challengerBar.getY() - challengerBar.getHeight())) {
                ball.setDir(1);
            }
        } else if (ball.getDir() == 3) {
            if (ball.getY() >= height) {
                ball.setDir(2);
            }

            if (ball.getX() < challengerBar.getX() + challengerBar.getWidth() &&
                    (ball.getY() + (ball.getHeight() / 3) <= challengerBar.getY() &&
                            ball.getY() - (ball.getHeight() / 3) >= challengerBar.getY() - challengerBar.getHeight())) {
                ball.setDir(1);
            }
        }
    }

    public PongGameFrame(Bar rightBar, Bar leftBar, Ball ball, int width, int height) {
        this.masterBar = rightBar;
        this.challengerBar = leftBar;
        this.ball = ball;
        this.width = width;
        this.height = height;
        this.initValue = new InitValue(rightBar, leftBar, ball, width, height);
    }
}
