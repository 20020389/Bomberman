package com.snow.bomberman.block;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Block {
    private double x;
    private double y;
    private double w;
    private double h;

    public Block(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Block(Block num) {
        this.x = num.x;
        this.y = num.y;
        this.w = num.w;
        this.h = num.h;
    }

    public void update(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void update(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean collision(Block num) {
        return isInside(x, y, num)
                || isInside(x + w, y, num)
                || isInside(x, y + h, num)
                || isInside(x + w, y + h, num);
    }

    public double calcR(Block num) {
        if (this.collision(num)) {
            return 0;
        }
        return num.getX() - this.getWX() - 1;
    }

    public double calcL(Block num) {
        if (this.collision(num)) {
            return 0;
        }
        return this.getX() - num.getWX() - 1;
    }

    public double calcU(Block num) {
        if (this.collision(num)) {
            return 0;
        }
        return this.getY() - num.getHY() - 1;
    }

    public double calcD(Block num) {
        if (this.collision(num)) {
            return 0;
        }
        return num.getY() - this.getHY() - 1;
    }

    private boolean isInside(double x, double y, Block num) {
        return x >= num.x && x <= num.x + num.w
                && y >= num.y && y <= num.y + num.h;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWX() {
        return x + w;
    }

    public double getHY() {
        return y + h;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public static void main(String[] args) throws IOException {
        Process process = Runtime.getRuntime().exec("gcc");
    }
}
