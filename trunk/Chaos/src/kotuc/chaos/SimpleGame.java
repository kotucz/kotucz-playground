package kotuc.chaos;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class SimpleGame implements Runnable {

    protected Location location;

    public SimpleGame() {
        ChaosFrame app = new ChaosFrame();
        this.location = app.location;
    }

    @Override
    public void run() {
        simpleInitGame();
        while (true) {
            simpleUpdate();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimpleGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void simpleInitGame() {
    }

    protected void simpleUpdate() {
    }

    protected void start() {
        new Thread(this).start();
    }
}
