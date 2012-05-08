package tradeworld;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tradeworld.graphics.ChaosFrame;

/**
 *
 * @author Kotuc
 */
public class Game {

    private List<Player> players = new LinkedList<Player>();
    private World world;
    private ChaosFrame chaosFrame;

    public Game() {
        world = new World();
        this.chaosFrame = new ChaosFrame(world);
    }

    private void gameStep() {
        world.gameStep();
        chaosFrame.updatePanels();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Game().start();
    }

    public void start() {
        new Thread() {

            @Override
            public void run() {
                while (true) {
                    gameStep();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();

    }
}
