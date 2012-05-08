package tradeworld.graphics;

import com.microcrowd.loader.java3d.max3ds.Loader3DS;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.BranchGroup;
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
public class Vehicle {

    private BranchGroup branchGroup;
    private TransformGroup transformGroup;
    private Point3d pos = new Point3d();
    private double angle;
    
    Point3d dest = new Point3d();

    public void setPos(Point3d pos) {
        this.pos = pos;
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

    /**
     * Goes to dest point not faster than speed (per one call)
     *
     * @param dest
     * @param speed
     * @return true if reached destination; false otherwise
     */
    public boolean navigate(Point3d dest, double speed) {
        Vector3d vec = new Vector3d(dest);
        vec.sub(pos);
        if (vec.lengthSquared() <= speed * speed) {
            pos.set(dest);
            return true;
        }
        this.angle = Math.atan2(vec.y, vec.x);
        vec.normalize();
        vec.scale(speed);
        pos.add(vec);
        return false;
    }

    public BranchGroup getBranchGroup() {
        return branchGroup;
    }

    public Vehicle() {
        load();
        this.setPos(new Point3d(5, 4, 0));
        this.setAngle(Math.PI / 4);
        refresh();
    }

    private void load() {
        try {
            System.out.println("loading vehicle");
//            Lw3dLoader loader = new Lw3dLoader();
//            Scene scene = loader.load("res/models/" + "Dodge Ram pick-up.lwo");
//            this.branchGroup = scene.getSceneGroup();
            Loader loader = new Loader3DS();
//            Scene scene = loader.load("res/models/" + "War.max");
            Scene scene = loader.load("res/models/" + "13train-model3d.3DS");

            Transform3D t3d = new Transform3D();
//            t3d.rotX(Math.PI/2);
//            t3d.set(0.01);
//            t3d.set(new Matrix3d(1, 0, 0, 0.25), new Vector3d(), 0.01);

            Quat4d quat4d = new Quat4d();
            quat4d.set(new AxisAngle4d(1, 0, 0, Math.PI / 2));
            Quat4d quat2 = new Quat4d();
            quat2.set(new AxisAngle4d(0, 0, 1, Math.PI / 2));
            quat2.mul(quat4d);
            t3d.set(quat2, new Vector3d(0, 0, 0.2), 0.02);

            TransformGroup tgIn = new TransformGroup(t3d);
            tgIn.addChild(scene.getSceneGroup());
            this.transformGroup = new TransformGroup();
            transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            transformGroup.addChild(tgIn);

            this.branchGroup = new BranchGroup();
            branchGroup.addChild(transformGroup);
            System.out.println("loading vehicle DONE");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IncorrectFormatException ex) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParsingErrorException ex) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void refresh() {
        Quat4d quat = new Quat4d();
        quat.set(new AxisAngle4d(0, 0, 1, angle));
        Transform3D t3d = new Transform3D();
        t3d.set(quat, new Vector3d(pos), 1);
        transformGroup.setTransform(t3d);
    }
}
