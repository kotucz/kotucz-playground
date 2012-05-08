package tradeworld.graphics;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import tradeworld.GetTransform;
import tradeworld.Time;
import tradeworld.war.TimePos3D;

/**
 *
 * @author Kotuc
 */
public class Model3D {

    private final BranchGroup root = new BranchGroup();
    private final TransformGroup transformGroup;
    private World3D world;

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
    }

    public final void setTransform(Point3d pos, double angle) {
        Transform3D t3d = new Transform3D();
        Transforms.setTransform(t3d, pos, angle);
        this.setTransform(t3d);
    }

    public void setTransform(TimePos3D timepos) {
        setTransform(timepos.getPos(getWorld3D().getCurrentTime()), 0);
    }

    public void setTransform(GetTransform gettransform) {
        Transform3D t3d = new Transform3D();
        gettransform.getTransform(t3d);
        this.setTransform(t3d);
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
