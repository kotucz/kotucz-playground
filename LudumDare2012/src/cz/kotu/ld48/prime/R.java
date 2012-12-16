package cz.kotu.ld48.prime;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Kotuc
 */
public class R {

    public final static R id = new R();

    private R() {
        // singleton
    }

    Image villain;
    Image villain_crash;

    Image moving_obstacle;

    Image hud;

    Image road_patch;


    Image tree;


    void loadImages() {

        try {
            villain = loadImage("/images/villain.png");
            villain_crash = loadImage("/images/villain_crash.png");

            moving_obstacle = loadImage("/images/moving_obstacle.png");

            hud = loadImage("/images/hud.png");

            road_patch = loadImage("/images/road_patch.png");

            tree = loadImage("/images/tree.png");
        } catch (IOException e) {
            Game.logException(e);
        }

    }

    private BufferedImage loadImage(String name) throws IOException {
        return ImageIO.read(getClass().getResource(name));
    }



    Sound crash;

    void loadSounds() {


        try {
            crash = loadSound("/sounds/crash.wav");
        } catch (Exception e) {
            Game.logException(e);
        }


    }

    private Sound loadSound(String name) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = null;
//        audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(name));
        audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(name));

        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        return new Sound(clip);

    }


}
