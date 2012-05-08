package arachnoid2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import tradeworld.graphics.Model3D;
import tradeworld.physics.PObject;
import tradeworld.physics.PhysicsWorld;

/**
 *
 * @author Tomas
 */
public class Arachnoid extends Model3D {

    private List<Leg> legs = new ArrayList<Leg>();
    private Map<Leg, Transform3D> transs = new HashMap<Leg, Transform3D>();
    private Walking walking = new Walking(this);
    private Ballancing ballancing = new Ballancing(this);
    Vector3d relativeForceVector = new Vector3d(0, -1, 0);
    double preferredHeigth = 0.1;

    public Arachnoid() {
        for (int i = 0; i < 4; i++) {
            Leg leg = new Leg(this);
            Transform3D t3d1 = new Transform3D();
            t3d1.setTranslation(new Vector3d(0.05, 0, 0));
            Transform3D t3d2 = new Transform3D();
            t3d2.rotY(Math.PI * (2 * i + 1) / 4);
            t3d2.mul(t3d1);
            leg.firstTransform.setTransform(t3d2);
            leg.transfrom = t3d2;
            leg.up = (i % 2 > 0);
            legs.add(leg);
            transs.put(leg, t3d2);
            addChild(leg.firstTransform);
        }
        setPreferredHeigth(0.03);
        setRelativeForceVector(new Vector3d(0, -1, 0));
    }

    void createPhysics(PhysicsWorld pworld) {
        PObject createBox = pworld.createBox(new Vector3f(0.5f, 0.5f, 0.5f), 0.1f, new Point3d());

//        hingeC = new HingeConstraint(bodies[1 + 2 * i], bodies[2 + 2 * i], localB, localC);
//        //hingeC.setLimit(-0.01f, 0.01f);
//        hingeC.setLimit(-BulletGlobals.SIMD_2_PI * 0.0625f, 0.2f);
//        joints[1 + 2 * i] = hingeC;
//        ownerWorld.addConstraint(joints[1 + 2 * i], true);


    }

    List<Leg> getLegs() {
        return legs;
    }

    Transform3D getTransform(Leg leg) {
        return transs.get(leg);
    }

    void setPreferredHeigth(double preferredHeigth) {
//        this.preferredHeigth = preferredHeigth;
//        Point3d pos = this.getPos();
//        pos.y = preferredHeigth;
//        this.setPos(pos);
    }

    public void setRelativeForceVector(Vector3d relativeForceVector) {
        this.relativeForceVector = relativeForceVector;
        relativeForceVector.normalize();
        relativeForceVector.scale(preferredHeigth);
        System.out.println("Force : " + relativeForceVector);
        for (Leg leg : legs) {
            //Point3d cpt = new Point3d();
            //leg.transfrom.transform(cpt);
            //Vector3d newCenter = new Vector3d(cpt);
            //newCenter.scale(2);
            Vector3d newCenter = new Vector3d(0.1, 0, 0);

            Vector3d force = new Vector3d(relativeForceVector);
            Transform3D inv = new Transform3D();
            inv.invert(leg.transfrom);
            inv.transform(force);
//            leg.transfrom.transform(force);
            newCenter.add(force);

            leg.toeCenter = newCenter;
            System.out.println("newCenter : " + newCenter);
        }
    }

    void go(Vector3d vector) {
//        walking.walking6(vector);
//        Point3d pos = this.getPos();
//        pos.add(vector);
//        pos.y = Math.abs(walking.toePos.y);
//        this.setPos(pos);
    }

    void move(Transform3D trans) {
//
//
//        Quat4d rotation = new Quat4d();
//        Vector3d translation = new Vector3d();
//        trans.get(rotation, translation);
//
//        Vector3d translation2 = new Vector3d(translation);
//
//        Transform3D inv = new Transform3D();
//        inv.invert(getRot());
//
////        Quat4d rotationInv = new Quat4d();
////        rotationInv.inverse(rotation);
////        inv.set(rotationInv);
//
//        inv.transform(translation2);
//        Transform3D relativeTransform = new Transform3D(rotation, translation2, 1);
//
//
//        ballancing.ballancing4(relativeTransform);
//
//
//        Point3d pos = this.getPos();
//        pos.add(translation);
////        pos.y = Math.abs(walking.toePos.y);
//        this.setPos(pos);
//
//
//        Transform3D newRot = new Transform3D();
//        newRot.set(rotation);
//        Transform3D thisRot = this.getRot();
//        newRot.mul(thisRot);
//        this.setRotation(newRot);
////        thisRot.mul(newRot);
////        this.setRotation(thisRot);
//
//
//
//
//

    }
}
