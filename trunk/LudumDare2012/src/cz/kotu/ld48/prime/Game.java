package cz.kotu.ld48.prime;

import java.awt.*;
import java.awt.geom.AffineTransform;
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

    Entity villain;


    double distance = 0; // m
    double speed = 2; // mps


    double nextdistance = 0;


    public Game(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        reset();
    }

    void reset() {
        villain = new Entity(R.id.villain, 0, 0, 2, 1);
        villain.rect.width = 1.5;
        villain.rect.height = 0.75;
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
        AffineTransform original = g.getTransform();
        g.scale(100, 100);
        g.translate(-view.x, -view.y);

//        System.out.println("" + width + " x " + height);

        g.setStroke(new BasicStroke(0.01f));

        for (Entity ent : ents) {
            AffineTransform tf = new AffineTransform();
            tf.translate(ent.rect.x, ent.rect.y);
            tf.scale(0.01, 0.01);
            g.drawImage(R.id.tree, tf, null);
            g.draw(ent.rect);
        }


        AffineTransform tf = new AffineTransform();
        tf.translate(villain.rect.x, villain.rect.y);
        tf.scale(0.01, 0.01);
        g.drawImage(R.id.villain, tf, null);
        g.draw(villain.rect);

        g.setTransform(original);

    }


    void delta(final double dt) {

        distance += dt * speed;

        villain.rect.x += dt * speed;

        view.x = distance - 0.5;

//        System.out.println("d "+dt+" "+distance);


        if (gamePanel.downKeys.contains(GamePanel.Key.UP)) {
            villain.rect.y -= dt;
        } else if (gamePanel.downKeys.contains(GamePanel.Key.DOWN)) {
            villain.rect.y += dt;
        } else {
//            y += dt;
        }


        double generatingDistance = distance + 10;
        if (nextdistance < generatingDistance) {
            Entity e = createTree(nextdistance, random.nextDouble() * 5);

            nextdistance += random.nextDouble()*10;

            ents.add(e);
        }

    }

    private Entity createTree(double x, double y) {
        Entity e = new Entity(R.id.tree, x, y, 1, 1);
        return e;
    }


}
