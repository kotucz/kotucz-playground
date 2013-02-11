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

    long time;
    private List<Entity> entities = new LinkedList<Entity>();
    private int width = 640,  height = 480;
    private int gravity = 10;
    Entity player;

    /**
     * 
     */
    public Room() {

        init();

        restart();

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                round();
            }
        }, 2000, 500);

    }

    public void init() {
        player = new Entity(this, 100, 100, 20, 40, Color.GREEN);
//        player.addAction(40, player.createTimeTravellAction(50));
        add(player);

        Entity box = new Entity(this, 150, 150, 20, 20, Color.YELLOW);
        add(box);

        Entity floor = new Entity(this, 50, 200, 200, 20, Color.RED);
        floor.solid = true;
        add(floor);
    }

//    public void playTo(long t) {
//        
//    }
    public void restart() {
        time = -1;
    }
    private List<Entity> toAdd = new LinkedList<Entity>();

    public void add(Entity entity) {
        entity.addAction(time, entity.createAppearAction(entity.x, entity.y));
        toAdd.add(entity);
    }

    public void paint(Graphics g) {

        synchronized (this) {

            g.setColor(Color.GRAY);
            g.fill3DRect(0, 0, width, height, true);
            g.setColor(Color.BLACK);
            g.draw3DRect(0, 0, width, height, true);

            for (int t = -1; t <= time; t++) {

                for (Entity entity : entities) {
                    entity.act(time);
                    entity.paint(g, time);
                }

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


            for (Entity entity : entities) {
                entity.act(time);
            }

            time++; // the most important thing :-p
        }
    }

    public void keyPressed(KeyEvent evt) {

        synchronized (this) {
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    player.addAction(time + player.timeShift, player.createGoAction(-1));
                    break;
                case KeyEvent.VK_RIGHT:
                    player.addAction(time + player.timeShift, player.createGoAction(1));
                    break;
                case KeyEvent.VK_UP:
                    player.addAction(time + player.timeShift, player.createJumpAction());
                    break;
                case KeyEvent.VK_T:
                    final long change = 20;
                    player.addAction(time, player.createTimeTravellAction(change));
                    break;
                case KeyEvent.VK_R:
                    restart();
                    break;
            }
        }
    }
}
