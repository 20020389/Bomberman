package com.snow.bomberman.guide;

import com.snow.bomberman.Game;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.Status;
import com.snow.bomberman.button.Button;
import com.snow.bomberman.something.Control;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Guide extends Control {
    public static int STATUS = 0;
    public static final int MAINGUIDE = 0;
    public static final int PLAYERSHOW = 1;
    public static final int MOBSHOW = 2;
    public static final int ITEMSHOW = 3;

    private final Button baseGuide;
    private final Button playerInfo;
    private final Button mobInfo;
    private final Button itemInfo;
    private final GuidePlayer guidePlayer;
    private final GuidePlayer guideMob;
    private final GuidePlayer guideItem;
    private final Button backPage;

    public Guide() {
        baseGuide = new Button("");
        baseGuide.setX(getX() + 75);
        baseGuide.setY(getY() + 70);
        baseGuide.setButtonImg("/assets/something/pausescreen1.png");

        playerInfo = new Button("");
        playerInfo.setX(getX() + 135);
        playerInfo.setY(getY() + 130);
        playerInfo.setButtonImg("/assets/something/play11.png");
        playerInfo.setButtonhHoverImg("/assets/something/play12.png");
        playerInfo.setW(52);
        playerInfo.setH(52);
        playerInfo.setImageIn("/assets/player/r1.png", 24, 32);

        mobInfo = new Button("");
        mobInfo.setX(getX() + 224);
        mobInfo.setY(getY() + 130);
        mobInfo.setButtonImg("/assets/something/play11.png");
        mobInfo.setButtonhHoverImg("/assets/something/play12.png");
        mobInfo.setW(52);
        mobInfo.setH(52);
        mobInfo.setImageIn("/assets/mobs/balloom/mob4.png", 32, 32);

        itemInfo = new Button("");
        itemInfo.setX(getX() + 313);
        itemInfo.setY(getY() + 130);
        itemInfo.setButtonImg("/assets/something/play11.png");
        itemInfo.setButtonhHoverImg("/assets/something/play12.png");
        itemInfo.setW(52);
        itemInfo.setH(52);
        itemInfo.setImageIn("/assets/items/item1.png", 32, 32);

        STATUS = MAINGUIDE;

        backPage = new Button("");
        backPage.setButtonImg("/assets/something/play11.png");
        backPage.setButtonhHoverImg("/assets/something/play12.png");
        backPage.setImageIn("/assets/something/back.png", 14, 16);
        backPage.setX(48);
        backPage.setY(getY() + 70);

        guidePlayer = new GuidePlayer(getX(), getY(), PLAYERSHOW);

        guideMob = new GuidePlayer(getX(), getY(), MOBSHOW);
        guideMob.setList("/assets/something/list2.png");
        guideMob.setIcon("/assets/mobs/balloom/mob4.png", 32, 32);

        guideItem = new GuidePlayer(getX(), getY(), ITEMSHOW);
        guideItem.setList("/assets/something/list3.png");
        guideItem.setIcon("/assets/items/item1.png", 32, 32);
    }

    public void draw(GraphicsContext render) {
        baseGuide.draw(render);
        if (STATUS == MAINGUIDE) {
            playerInfo.draw(render);
            mobInfo.draw(render);
            itemInfo.draw(render);
            render.setFont(PlayWindow.HOMEFONT);
            render.setFill(Color.web("262c40"));
            render.fillText("GUIDE", getX() + 225, getY() + 110);
            render.fillText("Highscore", getX() + 205, getY() + 220);
            render.fillText("NORMAL:", getX() + 130, getY() + 250);
            render.fillText(Game.getHighScoreNormal() + "",
                    getX() + 260, getY() + 250);
            render.fillText("AVENTURE:", getX() + 130, getY() + 280);
            render.fillText(Game.getHighScoreAventure() + "",
                    getX() + 260, getY() + 280);
        } else if (STATUS == PLAYERSHOW) {
            guidePlayer.draw(render);
        } else if (STATUS == MOBSHOW) {
            guideMob.draw(render);
        } else if (STATUS == ITEMSHOW) {
            guideItem.draw(render);
        }

        if (STATUS != MAINGUIDE) {
            backPage.draw(render);
        }
    }

    @Override
    public void setY(double y) {
        super.setY(y);
        baseGuide.setY(getY() + 70);
        playerInfo.setY(getY() + 130);
        mobInfo.setY(getY() + 130);
        itemInfo.setY(getY() + 130);
        guidePlayer.setY(getY());
        guideMob.setY(getY());
        guideItem.setY(getY());
        backPage.setY(getY() + 70);
    }

    public void addEvent(Scene scene) {
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, (e) -> {
            playerInfo.setHover(e);
            mobInfo.setHover(e);
            itemInfo.setHover(e);
            backPage.setHover(e);
        });
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            if (Game.STATUS == Status.guide && STATUS == MAINGUIDE) {
                if (playerInfo.isClick(e)) {
                    STATUS = PLAYERSHOW;
                }
                if (mobInfo.isClick(e)) {
                    STATUS = MOBSHOW;
                }
                if (itemInfo.isClick(e)) {
                    STATUS = ITEMSHOW;
                }
            } else if (Game.STATUS == Status.guide) {
                if (backPage.isClick(e)) {
                    STATUS = MAINGUIDE;
                }
            }
        });
        guidePlayer.addEvent(scene);
        guideMob.addEvent(scene);
        guideItem.addEvent(scene);

    }

    public void resetStatus() {
        STATUS = MAINGUIDE;
    }
}
