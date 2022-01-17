package com.snow.bomberman.player;

import com.snow.bomberman.Game;
import com.snow.bomberman.Mode;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.block.Block;
import com.snow.bomberman.bomb.Bomb;
import com.snow.bomberman.bomb.BombControl;
import com.snow.bomberman.mob.Mob;
import com.snow.bomberman.mob.Mobs;
import com.snow.bomberman.setting.Setting;
import com.snow.bomberman.something.Point;
import com.snow.bomberman.something.Time;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

import static com.snow.bomberman.Game.WINDOW;
import static com.snow.bomberman.Game.ZOOM;
import static com.snow.bomberman.PlayWindow.PAUSE;

public class Bomberman extends Mob {
    private Canvas camera;
    private static LinkedList<Image> player = null;
    private static LinkedList<Image> effect = null;
    public static int RIGHT = 0;
    public static int LEFT = 4;
    public static int UP = 8;
    public static int DOWN = 12;
    public static int LIMITBOMB;
    public static double SPEED;
    private int status;
    private boolean moving;
    private int timeAnimation;
    private int statusAnimation;
    private int standAnimation;
    private int deadStatus;
    private int timeDeadAnimation;
    private HashMap<KeyCode, Double> key;
    private HashMap<KeyCode, Integer> keyStatus;
    private boolean canMoveL;
    private boolean canMoveR;
    private boolean canMoveU;
    private boolean canMoveD;
    private Block[][] tileMap;
    private int[][] mapHash;
    private final int downHead = 8;
    public static LinkedList<Bomb> LISTBOMB;
    public static LinkedList<BombControl> LISTBOMBCONTROL;
    public static int WALLPASS;
    private Media dead1;
    private Media dead2;
    private boolean canPutBomb;
    private LocalDateTime timePutStart;
    private LocalDateTime timePutStop;
    private long timeFlamePass;
    private Image flamepassImg;
    private int flamepassAnimation;
    private boolean flashed;
    private int timeEffect;
    private double lastX;
    private double lastY;
    private Point portal;

    public static void init() {
        if (player == null) {
            try {
                player = new LinkedList<>();
                effect = new LinkedList<>();
                player.add(new Image(Bomberman.class.getResource("/assets/player/r1.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/r2.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/r3.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/r4.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/l1.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/l2.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/l3.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/l4.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/u1.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/u2.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/u3.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/u4.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/d1.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/d2.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/d3.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/d4.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/dead1.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/dead2.png").toURI().toString()));
                player.add(new Image(Bomberman.class.getResource("/assets/player/dead3.png").toURI().toString()));
                effect.add(new Image(Bomberman.class.getResource("/assets/effect/effect1.png").toURI().toString()));
                effect.add(new Image(Bomberman.class.getResource("/assets/effect/effect2.png").toURI().toString()));
                effect.add(new Image(Bomberman.class.getResource("/assets/effect/effect3.png").toURI().toString()));
                effect.add(new Image(Bomberman.class.getResource("/assets/effect/effect4.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public Bomberman(double x, double y) {
        camera = null;
        this.x = x;
        this.y = y;
        moving = false;
        status = 0;
        timeAnimation = 0;
        statusAnimation = 2;
        standAnimation = 0;
        keyStatus = new HashMap<>();
        keyStatus.put(KeyCode.D, RIGHT);
        keyStatus.put(KeyCode.A, LEFT);
        keyStatus.put(KeyCode.W, UP);
        keyStatus.put(KeyCode.S, DOWN);
        key = new HashMap<>();
        key.put(KeyCode.D, 0.0);
        key.put(KeyCode.A, 0.0);
        key.put(KeyCode.W, 0.0);
        key.put(KeyCode.S, 0.0);
        LISTBOMB = new LinkedList<>();
        LISTBOMBCONTROL = new LinkedList<>();
        timeDeadAnimation = 0;
        deadStatus = 0;
        try {
            init();
            dead1 = new Media(Bomb.class.getResource("/assets/audio/dead1.wav").toURI().toString());
            dead2 = new Media(Bomb.class.getResource("/assets/audio/dead2.wav").toURI().toString());
            flamepassImg = new Image(getClass().getResource("/assets/player/fire/fire5.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        objBlock = new Block(x + 1, y + downHead + 1, 20, 30 - downHead);
        Mobs.MOBS.add(this);
        survival = true;
        deading = false;
        canPutBomb = true;
        timeFlamePass = 0;
        flamepassAnimation = 0;
        flashed = false;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void checkCanPut() {
        if (!canPutBomb) {
            timePutStop = LocalDateTime.now();
            if (Duration.between(timePutStart, timePutStop).toMillis() > 250) {
                canPutBomb = true;
            }
        }
    }

    public void keyDownAdd(KeyEvent e) {
        if (PAUSE) {
            return;
        }
        try {
            key.put(e.getCode(), SPEED);
            status = keyStatus.get(e.getCode());
            setStatus();
            moving = checkRunning();
        } catch (Exception ex) {
            if (e.getCode().equals(KeyCode.SPACE) && canPutBomb) {
                if (LISTBOMB.size() < LIMITBOMB) {
                    Point putBomb = setBomb();
                    int i = (int) putBomb.getY() / 32;
                    int j = (int) putBomb.getX() / 32;
                    if (mapHash[i][j] != 2) {
                        return;
                    }
                    Bomb newBomb = new Bomb(putBomb, mapHash, this);
                    timePutStart = LocalDateTime.now();
                    canPutBomb = false;
                    LISTBOMB.add(newBomb);
                }
            }
            if (e.getCode().equals(KeyCode.N)) {
                Point putBomb = setBomb();
                int i = (int) putBomb.getY() / 32;
                int j = (int) putBomb.getX() / 32;
                if (mapHash[i][j] != 2) {
                    return;
                }
                if (PlayWindow.BOMBCONTROLLED > 0) {
                    BombControl newBomb = new BombControl(putBomb, mapHash, this);
                    LISTBOMBCONTROL.add(newBomb);
                    PlayWindow.BOMBCONTROLLED --;
                }
            }

            if (e.getCode().equals(KeyCode.M)) {
                if (LISTBOMBCONTROL.size() > 0) {
                    boolean canBoom = false;
                    for (BombControl i : LISTBOMBCONTROL) {
                        if (i.isTiming()) {
                            i.setBoom(true);
                            canBoom = true;
                        }
                    }
                    if (canBoom) {
                        AudioClip audioClip = null;
                        try {
                            audioClip = new AudioClip(Bomb.class.getResource("/assets/audio/boom.wav").toURI().toString());
                            audioClip.setVolume(Setting.getEFFECTVOLUME());
                            audioClip.play();
                        } catch (URISyntaxException uriSyntaxException) {
                            uriSyntaxException.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void keyUpAdd(KeyEvent e) {
        key.put(e.getCode(), 0.0);
        moving = checkRunning();
        setStatus();
    }

    private void setStatus() {
        double right = key.get(KeyCode.D) - key.get(KeyCode.A);
        double down = key.get(KeyCode.S) - key.get(KeyCode.W);
        if (right != 0 && down > 0) {
            status = DOWN;
        } else if (right != 0 && down < 0) {
            status = UP;
        } else if (down == 0 && right > 0) {
            status = RIGHT;
        } else if (down == 0 && right < 0) {
            status = LEFT;
        } else if (right == 0 && down > 0) {
            status = DOWN;
        } else if (right == 0 && down < 0) {
            status = UP;
        }
    }

    private boolean checkRunning() {
        boolean moveR = (key.get(KeyCode.D) == SPEED);
        boolean moveL = (key.get(KeyCode.A) == SPEED);
        boolean moveU = (key.get(KeyCode.W) == SPEED);
        boolean moveD = (key.get(KeyCode.S) == SPEED);
        if (!moveR && !moveL && !moveU && !moveD) {
            return false;
        }
        if (moveR && moveL && !moveU && !moveD) {
            return false;
        }
        if (moveU && moveD && !moveR && !moveL) {
            return false;
        }
        if (moveR && moveL && moveU && moveD) {
            return false;
        }
        return true;
    }


    /**hàm vẽ objectBlock
     * @param render render
     * @param block block
     */
    private void drawRect(GraphicsContext render, Block block) {
        render.setFill(Color.RED);
        render.fillRect(block.getX(), block.getY(), block.getW(), 1);
        render.fillRect(block.getX(), block.getHY(), block.getW(), 1);
        render.fillRect(block.getX(), block.getY(), 1, block.getH());
        render.fillRect(block.getWX(), block.getY(), 1, block.getH());
        render.setFill(Color.WHITE);
    }

    private void deadAudio() {
        MediaPlayer mediaPlayer = new MediaPlayer(dead1);
        mediaPlayer.setVolume(Setting.getEFFECTVOLUME());
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(() -> {
            MediaPlayer mediaPlayer1 = new MediaPlayer(dead2);
            mediaPlayer1.setVolume(Setting.getEFFECTVOLUME());
            mediaPlayer1.play();
        });
    }

    @Override
    public void draw(GraphicsContext render) {
        if (camera == null) {
            camera = render.getCanvas();
        }
        objBlock.update(x + 1, y + downHead + 1, 22, 30 - downHead);
        if (!deading) {
            checkCanPut();
            checkCanMove();
            if (!moving) {
                standAnimation(render);
            } else {
                runningAnimation(render);
                move();
            }
            int i = (int) portal.getY();
            int j = (int) portal.getX();
            if (Mobs.MOBS.size() == 1 && objBlock.collision(tileMap[i][j]) && mapHash[i][j] == 10) {
                PlayWindow.WINLEVEL = true;
            }

            if (flashed) {
                timeEffect --;
                render.drawImage(effect.get(3 - timeEffect / 10), lastX, lastY);
                if (timeEffect == 0) {
                    flashed = false;
                }
            }

            moveCamera();
//            drawRect(render, objBlock);
            if (isFlamepass()) {
                double cameraX = - render.getCanvas().getLayoutX() / Game.ZOOM;
                if (timeFlamePass - Time.getTime() <= 15) {
                    render.drawImage(flamepassImg, cameraX + 170, 7.5);
                } else {
                    flamepassAnimation ++;
                }
                if (flamepassAnimation < 40) {
                    render.drawImage(flamepassImg, cameraX + 170, 7.5);
                } else if (flamepassAnimation >= 60) {
                    flamepassAnimation = 0;
                }
                if (timeFlamePass - Time.getTime() > 20) {
                    this.setFlamepass(false);
                }
            }
        } else {
            deadAnimation(render);
        }
    }

    public void move() {
        if (PAUSE) {
            key.put(KeyCode.D, 0.0);
            key.put(KeyCode.A, 0.0);
            key.put(KeyCode.S, 0.0);
            key.put(KeyCode.W, 0.0);
            return;
        }
        if (canMoveR) {
            x += key.get(KeyCode.D);
        }
        if (canMoveL) {
            x -= key.get(KeyCode.A);
        }
        if (canMoveU) {
            y -= key.get(KeyCode.W);
        }
        if (canMoveD) {
            y += key.get(KeyCode.S);
        }
    }

    private void moveCamera() {
        double limitRight = camera.getWidth() - (WINDOW.getWidth() / 2) / ZOOM;
        if (x + 12 > (WINDOW.getWidth() / 2) / ZOOM && x + 12 <= limitRight) {
            camera.setLayoutX(- (x + 12 - (WINDOW.getWidth() / 2) / ZOOM) * ZOOM);
        } else if (x + 12 <= (WINDOW.getWidth() / 2) / ZOOM) {
            camera.setLayoutX(0);
        } else {
            camera.setLayoutX((WINDOW.getWidth() - camera.getWidth() * ZOOM));
        }
    }

    private void deadAnimation(GraphicsContext render) {
        timeDeadAnimation++;
        if (timeDeadAnimation % 45 == 0) {
            if (deadStatus < 3) {
                deadStatus++;
            }
        }
        if (timeDeadAnimation == 250) {
            this.survival = false;
        }
        render.setFont(PlayWindow.MCFONT);
        render.fillText("LIFE -1",
                x + 26,
                y + 20 - timeDeadAnimation / 5.0);
        if (deadStatus < 3) {
            render.drawImage(player.get(16 + deadStatus), x, y);
        }
    }

    private void standAnimation(GraphicsContext render) {
        timeAnimation++;
        if (timeAnimation == 20) {
            standAnimation++;
            timeAnimation = 0;
        }
        if (standAnimation >= 2) {
            standAnimation = 0;
        }
        render.drawImage(player.get(status + standAnimation), x, y);
    }

    private void runningAnimation(GraphicsContext render) {
        timeAnimation++;
        if (timeAnimation == 20) {
            statusAnimation++;
            timeAnimation = 0;
        }
        if (statusAnimation >= 4) {
            statusAnimation = 2;
        }
        render.drawImage(player.get(status + statusAnimation), x, y);
    }

    private void checkCanMove() {
        canMoveR = checkMoveR();
        canMoveL = checkMoveL();
        canMoveU = checkMoveU();
        canMoveD = checkMoveD();
        checkMoveHigher();
    }

    private boolean canMove(double tryX, double tryY) {
        int x1 = (int)(tryX / 32.0D);
        int y1 = (int)(tryY / 32.0D);
        int x2 = (int)((tryX + this.objBlock.getW()) / 32.0D);
        int y2 = (int)(tryY / 32.0D);
        int x3 = (int)(tryX / 32.0D);
        int y3 = (int)((tryY + this.objBlock.getH()) / 32.0D);
        int x4 = (int)((tryX + this.objBlock.getW()) / 32.0D);
        int y4 = (int)((tryY + this.objBlock.getH()) / 32.0D);
        return this.checkMapHash(this.mapHash[y1][x1], 9)
                && this.checkMapHash(this.mapHash[y2][x2], 9)
                && this.checkMapHash(this.mapHash[y3][x3], 9)
                && this.checkMapHash(this.mapHash[y4][x4], 9);
    }

    private void checkMoveHigher() {
        if (key.get(KeyCode.D) != 0 && key.get(KeyCode.S) != 0
                && key.get(KeyCode.A) == 0 && key.get(KeyCode.W) == 0) {
            if (!canMove(objBlock.getX() + SPEED, objBlock.getY() + SPEED)) {
                if (canMoveR) {
                    canMoveD = false;
                }
            }
        }
        if (key.get(KeyCode.A) != 0 && key.get(KeyCode.S) != 0
                && key.get(KeyCode.D) == 0 && key.get(KeyCode.W) == 0) {
            if (!canMove(objBlock.getX() - SPEED, objBlock.getY() + SPEED)) {
                if (canMoveL) {
                    canMoveD = false;
                }
            }
        }
        if (key.get(KeyCode.D) != 0 && key.get(KeyCode.W) != 0
                && key.get(KeyCode.A) == 0 && key.get(KeyCode.S) == 0) {
            if (!canMove(objBlock.getX() + SPEED, objBlock.getY() - SPEED)) {
                if (canMoveR) {
                    canMoveU = false;
                }
            }
        }
        if (key.get(KeyCode.A) != 0 && key.get(KeyCode.W) != 0
                && key.get(KeyCode.D) == 0 && key.get(KeyCode.S) == 0) {
            if (!canMove(objBlock.getX() - SPEED, objBlock.getY() - SPEED)) {
                if (canMoveL) {
                    canMoveU = false;
                }
            }
        }
    }

    private boolean checkMoveR() {
        int i1 = (int) (objBlock.getY() / 32);
        int i2 = (int) (objBlock.getHY() / 32);
        int j = (int) ((objBlock.getWX() + SPEED) / 32);
        if (checkMapHash(mapHash[i1][j], 9) && checkMapHash(mapHash[i2][j], 9)) {
            return true;
        } else {
            if (key.get(KeyCode.D) != 0 && key.get(KeyCode.A) == 0) {
                x += objBlock.calcR(tileMap[i1][j]);
            }
            return false;
        }
    }

    private boolean checkMoveL() {
        int i1 = (int) (objBlock.getY() / 32);
        int i2 = (int) (objBlock.getHY() / 32);
        int j = (int) ((objBlock.getX() - SPEED) / 32);
        if (checkMapHash(mapHash[i1][j], 9) && checkMapHash(mapHash[i2][j], 9)) {
            return true;
        } else {
            if (key.get(KeyCode.D) == 0 && key.get(KeyCode.A) != 0) {
                x -= objBlock.calcL(tileMap[i1][j]);
            }
            return false;
        }
    }

    private boolean checkMoveU() {
        int i = (int) ((objBlock.getY() - SPEED) / 32);
        int j1 = (int) (objBlock.getX() / 32);
        int j2 = (int) (objBlock.getWX() / 32);
        if (checkMapHash(mapHash[i][j1], 9) && checkMapHash(mapHash[i][j2], 9)) {
            return true;
        } else {
            if (key.get(KeyCode.S) == 0 && key.get(KeyCode.W) != 0) {
                y -= objBlock.calcU(tileMap[i][j1]);
            }
            return false;
        }
    }

    private boolean checkMoveD() {
        int i = (int) ((objBlock.getHY() + SPEED) / 32);
        int j1 = (int) (objBlock.getX() / 32);
        int j2 = (int) (objBlock.getWX() / 32);
        if (checkMapHash(mapHash[i][j1], 9) && checkMapHash(mapHash[i][j2], 9)) {
            return true;
        } else {
            if (key.get(KeyCode.W) == 0 && key.get(KeyCode.S) != 0) {
                y += objBlock.calcD(tileMap[i][j1]);
            }
            return false;
        }
    }

    private boolean checkMapHash(int mapHash, int add) {
        return mapHash == 2
                || mapHash == 4
                || mapHash == add
                || mapHash == WALLPASS
                || (mapHash == 10
                && (WALLPASS == 3 || Mobs.MOBS.size() == 1));
    }

    public void setMap(Block[][] tileMap, int[][] mapHash) {
        this.tileMap = tileMap;
        this.mapHash = mapHash;
        portal = PlayWindow.PORTAL;
    }

    public void setMap(Block[][] tileMap, int[][] mapHash, Point portal) {
        this.tileMap = tileMap;
        this.mapHash = mapHash;
        this.portal = portal;
    }

    public Point setBomb() {
        int i = (int) (objBlock.getY() / 32);
        int j = (int) (objBlock.getX() / 32);
        if (status == UP) {
            if (checkMapHash(mapHash[i - 1][j], 2)) {
                return new Point(x + 12, y + 8);
            }
        } else if (status == DOWN) {
            if (checkMapHash(mapHash[i + 1][j], 2)) {
                return new Point(x + 12, y + 24);
            }
        } else if (status == RIGHT) {
            if (checkMapHash(mapHash[i][j + 1], 2)) {
                return new Point(x + 16, y + 16);
            }
        } else if (status == LEFT) {
            if (checkMapHash(mapHash[i][j - 1], 2)) {
                return new Point(x + 8, y + 16);
            }
        }
        return new Point(x + 12, y + 16);
    }

    @Override
    public void setFlamepass(boolean flamepass) {
        super.setFlamepass(flamepass);
        timeFlamePass = Time.getTime();
    }

    public void flash() {
        flashed = true;
        timeEffect = 40;
        lastX = x;
        lastY = y;
        int ip = (int) ((objBlock.getY() + objBlock.getH() / 2) / 32);
        int jp = (int) ((objBlock.getX() + objBlock.getW() / 2) / 32);
        if (status == RIGHT) {
            for (int j = 3; j >= 1;) {
                if (jp + j >= mapHash[0].length) {
                    j--;
                    continue;
                }
                if (checkMapHash(mapHash[ip][jp + j], 9)) {
                    x = (jp + j) * 32;
                    y = ip * 32;
                    return;
                }
                if (j == 1) {
                    j = 4;
                    if (jp + j >= mapHash[0].length) {
                        return;
                    }
                    if (checkMapHash(mapHash[ip][jp + j], 9)) {
                        x = (jp + j) * 32;
                        y = ip * 32;
                    }
                    return;
                }
                j --;
            }
        }
        if (status == LEFT) {
            for (int j = 3; j >= 1;) {
                if (jp - j < 0) {
                    j --;
                    continue;
                }
                if (checkMapHash(mapHash[ip][jp - j], 9)) {
                    x = (jp - j) * 32;
                    y = ip * 32;
                    return;
                }
                if (j == 1) {
                    j = 4;
                    if (jp - j < 0) {
                        return;
                    }
                    if (checkMapHash(mapHash[ip][jp - j], 9)) {
                        x = (jp - j) * 32;
                        y = ip * 32;
                    }
                    return;
                }
                j --;
            }
        }
        if (status == UP) {
            for (int i = 3; i >= 1;) {
                if (ip - i < 0) {
                    i --;
                    continue;
                }
                if (checkMapHash(mapHash[ip - i][jp], 9)) {
                    x = jp * 32;
                    y = (ip - i) * 32;
                    return;
                }
                if (i == 1) {
                    i = 4;
                    if (ip - i < 0) {
                        return;
                    }
                    if (checkMapHash(mapHash[ip - i][jp], 9)) {
                        x = jp * 32;
                        y = (ip - i) * 32;
                    }
                    return;
                }
                i --;
            }
        }
        if (status == DOWN) {
            for (int i = 3; i >= 1;) {
                if (ip + i >= mapHash.length) {
                    i --;
                    continue;
                }
                if (checkMapHash(mapHash[ip + i][jp], 9)) {
                    x = jp * 32;
                    y = (ip + i) * 32;
                    return;
                }
                if (i == 1) {
                    i = 4;
                    if (ip + i >= mapHash.length) {
                        return;
                    }
                    if (checkMapHash(mapHash[ip + i][jp], 9)) {
                        x = jp * 32;
                        y = (ip + i) * 32;
                    }
                    return;
                }
                i --;
            }
        }
    }

    @Override
    public void setSurvival(boolean survival) {
        if (!this.deading && PlayWindow.getMODE() != Mode.training) {
            if (PlayWindow.FINDLIFEONDEAD) {
                PlayWindow.SCORE += 2000;
            }
            PlayWindow.GAMEAUDIO.stop();
            deadAudio();
            this.deading = !survival;
        }
    }

    public static boolean canWallPass() {
        return WALLPASS == 3;
    }

    public static boolean isHasSpeedUp() {
        return SPEED >= 2.3;
    }
}