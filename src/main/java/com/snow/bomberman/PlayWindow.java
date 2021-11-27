package com.snow.bomberman;

import com.snow.bomberman.bomb.Bomb;
import com.snow.bomberman.info.InfoPlayer;
import com.snow.bomberman.items.Item;
import com.snow.bomberman.map.AventureMap;
import com.snow.bomberman.map.ListMap;
import com.snow.bomberman.map.Map;
import com.snow.bomberman.mob.Mobs;
import com.snow.bomberman.mob.enemy.Kondoria;
import com.snow.bomberman.mob.enemy.Pass;
import com.snow.bomberman.player.Bomberman;
import com.snow.bomberman.setting.Setting;
import com.snow.bomberman.something.IntroScreen;
import com.snow.bomberman.something.Point;
import com.snow.bomberman.something.Time;
import com.snow.bomberman.screen.PauseScreen;
import com.snow.bomberman.something.textbox.TextBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URISyntaxException;

import static com.snow.bomberman.player.Bomberman.LISTBOMB;
import static com.snow.bomberman.player.Bomberman.LISTBOMBCONTROL;
import static com.snow.bomberman.Game.MUTEMUSIC;

public class PlayWindow {
    public static final int START = 0;
    private Media gameMedia;
    public static MediaPlayer GAMEAUDIO;
    public static Media CLEARAUDIO;
    private GraphicsContext render;
    private ListMap listMap;
    private Map map;
    private Bomberman bomberman;

    public static int LIFE;
    public static int BONUSLIFE;
    public static int BOMBCONTROLLED;
    public static int FLAMEPASS;
    public static int FLASH;
    public static boolean FINDLIFEONDEAD;

    private int level;
    private boolean lose;
    private boolean win;
    private InfoPlayer infoPlayer;
    public static Font IVFONT;
    public static Font MCFONT;
    public static Font INTROFONT;
    public static Font HOMEFONT;
    public static Point PORTAL;
    public static int SCORE;
    public static boolean WINLEVEL;
    private IntroScreen introScreen;
    private boolean introWin;
    private boolean introLose;
    public static boolean PAUSE;
    private static Mode MODE;
    private PauseScreen pauseScreen;
    private long timeLimitFlash;
    private boolean canFlash;
    private TextBox textBox;

    public PlayWindow(GraphicsContext render, Mode mode, PauseScreen pauseScreen) {
        this.render = render;
        MODE = mode;
        if (mode == Mode.normal) {
            listMap = new ListMap();
            Time.LIMITTIME = 200;
            BOMBCONTROLLED = 3;
            FLAMEPASS = 0;
            FLASH = 0;
            Bomb.BOOMLEGHT = 1;
            Bomberman.WALLPASS = 2;
            Bomberman.SPEED = 2;
            Bomberman.LIMITBOMB = 1;
        } else if (mode == Mode.aventure) {
            listMap = new AventureMap();
            Time.LIMITTIME = 300;
            int []item = Item.doItemAventure(true);
            BOMBCONTROLLED = item[0];
            FLAMEPASS = item[1];
            FLASH = item[2];
            Bomb.BOOMLEGHT = 1;
            Bomberman.WALLPASS = 2;
            FINDLIFEONDEAD = false;
            Bomberman.SPEED = 2;
            Bomberman.LIMITBOMB = 1;
            listMap.newMap(level);
        } else {
            listMap = new ListMap(true);
            Time.LIMITTIME = 3600;
            BOMBCONTROLLED = 0;
            FLAMEPASS = 0;
            FLASH = 0;
            Bomb.BOOMLEGHT = 1;
            Bomberman.WALLPASS = 2;
            Bomberman.SPEED = 2;
            Bomberman.LIMITBOMB = 1;
        }
        level = START;
        LIFE = 3;
        PAUSE = false;
        BONUSLIFE = 0;
        WINLEVEL = false;
        SCORE = 0;
        createObject();
        introWin = false;
        introLose = false;
        try {
            gameMedia = new Media(getClass().getResource("/assets/audio/gameaudio.wav").toURI().toString());
            CLEARAUDIO = new Media(getClass().getResource("/assets/audio/clear.wav").toURI().toString());
            createAudio();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        infoPlayer = new InfoPlayer();
        introScreen = new IntroScreen(level + 1 + "");
        this.pauseScreen = pauseScreen;
        timeLimitFlash = 0;
        canFlash = true;
    }

    public void createAudio() {
        GAMEAUDIO = new MediaPlayer(gameMedia);
        GAMEAUDIO.setCycleCount(Integer.MAX_VALUE);
    }

    public void createObject() {
        Mobs.MOBS.clear();
        map = new Map(listMap.createMap(level),
                listMap.getItem(level) ,
                listMap.getPortal(level));
        bomberman = new Bomberman(32, 32);
        bomberman.setMap(map.getTile(), map.getMapHash());
        lose = false;
        win = false;
        WINLEVEL = false;
        listMap.createEnemy(level, map.getMapHash());
        render.setFill(Color.WHITE);
        if (MODE == Mode.training) {
            textBox = new TextBox(map.getMapHash());
        }
    }

    public static void getFont() {
        IVFONT = null;
        try {
            IVFONT = Font.loadFont(PlayWindow.class.getResource("/assets/font/upheavtt.ttf").toURI().toString(), 10);
            MCFONT = Font.loadFont(PlayWindow.class.getResource("/assets/font/upheavtt.ttf").toURI().toString(), 14);
            INTROFONT = Font.loadFont(PlayWindow.class.getResource("/assets/font/upheavtt.ttf").toURI().toString(), 20);
            HOMEFONT = Font.loadFont(PlayWindow.class.getResource("/assets/font/upheavtt.ttf").toURI().toString(), 17);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void muteMusic() {
        if (GAMEAUDIO != null) {
            GAMEAUDIO.pause();
        }
    }

    public static void setVolume(double volume) {
        if (GAMEAUDIO != null) {
            GAMEAUDIO.setVolume(volume);
        }
    }

    public void drawIntro() {
        if (introScreen.isShowIntro()) {
            introScreen.draw(render);
        }
    }

    public void draw() {
        if (introScreen.isShowIntro()) {
            return;
        }
        render.clearRect(0, 0, render.getCanvas().getWidth(), render.getCanvas().getHeight());
        if (!bomberman.isDeading() && !MUTEMUSIC) {
            GAMEAUDIO.setVolume(Setting.MUSICVOLUME.getValue());
            GAMEAUDIO.play();
        }
        if (introWin) {
            win = true;
            return;
        }
        if (introLose) {
            lose = true;
            return;
        }
        if (WINLEVEL) {
            ++level;
            /*if (MODE == Mode.aventure && level % 10 == 5) {
                level += 5;
            }*/
            if (level < listMap.size()) {
                listMap.newMap(level);
                createObject();
                introScreen.setText(level + 1 + "");
                introScreen.setShowIntro(true);
            } else {
                introScreen.setIntro("YOU WIN");
                introScreen.setShowIntro(true);
                render.getCanvas().setLayoutX(0);
                introWin = true;
                return;
            }
        }
        if (!bomberman.isSurvival() || Time.getTime() == 0) {
            LIFE--;
            if (LIFE == 0) {
                introScreen.setIntro("YOU LOSE");
                introScreen.setShowIntro(true);
                render.getCanvas().setLayoutX(0);
                introLose = true;
                return;
            }
            PAUSE = false;
            createObject();
            introScreen.setShowIntro(true);
        }
        map.draw(render);
        drawBomb(render);
        drawEnemy(render);
        bomberman.draw(render);
        infoPlayer.draw(render);
        if (timeLimitFlash - Time.getTime() >= 1) {
            canFlash = true;
        }
        if (PAUSE && MODE != Mode.training) {
            pauseScreen.draw(render);
        } else if (PAUSE){
            if (!textBox.isShowing()) {
                pauseScreen.draw(render);
            } else {
                textBox.draw(render);
            }
        }
    }

    public void kill() {
        PAUSE = false;
        GAMEAUDIO.stop();
    }

    private void drawEnemy(GraphicsContext render) {
        for (int i = 1; i < Mobs.MOBS.size(); i++) {
            Mobs.MOBS.get(i).draw(render);
        }
    }

    private void drawBomb(GraphicsContext render) {
        if (LISTBOMB.size() > 0) {
            for (int i = 0; i < LISTBOMB.size(); ) {
                LISTBOMB.get(i).draw(render);
                if (LISTBOMB.get(i).isBoomed()) {
                    LISTBOMB.remove(i);
                    continue;
                }
                i++;
            }
        }
        if (LISTBOMBCONTROL.size() > 0) {
            for (int i = 0; i < LISTBOMBCONTROL.size(); i++) {
                LISTBOMBCONTROL.get(i).draw(render);
            }
        }
    }

    public double getWidth() {
        return map.getWidth();
    }

    public double getHeigh() {
        return map.getHeigh();
    }

    public void keyDown(KeyEvent e) {
        if (!PAUSE && !introScreen.isShowIntro()) {
            bomberman.keyDownAdd(e);
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                if (!PAUSE) {
                    PAUSE = true;
                }
            }
        }
        if (!PAUSE && MODE == Mode.training) {
            if (e.getCode().equals(KeyCode.T)) {
                textBox.show();
                PAUSE = true;
            }
        } else if (MODE == Mode.training) {
            if (textBox.isShowing() && e.getCode().equals(KeyCode.ESCAPE)) {
                PAUSE = false;
                textBox.hide();
            }
        }
        if (!PAUSE && e.getCode().equals(KeyCode.B)) {
            if (FLASH > 0 && !bomberman.isDeading() && canFlash) {
                canFlash = false;
                timeLimitFlash = Time.getTime();
                AudioClip audioClip = null;
                try {
                    audioClip = new AudioClip(Bomb.class.getResource("/assets/audio/flash.wav").toURI().toString());
                    audioClip.setVolume(Setting.getEFFECTVOLUME());
                    audioClip.play();
                } catch (URISyntaxException uriSyntaxException) {
                    uriSyntaxException.printStackTrace();
                }
                FLASH --;
                bomberman.flash();
            }
        }
    }

    public static void setFlamePass() {
        if (FLAMEPASS > 0 && !Mobs.MOBS.get(0).isFlamepass()) {
            Mobs.MOBS.get(0).setFlamepass(true);
            FLAMEPASS --;
        }
    }


    public void keyUp(KeyEvent e) {
        if (!PAUSE && !introScreen.isShowIntro()) {
            bomberman.keyUpAdd(e);
        }
    }

    public boolean canEscape() {
        return !introScreen.isShowIntro() && !bomberman.isDeading();
    }

    public boolean isLose() {
        return lose;
    }

    public boolean isWin() {
        return win;
    }

    public static Mode getMODE() {
        return MODE;
    }
}
