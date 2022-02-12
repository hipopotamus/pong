package com.hipo.domain.game;

import com.hipo.domain.entity.Account;
import com.hipo.exception.IllegalRequestException;
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

    private Bar masterBar; //right

    private Bar challengerBar; //left

    private Ball ball;

    private int width;

    private int height;

    private int masterWinNumber = 0;

    private int challengerWinNumber = 0;

    private boolean started = false;

    private boolean ready = false;

    private boolean end = true;

    private int maxCapacity;

    private Account master;

    private Account challenger;

    private List<String> spectator;

    private InitValue initValue;

    Timer gameStart = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ball.move();
            checkBallCollision();
            match();
            end();
        }
    });

    public void start() {
        end = false;
        started = true;
        initializationWinNumber();
        initialization();
        gameStart.start();
    }

    public void end() {
        if (end) {
            gameStart.stop();
        }
    }

    private void match() {
        if (ball.getX() >= width - ball.getWidth()) {
            challengerWinNumber += 1;
            initialization();

            if (challengerWinNumber == 3) {
                end = true;
            }
        }

        if (ball.getX() <= 0) {
            masterWinNumber += 1;
            initialization();

            if (masterWinNumber == 3) {
                end = true;
            }
        }
    }

    private void initialization() {
        masterBar.setX(initValue.getMasterBar().getX());
        masterBar.setY(initValue.getMasterBar().getY());
        challengerBar.setX(initValue.getChallengerBar().getX());
        challengerBar.setY(initValue.getChallengerBar().getY());
        ball.setX(initValue.getBall().getX());
        ball.setY(initValue.getBall().getY());
    }

    private void initializationWinNumber() {
        masterWinNumber = 0;
        challengerWinNumber = 0;
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

    public PongGameFrame(Bar rightBar, Bar leftBar, Ball ball, int width, int height, int maxCapacity) {
        this.masterBar = rightBar;
        this.challengerBar = leftBar;
        this.ball = ball;
        this.width = width;
        this.height = height;
        this.initValue = new InitValue(rightBar, leftBar, ball, width, height);
        this.maxCapacity = maxCapacity;
    }

    public void appendSpectator(String nickname) {
        if (spectator.size() < maxCapacity) {
            spectator.add(nickname);
            return;
        }
        throw new IllegalRequestException("현재 방의 인원이 최대여서 참여할 수 없습니다.");
    }
}
