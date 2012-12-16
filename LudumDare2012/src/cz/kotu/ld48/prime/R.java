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

    public final static Images id = new Images();
    public final static Sounds sound = new Sounds();

    private R() {
        // singleton
    }

    static class Images {
        final Image villain = loadImage("/images/villain.png");
        final Image villain_crash = loadImage("/images/villain_crash.png");

        final Image moving_obstacle = loadImage("/images/moving_obstacle.png");

        final Image hud = loadImage("/images/hud.png");


        final Image road_patch = loadImage("/images/road_patch_0.png");
        final Image road_patch_1 = loadImage("/images/road_patch_1.png");
        final Image road_patch_2 = loadImage("/images/road_patch_2.png");
        final Image road_patch_3 = loadImage("/images/road_patch_3.png");
        final Image road_patch_4 = loadImage("/images/road_patch_4.png");


        final Image tree = loadImage("/images/tree.png");
        final Image tree_palm = loadImage("/images/tree_palm.png");
        final Image tree_snow = loadImage("/images/tree_snow.png");
        final Image tree_sm = loadImage("/images/tree_snowman.png");

        final Image intro1 = loadImage("/images/intro1.png");
        final Image score = loadImage("/images/end_score.png");

        final Image guy = loadImage("/images/villain_guy.png");

        final Image bank = loadImage("/images/start_bank.png");

        final Image goat = loadImage("/images/bonus_goat.png");
        final Image blood = loadImage("/images/bonus_blood.png");
        final Image goat_ghost = loadImage("/images/bonus_goat_plus_1.png");


        private BufferedImage loadImage(String name) {
            try {
                return ImageIO.read(getClass().getResource(name));
            } catch (IOException e) {
                Game.logException(e);
                throw new RuntimeException(e);
            }
        }
    }

    static class Sounds {


        final Sound crash = loadSound("/sounds/crash.wav");

        final Sound bonus = loadSound("/sounds/bonus.wav");


        private Sound loadSound(String name) {
            try {
                AudioInputStream audioInputStream = null;
//        audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(name));
                audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(name));

                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                return new Sound(clip);
            } catch (Exception e) {
                Game.logException(e);
                throw new RuntimeException(e);
            }

        }

    }

}
