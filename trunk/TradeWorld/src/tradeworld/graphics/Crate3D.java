package tradeworld.graphics;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;
import tradeworld.physics.PObject;

/**
 *
 * @author Kotuc
 */
public class Crate3D extends Model3D {

    private final PObject pobject;

    public Crate3D(PObject pobject, Vector3d halfSize) {
        this.pobject = pobject;
        getRoot().setCapability(BranchGroup.ENABLE_PICK_REPORTING);
        addChild(Tools.superBox(halfSize, "mega4.png"));
    }

    public Crate3D(PObject pobject) {
        this(pobject, new Vector3d(0.5, 0.5, 0.5));
    }

    @Override
    public void refresh() {
        Transform3D trans = new Transform3D();
        pobject.getTransform(trans);
        this.setTransform(trans);
    }
}
