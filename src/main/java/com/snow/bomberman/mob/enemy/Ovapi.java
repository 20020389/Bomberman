package com.snow.bomberman.mob.enemy;

import com.snow.bomberman.Mode;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.block.Block;
import com.snow.bomberman.mob.Mob;
import com.snow.bomberman.mob.Mobs;
import com.snow.bomberman.mob.Way;
import com.snow.bomberman.something.Point;
import com.snow.bomberman.something.Time;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Random;

import static com.snow.bomberman.PlayWindow.PAUSE;

public class Ovapi extends Mob {
    private static LinkedList<Image> ovapi = null;
    private final double downHead = 1;
    private double speed;
    private final int LEFT = 0;
    private final int RIGHT = 4;
    private final int angreeL = 2;
    private final int angreeR = 6;
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
    private Point player;
    private boolean delayAngree;
    private static boolean ANGREE = false;
    private static long TIMEANGREE = 0;
    private boolean ghostMode;
    private int timeGhost;

    public static void init() {
        try {
            ovapi = new LinkedList<>();
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob1.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob2.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob3.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob4.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob5.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob6.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob7.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob8.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob9.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob10.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob11.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob12.png").toURI().toString()));
            ovapi.add(new Image(Ovapi.class.getResource("/assets/mobs/ovapi/mob13.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Ovapi(double x, double y) {
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
        speed = 1;
        delayAngree = true;
        ANGREE = false;
        ghostMode = false;
        timeGhost = 0;
    }

    public static void setAngree(boolean angree) {
        TIMEANGREE = Time.getTime();
        ANGREE = angree;
    }

    @Override
    public void draw(GraphicsContext render) {
        if (!deading) {
            player = Mobs.MOBS.get(0).getPoint();
            objBlock.update(x + 1, y + downHead + 1, 30, 30 - downHead);
            if ((x % 2 == 0 || y % 2 == 0) && delayAngree && ANGREE) {
                delayAngree = false;
            }
            renderOvapi(render);
            if (Mobs.MOBS.get(0).getObjBlock().collision(objBlock)) {
                Mobs.MOBS.get(0).setSurvival(false);
            }
            timeAnimation ++;
            if (timeAnimation == 20) {
                statusAnimation++;
                timeAnimation = 0;
            }
            if (statusAnimation >= 2) {
                statusAnimation = 0;
            }
        } else {
            deadAnimation(render);
        }
    }

    public void renderOvapi(GraphicsContext render) {
        if (PlayWindow.getMODE() != Mode.aventure) {
            if (!ANGREE || delayAngree) {
                speed = 1;
                moveEnemy();
                render.drawImage(ovapi.get(status + statusAnimation), x, y);
            } else {
                speed = 2;
                moveEnemyAngree();
                render.drawImage(ovapi.get(status + 2 + statusAnimation), x, y);
                if (TIMEANGREE - Time.getTime() >= 10) {
                    ANGREE = false;
                    delayAngree = true;
                }
            }
        } else {
            if (!PAUSE) {
                timeGhost ++;
                ghostMode = timeGhost >= 630;
                if (timeGhost >= 1260) {
                    timeGhost = 0;
                }
            }
            moveEnemy();
            if (!ghostMode) {
                render.drawImage(ovapi.get(status + statusAnimation), x, y);
            } else if ((timeGhost > 915 && timeGhost < 975)){
                render.drawImage(ovapi.get(12), x, y);
            }
        }
    }

    public void deadAnimation(GraphicsContext render) {
        timeDeadAnimation++;
        if (deadStatus == 0 && timeDeadAnimation == 60) {
            deadStatus ++;
        } else if (deadStatus > 0 && timeDeadAnimation % 30 == 0) {
            deadStatus++;
        }
        if (timeDeadAnimation == 120) {
            this.survival = false;
            Mobs.MOBS.remove(this);
        }
        if (deadStatus >= 4) {
            deadStatus = 0;
        }
        render.setFont(PlayWindow.MCFONT);
        render.fillText(" + 1200", x + 26, y + 20 - timeDeadAnimation / 5);
        render.drawImage(ovapi.get(8 + deadStatus), x, y);
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
            if (j + 1 >= 30) {
                canMoveR = false;
            }
            canMoveL = checkMapHash(mapHash[i][j - 1]);
            if (j - 1 <= 0) {
                canMoveL = false;
            }
            canMoveU = checkMapHash(mapHash[i - 1][j]);
            if (i - 1 <= 0) {
                canMoveU = false;
            }
            canMoveD = checkMapHash(mapHash[i + 1][j]);
            if (i + 1 >= 12) {
                canMoveD = false;
            }
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

    private void moveEnemyAngree() {
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
        return mapHash == 2
                || mapHash == 3
                || mapHash == (PlayWindow.getMODE() == Mode.aventure ? 2 : 1);
    }

    @Override
    public void setSurvival(boolean survival) {
        if (!this.deading) {
            this.deading = !survival;
            if (PlayWindow.getMODE() != Mode.aventure) {
                setAngree(true);
            }
            PlayWindow.SCORE += 1200;
            PlayWindow.BONUSLIFE += 1200;
            isLastEnemy();
        }
    }

    public void setMap(int[][] mapHash) {
        this.mapHash = mapHash;
    }
}
