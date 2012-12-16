package tradeworld.graphics;

import javax.media.j3d.Transform3D;
import tradeworld.physics.PObject;

/**
 *
 * @author Kotuc
 */
public class Crate3D extends Model3D {

    private final PObject pobject;

    public Crate3D(PObject pobject) {
        this.pobject = pobject;
        addChild(Tools.superBox(1, 1, 1, "mega4.png"));
    }

    @Override
    public void refresh() {
        Transform3D trans = new Transform3D();
        pobject.getTransform3D(trans);
        this.setTransform(trans);
    }
}