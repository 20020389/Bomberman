package com.snow.bomberman.mob.enemy;

import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.block.Block;
import com.snow.bomberman.mob.Mob;
import com.snow.bomberman.mob.Mobs;
import com.snow.bomberman.mob.Way;
import com.snow.bomberman.something.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Random;

import static com.snow.bomberman.PlayWindow.PAUSE;

public class Minvo extends Mob {
    private static LinkedList<Image> minvo = null;
    private final double downHead = 1;
    private final double speed = 3.2;
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
    private Way way;
    private Point player;

    public static void init() {
        if (minvo == null) {
            try {
                minvo = new LinkedList<>();
                minvo.add(new Image(Minvo.class.getResource("/assets/mobs/minvo/mob1.png").toURI().toString()));
                minvo.add(new Image(Minvo.class.getResource("/assets/mobs/minvo/mob2.png").toURI().toString()));
                minvo.add(new Image(Minvo.class.getResource("/assets/mobs/minvo/mob3.png").toURI().toString()));
                minvo.add(new Image(Minvo.class.getResource("/assets/mobs/minvo/mob4.png").toURI().toString()));
                minvo.add(new Image(Minvo.class.getResource("/assets/mobs/minvo/mob5.png").toURI().toString()));
                minvo.add(new Image(Minvo.class.getResource("/assets/mobs/minvo/mob6.png").toURI().toString()));
                minvo.add(new Image(Minvo.class.getResource("/assets/mobs/minvo/mob7.png").toURI().toString()));
                minvo.add(new Image(Minvo.class.getResource("/assets/mobs/minvo/mob8.png").toURI().toString()));
                minvo.add(new Image(Minvo.class.getResource("/assets/mobs/minvo/mob9.png").toURI().toString()));
                minvo.add(new Image(Minvo.class.getResource("/assets/mobs/minvo/mob10.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public Minvo(double x, double y) {
        this.x = x;
        this.y = y;
        init();
        objBlock = new Block(x + 1, y + downHead + 1, 30, 30 - downHead);
        statusAnimation = 0;
        deadStatus = 0;
        timeAnimation = 0;
        timeDeadAnimation = 0;
        moveHori = -speed;
        moveVerti = 0;
        status = LEFT;
        way = Way.left;
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
            player = Mobs.MOBS.get(0).getPoint();
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
            if (moveVerti == 0 && moveHori == 0) {
                render.drawImage(minvo.get(status), x, y);
            } else {
                render.drawImage(minvo.get(status + statusAnimation), x, y);
            }
            timeAnimation++;
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
            deadStatus++;
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
        render.fillText(" + 800", x + 26, y + 20 - timeDeadAnimation / 5);
        render.drawImage(minvo.get(6 + deadStatus), x, y);
    }

    private void moveEnemy() {
        if (PAUSE) {
            return;
        }
        int j = (int) ((x + 12) / 32);
        int i = (int) ((y + 16) / 32);
        int jp = (int) (player.getX() + 16) / 32;
        int ip = (int) (player.getY() + 16) / 32;
        if (jp < j) {
            status = LEFT;
        } else {
            status = RIGHT;
        }
        if ((j * 32 == x && i * 32 == y)) {
            moveHori = 0;
            moveVerti = 0;
            canMoveR = checkMapHash(mapHash[i][j + 1]);
            canMoveL = checkMapHash(mapHash[i][j - 1]);
            canMoveU = checkMapHash(mapHash[i - 1][j]);
            canMoveD = checkMapHash(mapHash[i + 1][j]);
            if (jp == j) {
                moveHori = 0;
            } else if (jp < j && canMoveL) {
                moveHori = - speed;
            } else if (jp > j && canMoveR) {
                moveHori = speed;
            }
            if (ip == i) {
                moveVerti = 0;
            } else if (ip < i && canMoveU) {
                moveVerti = - speed;
            } else if (ip > i && canMoveD) {
                moveVerti = speed;
            }
            if (moveVerti != 0 && moveHori != 0) {
                Random random = new Random();
                if (Math.abs(ip - i) > Math.abs(jp - j)) {
                    moveHori = 0;
                } else {
                    moveVerti = 0;
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
            this.deading = !survival;
            Ovapi.setAngree(true);
            PlayWindow.SCORE += 800;
            PlayWindow.BONUSLIFE += 800;
            isLastEnemy();
        }
    }

    public void setMap(int[][] mapHash) {
        this.mapHash = mapHash;
    }
}


/*LinkedList<Way> check = new LinkedList<>();
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
            }*/