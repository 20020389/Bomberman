package com.snow.bomberman.info;


import com.snow.bomberman.Game;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.something.Time;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URISyntaxException;
import java.util.LinkedList;

import static com.snow.bomberman.PlayWindow.LIFE;

public class InfoPlayer{
    private final LinkedList<Image> infoPlayer;

    public InfoPlayer() {
        infoPlayer = new LinkedList<>();
        try {
            infoPlayer.add(new Image(getClass().getResource("/assets/info/avata2.png").toURI().toString()));
            infoPlayer.add(new Image(getClass().getResource("/assets/info/bom1.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext render) {
        double x = render.getCanvas().getLayoutX() / Game.ZOOM;
        render.setFill(Color.WHITE);
        render.drawImage(infoPlayer.get(0), -x + 30, 8, 19, 15);
        render.drawImage(infoPlayer.get(1), -x + 95, 7.5);
        render.setFont(PlayWindow.IVFONT);
        render.fillText(" x", -x + 52, 20);
        render.fillText(" x", -x + 114, 20);
        render.setFont(PlayWindow.MCFONT);
        String text = LIFE + "";
        render.fillText(text, -x + 65, 20);
        text = PlayWindow.BOMBCONTROLLED + "";
        render.fillText(text, -x + 127, 20);
        Text calc = new Text(PlayWindow.SCORE + "");
        calc.setFont(PlayWindow.MCFONT);
        render.fillText(calc.getText(),
                -x + 245.5 - calc.getBoundsInLocal().getWidth() / 2, 20);
        text = "TIME: " + Time.getTime() + "";
        render.fillText(text, -x + 350, 20);
        text = String.format("FPS: %.0f", Game.FPS);
        render.fillText(text, -x + 430, 20);
    }
}
