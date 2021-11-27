package com.snow.bomberman.music;

import com.snow.bomberman.Game;
import com.snow.bomberman.HomeWindow;
import com.snow.bomberman.PlayWindow;
import com.snow.bomberman.button.Button;
import com.snow.bomberman.setting.Setting;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.net.URISyntaxException;
import java.util.LinkedList;

public class MusicPlayer {
    private static LinkedList<Media> media = null;
    private Button playMusic;
    private Button pauseMusic;
    private Button leftMusic;
    private Button rightMusic;
    private Button musicLabel;
    private Button volumeButton;
    private Button volumeMuteButton;
    private MediaPlayer mediaPlayer;
    private boolean playing;
    private int index;
    private String remove;

    public static Runnable SETVOLUME;

    public static void init() {
        if (media == null) {
            try {
                media = new LinkedList<>();
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/Numb_Vinsmoker.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/2_Phut_Hon.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/Levi_AMV.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/Hestisztd.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/No_Friends.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/I_Want_You_To_Know_Remix.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/Rise_Up.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/Plain_Jane.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/That_Girl_Remix.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/Lost_Sky.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/Warriyo.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/Ai_No.mp3").toURI().toString()));
                media.add(new Media(MusicPlayer.class.getResource("/assets/music/Nightcore_Centuries.mp3").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public MusicPlayer() {
        index = 0;
        try {
            init();
           /* media.add(new Media(getClass().getResource("/assets/music/Yen_Vo_Hiet_Remix.mp3").toURI().toString()));*/
            remove = getClass().getResource("/assets/music/").toURI().toString();
            playMusic = new Button("");
            playMusic.setButtonImg("/assets/something/play.png");
            playMusic.setW(15);
            playMusic.setH(15);
            playMusic.setX(40);
            playMusic.setY(392);

            //---------------------------
            pauseMusic = new Button("");
            pauseMusic.setButtonImg("/assets/something/pause.png");
            pauseMusic.setW(15);
            pauseMusic.setH(15);
            pauseMusic.setX(40);
            pauseMusic.setY(392);

            //----------------------------
            volumeButton = new Button("");
            volumeButton.setButtonImg("/assets/something/volume.png");
            volumeButton.setW(15);
            volumeButton.setH(15);
            volumeButton.setX(450);
            volumeButton.setY(392);

            //----------------------------
            volumeMuteButton = new Button("");
            volumeMuteButton.setButtonImg("/assets/something/volume-mute.png");
            volumeMuteButton.setW(17);
            volumeMuteButton.setH(15);
            volumeMuteButton.setX(449);
            volumeMuteButton.setY(392);

            //----------------------------
            leftMusic = new Button("");
            leftMusic.setButtonImg("/assets/something/left.png");
            leftMusic.setW(15);
            leftMusic.setH(15);
            leftMusic.setX(15);
            leftMusic.setY(392);

            //----------------------------
            rightMusic = new Button("");
            rightMusic.setButtonImg("/assets/something/right.png");
            rightMusic.setW(15);
            rightMusic.setH(15);
            rightMusic.setX(65);
            rightMusic.setY(392);

            //----------------------------
            musicLabel = new Button();
            musicLabel.setX(95);
            musicLabel.setY(392);
            musicLabel.setColor(Color.WHITE);
            musicLabel.setFont(PlayWindow.MCFONT);
            musicLabel.setW(0);
            musicLabel.setH(0);
            musicLabel.setTextCenter(11);
            musicLabel.setRotage(0);
            mediaPlayer = new MediaPlayer(media.get(index));

            createMusic();
            SETVOLUME = () -> {
                mediaPlayer.setVolume(Setting.MUSICVOLUME.getValue());
            };

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void createMusic() {
        mediaPlayer = new MediaPlayer(media.get(index));
        setMusicLabel();
        mediaPlayer.setOnEndOfMedia(() -> {
            index ++;
            if (index >= media.size()) {
                index = 0;
            }
            createMusic();
        });
        if (playing) {
            mediaPlayer.setVolume(Setting.MUSICVOLUME.getValue());
            mediaPlayer.play();
        }
    }

    private void setMusicLabel(){
        musicLabel.setText(media.get(index).getSource()
                .replace(remove, "")
                .replace("_", " ")
                .replace(".mp3", ""));
    }

    public void draw(GraphicsContext render) {
        if (!playing) {
            playMusic.draw(render);
        } else {
            pauseMusic.draw(render);
        }
        leftMusic.draw(render);
        rightMusic.draw(render);
        musicLabel.draw(render);
        render.setImageSmoothing(true);
        if (Game.MUTEMUSIC) {
            volumeMuteButton.draw(render);
        } else {
            volumeButton.draw(render);
        }
        render.setImageSmoothing(!Setting.GRAPHICSHIGH.isChecked());
    }

    public void click(MouseEvent e) {
        if (playMusic.isClick(e)) {
            if (!playing) {
                mediaPlayer.setVolume(Setting.MUSICVOLUME.getValue());
                mediaPlayer.play();
                HomeWindow.muteMusic();
                playing = true;
            } else {
                mediaPlayer.pause();
                playing = false;
            }
        }
        if (leftMusic.isClick(e)) {
            index --;
            if (index < 0) {
                index = media.size() - 1;
            }
            if (mediaPlayer!= null) {
                mediaPlayer.stop();
            }
            createMusic();
        }

        if (rightMusic.isClick(e)) {
            index ++;
            if (index >= media.size()) {
                index = 0;
            }
            if (mediaPlayer!= null) {
                mediaPlayer.stop();
            }
            createMusic();
        }

        if (volumeButton.isClick(e) || volumeMuteButton.isClick(e)) {
            if (Game.MUTEMUSIC) {
                mediaPlayer.pause();
                playing = false;
                HomeWindow.resumeMusic();
            } else {
                HomeWindow.muteMusic();
            }
        }
    }
}
