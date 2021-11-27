package com.snow.bomberman.setting;

import com.snow.bomberman.Game;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.button.Button;
import com.snow.bomberman.something.Control;
import com.snow.bomberman.something.slider.SlideBarH;
import com.snow.bomberman.something.slider.SliderButton;
import com.snow.bomberman.something.slider.ZoomSlider;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.snow.bomberman.Game.ZOOM;

public class Setting extends Control {
    private static double musicVolume = 0.5;
    private static double effectVolume = 0.5;
    private Button baseSetting;
    public static SlideBarH MUSICVOLUME;
    public static SlideBarH EFFECTVOLUME;
    public static ZoomSlider ZOOMSLIDER;
    public static SliderButton GRAPHICSHIGH;
    private static SliderButton HALLOWEENMODE;
    private static boolean INIT = false;
    private Color color;

    public Setting() {
        color = Color.web("262c40");
        baseSetting = new Button("");
        baseSetting.setX(getX() + 75);
        baseSetting.setY(getY() + 70);
        baseSetting.setButtonImg("/assets/something/pausescreen1.png");
        if (!INIT) {
            setX(0);
            MUSICVOLUME = new SlideBarH();
            MUSICVOLUME.setX(getX() + 200);
            MUSICVOLUME.setY(getY() + 100);
            MUSICVOLUME.setMaxValue(1);
            MUSICVOLUME.setValue(musicVolume);
            MUSICVOLUME.addValueChangeListener(() -> {
                musicVolume = MUSICVOLUME.getValue();
            });
            EFFECTVOLUME = new SlideBarH();
            EFFECTVOLUME.setX(getX() + 200);
            EFFECTVOLUME.setY(getY() + 150);
            EFFECTVOLUME.setMaxValue(1);
            EFFECTVOLUME.setValue(effectVolume);
            EFFECTVOLUME.addValueChangeListener(() -> {
                effectVolume = EFFECTVOLUME.getValue();
            });
            ZOOMSLIDER = new ZoomSlider();
            ZOOMSLIDER.setX(getX() + 200);
            ZOOMSLIDER.setY(getY() + 200);
            ZOOMSLIDER.addValueChangeListener(() -> {
                Game.setWindowScale(ZOOMSLIDER.getValue());
            });
            GRAPHICSHIGH = new SliderButton(0, 0);
            GRAPHICSHIGH.setX(getX() + 280);
            GRAPHICSHIGH.setY(getY() + 248);
            HALLOWEENMODE = new SliderButton(0, 0, "data/halloween.txt");
            HALLOWEENMODE.setX(getX() + 280);
            HALLOWEENMODE.setY(getY() + 280);
            INIT = true;
        }
    }

    public void draw(GraphicsContext render) {
        double cameraX = - (render.getCanvas().getLayoutX() / ZOOM);
        baseSetting.draw(render);
        render.setFill(color);
        String text = "MUSIC: ";
        render.setFont(PlayWindow.INTROFONT);
        render.fillText(text, cameraX + getX() + 100, getY() + 113);
        MUSICVOLUME.draw(render);
        render.setFill(color);
        text = "EFFECT: ";
        render.setFont(PlayWindow.INTROFONT);
        render.fillText(text, cameraX + getX() + 100, getY() + 163);
        EFFECTVOLUME.draw(render);
        render.setFill(color);
        text = "ZOOM: ";
        render.setFont(PlayWindow.INTROFONT);
        render.fillText(text, cameraX + getX() + 100, getY() + 213);
        ZOOMSLIDER.draw(render);
        if (GRAPHICSHIGH.isChecked()) {
            text = "GRAPHICS: HIGHT";
        } else {
            text = "GRAPHICS: LOW";
        }
        render.setFill(color);
        render.setFont(PlayWindow.INTROFONT);
        render.fillText(text, cameraX + getX() + 100, getY() + 263);
        GRAPHICSHIGH.draw(render);
        text = "HALLOWEEN MODE: ";
        render.setFont(PlayWindow.INTROFONT);
        render.fillText(text, cameraX + getX() + 100, getY() + 295);
        HALLOWEENMODE.draw(render);
    }


    @Override
    public void setX(double x) {
        super.setX(x);
        baseSetting.setX(getX() + 75);
        if (INIT) {
            MUSICVOLUME.setX(getX() + 200);
            EFFECTVOLUME.setX(getX() + 200);
            ZOOMSLIDER.setX(getX() + 200);
            GRAPHICSHIGH.setX(getX() + 280);
            HALLOWEENMODE.setX(getX() + 280);
        }
    }

    @Override
    public void setY(double y) {
        super.setY(y);
        baseSetting.setY(getY() + 70);
        if (INIT) {
            MUSICVOLUME.setY(getY() + 100);
            EFFECTVOLUME.setY(getY() + 150);
            ZOOMSLIDER.setY(getY() + 200);
            GRAPHICSHIGH.setY(getY() + 248);
            HALLOWEENMODE.setY(getY() + 280);
        }
    }

    public static double getMUSICVOLUME() {
        return musicVolume;
    }

    public static double getEFFECTVOLUME() {
        return effectVolume;
    }

    public static boolean isHalloween() {
        return HALLOWEENMODE.isChecked();
    }

    public void addEvent(Scene scene) {
        MUSICVOLUME.addListener(scene);
        EFFECTVOLUME.addListener(scene);
        ZOOMSLIDER.addListener(scene);
        GRAPHICSHIGH.addListener(scene);
        HALLOWEENMODE.addListener(scene);
    }

    public void setColorText(Color color) {
        this.color = color;
    }

    public static void saveData() {
        GRAPHICSHIGH.saveData();
        HALLOWEENMODE.saveData();
    }
}
