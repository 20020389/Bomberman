package com.snow.bomberman.something.textbox;

import com.snow.bomberman.Game;
import com.snow.bomberman.Mode;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.button.Button;
import com.snow.bomberman.items.Item;
import com.snow.bomberman.mob.Mob;
import com.snow.bomberman.mob.Mobs;
import com.snow.bomberman.mob.enemy.*;
import com.snow.bomberman.something.Control;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import static com.snow.bomberman.Game.ZOOM;

public class TextBox extends Control {
    private boolean showing;
    private String text;
    private String lastText;
    private boolean textChange;
    private int index;
    private int timeAnimation;
    private Button bg;
    private Font font;
    int[][] mapHash;
    private List<String> lastCommand;

    public TextBox(int[][] mapHash) {
        text = "/";
        lastText = text;
        textChange = false;
        timeAnimation = 0;
        showing = false;
        bg = new Button("");
        bg.setX(getX() + 75);
        bg.setY(getY() + 70);
        bg.setButtonImg("/assets/something/pausescreen1.png");
        lastCommand = new LinkedList<>();
        addEvent(Game.WINDOW);
        try {
            font = Font.loadFont(getClass().getResource("/assets/font/upheavtt.ttf").toURI().toString(), 17);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.mapHash = mapHash;
    }

    public boolean isShowing() {
        return showing;
    }

    public void draw(GraphicsContext render) {
        if (!lastText.equals(text)) {
            if (lastCommand.size() > 0) {
                index = lastCommand.size() - 1;
            }
        }
        double cameraX = - (render.getCanvas().getLayoutX() / ZOOM);
        bg.draw(render);
        render.setFont(font);
        render.fillText(text, cameraX + 100, 100);
        timeAnimation ++;
        Text check = new Text(text);
        check.setFont(font);
        if (timeAnimation < 20) {
            render.fillText("|", cameraX + 100 + check.getBoundsInLocal().getWidth(), 100);
        }
        if (timeAnimation >= 40) {
            timeAnimation = 0;
        }
    }

    public void addEvent(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (PlayWindow.getMODE() == Mode.training && isShowing()) {
                if (e.getCode().equals(KeyCode.ENTER) && text.length() > 0) {
                    if (commandDo()) {
                        index = lastCommand.size() - 1;
                        PlayWindow.PAUSE = false;
                        hide();
                    }
                }
                if (e.getCode().equals(KeyCode.UP)) {
                    if (lastCommand.size() > 0) {
                        text = lastCommand.get(index);
                        lastText = text;
                        if (index > 0) {
                            index --;
                        }
                    }
                }
                if (e.getCode().equals(KeyCode.DOWN)) {
                    if (lastCommand.size() > 0) {
                        if (index < (lastCommand).size() - 1) {
                            index ++;
                        }
                        text = lastCommand.get(index);
                        lastText = text;
                    }
                }
                if (e.getCode().equals(KeyCode.BACK_SPACE) && text.length() > 0) {
                    text = text.substring(0, text.length() - 1);
                }
                if (e.getCode().equals(KeyCode.SPACE)) {
                    if (text.length() < 30) {
                        text += " ";
                    }
                }
                String check = e.getCode().toString().replace("DIGIT", "");
                if (e.getCode().equals(KeyCode.SLASH)) {
                    if (text.length() < 30) {
                        text += '/';
                    }
                }
                if (check.length() == 1 && ((check.charAt(0) >= 65 && check.charAt(0) <= 90)
                   || (check.charAt(0) >= 47 && check.charAt(0) <= 57))) {
                    if (text.length() < 30) {
                        text += check;
                    }
                }
            }
        });
    }

    public void show() {
        showing = true;
    }

    public void hide() {
        showing = false;
        text = "/";
    }

    private boolean commandDo() {
        if (text.length() == 0) {
            return false;
        }
        if (text.charAt(0) != '/') {
            System.out.println("this is not a command!");
            return false;
        }
        if (text.equals("/CLEAR")) {
            List<Mob> mobs = Mobs.MOBS;
            for (int i = 1; i < mobs.size(); i++) {
                if (!mobs.get(i).isDeading()) {
                    mobs.get(i).setSurvival(false);
                }
            }
            lastCommand.add(text);
            return true;
        }
        String []cmd = text.split(" ");
        if (cmd.length < 2 || cmd.length > 3) {
            System.out.println("this is not a command!");
            return false;
        }
        if (cmd[0].equals("/SUMMON")) {
            if (cmd.length == 3) {
                int limit;
                try {
                    limit = Integer.parseInt(cmd[2]);
                } catch (Exception e) {
                    return false;
                }
                if (createEnemy(cmd[1], limit)) {
                    lastCommand.add(text);
                    return true;
                }

            }
            if (createEnemy(cmd[1], 1)) {
                lastCommand.add(text);
                return true;
            }
        }
        if (cmd[0].equals("/SET")) {
            if (cmd.length == 3) {
                int limit;
                try {
                    limit = Integer.parseInt(cmd[2]);
                } catch (Exception e) {
                    return false;
                }
                if (setItem(cmd[1], limit)) {
                    lastCommand.add(text);
                    return true;
                }
            }
            if (setItem(cmd[1], 1)) {
                lastCommand.add(text);
                return true;
            }
        }
        return false;
    }

    private boolean createEnemy(String name, int number) {
        if (name.equals("BALLOOM")) {
            for (int i = 0; i < number; i++) {
                Balloom balloom = new Balloom(15 * 32, 6 * 32);
                balloom.setMap(mapHash);
            }
            return true;
        }
        if (name.equals("ONEAL")) {
            for (int i = 0; i < number; i++) {
                Oneal balloom = new Oneal(15 * 32, 6 * 32);
                balloom.setMap(mapHash);
            }
            return true;
        }
        if (name.equals("DOLL")) {
            for (int i = 0; i < number; i++) {
                Doll balloom = new Doll(15 * 32, 6 * 32);
                balloom.setMap(mapHash);
            }
            return true;
        }
        if (name.equals("MINVO")) {
            for (int i = 0; i < number; i++) {
                Minvo balloom = new Minvo(15 * 32, 6 * 32);
                balloom.setMap(mapHash);
            }
            return true;
        }
        if (name.equals("KONDORIA")) {
            for (int i = 0; i < number; i++) {
                Kondoria balloom = new Kondoria(15 * 32, 6 * 32);
                balloom.setMap(mapHash);
            }
            return true;
        }
        if (name.equals("OVAPI")) {
            for (int i = 0; i < number; i++) {
                Ovapi balloom = new Ovapi(15 * 32, 6 * 32);
                balloom.setMap(mapHash);
            }
            return true;
        }
        if (name.equals("PASS")) {
            if (number > 3) {
                number = 3;
            }
            for (int i = 0; i < number; i++) {
                Pass balloom = new Pass(15 * 32, 6 * 32);
                balloom.setMap(mapHash);
            }
            return true;
        }
        return false;
    }

    public boolean setItem(String effect, int value) {
        if (effect.equals("BOMBCONTROLL")) {
            Item.setEffect(Item.Effect.bombcontroll, value);
            return true;
        }
        if (effect.equals("RANGEBOMB")) {
            Item.setEffect(Item.Effect.rangebomb, value);
            return true;
        }
        if (effect.equals("LIMITBOMB")) {
            Item.setEffect(Item.Effect.limitbomb, value);
            return true;
        }
        if (effect.equals("SPEED")) {
            Item.setEffect(Item.Effect.speedup, value);
            return true;
        }
        if (effect.equals("WALLPASS")) {
            Item.setEffect(Item.Effect.wallpass, value);
            return true;
        }
        if (effect.equals("FLASH")) {
            Item.setEffect(Item.Effect.flash, value);
            return true;
        }
        if (effect.equals("FLAMEPASS")) {
            Item.setEffect(Item.Effect.flamepass, value);
            return true;
        }
        return false;
    }
}
