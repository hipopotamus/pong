package com.hipo.domain.game;

import lombok.Data;

@Data
public class Bar {

    private int x;

    private int y;

    private int width;

    private int height;

    private int speed;

    private int maxWidth;

    private int maxHeight;

    public Bar(int x, int y, int width, int height, int speed, int maxWidth, int maxHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public void up() {
        y += speed;
        checkBoarder();
    }

    public void down() {
        y -= speed;
        checkBoarder();
    }

    private void checkBoarder() {
        if (y > maxHeight) {
            y = maxHeight;
        }
        if (y < 0) {
            y = height;
        }
    }
}
