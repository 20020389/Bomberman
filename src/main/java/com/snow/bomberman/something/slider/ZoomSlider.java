package com.snow.bomberman.something.slider;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.snow.bomberman.Game.ZOOM;

public class ZoomSlider extends SlideBarH {
    private double range;

    public ZoomSlider() {
        super();
        setMaxValue(2);
        setMinValue(0.5);
        setValue(ZOOM);
        range = (width - height) / 6;
    }

    @Override
    public void addMouseDown(MouseEvent e) {
        if (e.getX() / ZOOM >= thumbX && e.getX() / ZOOM <= thumbX + height
                && e.getY() / ZOOM >= y && e.getY() / ZOOM <= y + height) {
            mouseDown = true;
            mouseRange = e.getX() /ZOOM - thumbX;
        } else if (e.getX() / ZOOM >= x && e.getX() / ZOOM <= x + width
                && e.getY() / ZOOM >= y && e.getY() / ZOOM <= y + height) {
            mouseDown = true;
            mouseRange = height / 2;
            thumbX = e.getX() / ZOOM - mouseRange;
            thumbX = (thumbX - x) / range;
            thumbX = lamtron(thumbX) * (range) + x;
        }
    }

    double lamtron(double a) {
        double num = (int) (a);
        if (num + 0.5 <= a) {
            num += 1;
        }
        return num;
    }

    @Override
    public void draw(GraphicsContext render) {
        value = minValue + (thumbX - x) * (maxValue - minValue) / (width - height);
        double cameraX = - (render.getCanvas().getLayoutX() / ZOOM);
        render.drawImage(barImg, cameraX + x , y + (height / 2 - (height / 4)));
        render.drawImage(thumbImg, cameraX + thumbX, y);
        render.setFill(Color.WHITE);
        String text = String.format("%.1f", value);
        Text t = new Text(text);
        t.setFont(font);
        render.setFont(font);
        render.fillText(text, cameraX + thumbX + height / 2 - t.getBoundsInLocal().getWidth() / 2
                , y + 9);
//        render.appendSVGPath("M172.268 501.67C26.97 291.031 0 269.413 0 192 0 85.961 85.961 0 192 0s192 85.961 192 192c0 77.413-26.97 99.031-172.268 309.67-9.535 13.774-29.93 13.773-39.464 0z");

        render.setFill(Color.WHITE);
        if (lastValue != value) {
            for (Runnable i : work) {
                i.run();
            }
            lastValue = value;
        }
    }

    @Override
    public void addMouseHove(MouseEvent e) {

    }

    @Override
    public void setHeight(double height) {
        this.height = height;
        range = (width - height) / 6;
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
        range = (width - height) / 6;
    }

    @Override
    public void setValue(double value) {
        value = lamtron(value * 100) / 100;
        if (value >= minValue && value <= maxValue) {
            thumbX = (value - minValue) / (maxValue - minValue) * (width - height) + x;
            this.value = value;
        }
    }
}
