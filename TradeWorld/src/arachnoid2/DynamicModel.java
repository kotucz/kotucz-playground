package arachnoid2;

import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import com.bulletphysics.linearmath.Transform;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import tradeworld.graphics.Crate3D;
import tradeworld.graphics.World3D;
import tradeworld.physics.PObject;
import tradeworld.physics.PhysicsWorld;

/**
 *
 * @author Kotuc
 */
public class DynamicModel {

    StaticModel smodel;
    PhysicsWorld pworld;
    Map<BodyPart, PObject> pparts = new HashMap<BodyPart, PObject>();

    public DynamicModel(StaticModel smodel, PhysicsWorld world) {
        this.smodel = smodel;
        this.pworld = world;

        for (BodyPart bp : smodel.parts) {

            Transform3D t3d = new Transform3D();
            t3d.setIdentity();
            bp.getTransform(t3d);
            t3d.mul(bp.bodyShift);

            Transform transform = new Transform();
            transform.setIdentity();
            t3d.get(transform.basis);
            t3d.get(transform.origin);

//            PObject pbox = pworld.createBox(new Vector3f(bp.bodyBox), (float) bp.mass, new Point3d());
            PObject pbox = pworld.createBox(new Vector3f(bp.halfBodyBox), 1, new Point3d());
            pbox.body.setCenterOfMassTransform(transform);
//            pbox.motionState.setWorldTransform(transform);

            pworld.add(pbox);

            pparts.put(bp, pbox);

//            bodies[i].setDamping(0.05f, 0.85f);
//            bodies[i].setDeactivationTime(0.8f);
//            bodies[i].setSleepingThresholds(1.6f, 2.5f);


        }


        for (Joint joint : smodel.joints) {
            BodyPart partA = joint.part.parent;
            BodyPart partB = joint.part;


            Transform localA = new Transform();
            Vector3f pointA = new Vector3f();
            {
                Transform3D t3dA = new Transform3D(partB.jointLocation);
                Transform3D t3dI = new Transform3D();
                t3dI.invert(partB.parent.bodyShift);
                t3dA.mul(t3dI);

                t3dA.get(pointA);

                localA.setIdentity();
                t3dA.get(localA.basis);
                t3dA.get(localA.origin);
            }
            Transform localB = new Transform();
            {

//                Transform3D t3dB = new Transform3D(partB.bodyShift);

                Transform3D t3dB = new Transform3D();
                t3dB.invert(partB.bodyShift);
//                t3dB.get(pointB);
                localB.setIdentity();
                t3dB.get(localB.basis);
                t3dB.get(localB.origin);
            }

            System.out.println("" + partA + " " + partB);
            //ConeTwistConstraint* coneC;

            HingeConstraint hingeC =
                    new HingeConstraint(
                    (pparts.get(partA)).body, (pparts.get(partB)).body, localA, localB);

            hingeC.setLimit((float) joint.limitLow, (float) joint.limitHigh);
            hingeC.enableAngularMotor(true, 10.3f, (float) joint.momentum);


            pworld.addConstraint(hingeC);

        }

//        //
//        // Setup the constraints
//        //
//        HingeConstraint hingeC;
//        //ConeTwistConstraint* coneC;
//
//        Transform localA = new Transform();
//        Transform localB = new Transform();
//        Transform localC = new Transform();
//
//
//        for (i = 0; i < NUM_LEGS; i++) {
//            float angle = BulletGlobals.SIMD_2_PI * i / NUM_LEGS;
//            float sin = (float) Math.sin(angle);
//            float cos = (float) Math.cos(angle);
//
//            // hip joints
//            localA.setIdentity();
//            localB.setIdentity();
//            MatrixUtil.setEulerZYX(localA.basis, 0, -angle, 0);
//            localA.origin.set(cos * bodySize, 0.0f, sin * bodySize);
//            tmpTrans.inverse(bodies[1 + 2 * i].getWorldTransform(new Transform()));
//            tmpTrans.mul(tmpTrans, bodies[0].getWorldTransform(new Transform()));
//            localB.mul(tmpTrans, localA);
//            hingeC = new HingeConstraint(bodies[0], bodies[1 + 2 * i], localA, localB);
//            hingeC.setLimit(-0.75f * BulletGlobals.SIMD_2_PI * 0.125f, BulletGlobals.SIMD_2_PI * 0.0625f);
//            //hingeC.setLimit(-0.1f, 0.1f);
//            joints[2 * i] = hingeC;
//            ownerWorld.addConstraint(joints[2 * i], true);
//
//            // knee joints
//            localA.setIdentity();
//            localB.setIdentity();
//            localC.setIdentity();
//            MatrixUtil.setEulerZYX(localA.basis, 0, -angle, 0);
//            localA.origin.set(cos * (bodySize + legLength), 0.0f, sin * (bodySize + legLength));
//            tmpTrans.inverse(bodies[1 + 2 * i].getWorldTransform(new Transform()));
//            tmpTrans.mul(tmpTrans, bodies[0].getWorldTransform(new Transform()));
//            localB.mul(tmpTrans, localA);
//            tmpTrans.inverse(bodies[2 + 2 * i].getWorldTransform(new Transform()));
//            tmpTrans.mul(tmpTrans, bodies[0].getWorldTransform(new Transform()));
//            localC.mul(tmpTrans, localA);
//            hingeC = new HingeConstraint(bodies[1 + 2 * i], bodies[2 + 2 * i], localB, localC);
//            //hingeC.setLimit(-0.01f, 0.01f);
//            hingeC.setLimit(-BulletGlobals.SIMD_2_PI * 0.0625f, 0.2f);
//            joints[1 + 2 * i] = hingeC;
//            ownerWorld.addConstraint(joints[1 + 2 * i], true);
//        }


    }

    public void storeSites(List<Site> sites, SitesConfig config) {
        config.values = new double[sites.size() * 3];
        int i = 0;
        for (Site site : sites) {
            Transform trans = new Transform();
            pparts.get(site.part).body.getCenterOfMassTransform(trans);
            Transform3D trans2 = new Transform3D();
            trans2.invert(site.part.bodyShift);
            trans2.mul(site.offset);            
            Point3d point3d = new Point3d();
            trans2.transform(point3d);
            config.setTuple(point3d, i);
            i++;
        }
    }
    Marker3D[] markers;

    void createGraphics(World3D world3d) {
        markers = new Marker3D[]{new Marker3D(), new Marker3D(), new Marker3D(), new Marker3D()};
        for (Marker3D mark3D : markers) {
            world3d.addModel(mark3D);
        }
        for (BodyPart part : pparts.keySet()) {
            Crate3D crate = new Crate3D(pparts.get(part), part.halfBodyBox);
            world3d.addModel(crate);
        }
    }
}
