package com.hipo.domain.game;

import lombok.Data;

@Data
public class Bar {

    private int x;

    private int y;

    private int width;

    private int height;

    private int speed;

    public Bar(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void up() {
        y += speed;
    }

    public void down() {
        y -= speed;
    }
}
