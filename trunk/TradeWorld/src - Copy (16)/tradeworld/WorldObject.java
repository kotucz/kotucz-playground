package tradeworld;

import javax.vecmath.Point3d;
import tradeworld.graphics.Model3D;

/**
 *
 * @author Kotuc
 */
public class WorldObject {

    protected String name;
    protected final Pos pos = new Pos();
    protected transient Model3D model; // TODO private create()
    protected transient Player owner; // TODO private id
    private transient World world;
    //    private static int counter = 0;

    public WorldObject() {
    }

    public void act(Time time) {
    }

    public Model3D getModel() {
        if (model == null) {
            this.model = createModel();
        }
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

    protected Model3D createModel() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    
}
