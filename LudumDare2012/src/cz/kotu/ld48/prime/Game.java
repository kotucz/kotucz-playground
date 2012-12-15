package cz.kotu.ld48.prime;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Kotuc
 */
public class Game {

    final Random random = new Random();

    List<Entity> ents = new LinkedList<Entity>();

    final GamePanel gamePanel;

    final Rectangle2D.Double view = new Rectangle2D.Double(0, 0, 640, 480);

    final Entity villain = new Entity();

    double distance = 0; // m
    double speed = 2; // mps


    double nextdistance = 0;


    public Game(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // main game loop
    public void loop() {

        long lastStamp = 0;

        while (true) {


            long stamp = System.currentTimeMillis();
            double dt = (stamp - lastStamp) / 1000.0;
            lastStamp = stamp;

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


    public void paint(Graphics2D g) {
        g.translate(view.x, view.y);

//        System.out.println("" + width + " x " + height);

        for (Entity ent : ents) {
            g.drawImage(R.id.tree, (int) (ent.rect.x), (int) (ent.rect.y), null);
        }


        g.drawImage(R.id.villain, (int) (villain.rect.x), (int) (villain.rect.y), null);
//        g.draw();


    }


    void delta(final double dt) {

        distance += dt * speed;

        villain.rect.x += dt * speed;

//        System.out.println("d "+dt+" "+distance);


        if (gamePanel.downKeys.contains(GamePanel.Key.UP)) {
            villain.rect.y -= dt;
        } else if (gamePanel.downKeys.contains(GamePanel.Key.DOWN)) {
            villain.rect.y += dt;
        } else {
//            y += dt;
        }


        if (nextdistance < distance) {
            Entity e = new Entity();
            e.rect.x = random.nextDouble() * 1000;
            e.rect.y = random.nextDouble() * 1000;

            nextdistance += 10;

            ents.add(e);
        }

    }


}
