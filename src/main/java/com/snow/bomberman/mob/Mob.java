package com.snow.bomberman.mob;

import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.block.Block;
import com.snow.bomberman.setting.Setting;
import com.snow.bomberman.something.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioClip;

import java.net.URISyntaxException;

public abstract class Mob {
    protected boolean survival;
    protected double x;
    protected double y;
    protected Block objBlock;
    protected boolean deading;
    private boolean flamepass;

    public void draw(GraphicsContext render) { };

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isDeading() {
        return deading;
    }

    public Point getPoint() {
        return new Point(x, y);
    }


    public boolean isSurvival() {
        return survival;
    }

    public void setSurvival(boolean survival) {
        this.survival = survival;
    }

    public Block getObjBlock() {
        return objBlock;
    }

    public void setObjBlock(Block objBlock) {
        this.objBlock = objBlock;
    }

    public boolean isFlamepass() {
        return flamepass;
    }

    public void setFlamepass(boolean flamepass) {
        this.flamepass = flamepass;
    }

    protected void isLastEnemy() {
        try {
            while (PlayWindow.BONUSLIFE >= 10000) {
                PlayWindow.LIFE ++;
                PlayWindow.BONUSLIFE -= 10000;
            }
            boolean hasEnemy = false;
            for (int i = 1; i < Mobs.MOBS.size(); i++) {
                if (!Mobs.MOBS.get(i).isDeading()) {
                    hasEnemy = true;
                }
            }
            if (!hasEnemy) {
                AudioClip audioClip = new AudioClip(getClass().getResource("/assets/audio/clear.wav").toURI().toString());
                audioClip.setVolume(Setting.getEFFECTVOLUME());
                audioClip.play();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

