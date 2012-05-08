package arachnoid2;

import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.graphics.Model3D;

/**
 *
 * @author Kotuc
 */
public class Marker3D extends Model3D {

    public Marker3D() {
        final Appearance appear = new Appearance();
//        appear.setMaterial(new Material());
        addChild(new Sphere(0.2f, appear));//Tools.superBox(part.halfBodyBox, "mega4.png"));
    }

    public void setPos(Point3d pos) {
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(pos));
        setTransform(pos);
    }
//    @Override
//    public void refresh() {
//        Transform3D trans = new Transform3D();
//        trans.setIdentity();
//        part.getTransform(trans);
//        trans.mul(part.bodyShift);
//        this.setTransform(trans);
//    }
}
