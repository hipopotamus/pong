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

    private int maxWidth;

    private int maxHeight;

    public Ball(int x, int y, int width, int height, int speed, int dir, int maxWidth, int maxHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.dir = dir;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    private void checkBoarder() {
        if (x < 0) {
            x = 0;
        }
        if (x > maxWidth - width) {
            x = maxWidth - width;
        }
        if (y > maxHeight) {
            y = maxHeight;
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
