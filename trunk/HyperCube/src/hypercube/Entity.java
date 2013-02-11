package hypercube;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author Kotuc
 */
public class Entity {

    boolean solid = false;
    int x = 100, y = 100;
    private int w = 20,  h = 40;
    protected final Room room;
    private Image img;
    Color color;
//    private SortedMap<Long, List<Action>> actions = new TreeMap<Long, List<Entity.Action>>();
    private String name = "Huhu";

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

    public void paint(Graphics g) {
        g.setColor(color);
        g.fill3DRect(x, y, w, h, true);
        g.setColor(Color.BLACK);
        g.draw3DRect(x, y, w, h, true);

    }

//    private Action action;
    public void act() {

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
     * @param direction
     * @return
     */
    public TimeAction createGoAction(final int direction) {
        return new TimeAction() {

            @Override
            public void execute(Entity entity) {
                entity.x += 10 * direction;
            }
        };
    }

    public TimeAction createJumpAction() {
        return new TimeAction() {

            @Override
            public void execute(Entity entity) {
                entity.y -= 100;
            }
        };
    }

    public TimeAction createTimeTravellAction(final long shift) {
        return new TimeAction() {

            @Override
            public void execute(Entity entity) {
////                System.out.println(" Time travel: "+entity+": ");
//
//                Entity copy = entity.createCopy();
//                entity.room.add(copy);
//
////                entity.timeShift+=shift;
//
//                if (entity==room.player) {
//                    room.time -= shift;
//                }
//
//                System.out.println(""+room.getEntities());
//
////                System.out.println("   - "+entity+" into "+entity.timeShift);
////                System.out.println("   - "+copy+" (copy) in "+copy.timeShift);
//
//                
            }
        };
    }

    Entity createCopy() {
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
