package tradeworld.physics;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f;

/**
 *
 * @author Kotuc
 */
public class PObject {

    private PhysicsWorld pworld;
    final DefaultMotionState motionState;
    final RigidBody body;

    public PObject(DefaultMotionState motionState, RigidBody body) {
        this.motionState = motionState;
        this.body = body;
    }

    public void getTransform3D(Transform3D transform) {
        final Transform m = new Transform();
        motionState.getWorldTransform(m);
        transform.set(m.basis, m.origin, 1f);
    }

    public void setVelocity(Vector3f velocity) {
        body.setLinearVelocity(velocity);
    }
}
