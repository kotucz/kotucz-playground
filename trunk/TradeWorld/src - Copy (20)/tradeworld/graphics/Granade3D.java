package tradeworld.graphics;

import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.Transform3D;
import tradeworld.physics.PObject;

/**
 *
 * @author Kotuc
 */
public class Granade3D extends Model3D {

    private final PObject pobject;

    public Granade3D(PObject pobject) {
        this.pobject = pobject;
        addChild(new Sphere(0.1f));
    }

    @Override
    public void refresh() {
        Transform3D trans = new Transform3D();
        pobject.getTransform(trans);
        this.setTransform(trans);
    }
}
