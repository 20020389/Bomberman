package com.snow.bomberman.map;

import com.snow.bomberman.something.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class TraningMap {
    private int[][] maphash;
    private Point portal;

    public TraningMap() {
        maphash = loadMap();
    }

    public int[][] getMaphash() {
        int [][]cpy = new int[maphash.length][maphash[0].length];
        for (int i = 0; i < maphash.length; i++) {
            System.arraycopy(maphash[i], 0, cpy[i], 0, maphash[i].length);
        }
        return cpy;
    }

    public Point getPortal() {
        return portal;
    }

    public int[][] loadMap() {
        int[][] tileMap = new int[13][31];
        try {
            File file = new File(getClass().getResource("/tilemap/maptraining.txt").toURI());
            Scanner scanner = new Scanner(file);
            for (int i = 0; i < 13; i++) {
                String[] tile = (scanner.nextLine()).split(",");
                for (int j = 0; j < 31; j++) {
                    int hash = Integer.parseInt(tile[j]);
                    tileMap[i][j] = hash;
                    if (hash == 10) {
                        portal = new Point(j, i);
                    }
                }
            }
        } catch (URISyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return tileMap;
    }
}
