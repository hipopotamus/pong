package com.hipo.domain.game;

import lombok.Data;

@Data
public class Ball {

    private int x;

    private int y;

    private int width;

    private int height;

    private int speed;

    private int dir;

    private int frameWidth;

    private int frameHeight;

    public Ball(int x, int y, int width, int height, int speed, int dir, int frameWidth, int frameHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.dir = dir;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    private void checkBoarder() {
        if (x < 0) {
            x = 0;
        }
        if (x > frameWidth - width) {
            x = frameWidth - width;
        }
        if (y > frameHeight) {
            y = frameHeight;
        }
        if (y < height) {
            y = height;
        }
    }

    public void move() {
        if (dir == 0) { //up-right
            x += speed;
            y += speed;
        } else if (dir == 1) { //down-right
            x += speed;
            y -= speed;
        } else if (dir == 2) { //down-left
            x -= speed;
            y -= speed;
        } else if (dir == 3) { //up-left
            x -= speed;
            y += speed;
        }
        checkBoarder();
    }
}
