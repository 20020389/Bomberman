package com.snow.bomberman.screen;

import com.snow.bomberman.Game;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.Status;
import com.snow.bomberman.bomb.Bomb;
import com.snow.bomberman.button.Button;
import com.snow.bomberman.player.Bomberman;
import com.snow.bomberman.setting.Setting;
import com.snow.bomberman.something.Control;
import com.snow.bomberman.something.animation.TranslateAnimation;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URISyntaxException;

import static com.snow.bomberman.Game.ZOOM;
import static com.snow.bomberman.PlayWindow.PAUSE;


public class PauseScreen extends Control {
    private boolean showScreen;
    private final Button resumeButton;
    private final Button newgameButton;
    private final Button settingButton;
    private final Button profileButton;
    private final Button exitButton;

    private Button playerInfo;
    private Font font1;
    private Font font2;
    private Image playImg1;
    private Image playImg2;
    private int timeDrawPlayer;
    private Setting setting;

    private TranslateAnimation profileAnimation;
    private TranslateAnimation settingAnimation;

    private MessageBox newGameBox;
    private Runnable newGameDo;

    private MessageBox exitBox;
    private Runnable exitDo;

    enum Choosing {
        profile,
        setting
    }
    private Choosing choosing;

    public PauseScreen() {
        setW(Game.WINDOW.getWidth());
        setH(Game.WINDOW.getHeight());
        showScreen = false;
        resumeButton = new Button("RESUME");
        resumeButton.setX(15);
        resumeButton.setY(75);
        resumeButton.setButtonImg("/assets/something/play10.png");
        resumeButton.setButtonhHoverImg("/assets/something/play9.png");
        resumeButton.setFontSize(13);

        newgameButton = new Button("NEW GAME");
        newgameButton.setX(15);
        newgameButton.setY(125);
        newgameButton.setButtonImg("/assets/something/play10.png");
        newgameButton.setButtonhHoverImg("/assets/something/play9.png");
        newgameButton.setFontSize(13);

        profileButton = new Button("PLAYER");
        profileButton.setX(15);
        profileButton.setY(175);
        profileButton.setButtonImg("/assets/something/play10.png");
        profileButton.setButtonhHoverImg("/assets/something/play9.png");
        profileButton.setFontSize(13);

        settingButton = new Button("SETTING");
        settingButton.setX(15);
        settingButton.setY(225);
        settingButton.setButtonImg("/assets/something/play10.png");
        settingButton.setButtonhHoverImg("/assets/something/play9.png");
        settingButton.setFontSize(13);

        exitButton = new Button("EXIT");
        exitButton.setX(15);
        exitButton.setY(275);
        exitButton.setButtonImg("/assets/something/play10.png");
        exitButton.setButtonhHoverImg("/assets/something/play9.png");
        exitButton.setFontSize(13);

        playerInfo = new Button("");
        playerInfo.setX(125);
        playerInfo.setY(70);
        playerInfo.setButtonImg("/assets/something/pausescreen2.png");

        profileAnimation = new TranslateAnimation(playerInfo);

        timeDrawPlayer = 0;
        choosing = Choosing.profile;

        try {
            font1 = Font.loadFont(getClass().getResource("/assets/font/upheavtt.ttf").toURI().toString(), 17);
            font2 = Font.loadFont(getClass().getResource("/assets/font/upheavtt.ttf").toURI().toString(), 14);
            playImg1 = new Image(getClass().getResource("/assets/player/r1.png").toURI().toString());
            playImg2 = new Image(getClass().getResource("/assets/player/r2.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        newGameBox = null;
        exitBox = null;
        createMessageBox();
    }

    public void draw(GraphicsContext render) {
        double cameraX = - (render.getCanvas().getLayoutX() / ZOOM);

        render.setFill(Color.rgb(0, 0, 0, 0.5));
        render.fillRect(cameraX, 0, 1000, 1000);
        resumeButton.draw(render);
        newgameButton.draw(render);
        profileButton.draw(render);
        settingButton.draw(render);
        exitButton.draw(render);
        drawPlayerInfo(render, cameraX);
        setting.draw(render);

        newGameBox.draw(render);
        exitBox.draw(render);
        if (newGameBox.isYes()) {
            newGameBox.reset();
            resetLayout();
            newGameDo.run();
        }
        if (exitBox.isYes()) {
            exitBox.reset();
            resetLayout();
            exitDo.run();
        }

        settingAnimation.doing();
        profileAnimation.doing();
    }

    public void drawPlayerInfo(GraphicsContext render, double cameraX) {
        timeDrawPlayer ++;
        if (timeDrawPlayer > 40) {
            timeDrawPlayer = 0;
        }
        playerInfo.draw(render);
        render.setFill(Color.web("262c40"));
        render.setFont(font2);
        render.fillText("x", cameraX + playerInfo.getX() + 180, playerInfo.getY() + 42);
        render.setFont(font1);
        render.fillText(PlayWindow.LIFE + "", cameraX + playerInfo.getX() + 190, playerInfo.getY() + 42);
        render.fillText("SCORE: " + PlayWindow.SCORE, cameraX + playerInfo.getX() + 150, playerInfo.getY() + 70);
        if (timeDrawPlayer <= 20) {
            render.drawImage(playImg1, cameraX + playerInfo.getX() + 65, playerInfo.getY() + 30);
        } else {
            render.drawImage(playImg2, cameraX + playerInfo.getX() + 65, playerInfo.getY() + 30);
        }

        //info limit bomb
        render.setFont(font2);
        render.fillText("x", cameraX + playerInfo.getX() + 93, playerInfo.getY() + 115);
        render.setFont(font1);
        render.fillText(Bomberman.LIMITBOMB + "", cameraX + playerInfo.getX() + 103, playerInfo.getY() + 115);

        //info bomcontrolled
        render.setFont(font2);
        render.fillText("x", cameraX + playerInfo.getX() + 93, playerInfo.getY() + 165);
        render.setFont(font1);
        render.fillText(PlayWindow.BOMBCONTROLLED + "", cameraX + playerInfo.getX() + 103, playerInfo.getY() + 165);

        //info bomb leght
        render.setFont(font2);
        render.fillText("x", cameraX + playerInfo.getX() + 93, playerInfo.getY() + 215);
        render.setFont(font1);
        render.fillText(Bomb.BOOMLEGHT + "", cameraX + playerInfo.getX() + 103, playerInfo.getY() + 215);

        //info bomb leght
        render.setFont(font2);
        render.fillText("x", cameraX + playerInfo.getX() + 194, playerInfo.getY() + 115);
        render.setFont(font1);
        render.fillText(Bomberman.isHasSpeedUp() ? "1" : "0",
                cameraX + playerInfo.getX() + 204, playerInfo.getY() + 115);

        //info wallpass
        render.setFont(font2);
        render.fillText("x", cameraX + playerInfo.getX() + 194, playerInfo.getY() + 165);
        render.setFont(font1);
        render.fillText(Bomberman.canWallPass() ? "1" : "0",
                cameraX + playerInfo.getX() + 204, playerInfo.getY() + 165);

        //info flamepass
        render.setFont(font2);
        render.fillText("x", cameraX + playerInfo.getX() + 194, playerInfo.getY() + 215);
        render.setFont(font1);
        render.fillText(PlayWindow.FLAMEPASS + "",
                cameraX + playerInfo.getX() + 204, playerInfo.getY() + 215);

        //info flash
        render.setFont(font2);
        render.fillText("x", cameraX + playerInfo.getX() + 295, playerInfo.getY() + 115);
        render.setFont(font1);
        render.fillText(PlayWindow.FLASH + "",
                cameraX + playerInfo.getX() + 305, playerInfo.getY() + 115);

        //info find life on dead
        render.setFont(font2);
        render.fillText("x", cameraX + playerInfo.getX() + 295, playerInfo.getY() + 165);
        render.setFont(font1);
        render.fillText((PlayWindow.FINDLIFEONDEAD ? 1 : 0) + "",
                cameraX + playerInfo.getX() + 305, playerInfo.getY() + 165);
    }

    private void createMessageBox() {
        newGameBox = new MessageBox(MessageBox.YESandNO);
        newGameBox.setLabel("Do you want new game ?");
        newGameBox.setX(100);
        newGameBox.setY(100);

        exitBox = new MessageBox(MessageBox.YESandNO);
        exitBox.setLabel("Do you want exit ?");
        exitBox.setX(100);
        exitBox.setY(100);
    }

    public boolean isShown() {
        return Game.STATUS == Status.play && PAUSE && !MessageBox.SHOWN;
    }

    public void addResumeListener(Scene scene, Runnable runnable) {
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (isShown()) {
                if (resumeButton.isClick(e)) {
                    resetLayout();
                    runnable.run();
                }
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_MOVED, (e) -> {
            if (!MessageBox.SHOWN) {
                resumeButton.setHover(e);
            }
        });
    }

    public void addNewGameListener(Scene scene, Runnable runnable) {
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (isShown()) {
                if (newgameButton.isClick(e)) {
                    newGameBox.show();
                    newGameDo = runnable;
                }
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_MOVED, (e) -> {
            if (!MessageBox.SHOWN) {
                newgameButton.setHover(e);
            }
        });
    }

    public void addExitListener(Scene scene, Runnable runnable) {
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (isShown()) {
                if (exitButton.isClick(e)) {
                    exitBox.show();
                    exitDo = runnable;
                }
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_MOVED, (e) -> {
            if (!MessageBox.SHOWN) {
                exitButton.setHover(e);
            }
        });
    }

    public void addSettingListener(Scene scene) {
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (isShown()) {
                if (settingButton.isClick(e)) {
                    if (choosing == Choosing.profile) {
                        showSetting();
                        settingAnimation.play();
                        profileAnimation.play();
                        choosing = Choosing.setting;
                    }
                }
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_MOVED, (e) -> {
            if (!MessageBox.SHOWN) {
                settingButton.setHover(e);
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (isShown()) {
                if (profileButton.isClick(e)) {
                    if (choosing == Choosing.setting) {
                        showSetting();
                        settingAnimation.play();
                        profileAnimation.play();
                        choosing = Choosing.profile;
                    }
                }
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            if (!MessageBox.SHOWN) {
                profileButton.setHover(e);
            }
        });
    }

    public void show() {
        showScreen = true;
    }

    public void hide() {
        showScreen = false;
    }

    public void addSetting(Setting setting) {
        this.setting = setting;
        settingAnimation = new TranslateAnimation(setting);
    }

    public void resetLayout() {
        playerInfo.setX(125);
        playerInfo.setY(70);
        choosing = Choosing.profile;
        setting.setX(50);
        setting.setY(420);
    }

    private void showSetting() {
        settingAnimation.stop();
        profileAnimation.stop();
        if (choosing == Choosing.profile) {
            profileAnimation.setStartX(125);
            profileAnimation.setStartY(70);
            profileAnimation.setStopX(125);
            profileAnimation.setStopY(-500);
            profileAnimation.setSpeed(15);

            settingAnimation.setStartX(50);
            settingAnimation.setStartY(420);
            settingAnimation.setStopX(50);
            settingAnimation.setStopY(0);
            settingAnimation.setSpeed(15);
        } else {
            profileAnimation.setStartX(playerInfo.getX());
            profileAnimation.setStartY(-500);
            profileAnimation.setStopX(125);
            profileAnimation.setStopY(70);

            settingAnimation.setStopX(50);
            settingAnimation.setStopY(420);
            settingAnimation.setStartX(50);
            settingAnimation.setStartY(0);
        }
    }
}
