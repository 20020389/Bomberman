package com.snow.bomberman.button;

import com.snow.bomberman.something.Control;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URISyntaxException;

import static com.snow.bomberman.Game.ZOOM;

public class Button extends Control {
    private Image buttonImg;
    private Image buttonhHoverImg;
    private boolean hoverButton;
    private String text;
    private Font font;
    private Color color;
    private double rotage;
    private double center;
    private double centerHover;
    private Image imageIn;
    private double imageInX;
    private double imageInY;
    private double imageInW;
    private double imageInH;

    public Button() {
        try {
            setX(0);
            setY(0);
            text = "button";
            buttonImg = new Image(getClass().getResource("/assets/something/play3.png").toURI().toString());
            buttonhHoverImg = new Image(getClass().getResource("/assets/something/play4.png").toURI().toString());
            setW(buttonImg.getWidth());
            setH(buttonImg.getHeight());
            font = Font.loadFont(getClass().getResource("/assets/font/upheavtt.ttf").toURI().toString(), 17);
            color = Color.BLACK;
            rotage = 0.5;
            imageIn = null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Button(String text) {
        try {
            setX(0);
            setY(0);
            this.text = text;
            buttonImg = new Image(getClass().getResource("/assets/something/play3.png").toURI().toString());
            buttonhHoverImg = new Image(getClass().getResource("/assets/something/play4.png").toURI().toString());
            setW(buttonImg.getWidth());
            setH(buttonImg.getHeight());
            font = Font.loadFont(getClass().getResource("/assets/font/upheavtt.ttf").toURI().toString(), 17);
            color = Color.BLACK;
            rotage = 0.5;
            center = 24;
            centerHover = 24;
            imageIn = null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setButtonImg(String link) {
        try {
            buttonImg = new Image(getClass().getResource(link).toURI().toString());
            setW(buttonImg.getWidth());
            setH(buttonImg.getHeight());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setButtonhHoverImg(String link) {
        try {
            buttonhHoverImg = new Image(getClass().getResource(link).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setRotage(double rotage) {
        this.rotage = rotage;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(GraphicsContext render) {
        double cameraX = - (render.getCanvas().getLayoutX() / ZOOM);
        Text t = new Text(text);
        t.setFont(font);
        double wi = t.getBoundsInLocal().getWidth();
        wi = wi * rotage;
        render.setFont(font);
        if (hoverButton) {
            render.drawImage(buttonhHoverImg, cameraX + getX(), getY(), getW(), getH());
            render.setFill(Color.WHITE);
            render.fillText(t.getText(), cameraX + getX() + (getW() / 2) - wi, getY() + centerHover);
        } else {
            render.drawImage(buttonImg, cameraX + getX(), getY(), getW(), getH());
            render.setFill(color);
            render.fillText(t.getText(), cameraX + getX() + (getW() / 2) - wi, getY() + center);
            render.setFill(Color.WHITE);
        }
        if (imageIn != null) {
            render.drawImage(imageIn, cameraX + getX() + imageInX,
                    getY() + imageInY, imageInW, imageInH);
        }
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHover(MouseEvent e) {
        hoverButton = e.getX() > getX() * ZOOM && e.getX() < (getX() + getW()) * ZOOM
                && e.getY() > getY() * ZOOM && e.getY() < (getY() + getH()) * ZOOM;
    }

    public boolean isHover() {
        return hoverButton;
    }

    public boolean isClick(MouseEvent e) {
        return e.getX() > getX() * ZOOM && e.getX() < (getX() + getW()) * ZOOM
                && e.getY() > getY() * ZOOM && e.getY() < (getY() + getH()) * ZOOM;
    }

    public void addListener(Scene scene, Runnable runnable) {
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, this::setHover);
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (isClick(e)) {
                runnable.run();
            }
        });
    }

    public void setFontSize(double size) {
        try {
            this.font = Font.loadFont(getClass().getResource("/assets/font/upheavtt.ttf").toURI().toString(), size);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setTextCenter(double center) {
        this.center = center;
    }

    public void setTextCenterHover(double centerHover) {
        this.centerHover = centerHover;
    }

    public void setImageIn(String link, double w, double h) {
        try {
            this.imageIn = new Image(getClass().getResource(link).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.imageInW = w;
        this.imageInH = h;
        imageInX = getW() / 2 - w / 2;
        imageInY = getH() / 2 - h / 2;
    }

    public void setImageIn(Image image, double w, double h) {
        this.imageIn = image;
        this.imageInW = w;
        this.imageInH = h;
        imageInX = getW() / 2 - w / 2;
        imageInY = getH() / 2 - h / 2;
    }
}
