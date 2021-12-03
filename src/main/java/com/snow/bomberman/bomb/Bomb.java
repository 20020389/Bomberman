package com.snow.bomberman.bomb;

import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.block.Block;
import com.snow.bomberman.mob.Mob;
import com.snow.bomberman.mob.Mobs;
import com.snow.bomberman.mob.enemy.Kondoria;
import com.snow.bomberman.mob.enemy.Ovapi;
import com.snow.bomberman.player.Bomberman;
import com.snow.bomberman.setting.Setting;
import com.snow.bomberman.something.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.snow.bomberman.PlayWindow.PAUSE;

public class Bomb extends Mob{
    public static LinkedList<Image> BOMBIMG;
    public static int BOOMLEGHT;
    public static boolean INIT = false;
    public static int TIMELIMIT = 160;
    protected int statusAmination;
    protected int timeAnimation;
    protected int [][]mapHash;
    protected boolean boom;
    protected boolean timing;
    private int animationDirection;
    private int left;
    private int right;
    private int top;
    private int bot;
    private boolean stopL;
    private boolean stopR;
    private boolean stopT;
    private boolean stopB;
    private boolean destroyL;
    private boolean destroyR;
    private boolean destroyT;
    private boolean destroyB;
    private boolean checkBomb;
    protected Bomberman bomberman;
    protected HashMap<Integer, Point> wallDestroy;
    private int boomLeght;

    public static void init() {
        if (!INIT) {
            BOMBIMG = new LinkedList<>();
            try {
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/bom1.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/bom2.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/bom3.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom1.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom2.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom3.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom4.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom5.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom6.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom7.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom8.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom9.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom10.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom11.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom12.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom13.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom14.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom15.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom16.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom17.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom18.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom19.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom20.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom21.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom22.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom23.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/boom24.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/bom4.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/bom5.png").toURI().toString()));
                BOMBIMG.add(new Image(Bomb.class.getResource("/assets/bom/bom6.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            INIT = true;
        }

    }

    public Bomb(Point p, int[][] mapHash, Bomberman bomberman) {
        this.bomberman = bomberman;
        this.x = ((int) (p.getX() / 32)) * 32;
        this.y = ((int) (p.getY() / 32)) * 32;
        timeAnimation = 0;
        timing = true;
        statusAmination = 0;
        boom = false;
        left = 0;
        top = 0;
        right = 0;
        bot = 0;
        stopR = false;
        stopB = false;
        stopT = false;
        stopL = false;
        this.mapHash = mapHash;
        init();
        objBlock = new Block(x + 1, y + 1, 30, 30);
        checkBomb = false;
        wallDestroy = new HashMap<>();
        boomLeght = BOOMLEGHT;
        AudioClip audioClip = null;
        try {
            audioClip = new AudioClip(Bomb.class.getResource("/assets/audio/putbomb.mp3").toURI().toString());
            audioClip.setVolume(Setting.getEFFECTVOLUME());
            audioClip.play();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Bomb(Point p, int[][] mapHash, Bomberman bomberman, int boomLeght, int timeAnimation) {
        this.bomberman = bomberman;
        this.x = ((int) (p.getX() / 32)) * 32;
        this.y = ((int) (p.getY() / 32)) * 32;
        this.timeAnimation = timeAnimation;
        timing = true;
        statusAmination = 0;
        boom = false;
        left = 0;
        top = 0;
        right = 0;
        bot = 0;
        stopR = false;
        stopB = false;
        stopT = false;
        stopL = false;
        this.mapHash = mapHash;
        if (!INIT) {
            init();
        }
        objBlock = new Block(x + 1, y + 1, 30, 30);
        checkBomb = false;
        wallDestroy = new HashMap<>();
        this.boomLeght = boomLeght;
        AudioClip audioClip = null;
    }

    @Override
    public void draw(GraphicsContext render) {
        objBlock.update(x + 1, y + 1, 30, 30);
        int j = (int)x / 32;
        int i = (int)y / 32;
        if (timing) {
            render.drawImage(BOMBIMG.get(statusAmination), x, y);
            if (PAUSE) {
                return;
            }
            if (playerLeave()) {
                mapHash[i][j] = 5;
            } else {
                mapHash[i][j] = 4;
            }
            timeAnimation++;
            if (timeAnimation % 20 == 0) {
                statusAmination++;
            }
            if (timeAnimation == TIMELIMIT) {
                AudioClip audioClip = null;
                try {
                    audioClip = new AudioClip(Bomb.class.getResource("/assets/audio/boom.wav").toURI().toString());
                    audioClip.setVolume(Setting.getEFFECTVOLUME());
                    audioClip.play();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                timing = false;
                timeAnimation = 0;
                statusAmination = 2;
                getBombLocation(mapHash[0].length * 32);
            }
            if (statusAmination >= 3) {
                statusAmination = 0;
            }
        } else if (!boom){
            render.drawImage(BOMBIMG.get(3 + statusAmination), x, y);
            if (PAUSE) {
                return;
            }
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
            if (statusAmination == 2 && timeAnimation % 20 == 0) {
                statusAmination--;
            }
            if (statusAmination == 1 && timeAnimation % 10 == 0) {
                statusAmination--;
            }
            if (timeAnimation == 30) {
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

    protected void drawR(GraphicsContext render) {
        Block check = new Block( 0, 0, 0, 0);
        for (int i = 0; i < right; i++) {
            int j = (int)(x / 32) + (i + 1);
            int i1 = (int)y / 32;
            if (i < right - 1) {
                render.drawImage(BOMBIMG.get(18 + statusAmination), x + (i + 1) * 32, y);
            }
            else {
                if (!destroyR) {
                    render.drawImage(BOMBIMG.get(21 + statusAmination), x + (i + 1) * 32, y);
                } else {
                    render.drawImage(BOMBIMG.get(26 - statusAmination), x + (i + 1) * 32, y);
                    if (PlayWindow.PORTAL.equals(j, i1)) {
                        mapHash[i1][j] = 10;
                    } else {
                        mapHash[i1][j] = 6;
                        Point boomwall = new Point(i1, j);
                        wallDestroy.put(boomwall.hashCode(), boomwall);
                    }
                }
            }
            check.update(x + (i + 1) * 32, y, 32, 32);
            for (Mob k : Mobs.MOBS) {
                if (i < right - 1 || (i == right - 1 && !destroyR)) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        if (k instanceof Bomberman) {
                            PlayWindow.setFlamePass();
                        }
                        if ( !k.isFlamepass()) {
                            k.setSurvival(false);
                        }
                    }
                }
                if (k instanceof Kondoria) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        k.setSurvival(false);
                    }
                }
                if (k instanceof Ovapi) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        k.setSurvival(false);
                    }
                }
                if (k instanceof Bomberman && Bomberman.canWallPass()) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        PlayWindow.setFlamePass();
                        if ( !k.isFlamepass()) {
                            k.setSurvival(false);
                        }
                    }
                }
            }
            if (i < right - 1 || (i == right - 1 && !destroyR)) {
                if ((mapHash[i1][j] == 4 || mapHash[i1][j] == 5) && !checkBomb) {
                    for (Bomb k : Bomberman.LISTBOMB) {
                        if (this != k && k.timing) {
                            if (inBombWay(check, k)) {
                                k.setBoom(true);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void drawL(GraphicsContext render) {
        Block check = new Block( 0, 0, 0, 0);
        for (int i = 0; i < left; i++) {
            int j = (int)(x / 32) - (i + 1);
            int i1 = (int)y / 32;
            if (i < left - 1) {
                render.drawImage(BOMBIMG.get(18 + statusAmination), x - (i + 1) * 32, y);
            }
            else {
                if (!destroyL){
                    render.drawImage(BOMBIMG.get(15 + statusAmination), x - (i + 1) * 32, y);
                } else {
                    render.drawImage(BOMBIMG.get(26 - statusAmination), x - (i + 1) * 32, y);
                    if (PlayWindow.PORTAL.equals(j, i1)) {
                        mapHash[i1][j] = 10;
                    } else {
                        mapHash[i1][j] = 6;
                        Point boomwall = new Point(i1, j);
                        wallDestroy.put(boomwall.hashCode(), boomwall);
                    }
                }
            }
            check.update(x - (i + 1) * 32, y, 32, 32);
            for (Mob k : Mobs.MOBS) {
                if (i < left - 1 ||(i == left - 1 && !destroyL)) {
                    if (!k.isDeading() && k.getObjBlock().collision(check) && !k.isFlamepass()) {
                        if (!k.isDeading() && k.getObjBlock().collision(check)) {
                            if (k instanceof Bomberman) {
                                PlayWindow.setFlamePass();
                            }
                            if ( !k.isFlamepass()) {
                                k.setSurvival(false);
                            }
                        }
                    }
                }
                if (k instanceof Kondoria) {
                    if (k.getObjBlock().collision(check)) {
                        k.setSurvival(false);
                    }
                }
                if (k instanceof Ovapi) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        k.setSurvival(false);
                    }
                }
                if (k instanceof Bomberman && Bomberman.canWallPass()) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        PlayWindow.setFlamePass();
                        if ( !k.isFlamepass()) {
                            k.setSurvival(false);
                        }
                    }
                }
            }
            if (i < left - 1 ||(i == left - 1 && !destroyL)){
                if ((mapHash[i1][j] == 4 || mapHash[i1][j] == 5) && !checkBomb) {
                    for (Bomb k : Bomberman.LISTBOMB) {
                        if (this != k && k.timing) {
                            if (inBombWay(check, k)) {
                                k.setBoom(true);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void drawT(GraphicsContext render) {
        Block check = new Block( 0, 0, 0, 0);
        for (int i = 0; i < top; i++) {
            int j = (int)(x / 32);
            int i1 = (int)y / 32 - (i + 1);
            if (i < top- 1) {
                render.drawImage(BOMBIMG.get(9 + statusAmination), x , y - (i + 1) * 32);
            }
            else {
                if (!destroyT){
                    render.drawImage(BOMBIMG.get(6 + statusAmination), x, y - (i + 1) * 32);
                } else {
                    render.drawImage(BOMBIMG.get(26 - statusAmination), x, y - (i + 1) * 32);
                    if (PlayWindow.PORTAL.equals(j, i1)) {
                        mapHash[i1][j] = 10;
                    } else {
                        mapHash[i1][j] = 6;
                        Point boomwall = new Point(i1, j);
                        wallDestroy.put(boomwall.hashCode(), boomwall);
                    }
                }
            }
            check.update(x, y - (i + 1) * 32, 32, 32);
            for (Mob k : Mobs.MOBS) {
                if (i < top - 1 ||(i == top - 1 && !destroyT)) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        if (k instanceof Bomberman) {
                            PlayWindow.setFlamePass();
                        }
                        if ( !k.isFlamepass()) {
                            k.setSurvival(false);
                        }
                    }
                }
                if (k instanceof Kondoria) {
                    if (k.getObjBlock().collision(check)) {
                        k.setSurvival(false);
                    }
                }
                if (k instanceof Ovapi) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        k.setSurvival(false);
                    }
                }
                if (k instanceof Bomberman && Bomberman.canWallPass()) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        PlayWindow.setFlamePass();
                        if ( !k.isFlamepass()) {
                            k.setSurvival(false);
                        }
                    }
                }
            }
            if (i < top - 1 ||(i == top - 1 && !destroyT)){
                if ((mapHash[i1][j] == 4 || mapHash[i1][j] == 5) && !checkBomb) {
                    for (Bomb k : Bomberman.LISTBOMB) {
                        if (this != k && k.timing) {
                            if (inBombWay(check, k)) {
                                k.setBoom(true);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void drawB(GraphicsContext render) {
        Block check = new Block( 0, 0, 0, 0);
        for (int i = 0; i < bot; i++) {
            int j = (int)(x / 32);
            int i1 = (int)y / 32 + (i + 1);
            if (i < bot - 1) {
                render.drawImage(BOMBIMG.get(9 + statusAmination), x , y + (i + 1) * 32);
            }
            else {
                if (!destroyB){
                    render.drawImage(BOMBIMG.get(12 + statusAmination), x, y + (i + 1) * 32);
                } else {
                    render.drawImage(BOMBIMG.get(26 - statusAmination), x, y + (i + 1) * 32);
                    if (PlayWindow.PORTAL.equals(j, i1)) {
                        mapHash[i1][j] = 10;
                    } else {
                        mapHash[i1][j] = 6;
                        Point boomwall = new Point(i1, j);
                        wallDestroy.put(boomwall.hashCode(), boomwall);
                    }
                }
            }
            check.update(x, y + (i + 1) * 32, 32, 32);
            for (Mob k : Mobs.MOBS) {
                if (!k.isDeading() && k.getObjBlock().collision(check)) {
                    if (k instanceof Bomberman) {
                        PlayWindow.setFlamePass();
                    }
                    if ( !k.isFlamepass()) {
                        k.setSurvival(false);
                    }
                }
                if (k instanceof Kondoria) {
                    if (k.getObjBlock().collision(check)) {
                        k.setSurvival(false);
                    }
                }
                if (k instanceof Ovapi) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        k.setSurvival(false);
                    }
                }
                if (k instanceof Bomberman && Bomberman.canWallPass()) {
                    if (!k.isDeading() && k.getObjBlock().collision(check)) {
                        PlayWindow.setFlamePass();
                        if ( !k.isFlamepass()) {
                            k.setSurvival(false);
                        }
                    }
                }
            }
            if (i < bot - 1 || (i == bot - 1 && !destroyB)){
                if ((mapHash[i1][j] == 4 || mapHash[i1][j] == 5) && !checkBomb) {
                    for (Bomb k : Bomberman.LISTBOMB) {
                        if (this != k && k.timing) {
                            if (inBombWay(check, k)) {
                                k.setBoom(true);
                            }
                        }
                    }
                }
            }
        }
        checkBomb = true;
    }

    private boolean checkMapHash(int mapHash) {
        return mapHash == 2 || mapHash == 4 || mapHash == 5 || mapHash == 9;
    }

    protected void getBombLocation(int mapleght) {
        stopR = false;
        stopB = false;
        stopT = false;
        stopL = false;
        destroyR = false;
        destroyL = false;
        destroyT = false;
        destroyB = false;
        for (int i = 1; i <= boomLeght; i++) {
            if (x + 32 * i < mapleght && !stopR) {
                int j = (int) ((x + 32 * i) / 32);
                int i1 = (int) (y / 32);
                if (checkMapHash(mapHash[i1][j])) {
                    right ++;
                } else if (mapHash[i1][j] == 3) {
                    right ++;
                    stopR = true;
                    destroyR = true;
                } else {
                    stopR = true;
                }
            }
            if (x - 32 * i >= 0 && !stopL) {
                    int j = (int) ((x - 32 * i) / 32);
                    int i1 = (int) (y / 32);
                    if (checkMapHash(mapHash[i1][j])) {
                        left ++;
                    } else if (mapHash[i1][j] == 3) {
                        left ++;
                        stopL = true;
                        destroyL = true;
                    } else {
                        stopL = true;
                    }
            }

            if (y - 32 * i >= 0 && !stopT) {
                int j = (int) (x / 32);
                int i1 = (int) ((y - 32 * i) / 32);
                if (checkMapHash(mapHash[i1][j])) {
                    top ++;
                } else if (mapHash[i1][j] == 3) {
                    top ++;
                    stopT = true;
                    destroyT = true;
                } else {
                    stopT = true;
                }
            }

            if (y + 32 * i >= 0 && !stopB) {
                    int j = (int) (x / 32);
                    int i1 = (int) ((y + 32 * i) / 32);
                    if (checkMapHash(mapHash[i1][j])) {
                        bot ++;
                    } else if (mapHash[i1][j] == 3) {
                        bot ++;
                        stopB = true;
                        destroyB = true;
                    } else {
                        stopB = true;
                    }
            }
        }
    }

    protected boolean playerLeave() {
        if (bomberman.getObjBlock().collision(objBlock)) {
            return false;
        }
        return true;
    }

    public boolean isBoomed() {
        return boom;
    }

    public void setTiming(boolean timing) {
        this.timing = timing;
    }

    public boolean inBombWay(Block k, Bomb b) {
        if (b.getObjBlock().collision(k)) {
            return true;
        }
        return false;
    }

    public boolean equalLocation(Bomb b) {
        if (this.getPoint().equals(b.getPoint())) {
            return true;
        }
        return false;
    }

    public boolean isTiming() {
        return timing;
    }

    public void setTimeAnimation(int timeAnimation) {
        this.timeAnimation = timeAnimation;
    }

    public void setBoom(boolean isboom) {
        timing = !isboom;
        timeAnimation = 0;
        statusAmination = 2;
        getBombLocation(mapHash[0].length * 32);
    }
}
