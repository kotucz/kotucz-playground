package tradeworld.physics;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f;
import tradeworld.GetTransform;

/**
 *
 * @author Kotuc
 */
public class PObject implements GetTransform {

    private PhysicsWorld pworld;
    public final DefaultMotionState motionState;
    public final RigidBody body;

    public PObject(DefaultMotionState motionState, RigidBody body) {
        this.motionState = motionState;
        this.body = body;
    }

    /**
     * 
     * @param transform
     */
    public void getTransform(Transform3D transform) {
//        final Transform m = new Transform();
//        motionState.getWorldTransform(m);
//        transform.set(m.basis, m.origin, 1f);
        transform.set(motionState.graphicsWorldTrans.basis,
                motionState.graphicsWorldTrans.origin, 1f);
    }

    public void setFriction(float friction) {
        body.setFriction(friction);
    }

    public void setVelocity(Vector3f velocity) {
        body.setLinearVelocity(velocity);
    }

    public void setAcc(Vector3f velocity) {
        body.applyCentralImpulse(velocity);
//        body.applyCentralForce(velocity);
    }

    public void getVelocity(Vector3f out) {
        body.getLinearVelocity(out);
    }

    public void clearForces() {
        body.clearForces();
    }
}
