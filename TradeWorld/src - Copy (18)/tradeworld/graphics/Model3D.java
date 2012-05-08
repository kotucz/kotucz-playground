package tradeworld.graphics;

import javax.media.j3d.BoundingSphere;
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
    private double angle = 0;

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
        if (Double.isNaN(angle)) {
            System.err.println("NaN");
            return;
        }
        this.angle = angle;
    }

    BranchGroup getRoot() {
        return root;
    }

    public Model3D() {
        this.transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.setCapability(BranchGroup.ALLOW_DETACH);
        root.addChild(transformGroup);
        enableUpdater();
    }

    public void addChild(Node node) {
        transformGroup.addChild(node);
    }

    public void refresh() {
        setTransform(pos, angle);
    }

    public void setTransform(Point3d pos, double angle) {
        Quat4d quat = new Quat4d();
        quat.set(new AxisAngle4d(0, 0, 1, angle));
//        System.out.println("quat " + quat);
        Transform3D t3d = new Transform3D();
//        System.out.println("pos "+pos);
        t3d.set(quat, new Vector3d(pos), 1);
        setTransform(t3d);
    }

    public void setTransform(Transform3D transform) {
        transformGroup.setTransform(transform);
    }

    void setWorld(World3D world) {
        this.world = world;
    }

    World3D getWorld3D() {
        return world;
    }

    protected void enableUpdater() {
        ModelUpdater notificationBehavior = new ModelUpdater(this);
        notificationBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000));
        addChild(notificationBehavior);
    }
//    public void remove() {
//        world.remove(this);
//    }
}
