package tradeworld.war;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import tradeworld.Time;
import tradeworld.World;
import tradeworld.physics.PObject;

/**
 *
 * @author Kotuc
 */
public class PhySoldier extends Soldier {

    private final PObject pobject;

    public PhySoldier(World world, Point3d pos) {
        this.pobject = world.getPworld().createBox(new Vector3f(0.25f, 0.4f, 0.9f), 75, pos);
        world.getPworld().add(pobject);
//        this.setPos(pos);
    }

    public void go(Vector3d dir, Time when) {
//        dir.scale(7500);
        pobject.setAcc(new Vector3f(dir));
//        pobject.setVelocity(new Vector3f(dir));
        pobject.pullUp();
//        Point3d point = getPos(when);
//        if (onGround) {
//            if (dir.z > 0) {
//                // jump
//                onGround = false;
//                this.timepos = new TimePos3D(when, point, dir, gravity);
//            } else {
//                this.timepos = new TimePos3D(when, point, dir);
//            }
//        } else {
//            dir.z = timepos.getVelocity(when).z;
//            this.timepos = new TimePos3D(when, point, dir, gravity);
//        }
    }

    public void getTransform(Transform3D transform) {
        pobject.getTransform(transform);
    }
}
