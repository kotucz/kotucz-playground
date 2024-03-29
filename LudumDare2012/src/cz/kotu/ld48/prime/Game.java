package cz.kotu.ld48.prime;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Kotuc
 */
public class Game {

    public static final boolean DEBUG = false;

    public static final double MIN_CAR_Y = 3.25;
    private static final double MAX_CAR_Y = 6.25;
    public static final int MAX_CAR_LINES = 4;

    public static final double CAR_W = 1;
    public static final double CAR_H = 0.5;

    public static final int START_DUR_T = 2;
    public static final int GEN_AHEAD_M = 20;


    public static final double CAR_WIEV_SHIFT = 2;
    public static final double WAIT_AFTER_CRASH = 3;
    public static final double GUY_START_Y = 1;

    public boolean isScoreAnimFinished() {
        return goats <= scoregoats && (animtimeaftercrash > WAIT_AFTER_CRASH);
    }

    enum State {
        INTRO,
        START,
        PLAY,
        PAUSE,
        CRASH
    }

    State state;

    public static final int MAX_Y_VELOCITY = 5;

    final static double PIXELS_PER_METER = 50;
    // inv pixels per meter in the image
    final static double INV_PIXELS_PER_METER = 1.0 / 80;

    final Random random = new Random();

    List<Entity> ents = new LinkedList<Entity>();
    List<Entity> colidables = new LinkedList<Entity>();

    final GamePanel gamePanel;

    final Rectangle2D.Double view = new Rectangle2D.Double(0, 0, 640, 480);

    Entity villain = new Entity(R.id.villain, 0, 2, CAR_W, CAR_H);
    Entity guy = new Entity(R.id.guy, 1, GUY_START_Y, .5, 1);

//    boolean crashed;

    double distance; // m
    double speed; // mps
    double vy;

    double crashDistance;

    // time from 0 start animaiton
    double startanimtime;
    double animtimeaftercrash;


    double animtimeaftercrashscoreadded;

    double[] nextdistance;

    int goats;
    int scoregoats;

    public Game(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

    }

    Image[] road_types = new Image[]{
            R.id.road_patch,
            R.id.road_patch_1,
            R.id.road_patch_2,
            R.id.road_patch_3,
            R.id.road_patch_4,
    };

    Image[] car_types = new Image[]{
            R.id.moving_obstacle,
            R.id.car_small,
    };


    void reset() {
        state = State.INTRO;
        villain = new Entity(R.id.villain, 0, MIN_CAR_Y, CAR_W, CAR_H);
        speed = 0;
        distance = 0;
        crashDistance = 0;
        ents.clear();
        colidables.clear();
        nextdistance = new double[5];
        nextdistance[0] = 5;
        nextdistance[1] = 5;
        nextdistance[2] = 50;
        startanimtime = 0;
        animtimeaftercrash = 0;
        goats = 0;
//        goats = 50;
        scoreTotal = 0;
        animtimeaftercrashscoreadded = 2;
        scoregoats = 0;

        {
            Entity bank = new Entity(R.id.bank, 0, 0, 8, 6);
            ents.add(bank);
        }


    }

    // main game loop
    public void loop() {

        reset();

        long lastStamp = 0;

        try {

            while (true) {


                long stamp = System.currentTimeMillis();
                double dt = (stamp - lastStamp) / 1000.0;
                lastStamp = stamp;

                // Max step 0.1 s for lags
                dt = Math.min(dt, 0.1);

                if (state == State.PAUSE) {
                    delta(0);
                } else {
                    delta(dt);
                }

                gamePanel.redraw();

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

        } catch (Exception ex) {
            logException(ex);
        }


    }

    static void logException(Exception ex) {
        try {
            ex.printStackTrace(new PrintStream("err" + System.currentTimeMillis() + ".txt"));
        } catch (FileNotFoundException e) {
            // nothing to do here
        }
//        JOptionPane.showMessageDialog(gamePanel, ex.toString());
    }


    public synchronized void paint(Graphics2D g) {

        AffineTransform original = g.getTransform();
        g.scale(PIXELS_PER_METER, PIXELS_PER_METER);
        g.translate(-view.x, -view.y);

//        System.out.println("" + width + " x " + height);

        g.setStroke(new BasicStroke(0.01f));

        for (int i = -1; i < 20; i++) {

            AffineTransform tf = new AffineTransform();
            long d = Math.round(view.x) + i;
            tf.translate(d, 0);
            tf.scale(INV_PIXELS_PER_METER, INV_PIXELS_PER_METER);

            g.drawImage(road_types[getType(d)], tf, null);
//            g.drawImage(R.id.road_patch, tf, null);
//            g.draw(ent.rect);
        }

        for (Entity ent : ents) {
//            AffineTransform tf = new AffineTransform();
//            tf.translate(ent.rect.x, ent.rect.y);
//            tf.scale(INV_PIXELS_PER_METER, INV_PIXELS_PER_METER);
//            g.drawImage(ent.image, tf, null);
//            g.draw(ent.rect);
            ent.drawCentered(g);
        }

        if (State.START == state) {
            guy.draw(g);
        }


//        AffineTransform tf = new AffineTransform();
//        tf.translate(villain.rect.x, villain.rect.y);
//        tf.scale(INV_PIXELS_PER_METER, INV_PIXELS_PER_METER);
//        tf.rotate(vy * 0.1, 1, 0.5);
        villain.image = isCrashed() ? R.id.villain_crash : R.id.villain;
        villain.drawCentered(g, vy * 0.03);
//        g.draw(villain.rect);

        g.setTransform(original);


        paintHUD(g);

        if (state == State.INTRO) {
            g.drawImage(R.id.intro1, 0, 0, null);
        }

        if (state == State.PAUSE) {
            g.drawImage(R.id.pause, 0, 0, null);
        }

        // draw score
        if (state == State.CRASH) {
            drawScore(g);
        }

    }

    int scoreTotal;

    private void drawScore(Graphics2D g) {
        g.drawImage(R.id.score, 0, 0, null);

        g.setColor(Color.red);
        g.setStroke(new BasicStroke(3));
        g.setFont(Font.decode("Arial bold 48"));

        FontMetrics fontMetrics = g.getFontMetrics();

        String s1 = "" + distMetres();
        g.drawString(s1, 545 - fontMetrics.stringWidth(s1), 250);
        String s2 = "" + scoregoats;
        g.drawString(s2, 545 - fontMetrics.stringWidth(s2), 295);

        g.setColor(Color.green);
        String s3 = "" + distMetres() * (scoregoats + 1);
        g.drawString(s3, 545 - fontMetrics.stringWidth(s3), 345);

        g.setColor(Color.red);
        if (isScoreAnimFinished()) {
            g.setFont(Font.decode("Arial bold 24"));
            g.drawString("press any key", 240, 450);
        }
    }

    private int getType(double d) {
        return ((int) d / 100) % road_types.length;
//        return 0;
    }


    public void paintHUD(Graphics2D g) {
        AffineTransform original = g.getTransform();


        g.translate(0, 400);
//        g.setColor(Color.gray);
//        g.fill(new Rectangle2D.Double(1, 1, 640 - 2, 80 - 2));
        g.drawImage(R.id.hud, 0, 0, null);

        g.setColor(new Color(0xe84d00));
        g.setStroke(new BasicStroke(3));
        g.setFont(Font.decode("Arial bold 34"));

        FontMetrics fontMetrics = g.getFontMetrics();

        String s0 = "" + distMetres();
        g.drawString(s0, 150 - fontMetrics.stringWidth(s0), 65);
        String s1 = "" + (int) (speed * 10);
        g.drawString(s1, 450 - fontMetrics.stringWidth(s1), 65);
        String s2 = "" + goats;
        g.drawString(s2, 583 - fontMetrics.stringWidth(s2) / 2, 65);
        if (DEBUG) {
            g.drawString("state: " + state, 25, 50);
        }

        double sprd = asympt(speed / 5) * Math.PI;
        g.draw(new Line2D.Double(275, 70, 275 - 65 * Math.cos(sprd), 70 - Math.sin(sprd) * 45));

//        if (isCrashed()) {
//            g.drawString("total: " + (int) crashDistance + " m", 225, 25);
//        }

        g.setTransform(original);
    }

    private int distMetres() {
        return (int) (10 * distance / 3.6);
    }


    synchronized void delta(final double dt) {

        System.out.println("delta " + dt);
        if (state == State.START) {

            if (startanimtime > START_DUR_T) {
                state = State.PLAY;
            }

            startanimtime += dt;
            guy.rect.y = GUY_START_Y + startanimtime;


        }


        distance += dt * speed;

        villain.rect.x += dt * speed;

        if (isCrashed()) {
            speed -= (speed * dt);
            vy -= (vy * dt);
            animtimeaftercrash += dt;

            if (animtimeaftercrashscoreadded < animtimeaftercrash) {
                if (scoregoats < goats) {
                    scoregoats++;
                    R.sound.bonus.play();
                }
                animtimeaftercrashscoreadded += 3.0/(3+scoregoats);
            }

        } else if (state == State.PLAY) {
            if (speed < 5) {
                // accelerate fast
                speed += dt * 1;
            } else {
                // accelerate slowly
                speed += dt * 0.1;
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

        }
        for (Entity ent : colidables) {

            if (isCar(ent)) {
                ent.rect.x += dt * (2 + 3 - ent.rect.y / 3);

            }

            if (ent.image == R.id.goat) {
                ent.rect.y += dt * (random.nextDouble() - 0.3);
            }

            if (villain.rect.intersects(ent.rect)) {
                if (isCar(ent)) {
                    if (!isCrashed()) {
                        crashDistance = distance;
                    }
                    state = State.CRASH;
                    R.sound.crash.play();
                    ent.image = R.id.villain_crash;
                    break;
                } else if (ent.image == R.id.goat) {
                    ent.image = R.id.blood;
                    R.sound.bonus.play();
                    goats++;
                    ents.add(createGoatGhost(ent.rect.x, ent.rect.y));
                }
            }
        }

        for (Entity ent : ents) {
            if (ent.image == R.id.goat_ghost) {
                ent.rect.y -= dt;
            }
        }


        if (!isCrashed()) {
            view.x = distance - CAR_WIEV_SHIFT;
        }

        if (state == State.PLAY || state == State.CRASH) {

            villain.rect.y += dt * vy;
            villain.rect.y = Math.max(MIN_CAR_Y, Math.min(villain.rect.y, MAX_CAR_Y));


        }

        genEnviromentUpTo(distance + GEN_AHEAD_M);

    }

    private void genEnviromentUpTo(final double GEN_DIST) {

        // OBSTACLES
        if (nextdistance[0] < GEN_DIST) {
            Entity e = createCar(nextdistance[0], MIN_CAR_Y + random.nextInt(MAX_CAR_LINES));
            nextdistance[0] += 5 + random.nextDouble() * 10;
//            if (e.rect.x < 0 ||)
            ents.add(e);
            colidables.add(e);
        }

        if (nextdistance[1] < GEN_DIST) {
            Entity e = createTree(nextdistance[1], random.nextDouble() * 2);
            nextdistance[1] += random.nextDouble();
            ents.add(e);
        }

        // GOATS

        if (nextdistance[2] < GEN_DIST) {
            Entity e = createGoat(nextdistance[2], 1 + random.nextDouble() * 5);
            nextdistance[2] += 30 + random.nextDouble() * 30;
            ents.add(e);
            colidables.add(e);
        }
        // TODO delete passed ents
    }

    private Entity createTree(double x, double y) {
        Image img = R.id.tree;
        switch (getType(x)) {
            case 1:
                img = R.id.tree_palm;
                break;
            case 4:
                if (random.nextDouble() < 0.1) {
                    img = R.id.tree_sm;
                } else {
                    img = R.id.tree_snow;
                }
                break;
        }
        Entity e = new Entity(img, x, y, 1, 1);
        return e;
    }

    private Entity createCar(double x, double y) {
        Entity e = new Entity(car_types[random.nextInt(car_types.length)], x, y, CAR_W, CAR_H);
        return e;
    }

    private Entity createGoat(double x, double y) {
        Entity e = new Entity(R.id.goat, x, y, 0.4, 0.8);
        e.scale = 1 / 60.0;
        return e;
    }

    private Entity createGoatGhost(double x, double y) {
        Entity e = new Entity(R.id.goat_ghost, x, y, 0.4, 0.8);
        e.scale = 1 / 60.0;
        return e;
    }

    boolean isCrashed() {
        return state == State.CRASH;
    }

    /**
     * @param d
     * @return 0..1
     */
    double asympt(double d) {
        return Math.atan(d) / (Math.PI / 2);
    }

    boolean isCar(Entity ent) {
        for (Image car_type : car_types) {
            if (car_type == ent.image) {
                return true;
            }
        }
        return false;
    }


}
