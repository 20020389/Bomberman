package com.snow;

import com.snow.bomberman.Game;
import com.snow.bomberman.bomb.Bomb;
import com.snow.bomberman.items.Item;
import com.snow.bomberman.mob.enemy.*;
import com.snow.bomberman.music.MusicPlayer;
import com.snow.bomberman.player.Bomberman;
import com.snow.bomberman.screen.IntroGame;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class Main extends Application {
    //region variable
    private GraphicsContext render;
    private double width;
    private double height;
    private IntroGame introGame;
    private AnimationTimer loop;
    private Stage mainStage;
    private Game game;
    private boolean initdone;
//endregion
      //region 🡇
    //endregion
    //region function
    @Override
    public void start(Stage stage) {
        width = 416 * 1.2 + 100;
        height = 416;
        Group group = new Group();
        Canvas canvas = new Canvas(width, height);
        group.getChildren().add(canvas);
        Scene scene = new Scene(group, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        render = canvas.getGraphicsContext2D();
        introGame = new IntroGame();
        addLoop();
        mainStage = stage;
        stage.setTitle("Chờ tý game còn phải load :>");
        Scale scale = new Scale();
        scale.setPivotY(0);
        scale.setPivotX(0);
        scale.setX(1.5);
        scale.setY(1.5);
        canvas.getTransforms().clear();
        canvas.getTransforms().add(scale);
        stage.setWidth(width * 1.5);
        stage.setHeight(height * 1.5 + 20);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
        game = null;
        initdone = false;
        (new Thread(() -> {
            Bomb.init();
            Item.init();
            Balloom.init();
            Doll.init();
            Kondoria.init();
            Minvo.init();
            Oneal.init();
            Ovapi.init();
            Pass.init();
            MusicPlayer.init();
            Bomberman.init();
            initdone = true;
        })).start();
    }

    void addLoop() {
        loop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                render.clearRect(0, 0, width, height);
                introGame.draw(render);
//                introGame.setDone(true);
                if (introGame.isDone() && initdone) {
                    mainStage.close();
                    loop.stop();
                    if (game == null) {
                        game = new Game();
                    }
                }
            }
        };
        loop.start();
    }
    //endregion
      //region 🡇
    //endregion
     //region main
    public static void main(String[] args) {
        launch(args);
    }
    //endregion
}
