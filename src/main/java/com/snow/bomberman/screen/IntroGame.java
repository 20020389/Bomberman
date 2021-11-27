package com.snow.bomberman.screen;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.net.URISyntaxException;

public class IntroGame {
    private Image logo;
    private Image bar;
    private static double value;
    private double delayValue;
    public static final double MAXVALUE = 429;
    private boolean done;
    private  final double more = 100;

    public IntroGame() {
        try {
            logo = new Image(getClass().getResource("/assets/LogoIntro.png").toURI().toString());
            bar = new Image(getClass().getResource("/assets/something/bar3.png").toURI().toString());
            value = 0;
            delayValue = 0;
            done = false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext render) {
        render.setFill(Color.web("3c75d8"));
        render.fillRect(0, 0, 416 * 1.2 + more, 416);
        render.drawImage(logo, 41.6 + more / 2, 80, 416, 174.45);
        render.drawImage(bar, 27.6 + more / 2, 340);
        render.setFill(Color.WHITE);
        render.fillRect(34.6 + more / 2, 346, value, 8);
        if (value < 330) {
            value += 2;
        } else if (value < MAXVALUE) {
            delayValue ++;
            if (delayValue > 40 && delayValue < 180) {
                if (delayValue % 2 == 0) {
                    value += 1;
                }
            }
            if (delayValue > 280){
                value += 1;
            }
        } else {
            value = MAXVALUE;
            done = true;
        }
    }

    public static void setValue(double v) {
        value = v;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
