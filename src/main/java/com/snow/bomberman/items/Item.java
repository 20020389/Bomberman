package com.snow.bomberman.items;

import com.snow.bomberman.Mode;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.block.Block;
import com.snow.bomberman.bomb.Bomb;
import com.snow.bomberman.mob.Mob;
import com.snow.bomberman.mob.Mobs;
import com.snow.bomberman.player.Bomberman;
import com.snow.bomberman.setting.Setting;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class Item extends Mob {
    private static LinkedList<Image> ITEM = null;
    public static boolean INIT = false;
    private Bomberman bomberman;
    public enum Effect{
        bombcontroll,
        limitbomb,
        flash,
        rangebomb,
        speedup,
        flamepass,
        findlifeondead,
        wallpass;

        public static Effect setValue(int value) {
            if (value == 1) {
                return bombcontroll;
            }
            if (value == 2) {
                return limitbomb;
            }
            if (value == 3) {
                return flash;
            }
            if (value == 4) {
                return rangebomb;
            }
            if (value == 5 && Bomberman.SPEED < 2.5) {
                return speedup;
            }
            if (value == 6) {
                return flamepass;
            }
            if (value == 7) {
                if (PlayWindow.FINDLIFEONDEAD) {
                    return limitbomb;
                }
                return findlifeondead;
            }
            if (value == 8) {
                if (Bomberman.canWallPass() && PlayWindow.getMODE() == Mode.aventure) {
                    return limitbomb;
                }
                return wallpass;
            }
            return limitbomb;
        }

        public int getValue() {
            if (this == bombcontroll) {
                return 0;
            }
            if (this == limitbomb) {
                return 1;
            }
            if (this == flash) {
                return 2;
            }
            if (this == rangebomb) {
                return 3;
            }
            if (this == speedup) {
                return 4;
            }
            if (this == flamepass) {
                return 5;
            }
            if (this == findlifeondead) {
                return 6;
            }
            return 7;
        }
    }
    Effect status;

    public static void init() {
        if (!INIT) {
            ITEM = new LinkedList<>();
            try {
                ITEM.add(new Image(Item.class.getResource("/assets/items/item1.png").toURI().toString()));
                ITEM.add(new Image(Item.class.getResource("/assets/items/item2.png").toURI().toString()));
                ITEM.add(new Image(Item.class.getResource("/assets/items/item7.png").toURI().toString()));
                ITEM.add(new Image(Item.class.getResource("/assets/items/item4.png").toURI().toString()));
                ITEM.add(new Image(Item.class.getResource("/assets/items/item5.png").toURI().toString()));
                ITEM.add(new Image(Item.class.getResource("/assets/items/item6.png").toURI().toString()));
                ITEM.add(new Image(Item.class.getResource("/assets/items/item8.png").toURI().toString()));
                ITEM.add(new Image(Item.class.getResource("/assets/items/item3.png").toURI().toString()));
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
            INIT = true;
        }
    }

    public Item(Effect status, double x, double y) {
        this.x = x;
        this.y = y;
        survival = true;
        objBlock = new Block(x, y, 32, 32);
        init();
        this.status = status;
    }

    public void draw(GraphicsContext render, boolean canMovein) {
        objBlock.update(x, y, 32, 32);
        bomberman = (Bomberman) Mobs.MOBS.get(0);
        render.drawImage(ITEM.get(status.getValue()), x, y);
        if (bomberman.getObjBlock().collision(objBlock) && canMovein) {
            AudioClip audioClip = null;
            try {
                audioClip = new AudioClip(Bomb.class.getResource("/assets/audio/getitem.wav").toURI().toString());
                audioClip.setVolume(Setting.getEFFECTVOLUME());
                audioClip.play();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
            setEffect();
            survival = false;
        }
    }

    private void setEffect() {
        if (status == Effect.bombcontroll) {
            if (PlayWindow.getMODE() == Mode.normal) {
                PlayWindow.BOMBCONTROLLED += 3;
            } else {
                PlayWindow.BOMBCONTROLLED += 10;
            }
        }
        if (status == Effect.limitbomb) {
            Bomberman.LIMITBOMB += 1;
        }
        if (status == Effect.wallpass) {
            Bomberman.WALLPASS = 3;
        }
        if (status == Effect.rangebomb) {
            Bomb.BOOMLEGHT += 1;
        }
        if (status == Effect.speedup) {
            Bomberman.SPEED += 0.5;
        }
        if (status == Effect.flamepass) {
            PlayWindow.FLAMEPASS ++;
        }
        if (status == Effect.flash) {
            PlayWindow.FLASH += 3;
        }
        if (status == Effect.findlifeondead) {
            PlayWindow.FINDLIFEONDEAD = true;
        }
    }

    public static void setEffect(Effect effect, int value) {
        if (effect == Effect.bombcontroll) {
            PlayWindow.BOMBCONTROLLED = value;
        }
        if (effect == Effect.limitbomb) {
            Bomberman.LIMITBOMB = value;
        }
        if (effect == Effect.wallpass) {
            Bomberman.WALLPASS = (value == 0) ? 2 : 3;
        }
        if (effect == Effect.rangebomb) {
            Bomb.BOOMLEGHT = value;
        }
        if (effect == Effect.speedup) {
            Bomberman.SPEED = Math.min(value, 30);
        }
        if (effect == Effect.flamepass) {
            PlayWindow.FLAMEPASS = value;
        }
        if (effect == Effect.flash) {
            PlayWindow.FLASH = value;
        }
    }

    public static int[] doItemAventure(boolean read) {
        int []item = new int[3];
        try {
            File file = new File(Paths.get("data/item.txt").toUri());
            if (read) {
                Scanner scanner = new Scanner(file);
                for (int i = 0; i < 3; i++) {
                    item[i] = scanner.nextInt();
                }
                scanner.close();
            } else {
                item[0] = PlayWindow.BOMBCONTROLLED;
                item[1] = PlayWindow.FLAMEPASS;
                item[2] = PlayWindow.FLASH;
                FileWriter fileWriter = new FileWriter(file);
                for (int i = 0; i < 3; i++) {
                    fileWriter.write(item[i] +  " ");
                }
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }
}
