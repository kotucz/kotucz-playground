package cz.kotu.ld48.prime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.util.EnumSet;

/**
 * @author Kotuc
 */
public class GamePanel extends JPanel {


//    private final BufferStrategy bufferStrategy;
//    private JFrame jFrame;

    enum Key {
        ELSE,
        UP,
        DOWN
    }

    EnumSet<Key> downKeys = EnumSet.noneOf(Key.class);

//    public static final String ACTION_MAP_KEY_UP = "up";


    final Game game = new Game(this
    );


    public GamePanel(JFrame jFrame) {
        super(true);
//        this.jFrame = jFrame;
//        jFrame.setIgnoreRepaint(true);
//        jFrame.createBufferStrategy(2);
//        bufferStrategy = jFrame.getBufferStrategy();

        setPreferredSize(new Dimension(640, 480));
        setSize(640, 480);

//        getInputMap().put(KeyStroke.getKeyStroke('w'), ACTION_MAP_KEY_UP);
//        getActionMap().put(ACTION_MAP_KEY_UP, new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                downKeys.add(toKey(e));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                downKeys.remove(toKey(e));
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                    case KeyEvent.VK_P:
                    case KeyEvent.VK_PAUSE:
//                        System.exit(0);
                        if (game.state == Game.State.PAUSE) {
                            game.state = Game.State.PLAY;
                        } else if (game.state == Game.State.PLAY) {
                            game.state = Game.State.PAUSE;
                        }
                        break;

                    case KeyEvent.VK_R:
                        game.reset();
                        break;
                    case KeyEvent.VK_SPACE:
                        // any key
                    default:
                        if (game.state == Game.State.INTRO) {
                            game.state = Game.State.START;
                        } else if (game.isCrashed()) {
                            // wait for 5 seconds
//                            if (game.animtimeaftercrash > Game.WAIT_AFTER_CRASH) {
                            if (game.isScoreAnimFinished()) {
                                game.reset();
                            }
//                            game.state = Game.State.START;
                        } else if (game.state == Game.State.PAUSE) {
                            game.state = Game.State.PLAY;
                        }
                        break;
                }
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
//        System.out.println("paint");

        Graphics2D g = (Graphics2D) g1;
        game.paint(g);

        final int width = getWidth();
        final int height = getHeight();

//        g.draw(new Rectangle2D.Double(2, 2, width - 4, height - 4));
//
//        g.drawString((downKeys.contains(Key.UP) ? "UP" : "up"), 50, 50);
//        g.drawString((downKeys.contains(Key.DOWN) ? "DOWN" : "down"), 50, 75);
//        g.drawString((downKeys.contains(Key.ELSE) ? "ELSE" : "else"), 50, 100);


    }


    public void redraw() {
        repaint();
//        Graphics g = bufferStrategy.getDrawGraphics();
//        paint(g);
//        g.dispose();
//        bufferStrategy.show();
    }

}
