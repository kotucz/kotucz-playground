package cz.kotu.ld48.prime;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumSet;

/**
 * @author Kotuc
 */
public class GamePanel extends JPanel {

    private Image villain;
    private GamePanel.Sound crash;


    enum Key {
        ELSE,
        UP,
        DOWN
    }

    EnumSet<Key> downKeys = EnumSet.noneOf(Key.class);

    public static final String ACTION_MAP_KEY_UP = "up";


    final Game game = new Game(this
    );


    public GamePanel() {
        super(true);
        setPreferredSize(new Dimension(640, 480));
        setSize(640, 480);

        getInputMap().put(KeyStroke.getKeyStroke('w'), ACTION_MAP_KEY_UP);
        getActionMap().put(ACTION_MAP_KEY_UP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                crash.play();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                downKeys.add(toKey(e));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                downKeys.remove(toKey(e));
            }
        });

        setFocusable(true);
        requestFocus();
    }

    private Key toKey(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                return Key.UP;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                return Key.DOWN;
            default:
                return Key.ELSE;

        }
    }


    @Override
    public void paint(Graphics g1) {
        super.paint(g1);

        Graphics2D g = (Graphics2D) g1;

        final int width = getWidth();
        final int height = getHeight();

//        System.out.println("" + width + " x " + height);

        g.drawImage(villain, (int) (game.x % width), (int) (game.y % height), null);

        g.draw(new Rectangle2D.Double(2, 2, width - 4, height - 4));

        g.draw(new Rectangle2D.Double(game.x % width, game.y % height, 20, 20));


        g.drawString((downKeys.contains(Key.UP) ? "UP" : "up"), 50, 50);
        g.drawString((downKeys.contains(Key.DOWN) ? "DOWN" : "down"), 50, 75);
        g.drawString((downKeys.contains(Key.ELSE) ? "ELSE" : "else"), 50, 100);


    }

    void loadImages() {

        try {
            villain = loadImage("/villain.png");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private BufferedImage loadImage(String name) throws IOException {
        return ImageIO.read(getClass().getResourceAsStream(name));
    }

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


    class Sound{
        private Clip clip;

        public Sound(Clip clip) {
            this.clip = clip;
        }

        void play() {
            // TODO volume mute
//            if (volume != Volume.MUTE) {
                if (clip.isRunning())
                    clip.stop();   // Stop the player if it is still running
                clip.setFramePosition(0); // rewind to the beginning
                clip.start();     // Start playing
//            }
        }
    }


}
