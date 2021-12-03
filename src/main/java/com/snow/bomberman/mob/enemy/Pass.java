package com.snow.bomberman.mob.enemy;

import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.block.Block;
import com.snow.bomberman.bomb.Bomb;
import com.snow.bomberman.mob.Mob;
import com.snow.bomberman.mob.Mobs;
import com.snow.bomberman.mob.Way;
import com.snow.bomberman.player.Bomberman;
import com.snow.bomberman.setting.Setting;
import com.snow.bomberman.something.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Random;

import static com.snow.bomberman.PlayWindow.PAUSE;

public class Pass extends Mob {
    private static LinkedList<Image> pass = null;
    private final double downHead = 1;
    private double speed = 1;
    private final int LEFT = 0;
    private final int RIGHT = 3;
    private int timeGhost;
    private int timeDeadAnimation;
    private int statusAnimation;
    private int deadStatus;
    private boolean ghostMode;
    private double moveHori;
    private double moveVerti;
    private int[][] mapHash;
    private boolean canMoveR;
    private boolean canMoveL;
    private boolean canMoveU;
    private boolean canMoveD;
    private Way lastWay;
    private boolean predaytor;
    private Point player;
    GraphicsContext render;
    private LinkedList<LinkedList<Point>> listWay;
    private boolean beKill;

    public static void init() {
        if (pass == null) {
            try {
                pass = new LinkedList<>();
                pass.add(new Image(Pass.class.getResource("/assets/mobs/pass/mob.png").toURI().toString()));
                pass.add(new Image(Pass.class.getResource("/assets/mobs/pass/mob7.png").toURI().toString()));
                pass.add(new Image(Pass.class.getResource("/assets/mobs/pass/mob18.png").toURI().toString()));
                pass.add(new Image(Pass.class.getResource("/assets/mobs/pass/mob19.png").toURI().toString()));
                pass.add(new Image(Pass.class.getResource("/assets/mobs/pass/mob20.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public Pass(double x, double y) {
        this.x = x;
        this.y = y;
        init();
        objBlock = new Block(x + 1, y + downHead + 1, 30, 30 - downHead);
        statusAnimation = 0;
        deadStatus = 0;
        timeDeadAnimation = 0;
        moveHori = -1;
        moveVerti = 0;
        lastWay = Way.left;
        survival = true;
        deading = false;
        canMoveR = false;
        canMoveL = false;
        canMoveU = false;
        canMoveD = false;
        Mobs.MOBS.add(this);
        predaytor = false;
        listWay = new LinkedList<>();
        beKill = false;
    }

    @Override
    public void draw(GraphicsContext render) {
        this.render = render;
        if (!deading) {
            player = Mobs.MOBS.get(0).getPoint();
            int j = (int) x / 32;
            int i = (int) y / 32;
            mapHash[i][j] = 2;
//            moveEnemy();
            movePredator();
            if (predaytor) {
                speed = 2;
            } else {
                speed = 1;
            }
            if (!PAUSE) {
                x += moveHori;
                y += moveVerti;
                x = lamtron(x * 10) / 10;
                y = lamtron(y * 10) / 10;
            }
            j = (int) x / 32;
            i = (int) y / 32;

            if (Mobs.MOBS.get(0).getObjBlock().collision(objBlock)) {
                setBoom(true);
            }
            mapHash[i][j] = 9;
            objBlock.update(x + 1, y + downHead + 1, 30, 30 - downHead);
            timeGhost ++;
            ghostMode = timeGhost >= 315 && timeGhost < 375;
            if (timeGhost >= 630) {
                timeGhost = 0;
            }
            if (ghostMode) {
                render.drawImage(pass.get(0), x, y);
            }
        } else {
            deadAnimation(render);
        }
    }

    public Point setBomb() {
        return new Point(x + 12, y + 16);
    }

    public void deadAnimation(GraphicsContext render) {
        int j = (int) x / 32;
        int i = (int) y / 32;
        timeDeadAnimation++;
        if (deadStatus == 0 && timeDeadAnimation == 40) {
            deadStatus ++;
        } else if (deadStatus > 0 && timeDeadAnimation % 20 == 0) {
            deadStatus++;
        }
        mapHash[i][j] = 2;
        if (timeDeadAnimation >= 80) {
            Bomberman.LISTBOMB.add(
                    new Bomb(setBomb(), mapHash,
                            (Bomberman) Mobs.MOBS.get(0), 3, 160)
            );
            Mobs.MOBS.remove(this);
        }
        if (deadStatus >= 4) {
            deadStatus = 0;
        }
        if (beKill) {
            render.setFont(PlayWindow.MCFONT);
            render.fillText(" + 2000", x + 26, y + 20 - timeDeadAnimation / 5);
        }
        render.drawImage(pass.get(1 + deadStatus), x, y);
    }

    private void checkPlayer(int i, int j, int ip, int jp) {
        moveHori = 0;
        moveVerti = 0;
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

    private void checkPlayer(int i, int j, int ip, int jp,
                             boolean moveR, boolean moveL, boolean moveU, boolean moveD) {
        moveHori = 0;
        moveVerti = 0;
        if (jp == j) {
            moveHori = 0;
        } else if (jp < j && moveL) {
            moveHori = - (j - jp);
        } else if (jp > j && moveR) {
            moveHori = jp - j;
        }
        if (ip == i) {
            moveVerti = 0;
        } else if (ip < i && moveU) {
            moveVerti = - (i - ip);
        } else if (ip > i && moveD) {
            moveVerti = ip - i;
        }
    }

    private void movePredator() {
        if (PAUSE) {
            return;
        }
        int j = (int) (x / 32);
        int i = (int) (y / 32);
        int jp = (int) (player.getX() + 16) / 32;
        int ip = (int) (player.getY() + 16) / 32;
        if (j * 32 == x && i * 32 == y) {
            LinkedList<Point> way = new LinkedList<>();
            findBestWay(i, j, way);
            if (listWay.size() == 0) {
                speed = 1;
                predaytor = false;
            }
            if (!predaytor) {
                canMoveR = checkMapHash(mapHash[i][j + 1]);
                canMoveL = checkMapHash(mapHash[i][j - 1]);
                canMoveU = checkMapHash(mapHash[i - 1][j]);
                canMoveD = checkMapHash(mapHash[i + 1][j]);

                checkPlayer(i, j, ip, jp);

                LinkedList<Way> check = new LinkedList<>();
                if (canMoveR) {
                    if (moveHori > 0) {
                        check.add(Way.right);
                        check.add(Way.right);
                    }
                    if (lastWay == Way.right) {
                        check.add(Way.right);
                        check.add(Way.right);
                    }
                    check.add(Way.right);
                }
                if (canMoveL) {
                    if (moveHori < 0) {
                        check.add(Way.left);
                        check.add(Way.left);
                    }
                    if (lastWay == Way.left) {
                        check.add(Way.left);
                        check.add(Way.left);
                    }
                    check.add(Way.left);
                }
                if (canMoveU) {
                    if (moveVerti < 0) {
                        check.add(Way.up);
                        check.add(Way.up);
                    }
                    if (lastWay == Way.up) {
                        check.add(Way.up);
                        check.add(Way.up);
                    }
                    check.add(Way.up);
                }
                if (canMoveD) {
                    if (moveVerti > 0) {
                        check.add(Way.down);
                        check.add(Way.down);
                    }
                    if (lastWay == Way.down) {
                        check.add(Way.down);
                        check.add(Way.down);
                    }
                    check.add(Way.down);
                }
                moveVerti = 0;
                moveHori = 0;
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
                    }
                    if (check.get(ran) == Way.left) {
                        moveHori = - speed;
                        lastWay = Way.left;
                    }
                }
                return;
            }
            int min = 999;
            for (LinkedList<Point> k : listWay) {
                if (min > k.size()) {
                    min = k.size();
                    way = k;
                }
            }
            if (way.size() >= 2) {
                canMoveR = checkMapHash(mapHash[i][j + 1]);
                canMoveL = checkMapHash(mapHash[i][j - 1]);
                canMoveU = checkMapHash(mapHash[i - 1][j]);
                canMoveD = checkMapHash(mapHash[i + 1][j]);
                checkPlayer(i, j, (int) way.get(1).getY(), (int) way.get(1).getX());

//                System.out.println(way.get(1));
            }
            listWay.clear();
            /*render.setFill(Color.rgb(255, 1, 1, 0.6));
            for (Point k : way) {
                render.fillRect(k.getX() * 32, k.getY() * 32, 32, 32);

            }*/

        }
    }

    private void checkPredator() {
        int j = (int) (x / 32);
        int i = (int) (y / 32);
        int jp = (int) (player.getX() + 16) / 32;
        int ip = (int) (player.getY() + 16) / 32;
        if (j * 32 == x && i * 32 == y) {
            predaytor = Math.abs(i - ip) <= 3
                    && Math.abs(j - jp) <= 3;
        }
    }

    private void findBestWay(int im, int jm, LinkedList<Point> way) {
        int j = (int) (x / 32);
        int i = (int) (y / 32);
        int jp = (int) (player.getX() + 16) / 32;
        int ip = (int) (player.getY() + 16) / 32;
        int limit = 3;
        if (Math.abs(i - ip) > limit
                || Math.abs(j - jp) > limit
                || mapHash[ip][jp] != 2
                || listWay.size() == 10
                || (i == ip && j == jp) ) {
            predaytor = false;
            speed = 1;
            return;
        } else {
            predaytor = true;
            speed = 2;
        }

        /*int num = Math.max(Math.abs(i - ip), Math.abs(j - jp));
        limit = Math.max(limit, num);*/

        /*render.setFill(Color.rgb(0, 0, 0, 0.1));
        render.fillRect(jm * 32, im * 32, 32, 32);*/
        if (Math.abs(i - im) > limit || Math.abs(j - jm) > limit) {
            return;
        }

        Point check = new Point(jm, im);
        way.add(check);

        if (im == ip && jm == jp) {
            listWay.add(cpyList(way));
            way.remove(check);
            return;
        }

        boolean moveR = checkMapHash(mapHash[im][jm + 1]);
        boolean moveL = checkMapHash(mapHash[im][jm - 1]);
        boolean moveU = checkMapHash(mapHash[im - 1][jm]);
        boolean moveD = checkMapHash(mapHash[im + 1][jm]);

        checkPlayer(im, jm, ip, jp, moveR, moveL, moveU, moveD);
        double moveH = moveHori;
        double moveV = moveVerti;
        moveHori = 0;
        moveVerti = 0;
//        System.out.println(moveH + " " + moveV);

        for (int l = 0; l < 3; l++) {
            if (listWay.size() >= 10) {
                return;
            }
            if (l >= 2 || (moveH > 0 && (Math.abs(moveH) >= Math.abs(moveV) || l >= 1))) {
                if (moveR) {
                    boolean move = true;
                    for (Point k : way) {
                        if (k.equals(jm + 1, im)) {
                            move = false;
                        }
                    }
                    if (move && Math.abs(i - im) <= limit && Math.abs(j - (jm + 1)) <= limit) {
                        findBestWay(im, jm + 1, way);
                    }
                    moveR = false;
                }
            }

            if (l >= 2 || (moveH < 0 && (Math.abs(moveH) >= Math.abs(moveV) || l >= 1))) {
                if (moveL) {
                    boolean move = true;
                    for (Point k : way) {
                        if (k.equals(jm - 1, im)) {
                            move = false;
                        }
                    }
                    if (move && Math.abs(i - im) <= limit && Math.abs(j - (jm - 1)) <= limit) {
                        findBestWay(im, jm - 1, way);
                    }
                    moveL = false;
                }
            }
            if (l >= 2 || (moveV < 0 && (Math.abs(moveH) <= Math.abs(moveV) || l >= 1))) {
                if (moveU) {
                    boolean move = true;
                    for (Point k : way) {
                        if (k.equals(jm, im - 1)) {
                            move = false;
                        }
                    }
                    if (move && Math.abs(i - (im - 1)) <= limit && Math.abs(j - jm) <= limit) {
                        findBestWay(im - 1, jm, way);
                    }
                    moveU = false;
                }
            }
            if (l >= 2 || (moveV > 0 && (Math.abs(moveH) <= Math.abs(moveV) || l >= 1))) {
                if (moveD) {
                    boolean move = true;
                    for (Point k : way) {
                        if (k.equals(jm, im + 1)) {
                            move = false;
                        }
                    }
                    if (move && Math.abs(i - (im + 1)) <= limit && Math.abs(j - jm) <= limit) {
                        findBestWay(im + 1, jm, way);
                    }
                    moveD = false;
                }
            }
        }

        way.remove(check);
    }

    private LinkedList<Point> cpyList(LinkedList<Point> cpy) {
        return new LinkedList<>(cpy);
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
            PlayWindow.SCORE += 2000;
            PlayWindow.BONUSLIFE += 2000;
            isLastEnemy();
            beKill = true;
        }
    }

    public void setBoom(boolean survival) {
        if (!this.deading) {
//            System.out.println("Mob left: " + Mobs.MOBS.size());
            this.deading = survival;
            isLastEnemy();
        }
    }

    public void setMap(int[][] mapHash) {
        this.mapHash = mapHash;
    }
}
