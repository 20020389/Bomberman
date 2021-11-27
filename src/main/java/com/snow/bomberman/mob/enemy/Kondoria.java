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

import static com.snow.bomberman.PlayWindow.PAUSE;

public class Kondoria extends Mob {
    private static LinkedList<Image> kondoria = null;
    private final double downHead = 1;
    public static double SPEED = 0.8;
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
    private Way way;
    private Point player;

    public static void init() {
        if (kondoria == null) {
            try {
                kondoria = new LinkedList<>();
                kondoria.add(new Image(Kondoria.class.getResource("/assets/mobs/kondoria/mob1.png").toURI().toString()));
                kondoria.add(new Image(Kondoria.class.getResource("/assets/mobs/kondoria/mob2.png").toURI().toString()));
                kondoria.add(new Image(Kondoria.class.getResource("/assets/mobs/kondoria/mob3.png").toURI().toString()));
                kondoria.add(new Image(Kondoria.class.getResource("/assets/mobs/kondoria/mob4.png").toURI().toString()));
                kondoria.add(new Image(Kondoria.class.getResource("/assets/mobs/kondoria/mob5.png").toURI().toString()));
                kondoria.add(new Image(Kondoria.class.getResource("/assets/mobs/kondoria/mob6.png").toURI().toString()));
                kondoria.add(new Image(Kondoria.class.getResource("/assets/mobs/kondoria/mob7.png").toURI().toString()));
                kondoria.add(new Image(Kondoria.class.getResource("/assets/mobs/kondoria/mob8.png").toURI().toString()));
                kondoria.add(new Image(Kondoria.class.getResource("/assets/mobs/kondoria/mob9.png").toURI().toString()));
                kondoria.add(new Image(Kondoria.class.getResource("/assets/mobs/kondoria/mob10.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public Kondoria(double x, double y) {
        this.x = x;
        this.y = y;
        init();
        objBlock = new Block(x + 1, y + downHead + 1, 30, 30 - downHead);
        statusAnimation = 0;
        deadStatus = 0;
        timeAnimation = 0;
        timeDeadAnimation = 0;
        moveHori = -SPEED;
        moveVerti = 0;
        status = LEFT;
        way = Way.left;
        survival = true;
        deading = false;
        Mobs.MOBS.add(this);
    }

    @Override
    public void draw(GraphicsContext render) {
        if (!deading) {
            player = Mobs.MOBS.get(0).getPoint();
            int j = (int) x / 32;
            int i = (int) y / 32;
            moveEnemy();
            j = (int) x / 32;
            i = (int) y / 32;
            if (Mobs.MOBS.get(0).getObjBlock().collision(objBlock)) {
                Mobs.MOBS.get(0).setSurvival(false);
            }
            objBlock.update(x + 1, y + downHead + 1, 30, 30 - downHead);
            if (moveVerti == 0 && moveHori == 0) {
                render.drawImage(kondoria.get(status), x, y);
            } else {
                render.drawImage(kondoria.get(status + statusAnimation), x, y);
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
        timeDeadAnimation++;
        if (deadStatus == 0 && timeDeadAnimation == 60) {
            deadStatus++;
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
        render.fillText(" + 1000", x + 26, y + 20 - timeDeadAnimation / 5);
        render.drawImage(kondoria.get(6 + deadStatus), x, y);
    }

    private void moveEnemy() {
        if (PAUSE) {
            return;
        }
        int j = (int) ((x + 12) / 32);
        int i = (int) ((y + 16) / 32);
        int jp = (int) (player.getX() + 16) / 32;
        int ip = (int) (player.getY() + 16) / 32;
        if ((j * 32 == x && i * 32 == y)) {
            moveHori = 0;
            moveVerti = 0;
            if (jp == j) {
                moveHori = 0;
            } else if (jp < j) {
                moveHori = -SPEED;
            } else {
                moveHori = SPEED;
            }
            if (ip == i) {
                moveVerti = 0;
            } else if (ip < i) {
                moveVerti = -SPEED;
            } else {
                moveVerti = SPEED;
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
        return mapHash == 2;
    }

    @Override
    public void setSurvival(boolean survival) {
        if (!this.deading) {
            this.deading = !survival;
            Ovapi.setAngree(true);
            PlayWindow.SCORE += 1000;
            PlayWindow.BONUSLIFE += 1000;
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