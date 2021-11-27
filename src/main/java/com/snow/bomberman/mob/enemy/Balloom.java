package com.snow.bomberman.mob.enemy;

import com.snow.bomberman.block.Block;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.mob.Mob;
import com.snow.bomberman.mob.Mobs;
import com.snow.bomberman.mob.Way;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Random;

import static com.snow.bomberman.PlayWindow.PAUSE;

public class Balloom extends Mob {
    private static LinkedList<Image> balloon;
    public static boolean INIT = false;
    private final double downHead = 1;
    private final double speed = 1;
    private final int LEFT = 0;
    private final int RIGHT = 3;
    private int timeAnimation;
    private int timeDeadAnimation;
    private int statusAnimation;
    private int deadStatus;
    private int status;
    private double moveHori;
    private double moveVerti;
    private int[][] mapHash;
    private boolean canMoveR;
    private boolean canMoveL;
    private boolean canMoveU;
    private boolean canMoveD;
    private Way lastWay;

    public static void init() {
        if (!INIT) {
            try {
                balloon = new LinkedList<>();
                balloon.add(new Image(Balloom.class.getResource("/assets/mobs/balloom/mob1.png").toURI().toString()));
                balloon.add(new Image(Balloom.class.getResource("/assets/mobs/balloom/mob2.png").toURI().toString()));
                balloon.add(new Image(Balloom.class.getResource("/assets/mobs/balloom/mob3.png").toURI().toString()));
                balloon.add(new Image(Balloom.class.getResource("/assets/mobs/balloom/mob4.png").toURI().toString()));
                balloon.add(new Image(Balloom.class.getResource("/assets/mobs/balloom/mob5.png").toURI().toString()));
                balloon.add(new Image(Balloom.class.getResource("/assets/mobs/balloom/mob6.png").toURI().toString()));
                balloon.add(new Image(Balloom.class.getResource("/assets/mobs/balloom/mob7.png").toURI().toString()));
                balloon.add(new Image(Balloom.class.getResource("/assets/mobs/balloom/mob8.png").toURI().toString()));
                balloon.add(new Image(Balloom.class.getResource("/assets/mobs/balloom/mob9.png").toURI().toString()));
                balloon.add(new Image(Balloom.class.getResource("/assets/mobs/balloom/mob10.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            INIT = true;
        }
    }

    public Balloom(double x, double y) {
        this.x = x;
        this.y = y;
        init();
        objBlock = new Block(x + 1, y + downHead + 1, 30, 30 - downHead);
        statusAnimation = 0;
        deadStatus = 0;
        timeAnimation = 0;
        timeDeadAnimation = 0;
        moveHori = - speed;
        moveVerti = 0;
        status = LEFT;
        lastWay = Way.left;
        survival = true;
        deading = false;
        canMoveR = false;
        canMoveL = false;
        canMoveU = false;
        canMoveD = false;
        Mobs.MOBS.add(this);
    }

    @Override
    public void draw(GraphicsContext render) {
        if (!deading) {
            int j = (int) x / 32;
            int i = (int) y / 32;
            mapHash[i][j] = 2;
            moveEnemy();
            j = (int) x / 32;
            i = (int) y / 32;
            if (Mobs.MOBS.get(0).getObjBlock().collision(objBlock)) {
                Mobs.MOBS.get(0).setSurvival(false);
            }
            mapHash[i][j] = 9;
            objBlock.update(x + 1, y + downHead + 1, 30, 30 - downHead);
            render.drawImage(balloon.get(status + statusAnimation), x, y);
//            System.out.println(statusAnimation);
            timeAnimation ++;
            if (timeAnimation == 20) {
                statusAnimation++;
                timeAnimation = 0;
            }
            if (statusAnimation >= 3) {
                statusAnimation = 0;
            }
        } else {
            deadAnimation(render);
        }
    }

    public void deadAnimation(GraphicsContext render) {
        int j = (int) x / 32;
        int i = (int) y / 32;
        timeDeadAnimation++;
        if (deadStatus == 0 && timeDeadAnimation == 60) {
            deadStatus ++;
        } else if (deadStatus > 0 && timeDeadAnimation % 30 == 0) {
            deadStatus++;
        }
        mapHash[i][j] = 2;
        if (timeDeadAnimation == 120) {
            this.survival = false;
            Mobs.MOBS.remove(this);
        }
        if (deadStatus >= 4) {
            deadStatus = 0;
        }
        render.setFont(PlayWindow.MCFONT);
        render.fillText(" + 200", x + 26, y + 20 - timeDeadAnimation / 5);
        render.drawImage(balloon.get(6 + deadStatus), x, y);
    }

    private void moveEnemy() {
        if (PAUSE) {
            return;
        }
        int j = (int) (x / 32);
        int i = (int) (y / 32);
        if (j * 32 == x && i * 32 == y) {
            moveHori = 0;
            moveVerti = 0;
            canMoveR = checkMapHash(mapHash[i][j + 1]);
            canMoveL = checkMapHash(mapHash[i][j - 1]);
            canMoveU = checkMapHash(mapHash[i - 1][j]);
            canMoveD = checkMapHash(mapHash[i + 1][j]);
            LinkedList<Way> check = new LinkedList<>();
            if (canMoveR) {
                if (lastWay == Way.right) {
                    check.add(Way.right);
                    check.add(Way.right);
                }
                check.add(Way.right);
            }
            if (canMoveL) {
                if (lastWay == Way.left) {
                    check.add(Way.left);
                    check.add(Way.left);
                }
                check.add(Way.left);
            }
            if (canMoveU) {
                if (lastWay == Way.up) {
                    check.add(Way.up);
                    check.add(Way.up);
                }
                check.add(Way.up);
            }
            if (canMoveD) {
                if (lastWay == Way.down) {
                    check.add(Way.down);
                    check.add(Way.down);
                }
                check.add(Way.down);
            }
            if (check.size() > 0) {
                Random r = new Random();
                int ran = r.nextInt(check.size());
                if (check.get(ran) == Way.up) {
                    moveVerti = - speed;
                    lastWay = Way.up;
                }
                if (check.get(ran) == Way.down) {
                    moveVerti = speed;
                    lastWay = Way.down;
                }
                if (check.get(ran) == Way.right) {
                    moveHori = speed;
                    lastWay = Way.right;
                    status = RIGHT;
                }
                if (check.get(ran) == Way.left) {
                    moveHori = - speed;
                    lastWay = Way.left;
                    status = LEFT;
                }
            }
        }
        x += moveHori;
        y += moveVerti;
        x = lamtron(x * 10) / 10;
        y = lamtron(y * 10) / 10;
    }

    double lamtron(double a) {
        double num = (int) (a);
        if (num + 0.5 <= a) {
            num += 1;
        }
        return num;
    }

    private boolean checkMapHash(int mapHash) {
        return mapHash == 2;
    }

    @Override
    public void setSurvival(boolean survival) {
        if (!this.deading) {
//            System.out.println("Mob left: " + Mobs.MOBS.size());
            this.deading = !survival;
            Ovapi.setAngree(true);
            PlayWindow.SCORE += 200;
            PlayWindow.BONUSLIFE += 200;
            isLastEnemy();
        }
    }

    public void setMap(int[][] mapHash) {
        this.mapHash = mapHash;
    }
}
