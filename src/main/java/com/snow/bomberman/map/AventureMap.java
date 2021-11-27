package com.snow.bomberman.map;

import com.snow.bomberman.items.Item;
import com.snow.bomberman.mob.enemy.*;
import com.snow.bomberman.setting.Setting;
import com.snow.bomberman.something.ListEnemy;
import com.snow.bomberman.something.Point;

import java.util.LinkedList;
import java.util.Random;

public class AventureMap extends ListMap {
    private int[][] basemap = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 1, 3, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 1, 3, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1},
            {1, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 3, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    private int[][] tilemap;
    private Item item;
    private LinkedList<ListEnemy> listEnemy;
    private Point portal;

    public AventureMap() {
        listEnemy = new LinkedList<>();
    }

    private void resetTile() {
        tilemap = new int[basemap.length][basemap[0].length];
        for (int i = 0; i < basemap.length; i++) {
            for (int j = 0; j < basemap[i].length; j++) {
                tilemap[i][j] = basemap[i][j];
            }
        }
    }

    public Point getPortal(int level) {
        return portal;
    }

    @Override
    public void newMap(int level) {
        listEnemy.clear();
        resetTile();
        int limit = 6 + level % 10;
        Random randomM = new Random();
        for (int i = 0; i < tilemap.length; i++) {
            for (int j = 0; j < tilemap[i].length; j++) {
                if (j > 4  || i > 4) {
                    if (tilemap[i][j] == 2) {
                        int num = randomM.nextInt(10);
                        tilemap[i][j] = (num > 1) ? 2 : 3;
                    }
                }
            }
        }
        LinkedList<Point> check = new LinkedList<>();
        for (int i = 0; i < tilemap.length; i++) {
            for (int j = 0; j < tilemap[i].length; j++) {
                if (j > 10 || i > 5) {
                    if (tilemap[i][j] == 2) {
                        if (tilemap[i - 1][j] == 2
                                || tilemap[i + 1][j] == 2
                                || tilemap[i][j - 1] == 2
                                || tilemap[i][j + 1] == 2) {
                            check.add(new Point(i, j));
                        }
                    }
                }
            }
        }
        Random randomP = new Random();
        for (int i = 0; i < limit; i++) {
            int ran = randomP.nextInt(check.size());
            int enemy;
            if (level / 10 == 0) {
                enemy = 11;
            } else {
                enemy = (11 + (level / 10) * 2);
                if (enemy == 15) {
                    enemy = 16;
                }
            }
            listEnemy.add(new ListEnemy(enemy, check.get(ran).getX(), check.get(ran).getY()));
        }
        if (Setting.isHalloween()) {
            for (int i = 0; i < 3; i++) {
                int ran = randomP.nextInt(check.size());
                int enemy = 17;
                listEnemy.add(new ListEnemy(enemy, check.get(ran).getX(), check.get(ran).getY()));
            }
        }
        randomP = new Random();
        if (level > 20) {
            item = new Item(Item.Effect.setValue(randomP.nextInt(8) + 1), 0, 0);
        } else {
            item = new Item(Item.Effect.setValue(randomP.nextInt(7) + 1), 0, 0);
        }
        /*for (int i = 0; i < tilemap.length; i++) {
            for (int j = 0; j < tilemap[i].length; j++) {
                System.out.print(tilemap[i][j] + ",");
            }
            System.out.println();
        }*/
    }

    @Override
    public int[][] createMap(int level) {
        return tilemap;
    }

    @Override
    public Item getItem(int level) {
        return item;
    }

    @Override
    public int size() {
        return 29;
    }

    @Override
    public void createEnemy(int level, int[][] mapHash) {
        LinkedList<ListEnemy> num = listEnemy;
        for (int i = 0; i < num.size(); i++) {
            ListEnemy enemy = num.get(i);
            if (enemy.getValue() == 11) {
                Balloom balloom = new Balloom(enemy.getX(), enemy.getY());
                balloom.setMap(mapHash);
            } else if (enemy.getValue() == 12) {
                Oneal oneal = new Oneal(enemy.getX(), enemy.getY());
                oneal.setMap(mapHash);
            } else if (enemy.getValue() == 13) {
                Doll doll = new Doll(enemy.getX(), enemy.getY());
                doll.setMap(mapHash);
            } else if (enemy.getValue() == 14) {
                Minvo minvo = new Minvo(enemy.getX(), enemy.getY());
                minvo.setMap(mapHash);
            } else if (enemy.getValue() == 15) {
                Kondoria kondoria = new Kondoria(enemy.getX(), enemy.getY());
                kondoria.setMap(mapHash);
            } else if (enemy.getValue() == 16) {
                Ovapi ovapi = new Ovapi(enemy.getX(), enemy.getY());
                ovapi.setMap(mapHash);
            } else if (enemy.getValue() == 17 && Setting.isHalloween()){
                Pass pass = new Pass(enemy.getX(), enemy.getY());
                pass.setMap(mapHash);
            }
        }
    }
}
