package com.snow.bomberman.something;

import com.snow.bomberman.Game;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.setting.Setting;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.snow.bomberman.Game.*;

public class IntroScreen {
    public static Media INTROAUDIO;
    private String text;
    private double w;
    private double h;
    private Font lastFont;
    private int timeShowIntro;
    private boolean showIntro;
    private LocalDateTime timeStart;
    private LocalDateTime timeNow;

    public IntroScreen(String text) {
        try {
            INTROAUDIO = new Media(IntroScreen.class.getResource("/assets/audio/intro.wav").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.text = "LEVEL " + text;
        setShowIntro(true);
        timeShowIntro = 0;
    }

    public void setText(String text) {
        this.text = "LEVEL " + text;
    }

    public void setIntro(String text) {
        this.text = text;
    }

    public void setShowIntro(boolean showIntro) {
        timeStart = LocalDateTime.now();
        MediaPlayer mediaPlayer = new MediaPlayer(INTROAUDIO);
        if (!MUTEMUSIC) {
            mediaPlayer.setVolume(Setting.MUSICVOLUME.getValue());
            mediaPlayer.play();
        }
        PlayWindow.GAMEAUDIO.stop();
        this.showIntro = showIntro;
    }

    public boolean isShowIntro() {
        return showIntro;
    }

    public void draw(GraphicsContext render) {
        if (showIntro) {
            double cameraX = - (render.getCanvas().getLayoutX() / ZOOM);
            w = WINDOW.getWidth() / Game.ZOOM;
            h = WINDOW.getHeight() / Game.ZOOM;
            render.setFill(Color.BLACK);
            render.fillRect(cameraX, 0, w, h);
            render.setFill(Color.WHITE);
            render.setFont(PlayWindow.INTROFONT);
            render.fillText(text, cameraX + w / 2 - 40, h / 2);
            timeNow = LocalDateTime.now();
            if (Duration.between(timeStart, timeNow).getSeconds() == 3) {
                showIntro = false;
                Time.resetTime();
                timeShowIntro = 0;
            }
        }
    }
}
