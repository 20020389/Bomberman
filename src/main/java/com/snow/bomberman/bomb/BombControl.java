package com.snow.bomberman.bomb;

import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.block.Block;
import com.snow.bomberman.mob.Mob;
import com.snow.bomberman.mob.Mobs;
import com.snow.bomberman.player.Bomberman;
import com.snow.bomberman.something.Point;
import javafx.scene.canvas.GraphicsContext;

import java.util.Map;

public class BombControl extends Bomb{
    public BombControl(Point p, int[][] mapHash, Bomberman bomberman) {
        super(p, mapHash, bomberman);
    }

    @Override
    public void draw(GraphicsContext render) {
        objBlock.update(x + 1, y + 1, 30, 30);
        int j = (int)x / 32;
        int i = (int)y / 32;
        if (timing) {
            render.drawImage(BOMBIMG.get(27 + statusAmination), x, y);
            if (playerLeave()) {
                mapHash[i][j] = 5;
            } else {
                mapHash[i][j] = 4;
            }
            timeAnimation++;
            if (timeAnimation % 20 == 0) {
                statusAmination++;
            }
            if (timeAnimation == 160) {
                timeAnimation = 0;
            }
            if (statusAmination >= 3) {
                statusAmination = 0;
            }
        } else if (!boom){
            render.drawImage(BOMBIMG.get(3 + statusAmination), x, y);
            Block check = new Block(x , y, 32, 32);
            for (Mob k : Mobs.MOBS) {
                if (!k.isDeading() && k.getObjBlock().collision(check)) {
                    if (k instanceof  Bomberman) {
                        PlayWindow.setFlamePass();
                    }
                    if (!k.isFlamepass()) {
                        k.setSurvival(false);
                    }
                }
            }
            drawR(render);
            drawL(render);
            drawT(render);
            drawB(render);
            timeAnimation++;
            if (statusAmination == 2 && timeAnimation % 30 == 0) {
                statusAmination--;
            }
            if (statusAmination == 1 && timeAnimation % 10 == 0) {
                statusAmination--;
            }
            if (timeAnimation == 40) {
                boom = true;
                timeAnimation = 0;
                mapHash[i][j] = 2;
                if (wallDestroy.size() > 0) {
                    for (Map.Entry<Integer, Point> map : wallDestroy.entrySet()) {
                        Point p = map.getValue();
                        mapHash[(int)p.getX()][(int)p.getY()] = 2;
                    }
                }
            }
        }
    }
}
