package com.snow.bomberman.guide;

import com.snow.bomberman.Game;
import com.snow.bomberman.button.Button;
import com.snow.bomberman.something.Control;
import com.snow.bomberman.something.slider.SlideBarV;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.net.URISyntaxException;

public class GuidePlayer extends Control {
    private Button playerIcon;
    private Image slider;
    private Button checkSlider;
    private double sliderY;
    private SlideBarV slideBar;
    private int status;

    public GuidePlayer(double x, double y, int status) {
        setX(x);
        setY(y);
        this.status = status;
        playerIcon = new Button("");
        playerIcon.setX(getX() + 224);
        playerIcon.setY(getY() + 45);
        playerIcon.setButtonImg("/assets/something/play11.png");
        playerIcon.setW(52);
        playerIcon.setH(52);
        playerIcon.setImageIn("/assets/player/r1.png", 24, 32);
        try {
            slider = new Image(getClass().getResource("/assets/something/list1.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        checkSlider = new Button("");
        checkSlider.setX(getX() + 110);
        checkSlider.setY(getY() + 100);
        checkSlider.setW(280);
        checkSlider.setH(188);
        sliderY = 0;
        slideBar = new SlideBarV();
        slideBar.setX(getX() + 110 + 280);
        slideBar.setY(getY() + 100);
        slideBar.addListener(Game.WINDOW);
        Game.WINDOW.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (Guide.STATUS == status) {
                slideBar.addMouseDown(e);
            }
        });
        slideBar.setMaxValue(slider.getHeight() - 188);
        slideBar.setValue(0);
        slideBar.addValueChangeListener(() -> {
            sliderY = slideBar.getValue();
        });
    }

    public void draw(GraphicsContext render) {
        playerIcon.draw(render);
        render.drawImage(slider,0, sliderY, 280, 188,
                getX() + 110, getY() + 100, 280, 188);
        slideBar.draw(render);
    }

    @Override
    public void setY(double y) {
        super.setY(y);
        if (playerIcon != null) {
            playerIcon.setY(getY() + 45);
            checkSlider.setY(getY() + 100);
            slideBar.setY(getY() + 100);
        }
    }

    public void addEvent(Scene scene) {
        scene.addEventHandler(ScrollEvent.SCROLL, e -> {
            if (Guide.STATUS == status && checkSlider.isHover()) {
                if (e.getDeltaY() < 0) {
                    if (sliderY + 10 < slider.getHeight() - 188) {
                        sliderY += 10;
                    } else {
                        sliderY = slider.getHeight() - 188;
                    }
                    slideBar.setValue(sliderY);
                } else if (e.getDeltaY() > 0) {
                    if (sliderY - 10 > 0) {
                        sliderY -= 10;
                    } else {
                        sliderY = 0;
                    }
                    slideBar.setValue(sliderY);
                }
            }
        });
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            checkSlider.setHover(e);
        });
    }

    public void setList(String link) {
        try {
            slider = new Image(getClass().getResource(link).toURI().toString());
            slideBar.setMaxValue(slider.getHeight() - 188);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setIcon(String link, double w, double h) {
        playerIcon.setImageIn(link, w, h);
    }
}
