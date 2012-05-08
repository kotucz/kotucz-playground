package tradeworld.war;

import com.bulletphysics.dynamics.RigidBody;
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
    private final float weight = 75; // kg

    public PhySoldier(World world, Point3d pos) {
//        this.pobject = world.getPworld().createBox(new Vector3f(0.25f, 0.4f, 0.9f), weight, pos);
        this.pobject = world.getPworld().createCapsuleZ(0.35f, 0.9f, weight, pos);
        pobject.motionState.centerOfMassOffset.origin.z = -0.9f;
        pobject.body.setActivationState(RigidBody.DISABLE_DEACTIVATION);
        pobject.setFriction(0);
        pobject.body.setDamping(1, 1);
        world.getPworld().add(pobject);
//        this.setPos(pos);
    }
    protected double turning = 0;

    @Override
    public void turnLeft(double radians) {
//        getTransform(null);
        this.turning = radians;
//        pobject.body.applyTorque(new Vector3f(0, 0, (float) radians * 100 * weight));
//        super.turnLeft(radians);
    }

    @Override
    public void jump() {
        if (isOnGround()) {
            pobject.body.applyCentralImpulse(new Vector3f(0, 0, 7 * weight));
            onGround = false;
        }
    }

    public void go(Vector3d dir, Time when) {
//        dir.scale(7500);
//        pobject.setAcc(new Vector3f(dir));       
        
//        dir.z = prevVel.z;
//        pobject.setVelocity(new Vector3f(dir));

        dir.z = 0;
        dir.scale(weight);
        pobject.body.applyCentralForce(new Vector3f(dir));

    }

    @Override
    public void act(Time time) {

        pobject.clearForces();

        Vector3f prevVel = new Vector3f();
        pobject.getVelocity(prevVel);
        if (Math.abs(prevVel.z) < 0.1) {
            onGround = true;
        }

        if (isAlive()) {
            
            go(moveVector, time);

            pobject.body.applyTorque(new Vector3f(0, 0, (float) turning * 10 * weight));

            if (isOnGround()) {
                pullUp();
            }

        } else {
            pobject.setFriction(0.5f);
        }
    }

    public void pullUp() {
        Transform3D transform3D = new Transform3D();
        getTransform(transform3D);
        Vector3f vector3f = new Vector3f(0, 0, 1);
        transform3D.transform(vector3f);
        Vector3f vector3f1 = new Vector3f(0, 0, 1);
        vector3f1.sub(vector3f);
        vector3f1.scale(1800);
        pobject.body.applyForce(vector3f1, vector3f);//pobject.pull(, Vector3d());
//        body.applyForce(new Vector3f(0, 0, 500), vector3f);//pobject.pull(, Vector3d());
    }

    public void getTransform(Transform3D transform) {
        pobject.getTransform(transform);
    }
}
