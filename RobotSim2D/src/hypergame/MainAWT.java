package hypergame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kotuc
 */
public class MainAWT {

    public static void main(String[] args) {

        final Game game = new Game();

        final ViewPanel viewPanel = new ViewPanel(game);

        game.initGame();

        Frame frame = new Frame("AWT Window Test");
        frame.setSize(700, 600);
        frame.add(viewPanel);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);


            }
        });
//        frame.addComponentListener(new ComponentAdapter() {
//
//            Point oldloc = null;
//
//            @Override
//            public void componentMoved(ComponentEvent e) {
//                Point nloc = e.getComponent().getLocation();
//                if (oldloc != null) {
//                    float vx = -(nloc.x - oldloc.x) * 0.1f;
//                    float vy = (nloc.y - oldloc.y) * 0.1f;
//                    game.shake(vx, vy);
//                }
//                oldloc = nloc;
//            }
//        });

        new Thread() {

            @Override
            public void run() {
                while (true) {

                    game.update();
                    viewPanel.repaint();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ViewPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();

    }

}
