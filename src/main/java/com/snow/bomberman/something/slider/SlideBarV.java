package com.snow.bomberman.something.slider;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URISyntaxException;
import java.util.LinkedList;

import static com.snow.bomberman.Game.ZOOM;

public class SlideBarV {
    protected double x;
    protected double y;
    protected double thumbY;
    protected double width;
    protected double height;
    protected double minValue;
    protected double maxValue;
    protected double value;
    protected boolean mouseDown;
    protected double mouseRange;
    protected LinkedList<Runnable> work;
    protected double lastValue;
    protected Font font;
    protected Image thumbImg;
    protected Image barImg;

    public SlideBarV() {
        x = 0;
        y = 0;
        width = 16;
        height = 188;
        thumbY = 0;
        mouseDown = false;
        mouseRange = 0;
        value = 30;
        lastValue = value;
        minValue = 0;
        maxValue = 100;
        try {
            font = Font.loadFont(getClass().getResource("/assets/font/upheavtt.ttf").toURI().toString(), 7);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            thumbImg = new Image(getClass().getResource("/assets/something/thumb1.png").toURI().toString());
            barImg = new Image(getClass().getResource("/assets/something/bar4.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        work = new LinkedList<>();
    }

    public void draw(GraphicsContext render) {
        value = minValue + (thumbY - y) * (maxValue - minValue) / (height - width);
        double cameraX = - (render.getCanvas().getLayoutX() / ZOOM);
        render.drawImage(barImg, cameraX + x + (width / 2 - (width / 4)), y);
        render.drawImage(thumbImg, cameraX + getX(), thumbY);
        render.setFill(Color.WHITE);
        /*String text = (int) (value * 100) + "";
        Text t = new Text(text);
        t.setFont(font);
        render.setFont(font);
        render.fillText(text, cameraX + thumbY + height / 2 - t.getBoundsInLocal().getWidth() / 2
                , y + 10);
//        render.appendSVGPath("M172.268 501.67C26.97 291.031 0 269.413 0 192 0 85.961 85.961 0 192 0s192 85.961 192 192c0 77.413-26.97 99.031-172.268 309.67-9.535 13.774-29.93 13.773-39.464 0z");
*/
        render.setFill(Color.WHITE);
        if (lastValue != value) {
            for (Runnable i : work) {
                i.run();
            }
            lastValue = value;
        }
    }

    public void addListener(Scene scene) {
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::addMouseHove);
        scene.addEventHandler(MouseEvent.MOUSE_RELEASED, this::addMouseUp);
    }

    public void addMouseDown(MouseEvent e) {
        if (e.getX() / ZOOM >= x && e.getX() / ZOOM <= x + width
                && e.getY() / ZOOM >= thumbY && e.getY() / ZOOM <= thumbY + width) {
            mouseDown = true;
            mouseRange = e.getY() / ZOOM - thumbY;
        } else if (e.getX() / ZOOM >= x && e.getX() / ZOOM <= x + width
                && e.getY() / ZOOM >= y && e.getY() / ZOOM <= y + height) {
            mouseDown = true;
            mouseRange = width / 2;
            thumbY = e.getY() / ZOOM - mouseRange;
        }
    }

    public void addValueChangeListener(Runnable runnable) {
        work.add(runnable);
    }

    public void addMouseHove(MouseEvent e) {
        if (mouseDown) {
            if (e.getY() / ZOOM - mouseRange >= y && e.getY() / ZOOM - mouseRange + width <= y + height) {
                thumbY = e.getY() / ZOOM - mouseRange;
            } else if (e.getY() / ZOOM - mouseRange < y){
                thumbY = y;
            } else {
                thumbY = y + height - width;
            }
        }
    }

    public void addMouseUp(MouseEvent e) {
        mouseDown = false;
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
        thumbY = (value - minValue) / (maxValue - minValue) * (height - width) + y;
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (value >= minValue && value <= maxValue) {
            thumbY = (value - minValue) / (maxValue - minValue) * (height - width) + y;
            this.value = value;
        }
    }
}
