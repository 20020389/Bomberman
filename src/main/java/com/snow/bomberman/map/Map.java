//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.snow.bomberman.map;

import com.snow.bomberman.Mode;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.block.Block;
import com.snow.bomberman.items.Item;
import com.snow.bomberman.something.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URISyntaxException;
import java.util.LinkedList;

public class Map {
    protected LinkedList<Image> map;

    protected int[][] mapHash;

    protected Block[][] tileMap;
    protected LinkedList<Point> listOne;
    protected Item item;
    protected Point itemLocation;
    private Point portal;

    public Map(int[][] mapHash, Item item, Point portal) {
        this.item = item;
        this.portal = portal;
        this.cpyHash(mapHash);
        this.map = new LinkedList();
        this.tileMap = new Block[mapHash.length][mapHash[0].length];

        for(int i = 0; i < this.tileMap.length; ++i) {
            for(int j = 0; j < this.tileMap[i].length; ++j) {
                this.tileMap[i][j] = new Block((double)(j * 32), (double)(i * 32), 32.0D, 32.0D);
            }
        }

        try {
            this.map.add(new Image(this.getClass().getResource("/assets/map/wall1.png").toURI().toString()));
            this.map.add(new Image(this.getClass().getResource("/assets/map/wall2.png").toURI().toString()));
            this.map.add(new Image(this.getClass().getResource("/assets/map/wall3.png").toURI().toString()));
            this.map.add(new Image(this.getClass().getResource("/assets/map/port.png").toURI().toString()));
        } catch (URISyntaxException var5) {
            var5.printStackTrace();
        }

    }

    protected void cpyHash(int[][] mapHash) {
        this.mapHash = new int[mapHash.length][mapHash[0].length];
        listOne = new LinkedList<>();
        int j;
        for(int i = 0; i < mapHash.length; ++i) {
            for(j = 0; j < mapHash[i].length; ++j) {
                this.mapHash[i][j] = mapHash[i][j];
                if (mapHash[i][j] == 3) {
                    listOne.add(new Point(j, i));
                }
            }
        }
        if (PlayWindow.getMODE() == Mode.aventure) {
            itemLocation = listOne.get(1);
            item.setLocation(itemLocation.getX() * 32, itemLocation.getY() * 32);
            PlayWindow.PORTAL = listOne.get(2);
            return;
        }
        PlayWindow.PORTAL = portal;
        this.itemLocation = new Point(item.getX() / 32, item.getY() / 32);
    }

    public void draw(GraphicsContext render) {
        for(int i = 0; i < this.mapHash.length; ++i) {
            for(int j = 0; j < this.mapHash[i].length; ++j) {
                render.drawImage(this.map.get(1), (32 * j), (32 * i));
                if (this.item.isSurvival() && this.itemLocation.equals(j, i)) {
                    this.item.draw(render, this.mapHash[i][j] == 2);
                }

                if (PlayWindow.PORTAL.equals(j, i)) {
                    render.drawImage(this.map.get(3), PlayWindow.PORTAL.getX() * 32.0D, PlayWindow.PORTAL.getY() * 32.0D);
                }

                if (this.mapHash[i][j] == 3) {
                    render.drawImage(this.map.get(this.mapHash[i][j] - 1), (32 * j), (32 * i));
                } else if (this.mapHash[i][j] == 1) {
                    render.drawImage(this.map.get(this.mapHash[i][j] - 1), (32 * j), (32 * i));
                }
            }
        }

    }

    public Block[][] getTile() {
        return this.tileMap;
    }

    public int[][] getMapHash() {
        return this.mapHash;
    }

    public int getWidth() {
        return this.mapHash[0].length * 32;
    }

    public int getHeigh() {
        return this.mapHash.length * 32;
    }
}
