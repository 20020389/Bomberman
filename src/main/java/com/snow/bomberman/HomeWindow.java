package com.snow.bomberman;

import com.snow.bomberman.button.Button;
import com.snow.bomberman.music.MusicPlayer;
import com.snow.bomberman.setting.Setting;
import com.snow.bomberman.something.Control;
import com.snow.bomberman.something.animation.TranslateAnimation;
import com.snow.bomberman.guide.Guide;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;

import static com.snow.bomberman.Game.MUTEMUSIC;
import static com.snow.bomberman.setting.Setting.MUSICVOLUME;

public class HomeWindow extends Control {
    private double x1;
    private double x2;
    private double w;
    private double h;
    private Image background;
    private Image logo;
    private static MediaPlayer audio;
    private final Button playBt;
    private final Button settingBt;
    private final Button aventureBt;
    private final Button guideBt;
    private final Button trainingBt;
    private Setting settingScreen;
    private Guide guideScreen;

    private final TranslateAnimation toSetting;
    private final TranslateAnimation switchSetting;
    private final TranslateAnimation toHome;
    private final TranslateAnimation switchHome;
    private final TranslateAnimation toGuide;
    private final TranslateAnimation switchGuide;

    public HomeWindow() {
        this.w = 576;
        this.h = 417;
        x1 = 0;
        x2 = x1 + w;
        playBt = new Button("PLAY");
        playBt.setX(getX() + 149.6);
        playBt.setY(150);
        playBt.setButtonImg("/assets/something/play8.png");
        playBt.setButtonhHoverImg("/assets/something/play7.png");


//        playBt.setColor(Color.WHITE);

        settingBt = new Button("SETTING");
        settingBt.setX(getX() + 149.6);
        settingBt.setY(300);
        settingBt.setButtonImg("/assets/something/play10.png");
        settingBt.setButtonhHoverImg("/assets/something/play9.png");

//        settingBt.setColor(Color.WHITE);

        trainingBt = new Button("Training");
        trainingBt.setX(getX() + 149.6);
        trainingBt.setY(250);
        trainingBt.setButtonImg("/assets/something/play8.png");
        trainingBt.setButtonhHoverImg("/assets/something/play7.png");

        aventureBt = new Button("AVENTURE");
        aventureBt.setX(getX() + 149.6);
        aventureBt.setY(200);
        aventureBt.setButtonImg("/assets/something/play8.png");
        aventureBt.setButtonhHoverImg("/assets/something/play7.png");

//        aventureBt.setColor(Color.WHITE);

        guideBt = new Button("GUIDE");
        guideBt.setX(getX() + 249.6);
        guideBt.setY(300);
        guideBt.setButtonImg("/assets/something/play10.png");
        guideBt.setButtonhHoverImg("/assets/something/play9.png");

        setX(0);
        try {
            background = new Image(HomeWindow.class.getResource("/assets/bg.png").toURI().toString());
            logo = new Image(HomeWindow.class.getResource("/assets/Logo2.png").toURI().toString());
            Media media = new Media(HomeWindow.class.getResource("/assets/audio/homestart.mp3").toURI().toString());
            settingScreen = new Setting();
            settingScreen.setX(w - 100);
            settingScreen.addEvent(Game.WINDOW);
            guideScreen = new Guide();
            guideScreen.setY(h - 40);
            guideScreen.addEvent(Game.WINDOW);

            MUSICVOLUME.addValueChangeListener(() -> {
                setVolume(MUSICVOLUME.getValue());
                MusicPlayer.SETVOLUME.run();
            });
            audio = new MediaPlayer(media);
            audio.setVolume(Setting.MUSICVOLUME.getValue());
            audio.setCycleCount(Integer.MAX_VALUE);
            if (!MUTEMUSIC) {
                audio.play();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Animation
        toSetting = new TranslateAnimation(settingScreen);
        toSetting.setStopX(0);
        toSetting.setSpeed(10);

        switchSetting = new TranslateAnimation(settingScreen);
        switchSetting.setStopX(settingScreen.getX());
        switchSetting.setSpeed(10);

        toHome = new TranslateAnimation(this);
        toHome.setStopX(0);
        toHome.setSpeed(10);

        switchHome = new TranslateAnimation(this);
        switchHome.setStopX(-w);
        switchHome.setSpeed(10);

        toGuide = new TranslateAnimation(guideScreen);
        toGuide.setStopY(0);
        toGuide.setSpeed(10);

        switchGuide = new TranslateAnimation(guideScreen);
        switchGuide.setStopY(h - 40);
        switchGuide.setSpeed(10);
    }

    public void turnOffAudio() {
        audio.stop();
    }

    public static void muteMusic() {
        MUTEMUSIC = true;
        PlayWindow.muteMusic();
        if (audio != null) {
            audio.pause();
        }
    }

    public static void resumeMusic() {
        MUTEMUSIC = false;
        if (audio != null && (Game.STATUS != Status.play)) {
            audio.play();
        }
    }


    public void setVolume(double volume) {
        PlayWindow.setVolume(volume);
        if (audio != null) {
            audio.setVolume(volume);
        }
    }

    public void setW(double w) {
        this.w = w;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void draw(GraphicsContext render) {
        render.drawImage(background, x1, 0);
        render.drawImage(background, x2, 0);

        updateBt();

        render.drawImage(logo, 14.6 + getX(), 30);
        playBt.draw(render);
        settingBt.draw(render);
        aventureBt.draw(render);
        guideBt.draw(render);
        trainingBt.draw(render);

        settingScreen.draw(render);
        guideScreen.draw(render);

        /*if (Game.STATUS == Status.home) {

        } else if (Game.STATUS == Status.setting){

        }*/

        x1--;
        x2--;
        if (x1 == - w) {
            x1 = w;
        }
        if (x2 == - w) {
            x2 = w;
        }
    }


    @Override
    public void setX(double x) {
        super.setX(x);
        playBt.setX(getX() + 149.6);
        trainingBt.setX(getX() + 149.6);
        aventureBt.setX(getX() + 149.6);
        settingBt.setX(getX() + 149.6);
        guideBt.setX(getX() + 249.6);
    }

    private void updateBt() {
        toHome.doing();
        toSetting.doing();
        switchHome.doing();
        switchSetting.doing();
        toGuide.doing();
        switchGuide.doing();
    }

    public void checkHoverPlay(MouseEvent e) {
        playBt.setHover(e);
        settingBt.setHover(e);
        aventureBt.setHover(e);
        guideBt.setHover(e);
        trainingBt.setHover(e);
    }

    public boolean playClick(MouseEvent e) {
        return playBt.isClick(e);
    }

    public boolean aventureClick(MouseEvent e) {
        return aventureBt.isClick(e);
    }

    public boolean trainingClick(MouseEvent e) {
        return trainingBt.isClick(e);
    }

    public void settingClick(MouseEvent e) {
        if (settingBt.isClick(e)) {
            toSetting.play();
            switchHome.play();
            toHome.stop();
            switchSetting.stop();
            Game.STATUS = Status.setting;
        }
    }

    public void guideClick(MouseEvent e) {
        if (guideBt.isClick(e)) {
            toGuide.play();
            switchHome.play();
            switchGuide.stop();
            toHome.stop();
            Game.STATUS = Status.guide;
            guideScreen.resetStatus();
        }
    }

    public void backHome() {
        toHome.play();
        switchSetting.play();
        switchGuide.play();
        toGuide.stop();
        toSetting.stop();
        switchHome.stop();
        Game.STATUS = Status.home;
    }

    public void resetSettingScreen() {
        settingScreen.setX(w - 100);
        settingScreen.setY(0);
    }

    public Setting getSetting() {
        return settingScreen;
    }
}


