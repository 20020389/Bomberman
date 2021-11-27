package com.snow.bomberman.something;

public class Control {
    private double x;
    private double y;
    private double width;
    private double height;
    private boolean animation;

    public Control() {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setW(double width) {
        this.width = width;
    }

    public void setH(double height) {
        this.height = height;
    }

    public double getW() {
        return width;
    }

    public double getH() {
        return height;
    }

    public boolean isAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }
}
