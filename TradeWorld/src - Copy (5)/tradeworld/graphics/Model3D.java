package tradeworld.graphics;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Kotuc
 */
public class Model3D {

    private final BranchGroup root = new BranchGroup();
    private final TransformGroup transformGroup;
    private World3D world;
    private final Point3d pos = new Point3d();
    private double angle;

    public void setPos(Point3d pos) {
        this.pos.set(pos);
    }

    public Point3d getPos() {
        return pos;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    BranchGroup getRoot() {
        return root;
    }

    public Model3D() {
        this.transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(transformGroup);
    }

    void addChild(Node node) {
        transformGroup.addChild(node);
    }

    void refresh() {
        Quat4d quat = new Quat4d();
        quat.set(new AxisAngle4d(0, 0, 1, angle));
        Transform3D t3d = new Transform3D();
        t3d.set(quat, new Vector3d(pos), 1);
        transformGroup.setTransform(t3d);
    }

    void setWorld(World3D world) {
        this.world = world;
    }

    World3D getWorld() {
        return world;
    }

    public void remove() {
        world.remove(this);
    }
}
