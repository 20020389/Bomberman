package com.snow.bomberman.map;

import com.snow.bomberman.items.Item;
import com.snow.bomberman.mob.enemy.*;
import com.snow.bomberman.setting.Setting;
import com.snow.bomberman.something.ListEnemy;
import com.snow.bomberman.something.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Scanner;

public class ListMap {
    private LinkedList<int[][]> listMap;
    private LinkedList<Item> listItem;
    LinkedList<LinkedList<ListEnemy>> listEnemy;
    private LinkedList<Point> listPortal;
    private boolean training;

    public ListMap() {
        listMap = new LinkedList<>();
        listEnemy = new LinkedList<>();
        listItem = new LinkedList<>();
        listPortal = new LinkedList<>();
        listMap.add(loadMap(1));
        listMap.add(loadMap(2));
        listMap.add(loadMap(3));
        listMap.add(loadMap(4));
        listMap.add(loadMap(5));
        listMap.add(loadMap(6));
        listMap.add(loadMap(7));
        listMap.add(loadMap(8));
        listMap.add(loadMap(9));
        listMap.add(loadMap(10));
        training = false;
    }

    public ListMap(boolean training) {
        this.training = training;
        listMap = new LinkedList<>();
        listEnemy = new LinkedList<>();
        listItem = new LinkedList<>();
        listPortal = new LinkedList<>();
        listMap.add(loadMap(1));
    }

    public int[][] createMap(int level) {
        return listMap.get(level);
    }

    public Item getItem(int level) {
        return listItem.get(level);
    }

    public Item getItem1() {
        return listItem.get(1);
    }

    public Item getItem2() {
        return listItem.get(1);
    }

    public int size() {
        return listMap.size();
    }

    public void createEnemy(int level, int[][] mapHash) {
        LinkedList<ListEnemy> num = listEnemy.get(level);
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

    public void newMap(int level) {

    }

    public Point getPortal(int level) {
        return listPortal.get(level);
    }

    public int[][] loadMap(int level) {
        int[][] tileMap = new int[13][31];
        try {
            File file = null;
            if (!training){
                file = new File(getClass().getResource("/tilemap/map" + level + ".map").toURI());
            } else {
                file = new File(getClass().getResource("/tilemap/maptraining.map").toURI());
            }
            Scanner scanner = new Scanner(file);
            LinkedList<ListEnemy> list = new LinkedList<>();
            for (int i = 0; i < 13; i++) {
                String[] tile = (scanner.nextLine()).split(",");
                for (int j = 0; j < 31; j++) {
                    int hash = Integer.parseInt(tile[j]);
                    if (!(hash > 0 && hash < 4)) {
                        if (hash == 9) {
                            listPortal.add(new Point(j, i));
                            hash = 3;
                        } else {
                            list.add(new ListEnemy(hash, i, j));
                            hash = 2;
                        }
                    }
                    tileMap[i][j] = hash;
                }
            }
            listEnemy.add(list);
            String[] value = scanner.nextLine().split(" ");
            listItem.add(new Item(Item.Effect.setValue(Integer.parseInt(value[0])),
                    Double.parseDouble(value[1]) * 32.0,
                    Double.parseDouble(value[2]) * 32.0));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tileMap;
    }

}
