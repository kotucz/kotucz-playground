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

    private long time;
    private List<Entity> entities = new LinkedList<Entity>();
    private int width = 640,  height = 480;
    private int gravity = 10;
    private Entity player;

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
        }, 2000, 100);

    }
    
    public void init() {
        player = new Entity(this, 0, 100, 100, 20, 40, Color.GREEN);
        entities.add(player);
        Entity box = new Entity(this, 0, 150, 150, 20, 20, Color.YELLOW);
        entities.add(box);
        Entity floor = new Entity(this, 0, 50, 200, 200, 20, Color.RED);
        floor.solid = true;
        entities.add(floor);
    }
    
    public void restart() {
        time = -1;
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

    public void put(long time, Entity entity, int x, int y) {
        
    }

    public int getGravity() {
        return gravity;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    private synchronized void round() {

        if (time==100) {
            restart();
        }
                
        
        time++; // the most important thing :-p

        for (Entity entity : entities) {
            entity.act(time);
        }
        
        

    }

    public synchronized void keyPressed(KeyEvent evt) {

        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.addAction(time + 1, player.createGoAction(-1));
                break;
            case KeyEvent.VK_RIGHT:
                player.addAction(time + 1, player.createGoAction(1));
                break;
            case KeyEvent.VK_UP:
                player.addAction(time + 1, player.createJumpAction());
                break;
        }

    }

    public synchronized void setTime(long time) {
        
    }
}
