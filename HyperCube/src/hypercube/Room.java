/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hypercube;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Kotuc
 */
public class Room {

    private int time;
    private List<Entity> entities = new LinkedList<Entity>();
    private int width = 640,  height = 480,  timeLoop = 20;
    private int gravity = 10;
    private Player player;
    private GamePanel panel;
    private TimeLine timeline = new TimeLine();

    public Room(GamePanel aThis) {
        time = 0;
        this.panel = aThis;
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                round();
            }
        }, 2000, 1000);
//        reset();
        init();
    }

    /*
     * add static things
     */
    public void init() {
        // optional
        entities.clear();

        Entity box = new Entity(this, 150, 150, 20, 20, Color.YELLOW);
        add(box);

        Entity floor = new Entity(this, 50, 200, 200, 20, Color.RED);
        floor.solid = true;
        add(floor);

        Player player0 = new Player(this);
        player0.x = 50;
        player0.y = 50;
        player0.color = Color.GREEN;
        timeline.addAction(5, createAppearAction(50, 50, player0));

    }
    private List<Entity> toAdd = new LinkedList<Entity>();

    public void add(Entity entity) {
        toAdd.add(entity);
    }

    public void paint(Graphics g) {

        synchronized (this) {

            g.setColor(Color.GRAY);
            g.fill3DRect(0, 0, width, height, true);
            g.setColor(Color.BLACK);
            g.draw3DRect(0, 0, width, height, true);

            for (Entity entity : entities) {
                entity.paint(g);
            }

            g.drawString("TIME: " + time, width - 60, height - 20);

        }

    }

    public int getGravity() {
        return gravity;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    private void round() {

        synchronized (this) {

            entities.addAll(toAdd);

            toAdd.clear();

//            for (Entity entity : entities) {
//                entity.act();
//            }

            for (TimeAction timeAction : timeline.getActions(time)) {
                timeAction.execute(null);
            }
            for (Entity entity : entities) {
                entity.act();
            }

            time++; // the most important thing :-p
            time %= timeLoop;
            panel.tiimeLabel.setText("TIME: " + time);
            panel.repaint();

        }
    }

    public void keyPressed(KeyEvent evt) {

        synchronized (this) {
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    player.addAction(player.time, player.createGoAction(-1));
                    break;
                case KeyEvent.VK_RIGHT:
                    player.addAction(player.time, player.createGoAction(1));
                    break;
                case KeyEvent.VK_UP:
                    player.addAction(player.time, player.createJumpAction());
                    break;
                case KeyEvent.VK_T:
                    final long change = 6;
                    player.addAction(player.time, player.createTimeTravellAction(change));
                    break;
                case KeyEvent.VK_R:
//                    restart();
                    break;
            }
        }
    }

    public TimeAction createAppearAction(final int x1, final int y1, final Entity entity) {
        return new TimeAction() {

            @Override
            public void execute(Entity enull) {
                Entity copy = entity.createCopy();
                // TODO FUJ FUJ !
                if (copy instanceof Player && player == null) {
                    player = (Player) copy;
                }
                add(copy);
            }
        };
    }
}
