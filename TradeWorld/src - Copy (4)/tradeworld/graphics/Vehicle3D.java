package tradeworld.graphics;

import com.microcrowd.loader.java3d.max3ds.Loader3DS;
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.Scene;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Group;
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
public class Vehicle3D extends Model3D {

    Point3d dest = new Point3d();

    static {
        try {
            Models.getInstance().store("Train1", loadVehicle());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Vehicle3D.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Goes to dest point not faster than speed (per one call)
     *
     * @param dest
     * @param speed
     * @return true if reached destination; false otherwise
     */
    public boolean navigate(Point3d dest, double speed) {
        Point3d pos = getPos();
        Vector3d vec = new Vector3d(dest);

        vec.sub(pos);
        if (vec.lengthSquared() <= speed * speed) {
            pos.set(dest);
            setPos(pos);
            return true;
        }
        this.setAngle(Math.atan2(vec.y, vec.x));
        vec.normalize();
        vec.scale(speed);
        pos.add(vec);
        setPos(pos);
        return false;
    }

    public Vehicle3D() {
//        try {
//            this.addChild(loadVehicle());
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Vehicle3D.class.getName()).log(Level.SEVERE, null, ex);
//        }
        this.addChild(Models.getInstance().createLink("Train1"));
        this.setPos(new Point3d(5, 4, 0));
        this.setAngle(Math.PI / 4);
        refresh();

    }

    private static Group loadVehicle() throws FileNotFoundException {

        System.out.println("loading vehicle");
//            Lw3dLoader loader = new Lw3dLoader();
//            Scene scene = loader.load("res/models/" + "Dodge Ram pick-up.lwo");
//            this.branchGroup = scene.getSceneGroup();
        Loader loader = new Loader3DS();
//            Scene scene = loader.load("res/models/" + "War.max");
        Scene scene = loader.load("res/models/" + "train13.3ds");

        Transform3D t3d = new Transform3D();
//            t3d.rotX(Math.PI/2);
//            t3d.set(0.01);
//            t3d.set(new Matrix3d(1, 0, 0, 0.25), new Vector3d(), 0.01);

        Quat4d quat4d = new Quat4d();
        quat4d.set(new AxisAngle4d(1, 0, 0, Math.PI / 2));
        Quat4d quat2 = new Quat4d();
        quat2.set(new AxisAngle4d(0, 0, 1, Math.PI / 2));
        quat2.mul(quat4d);
        t3d.set(quat2, new Vector3d(0, 0, 0.0), 0.1);

        TransformGroup tgInner = new TransformGroup(t3d);
        tgInner.addChild(scene.getSceneGroup());

        System.out.println("loading vehicle DONE");
        return tgInner;

    }
}
