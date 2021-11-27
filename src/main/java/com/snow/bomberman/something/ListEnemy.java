package com.snow.bomberman.something;

public class ListEnemy {
    private int value;
    private double i;
    private double j;

    public ListEnemy(int value, double x, double y) {
        this.value = value;
        this.i = x;
        this.j = y;
    }

    public int getValue() {
        return value;
    }

    public double getY() {
        return i * 32;
    }

    public double getX() {
        return j * 32;
    }
}
