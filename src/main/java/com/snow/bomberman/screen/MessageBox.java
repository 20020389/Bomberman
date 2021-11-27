package com.snow.bomberman.screen;

import com.snow.bomberman.button.Button;
import com.snow.bomberman.something.Control;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URISyntaxException;

import static com.snow.bomberman.Game.WINDOW;
import static com.snow.bomberman.Game.ZOOM;

public class MessageBox extends Control {
    public static boolean SHOWN = false;
    public static int YESandNO = 0;

    private Button messageboxBase;
    private Button yesBt;
    private Button noBt;
    private boolean yes;
    private boolean no;
    private Font font;
    private String label;
    private boolean showing;

    public MessageBox(int mode) {
        SHOWN = false;
        setX(0);
        setY(0);
        setW(300);
        setH(150);
        yes = false;
        no = false;
        if (mode == YESandNO) {
            messageboxBase = new Button("");
            messageboxBase.setX(getX());
            messageboxBase.setY(getY());
            messageboxBase.setButtonImg("/assets/something/messagebox1.png");

            yesBt = new Button("YES");
            yesBt.setX(getX() + 33);
            yesBt.setY(getY() + 88);
            yesBt.setTextCenter(24);
            yesBt.setTextCenterHover(24);
            yesBt.setButtonImg("/assets/something/play10.png");
            yesBt.setButtonhHoverImg("/assets/something/play9.png");

            noBt = new Button("NO");
            noBt.setX(getX() + 167);
            noBt.setY(getY() + 88);
            noBt.setTextCenter(24);
            noBt.setTextCenterHover(24);
            noBt.setButtonImg("/assets/something/play10.png");
            noBt.setButtonhHoverImg("/assets/something/play9.png");

            label = "";

            try {
                font = Font.loadFont(getClass().getResource("/assets/font/upheavtt.ttf").toURI().toString(), 17);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            addListener();
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void draw(GraphicsContext render) {
        if (SHOWN && showing) {
            double cameraX = - (render.getCanvas().getLayoutX() / ZOOM);
            render.setFill(Color.rgb(0, 0, 0, 0.5));
            render.fillRect(cameraX, 0, 1000, 1000);
            messageboxBase.draw(render);
            render.setFont(font);
            render.setFill(Color.BLACK);
            render.fillText(label, cameraX + getX() + 38, getY() + 40);
            yesBt.draw(render);
            noBt.draw(render);
        }
    }

    public void show() {
        SHOWN = true;
        showing = true;
    }

    public void hide() {
        SHOWN = false;
        showing = false;
    }

    public void reset() {
        yes = false;
        no = false;
        hide();
    }

    public void addListener() {
        WINDOW.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (showing) {
                if (yesBt.isClick(e)) {
                    yes = true;
                    hide();
                }
                if (noBt.isClick(e)) {
                    no = true;
                    hide();
                }
            }
        });

        WINDOW.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            yesBt.setHover(e);
            noBt.setHover(e);
        });
    }


    public boolean isYes() {
        return yes;
    }

    public boolean isNo() {
        return no;
    }

    @Override
    public void setX(double x) {
        super.setX(x);
        if (messageboxBase != null) {
            messageboxBase.setX(getX());
            yesBt.setX(getX() + 33);
            noBt.setX(getX() + 167);
        }
    }

    @Override
    public void setY(double y) {
        super.setY(y);
        if (messageboxBase != null) {
            messageboxBase.setY(getY());
            yesBt.setY(getY() + 88);
            noBt.setY(getY() + 88);
        }
    }
}
