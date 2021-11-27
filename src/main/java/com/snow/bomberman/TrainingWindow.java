package com.snow.bomberman;

import com.snow.bomberman.block.Block;
import com.snow.bomberman.map.TraningMap;
import com.snow.bomberman.player.Bomberman;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import java.net.URISyntaxException;
import java.util.LinkedList;

public class TrainingWindow {
    int[][] mapHash;
    TraningMap map;
    protected LinkedList<Image> mapImg;
    private Bomberman player;
    private Block[][] tileMap;

    public TrainingWindow() {
        map = new TraningMap();
        mapHash = map.getMaphash();
        mapImg = new LinkedList<>();
        this.tileMap = new Block[mapHash.length][mapHash[0].length];

        for(int i = 0; i < this.tileMap.length; ++i) {
            for(int j = 0; j < this.tileMap[i].length; ++j) {
                this.tileMap[i][j] = new Block((double)(j * 32), (double)(i * 32), 32.0D, 32.0D);
            }
        }
        player = new Bomberman(32, 32);
        player.setMap(tileMap, mapHash, map.getPortal());
        addEvent(Game.WINDOW);
        try {
            this.mapImg.add(new Image(this.getClass().getResource("/assets/map/wall1.png").toURI().toString()));
            this.mapImg.add(new Image(this.getClass().getResource("/assets/map/wall2.png").toURI().toString()));
            this.mapImg.add(new Image(this.getClass().getResource("/assets/map/wall3.png").toURI().toString()));
            this.mapImg.add(new Image(this.getClass().getResource("/assets/map/port.png").toURI().toString()));
        } catch (URISyntaxException var5) {
            var5.printStackTrace();
        }
    }

    public void addEvent(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            player.keyDownAdd(e);
            player.keyUpAdd(e);
        });
    }

    public void draw(GraphicsContext render) {
        for (int i = 0; i < mapHash.length; i++) {
            for (int j = 0; j < mapHash[i].length; j++) {
                render.drawImage(this.mapImg.get(1), (double)(32 * j), (double)(32 * i));
                if (mapHash[i][j] == 1) {
                    render.drawImage((Image)this.mapImg.get(0), (double)(32 * j), (double)(32 * i));
                }
                if (mapHash[i][j] == 10) {
                    render.drawImage((Image)this.mapImg.get(3), (double)(32 * j), (double)(32 * i));
                }
                if (mapHash[i][j] == 3) {
                    render.drawImage((Image)this.mapImg.get(2), (double)(32 * j), (double)(32 * i));
                }
            }
        }
        player.draw(render);
    }

}
