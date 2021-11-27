package com.snow.bomberman;

import com.snow.bomberman.items.Item;
import com.snow.bomberman.music.MusicPlayer;
import com.snow.bomberman.player.Bomberman;
import com.snow.bomberman.screen.PauseScreen;
import com.snow.bomberman.setting.Setting;
import com.snow.bomberman.something.ComboKey;
import com.snow.bomberman.something.Time;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Scanner;

public class Game {
    public static double ZOOM = 1.75;
    public static Scene WINDOW;
    private static int highScoreN, highScoreA;
    public static double FPS = 0;
    private static Stage mainWindow;
    private static GraphicsContext render;
    private double width, height;
    public Image cursor;
    private static double WINDOWHEIGH;
    public static Status STATUS;
    private HomeWindow homeWindow;
    private PlayWindow map;
    private String title;
    private MusicPlayer musicPlayer;
    private ComboKey comboKey;
    public static boolean MUTEMUSIC;
    private PauseScreen pauseScreen;
    private MouseEvent mouse;
    private LinkedList<Image> icon;

    public Game() {
        STATUS = Status.home;
        width = 416 * 1.2;
        height = 416;
        MUTEMUSIC = false;
        PlayWindow.getFont();
        title = Bomberman.class.getSimpleName();
        Group group = new Group();
        Scene scene = new Scene(group, width, height);
        WINDOW = scene;
        Canvas canvas = new Canvas(width, height);
        canvas.setWidth(992);
        group.getChildren().add(canvas);
        render = canvas.getGraphicsContext2D();
        mainWindow = new Stage();
        mainWindow.setScene(scene);
        mainWindow.setResizable(false);
        mainWindow.centerOnScreen();
        mainWindow.setTitle(title);
        WINDOWHEIGH = canvas.getHeight();
        mainWindow.setHeight(WINDOWHEIGH + 37);
        mainWindow.show();
        mainWindow = mainWindow;
        doHighScore(true);
        homeWindow = new HomeWindow();
        addScale(ZOOM);
        STATUS = Status.home;
        musicPlayer = new MusicPlayer();
        addLoop();
        comboKey = new ComboKey(scene);
        addPauseButton(scene);
        addEvent(scene, mainWindow);
        render.setImageSmoothing(!Setting.GRAPHICSHIGH.isChecked());
        try {
            setCursor(canvas);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void setCursor(Canvas canvas) throws URISyntaxException {
        icon = new LinkedList<>();
        icon.add(new Image(getClass().getResource("/assets/iconapp/icon4.png").toURI().toString()));
        icon.add(new Image(getClass().getResource("/assets/iconapp/icon3.png").toURI().toString()));
        mainWindow.getIcons().add(icon.get(1));
        cursor = new Image(Game.class.getResource("/assets/cursor/normal2.png").toURI().toString());
        canvas.setCursor(Cursor.NONE);
    }

    public static void setWindowScale(double scale) {
        double lastScale = ZOOM;
        ZOOM = scale;
        addScale(lastScale);
    }

    private static void addScale(double lastScale) {
        Scale scale = new Scale();
        scale.setPivotY(0);
        scale.setPivotX(0);
        scale.setX(ZOOM);
        scale.setY(ZOOM);
        render.getCanvas().getTransforms().clear();
        render.getCanvas().getTransforms().add(scale);
        Canvas canvas = render.getCanvas();
        mainWindow.setWidth(WINDOWHEIGH * 1.2 * ZOOM);
        mainWindow.setHeight(WINDOWHEIGH * ZOOM + 37);
        mainWindow.centerOnScreen();
    }

    private void addPauseButton(Scene scene) {
        pauseScreen = new PauseScreen();
        pauseScreen.addSetting(homeWindow.getSetting());
        pauseScreen.addResumeListener(scene, () -> {
            PlayWindow.PAUSE = false;
        });
        pauseScreen.addNewGameListener(scene, () -> {
            PlayWindow.PAUSE = false;
            Mode mode = map.getMODE();
            createPlayScreen(scene, mode);
        });
        pauseScreen.addExitListener(scene, () -> {
            STATUS = Status.home;
            homeWindow.resetSettingScreen();
            if (!MUTEMUSIC) {
                HomeWindow.resumeMusic();
            }
            map.kill();
            map = null;
            render.getCanvas().setLayoutX(0);
        });
        pauseScreen.addSettingListener(scene);
    }

    private void createPlayScreen(Scene scene, Mode mode) {
        STATUS = Status.play;
        if (map != null) {
            map.muteMusic();
        }
        map = new PlayWindow(render, mode, pauseScreen);
    }

    private void addEvent(Scene scene, Stage stage) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (STATUS == Status.play){
                    map.keyDown(e);
                    /*if (e.getCode().equals(KeyCode.ESCAPE) && map.canEscape()) {

                    }*/
                }
                if (STATUS == Status.setting) {
                    if (e.getCode().equals(KeyCode.ESCAPE)) {
                        homeWindow.backHome();
                    }
                }
                if (STATUS == Status.guide) {
                    if (e.getCode().equals(KeyCode.ESCAPE)) {
                        homeWindow.backHome();
                    }
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (STATUS == Status.play){
                    map.keyUp(e);
                }
            }
        });

        scene.setOnMouseClicked(e -> {
            if (homeWindow.playClick(e) && STATUS == Status.home) {
                homeWindow.turnOffAudio();
                homeWindow.getSetting().setX(50);
                homeWindow.getSetting().setY(420);
                createPlayScreen(scene, Mode.normal);
            }
            if (homeWindow.aventureClick(e) && STATUS == Status.home) {
                homeWindow.turnOffAudio();
                homeWindow.getSetting().setX(50);
                homeWindow.getSetting().setY(420);
                createPlayScreen(scene, Mode.aventure);
            }
            if (homeWindow.trainingClick(e) && STATUS == Status.home) {
                homeWindow.turnOffAudio();
                homeWindow.getSetting().setX(50);
                homeWindow.getSetting().setY(420);
                createPlayScreen(scene, Mode.training);
            }
            if (STATUS == Status.home) {
                homeWindow.settingClick(e);
                homeWindow.guideClick(e);
            }
            musicPlayer.click(e);
        });
        scene.setOnMouseMoved(e -> {
            homeWindow.checkHoverPlay(e);
        });

        // Ctrl = + để phóng to
        comboKey.addTask(() -> {
            if (ZOOM < 2) {
                ZOOM += 0.25;
                Setting.ZOOMSLIDER.setValue(ZOOM);
                setWindowScale(ZOOM);
            }
        }, KeyCode.CONTROL, KeyCode.EQUALS);
        comboKey.addTask(() -> {
            if (ZOOM > 0.5) {
                ZOOM -= 0.25;
                Setting.ZOOMSLIDER.setValue(ZOOM);
                setWindowScale(ZOOM);
            }
        }, KeyCode.CONTROL, KeyCode.MINUS);

        //clear key when out window
        stage.focusedProperty().addListener((ov, onHidden, onShown) -> comboKey.clearCode());

        stage.setOnCloseRequest(e -> {
            Setting.saveData();
        });

        Setting.GRAPHICSHIGH.addChangeListener(() -> {
            render.setImageSmoothing(!Setting.GRAPHICSHIGH.isChecked());
        });

        scene.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            mouse = e;
        });

        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            mouse  = e;
        });

    }

    private void addLoop() {
        (new AnimationTimer() {
            private LocalDateTime start = LocalDateTime.now();
            private LocalDateTime stop = LocalDateTime.now();
            private LocalDateTime calcStart = LocalDateTime.now();
            private int frame = 0;

            private void getFps() {
                frame++;
                long time = Duration.between(calcStart, stop).toMillis();
                if (time >= 1000) {
                    FPS = frame;
                    frame = 0;
                    calcStart = LocalDateTime.now();
                }
            }

            private int timeAnimation = 0;
            private int statusAnimation = 0;

            private void stageIconAnimation(){
                mainWindow.getIcons().clear();
                timeAnimation ++;
                if (timeAnimation >= 30) {
                    timeAnimation = 0;
                    statusAnimation ++;
                    if (statusAnimation >= 2) {
                        statusAnimation = 0;
                    }
                }
                mainWindow.getIcons().add(icon.get(statusAnimation));
            }

            @Override
            public void handle(long l) {
                stop = LocalDateTime.now();
                long delay = Duration.between(start, stop).toMillis();
                if (delay >= 14) {
                    getFps();
                    start = LocalDateTime.now();
                    update();
                }
                if (mouse != null) {
                    double cameraX = - render.getCanvas().getLayoutX() / Game.ZOOM;
                    render.setImageSmoothing(true);
                    render.drawImage(cursor,cameraX + mouse.getX() / ZOOM, mouse.getY() / ZOOM, 25 / ZOOM, 40 / ZOOM);
                    render.setImageSmoothing(!Setting.GRAPHICSHIGH.isChecked());
                }
            }
        }).start();
    }

    private void update() {
        Time.clock();
        renderGame();
        comboKey.run();
        if (STATUS == Status.play){
            if (map.isWin()) {
                if (PlayWindow.getMODE() == Mode.aventure) {
                    Item.doItemAventure(false);
                    if(PlayWindow.SCORE > highScoreA) {
                        highScoreA = PlayWindow.SCORE;
                        doHighScore(false);
                    }
                }
                if (PlayWindow.getMODE() == Mode.normal) {
                    if(PlayWindow.SCORE > highScoreN) {
                        highScoreN = PlayWindow.SCORE;
                        doHighScore(false);
                    }
                }
                STATUS = Status.home;
                homeWindow.resetSettingScreen();
                if (!MUTEMUSIC) {
                    HomeWindow.resumeMusic();
                }
                map.kill();
                map = null;
                render.getCanvas().setLayoutX(0);
            }
            else if (map.isLose()) {
                if (PlayWindow.getMODE() == Mode.aventure) {
                    Item.doItemAventure(false);
                    if(PlayWindow.SCORE > highScoreA) {
                        highScoreA = PlayWindow.SCORE;
                        doHighScore(false);
                    }
                }
                if (PlayWindow.getMODE() == Mode.normal) {
                    if(PlayWindow.SCORE > highScoreN) {
                        highScoreN = PlayWindow.SCORE;
                        doHighScore(false);
                    }
                }
                Mode mode = map.getMODE();
                createPlayScreen(WINDOW, mode);
            }
        }
    }

    private void renderGame() {
        if (Game.STATUS != Status.play) {
            render.clearRect(0, 0, 900, 600);
            homeWindow.draw(render);
        } else {
            map.draw();
        }
        musicPlayer.draw(render);
        if (STATUS == Status.play) {
            map.drawIntro();
        }
    }

    public static void doHighScore(boolean read) {
        try {
            File file = new File(Paths.get("data/highscore.txt").toUri());
            if (read) {
                Scanner scanner = new Scanner(file);
                highScoreN = scanner.nextInt();
                highScoreA = scanner.nextInt();
                scanner.close();
            } else {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(highScoreN + " " + highScoreA);
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getHighScoreNormal() {
        return highScoreN;
    }

    public static int getHighScoreAventure() {
        return highScoreA;
    }

}

