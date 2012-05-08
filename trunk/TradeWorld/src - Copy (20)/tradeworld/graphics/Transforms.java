package tradeworld.graphics;

import javax.media.j3d.Transform3D;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Kotuc
 */
public final class Transforms {

    private Transforms() {
    }

    public static final void rotateFromXAnimToZeroAngle(Transform3D transform) {
        Quat4d quat4d = new Quat4d();
        quat4d.set(new AxisAngle4d(1, 0, 0, Math.PI / 2));
        Quat4d quat2 = new Quat4d();
        quat2.set(new AxisAngle4d(0, 0, 1, Math.PI / 2));
        quat2.mul(quat4d);
        transform.set(quat2, new Vector3d(0, 0, 0.0), 1);
    }

    public static final void setTransform(Transform3D t3d, Point3d pos, double angle) {
        Quat4d quat = new Quat4d();
        quat.set(new AxisAngle4d(0, 0, 1, angle));
        t3d.set(quat, new Vector3d(pos), 1);
    }


}
