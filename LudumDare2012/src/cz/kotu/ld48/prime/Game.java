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

    enum State {
        INTRO,
        PLAY,
        PAUSE,
        CRASH
    }

    State state;

    public static final int MAX_Y_VELOCITY = 2;

    double PIXELS_PER_METER = 80;
    double INV_PIXELS_PER_METER = 1.0/80;

    final Random random = new Random();

    List<Entity> ents = new LinkedList<Entity>();
    List<Entity> colidables = new LinkedList<Entity>();

    final GamePanel gamePanel;

    final Rectangle2D.Double view = new Rectangle2D.Double(0, 0, 640, 480);

    Entity villain;

//    boolean crashed;

    double distance; // m
    double speed; // mps
    double vy;

    double crashDistance;

    double[] nextdistance;

    public Game(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    void reset() {
        state = State.INTRO;
        villain = new Entity(R.id.villain, 0, 2, 2, 1);
        speed = 0;
        distance = 0;
        crashDistance = 0;
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
        g.scale(PIXELS_PER_METER, PIXELS_PER_METER);
        g.translate(-view.x, -view.y);

//        System.out.println("" + width + " x " + height);

        g.setStroke(new BasicStroke(0.01f));

        for (int i = -1; i < 20; i++) {

            AffineTransform tf = new AffineTransform();
            tf.translate(Math.round(view.x)+ i, 0);
            tf.scale(INV_PIXELS_PER_METER, INV_PIXELS_PER_METER);
            g.drawImage(R.id.road_patch, tf, null);
//            g.draw(ent.rect);
        }

        for (Entity ent : ents) {
            AffineTransform tf = new AffineTransform();
            tf.translate(ent.rect.x, ent.rect.y);
            tf.scale(INV_PIXELS_PER_METER, INV_PIXELS_PER_METER);
            g.drawImage(ent.image, tf, null);
            g.draw(ent.rect);
        }


        AffineTransform tf = new AffineTransform();
        tf.translate(villain.rect.x, villain.rect.y);
        tf.scale(INV_PIXELS_PER_METER, INV_PIXELS_PER_METER);
        tf.rotate(vy * 0.1, 1, 0.5);
        if (isCrashed()) {
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
        g.setColor(Color.gray);
//        g.fill(new Rectangle2D.Double(1, 1, 640 - 2, 80 - 2));
        g.drawImage(R.id.hud, 0, 0, null);

        g.setColor(Color.green);
        g.setStroke(new BasicStroke(3));
        g.setFont(Font.decode("Courier New bold 24"));

        g.drawString("distance: " + (int) distance + " m", 25, 25);
        g.drawString("speed: " + (int) speed + " m", 425, 25);
        g.drawString("state: " + state, 25, 50);

        if (isCrashed()) {
            g.drawString("total: " + (int) crashDistance + " m", 225, 25);
        }

        g.setTransform(original);
    }


    void delta(final double dt) {

        distance += dt * speed;

        villain.rect.x += dt * speed;

        if (isCrashed()) {
            speed -= (speed * dt);
            vy -= (vy * dt);
        } else if (state == State.PLAY) {
            if (speed < 5) {
                // accelerate fast
                speed += dt * 1;
            } else {
                // accelerate slowly
                speed += dt * 0.01;
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

            for (Entity ent : colidables) {
                if (villain.rect.intersects(ent.rect)) {
                    crashDistance = distance;
                    state = State.CRASH;
                    R.id.crash.play();
                    break;
                }
            }

        }


        if (!isCrashed()) {
            view.x = distance - 0.5;
        }

        if (state == State.PLAY || state == State.CRASH) {

            villain.rect.y += dt * vy;
            villain.rect.y = Math.max(1, Math.min(villain.rect.y, 3));


        }

        genEnviromentUpTo(distance + 10);

    }

    private void genEnviromentUpTo(final double GEN_DIST) {

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

    boolean isCrashed() {
        return state == State.CRASH;
    }


}
