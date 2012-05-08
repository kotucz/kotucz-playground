package arachnoid2;

import javax.media.j3d.Transform3D;
import tradeworld.graphics.Model3D;
import tradeworld.graphics.Tools;

/**
 *
 * @author Kotuc
 */
public class Part3D extends Model3D {

    private final BodyPart part;

    public Part3D(BodyPart part) {
        this.part = part;
        addChild(Tools.superBox(part.halfBodyBox, "mega4.png"));
    }

    @Override
    public void refresh() {
        Transform3D trans = new Transform3D();
        trans.setIdentity();
        part.getTransform(trans);
        trans.mul(part.bodyShift);
        this.setTransform(trans);
    }
}
