package cz.kotu.ld48.prime;

/**
 * @author Kotuc
 */
public class Game {

    final GamePanel gamePanel;

    double x = 0;
    double y = 0;
    boolean up = false;


    public Game(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // main game loop
    public void loop() {

        long lastStamp = 0;

        while (true) {



            double dt = (System.currentTimeMillis() - lastStamp) / 1000.0;

            // Max step 0.1 s for lags
            dt = Math.min(dt, 0.1);

            delta(dt);

            gamePanel.repaint();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    void delta(double dt) {

        x += dt;

        if (gamePanel.downKeys.contains(GamePanel.Key.UP)) {
            y -= dt;
        } else if (gamePanel.downKeys.contains(GamePanel.Key.DOWN)) {
            y += dt;
        } else {
//            y += dt;
        }



    }


}
