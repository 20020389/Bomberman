package com.snow.bomberman.something.slider;

import com.snow.bomberman.screen.MessageBox;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

import static com.snow.bomberman.Game.ZOOM;

public class SliderButton {
    private boolean checked;
    private double x;
    private double y;
    private double width;
    private double height;
    private boolean effect;
    private double thumbX;
    private String linkData;
    private LinkedList<Runnable> event;
    protected Image thumbImg;
    protected Image barImg;

    public SliderButton(int x, int y) {
        linkData = "data/graphics.txt";
        this.x = x;
        this.y = y;
        doData(true);
        effect = false;
        event = new LinkedList<>();
        try {
            thumbImg = new Image(getClass().getResource("/assets/something/thumb2.png").toURI().toString());
            barImg = new Image(getClass().getResource("/assets/something/bar2.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        height = barImg.getHeight();
        width = barImg.getWidth();
        if (checked) {
            thumbX = x + width - thumbImg.getWidth();
        } else {
            thumbX = x;
        }
    }

    public SliderButton(int x, int y, String linkData) {
        this.linkData = linkData;
        this.x = x;
        this.y = y;
        doData(true);
        effect = false;
        event = new LinkedList<>();
        try {
            thumbImg = new Image(getClass().getResource("/assets/something/thumb2.png").toURI().toString());
            barImg = new Image(getClass().getResource("/assets/something/bar2.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        height = barImg.getHeight();
        width = barImg.getWidth();
        if (checked) {
            thumbX = x + width - thumbImg.getWidth();
        } else {
            thumbX = x;
        }
    }

    public void draw(GraphicsContext render) {
        double cameraX = - (render.getCanvas().getLayoutX() / ZOOM);
        Paint lastColor = render.getFill();
        if (effect) {
            setEffect();
        }
        if (checked) {
            render.setFill(Color.web("#0C9CFF"));
        } else {
            render.setFill(Color.GRAY);
        }
        render.drawImage(barImg, cameraX + x, y);
        render.setFill(Color.WHITE);
        render.drawImage(thumbImg, cameraX + thumbX, y);
        render.setFill(lastColor);
    }

    private void setEffect() {
        if (!checked) {
            thumbX -= 2;
            if (thumbX < x) {
                thumbX = x;
                effect = false;
                return;
            }
        } else {
            thumbX += 2;
            if (thumbX > x + width - thumbImg.getWidth()) {
                thumbX = x + width - thumbImg.getWidth();
                effect = false;
                return;
            }
        }
    }

    public void addListener(Scene scene) {
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (!MessageBox.SHOWN) {
                if (e.getX() / ZOOM >= x && e.getX() / ZOOM <= x + width
                        && e.getY() / ZOOM >= y && e.getY() / ZOOM <= y + height) {
                    if (!effect) {
                        effect = true;
                        for (Runnable i : event) {
                            i.run();
                        }
                        if (checked) {
                            checked = false;
                        } else {
                            checked = true;
                        }
                    }
                }
            }
        });
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, (e) -> {
            if (e.getX() / ZOOM >= x && e.getX() / ZOOM <= x + width
                    && e.getY() / ZOOM >= y && e.getY() / ZOOM <= y + height) {
                scene.setCursor(Cursor.HAND);
            } else {
                scene.setCursor(Cursor.DEFAULT);
            }
        });
    }

    public void addChangeListener(Runnable e) {
        event.add(e);
    }

    public void removeChangeListener(Runnable e) {
        event.remove(e);
    }

    private void doData(boolean read) {
        try {
            File file = new File(Paths.get(linkData).toUri());
            if (read) {
                Scanner scanner = new Scanner(file);
                int check = scanner.nextInt();
                checked = check == 1;
                scanner.close();
            } else {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write((checked) ? 1 + "" : 0 + "");
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLinkData(String linkData) {
        this.linkData = linkData;
    }

    public boolean isChecked() {
        return checked;
    }

    public void saveData() {
        doData(false);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        if (checked) {
            thumbX = x + width - height - height / 16;
        } else {
            thumbX = x - height / 16;
        }
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        if (checked) {
            thumbX = x + width - height - height / 16;
        } else {
            thumbX = x - height / 16;
        }
        this.y = y;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        width = height * 2;
        if (checked) {
            thumbX = x + width - height - height / 16;
        } else {
            thumbX = x - height / 16;
        }
    }
}


/*
Paint lastColor = render.getFill();
        if (effect) {
            setEffect();
        }
        if (checked) {
            render.setFill(Color.web("#0C9CFF"));
        } else {
            render.setFill(Color.GRAY);
        }
        render.fillOval(x, y, height, height);
        render.fillOval(x + width - height, y, height, height);
        render.fillRect(x + height / 2, y, height, height);
        render.setFill(Color.WHITE);
        render.fillOval(thumbX,
                y - height / 16, height + height / 8, height + height / 8);
        render.setFill(lastColor);
* */