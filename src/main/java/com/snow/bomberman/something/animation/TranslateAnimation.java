package com.snow.bomberman.something.animation;

import com.snow.bomberman.something.Control;

public class TranslateAnimation {
    private Control control;
    private double startX;
    private double startY;
    private double stopX;
    private double stopY;
    private double wayX;
    private double wayY;
    private double speed;
    private boolean doneX;
    private boolean doneY;
    private boolean stopTrans;

    public TranslateAnimation(Control control) {
        this.control = control;
        startX = control.getX();
        startY = control.getY();
        stopX = startX;
        stopY = startY;
        speed = 2;
        wayX = (stopX > startX) ? speed : -speed;
        wayY = (stopY > startY) ? speed : -speed;
        doneX = true;
        doneY = true;
        stopTrans = false;
    }

    public void doing() {
        if ((!doneX || !doneY) && !stopTrans) {
            control.setAnimation(true);
            if ((wayX > 0) ? control.getX() < stopX : control.getX() > stopX) {
                control.setX(control.getX() + wayX);
            } else {
                control.setX(stopX);
                doneX = true;
            }
            if ((wayY > 0) ? control.getY() < stopY : control.getY() > stopY) {
                control.setY(control.getY() + wayY);
            } else {
                control.setY(stopY);
                doneY = true;
            }
        } else {
            control.setAnimation(false);
        }
    }

    public void play() {
        doneX = false;
        doneY = false;
        wayX = (stopX >= startX) ? speed : -speed;
        wayY = (stopY >= startY) ? speed : -speed;
        stopTrans = false;
    }

    public void stop() {
        stopTrans = true;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        wayX = (stopX > startX) ? speed : -speed;
        wayY = (stopY > startY) ? speed : -speed;
    }

    public void setStartX(double startX) {
        this.startX = startX;
        wayX = (stopX > startX) ? speed : -speed;
    }

    public void setStartY(double startY) {
        this.startY = startY;
        wayY = (stopY > startY) ? speed : -speed;
    }

    public void setStopX(double stopX) {
        this.stopX = stopX;
        wayX = (stopX > startX) ? speed : -speed;
    }

    public void setStopY(double stopY) {
        this.stopY = stopY;
        wayY = (stopY > startY) ? speed : -speed;
    }
}
