/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hypercube;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author Kotuc
 */
public class Entity {

    private boolean enabled = false;
    
    private int x = 100,  y = 100;
    private int w = 20,  h = 40;
    private Room room;
    private Image img;
    private Color color;
    private SortedMap<Long, Action> actions = new TreeMap<Long, Entity.Action>();

    public Entity(Room room) {
        this.room = room;
//        img = 
    }


    public Entity(Room room, long time, int x, int y, int w, int h, Color color) {
        this.room = room;
//        this.x = x;
//        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
        addAction(time, createAppearAction(x, y));
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.fill3DRect(x, y, w, h, true);
        g.setColor(Color.BLACK);
        g.draw3DRect(x, y, w, h, true);
//        g.drawImage(img, pos.x, pos.y, null);
    }

    Action action;
    public void act(long time) {

        if (actions.containsKey(time)) {
            action = actions.get(time);
        }
        
        if (isSolid()) {

        } else {

            y += room.getGravity();

            if (action!= null) {
                action = action.execute();
            }
            
            for (Entity entity : room.getEntities()) {
                if (entity.isSolid()) {
                    collisionY(entity);
                }
            }
        }


    }

    public abstract class Action {
       
        public abstract Action execute();
        
    }

    private void collisionY(Entity entity) {
        if (y + h > entity.y) {
            if (((x + w) < entity.x) || ((entity.x + entity.w) < x)) {
                        return;
            }
            this.y = entity.y - this.h;
        }


    }
    boolean solid = false;

    /**
     * 
     * @return
     */
    public boolean isSolid() {
        return solid;
    }
    
    /**
     * 
     * @param time
     * @param action
     */
    public void addAction(long time, Action action) {
        actions.put(time, action);
    }
    
    /**
     * 
     * @param direction
     * @return
     */
    public Action createGoAction(final int direction) {
        return new Action() {
            @Override
            public Action execute() {
                x += 10*direction;
                return null;
            }
        };
    }
        
    public Action createJumpAction() {
        return new Action() {
            @Override
            public Action execute() {
                y -= 100;
                return null;
            }
        };
    }
    
    public Action createAppearAction(final int x1, final int y1) {
        return new Action() {
            @Override
            public Action execute() {
                x = x1;
                y = y1;
                enabled = true;
                return null;
            }
        };
    }
    
    public Action createTimeTravellAction(final int timeShift1) {
        return new Action() {
            @Override
            public Action execute() {
                x = timeShift1;
                // creates itself in the time it comes from
//                Entity itself = this.clone();
//                itself.setTimeShift(timeShift1);
                
                return null;
            }
        };
    }
    
    private long timeShift = 0;

    private void setTimeShift(long timeShift) {
        this.timeShift = timeShift;
    }

//    @Override
//    protected Entity clone() throws CloneNotSupportedException {
//        Entity itself = new Entity(room);
//        return itself;
//    }
    
    
}
