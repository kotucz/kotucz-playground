/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hypercube;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Kotuc
 */
public class Entity {

    private boolean enabled = false;
    boolean solid = false;
    
    int x = 100,  y = 100;
    private int w = 20,  h = 40;
    private Room room;
    private Image img;
    private Color color;
//    private SortedMap<Long, List<Action>> actions = new TreeMap<Long, List<Entity.Action>>();
    private List<Set<Action>> timeline = new ArrayList<Set<Action>>();
    private String name = "Huhu";
    
    long time;
    
    public Entity(Room room) {
        this.room = room;
//        img = 
    }


    public Entity(Room room, int x, int y, int w, int h, Color color) {
        this.room = room;
//        this.x = x;
//        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
        
    }

    public void paint(Graphics g, long roomtime) {
        if (this.time>=roomtime) {
            g.setColor(color);
            g.fill3DRect(x, y, w, h, enabled);
            g.setColor(Color.BLACK);
            g.draw3DRect(x, y, w, h, enabled);       
        }
    }

//    private Action action;
    public void act() {
        
        time++;
       
//        Collection<Action> acts = timeline.get(time);
//        if (acts!=null) {
//            for (Action action : acts.toArray(new Action[0])) {
//                action.execute(this);
//            }
//        }
        
        if (isSolid()) {

        } else {

            y += room.getGravity();


            
            for (Entity entity : room.getEntities()) {
                if (entity.isSolid()) {
                    collisionY(entity);
                }
            }
        }


    }

    public abstract class Action {
       
        public abstract Action execute(Entity entity);
        
    }

    private void collisionY(Entity entity) {
        if (y + h > entity.y) {
            if (((x + w) < entity.x) || ((entity.x + entity.w) < x)) {
                        return;
            }
            this.y = entity.y - this.h;
        }


    }
    

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
//        List<Action> acts = actions.get(time);
//        if (acts==null) {
//            acts = new ArrayList<Entity.Action>();
//            actions.put(time, acts);
//        }
//        acts.add(action);
//        System.out.println(""+actions);
    }
    
    /**
     * 
     * @param direction
     * @return
     */
    public Action createGoAction(final int direction) {
        return new Action() {
            @Override
            public Action execute(Entity entity) {
                entity.x += 10*direction;
                return null;
            }
        };
    }
        
    public Action createJumpAction() {
        return new Action() {
            @Override
            public Action execute(Entity entity) {
                entity.y -= 100;
                return null;
            }
        };
    }
    
    public Action createAppearAction(final int x1, final int y1) {
        return new Action() {
            @Override
            public Action execute(Entity entity) {
                entity.x = x1;
                entity.y = y1;
                entity.enabled = true;
                return null;
            }
        };
    }
    
    public Action createTimeTravellAction(final long shift) {
        return new Action() {
            @Override
            public Action execute(Entity entity) {
                
//                System.out.println(" Time travel: "+entity+": ");
                
                Entity copy = entity.createCopy();
                entity.room.add(copy);
                
//                entity.timeShift+=shift;
                
                if (entity==room.player) {
                    room.time -= shift;
                }     
                
                System.out.println(""+room.getEntities());
                
//                System.out.println("   - "+entity+" into "+entity.timeShift);
//                System.out.println("   - "+copy+" (copy) in "+copy.timeShift);
                
                return null;
            }
        };
    }

    private Entity createCopy() {
        final Entity copy = new Entity(room, x, y, w, h, color);
//        copy.actions = this.actions; // the timeline is equal
        return copy;
    }

    @Override
    public String toString() {
        return name;
    }
    
        
//    @Override
//    protected Entity clone() throws CloneNotSupportedException {
//        Entity itself = new Entity(room);
//        return itself;
//    }
    
    
}
