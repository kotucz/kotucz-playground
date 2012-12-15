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

    public static final int MAX_Y_VELOCITY = 2;
    final Random random = new Random();

    List<Entity> ents = new LinkedList<Entity>();
    List<Entity> colidables = new LinkedList<Entity>();

    final GamePanel gamePanel;

    final Rectangle2D.Double view = new Rectangle2D.Double(0, 0, 640, 480);

    Entity villain;

    boolean crashed;

    double distance; // m
    double speed; // mps
    double vy;

    double crashDistance;

    double[] nextdistance;

    public Game(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    void reset() {
        villain = new Entity(R.id.villain, 0, 2, 2, 1);
        speed = 0;
        distance = 0;
        crashDistance = 0;
        crashed = false;
        ents.clear();
        colidables.clear();
        nextdistance = new double[5];
        nextdistance[0] = 10;
    }

    // main game loop
    public void loop() {

        reset();

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
            g.drawImage(ent.image, tf, null);
            g.draw(ent.rect);
        }


        AffineTransform tf = new AffineTransform();
        tf.translate(villain.rect.x, villain.rect.y);
        tf.scale(0.01, 0.01);
        if (crashed) {
            g.drawImage(R.id.villain_crash, tf, null);
        } else {
            g.drawImage(R.id.villain, tf, null);
        }
        g.draw(villain.rect);

        g.setTransform(original);

    }


    public void paintHUD(Graphics2D g) {
        AffineTransform original = g.getTransform();

        g.translate(0, 400);
        g.draw(new Rectangle2D.Double(2, 2, 640 - 4, 80 - 4));

        g.drawString("distance: " + (int) distance + " m", 25, 25);

        if (crashed) {
            g.drawString("total: " + crashDistance + " m", 125, 25);
        }

        g.setTransform(original);
    }


    void delta(final double dt) {

        distance += dt * speed;

        villain.rect.x += dt * speed;

        if (crashed) {
            speed -= (speed*dt);
        } else {
            if (speed<5) {
                // accelerate fast
                speed += dt*1;
            }  else {
                // accelerate slowly
                speed += dt*0.01;
            }
            view.x = distance - 0.5;
        }

//        System.out.println("d "+dt+" "+distance);




        if (gamePanel.downKeys.contains(GamePanel.Key.UP)) {
//            vy -= 10*dt;
            vy = -MAX_Y_VELOCITY;
        } else if (gamePanel.downKeys.contains(GamePanel.Key.DOWN)) {
//            vy += 10*dt;
            vy = MAX_Y_VELOCITY;
        } else {
//            vy -= (vy*dt);
            vy = 0;
        }
//        vy = Math.max(-MAX_Y_VELOCITY, Math.min(vy, MAX_Y_VELOCITY));
        villain.rect.y += dt*vy;
        villain.rect.y = Math.max(1, Math.min(villain.rect.y, 3));


        for (Entity ent : colidables) {
            if (villain.rect.intersects(ent.rect)) {
                crashDistance = distance;
                crashed = true;
            }
        }


        final double GEN_DIST = distance + 10;


        // OBSTACLES
        if (nextdistance[0] < GEN_DIST) {
            Entity e = createObstacle(nextdistance[0], random.nextDouble() * 5);
            nextdistance[0] += random.nextDouble() * 10;
            ents.add(e);
            colidables.add(e);
        }

        if (nextdistance[1] < GEN_DIST) {
            Entity e = createTree(nextdistance[1], random.nextDouble() * 1);
            nextdistance[1] += random.nextDouble();
            ents.add(e);
        }


        // TODO delete passed ents

    }

    private Entity createTree(double x, double y) {
        Entity e = new Entity(R.id.tree, x, y, 1, 1);
        return e;
    }

    private Entity createObstacle(double x, double y) {
        Entity e = new Entity(R.id.moving_obstacle, x, y, 2, 1);
        return e;
    }


}
