package tradeworld;

import javax.vecmath.Point3d;
import tradeworld.graphics.Model3D;

/**
 *
 * @author Kotuc
 */
public class WorldObject {

    protected transient Model3D model;
    protected String name;
    protected Player owner;
    protected final Pos pos = new Pos();
    protected World world;
    //    private static int counter = 0;
    
    public WorldObject() {
    }

    public void act(Time time) {
    }

    public Model3D getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public Player getOwner() {
        return owner;
    }

    protected World getWorld() {
        return world;
    }

    public void setPos(Point3d pos) {
        this.pos.set(pos);
//        this.model.setPos(pos);
//        this.model.refresh();
    }

    public Point3d getPos() {
        return new Point3d(pos);
    }
    
    void setWorld(World world) {
        this.world = world;
    }

    public void remove() {
        getWorld().remove(this);
    }

}
