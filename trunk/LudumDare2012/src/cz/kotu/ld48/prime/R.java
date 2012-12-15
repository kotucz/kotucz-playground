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

    Image villain;
    Image villain_crash;

    Image moving_obstacle;


    Image tree;


    void loadImages() {

        try {
            villain = loadImage("/images/villain.png");
            villain_crash = loadImage("/images/villain_crash.png");

            moving_obstacle = loadImage("/images/moving_obstacle.png");

            tree = loadImage("/images/tree.png");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private BufferedImage loadImage(String name) throws IOException {
        return ImageIO.read(getClass().getResourceAsStream(name));
    }



    Sound crash;

    void loadSounds() {


        try {
            crash = loadSound("/sounds/crash.wav");
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (LineUnavailableException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    private Sound loadSound(String name) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = null;
        audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(name));

        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        return new Sound(clip);

    }


}
