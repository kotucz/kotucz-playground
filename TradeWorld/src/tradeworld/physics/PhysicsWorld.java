package tradeworld.physics;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CapsuleShapeZ;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author Kotuc
 */
public class PhysicsWorld {

    protected final DynamicsWorld dynamicsWorld;
    private final List<CollisionShape> collisionShapes = new ArrayList<CollisionShape>();
    private final BroadphaseInterface overlappingPairCache;
    private final CollisionDispatcher dispatcher;
    private final ConstraintSolver solver;
    private final DefaultCollisionConfiguration collisionConfiguration;
    // maximum number of objects (and allow user to shoot additional boxes)
    private static final int MAX_PROXIES = (1024);

    public PhysicsWorld() {

        // collision configuration contains default setup for memory, collision setup
        collisionConfiguration = new DefaultCollisionConfiguration();

        // use the default collision dispatcher. For parallel processing you can use a diffent dispatcher (see Extras/BulletMultiThreaded)
        dispatcher = new CollisionDispatcher(collisionConfiguration);

        // the maximum size of the collision world. Make sure objects stay within these boundaries
        // TODO: AxisSweep3
        // Don't make the world AABB size too large, it will harm simulation quality and performance
        Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
        Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
        overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax, MAX_PROXIES);
//        overlappingPairCache = new SimpleBroadphase(MAX_PROXIES);

        // the default constraint solver. For parallel processing you can use a different solver (see Extras/BulletMultiThreaded)
        SequentialImpulseConstraintSolver sol = new SequentialImpulseConstraintSolver();
        solver = sol;

        // TODO: needed for SimpleDynamicsWorld
        //sol.setSolverMode(sol.getSolverMode() & ~SolverMode.SOLVER_CACHE_FRIENDLY.getMask());

        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);
        //dynamicsWorld = new SimpleDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);

        dynamicsWorld.setGravity(new Vector3f(0f, 0f, -10f));

        {
            // create a few basic rigid bodies
            //CollisionShape groundShape = new BoxShape(new Vector3f(50f, 50f, 50f));
            CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 0, 1), 0);

            collisionShapes.add(groundShape);

            Transform groundTransform = new Transform();
            groundTransform.setIdentity();
//            groundTransform.origin.set(0, -56, 0);

            add(localCreateRigidBody(0f, groundTransform, groundShape));
        }

//        {
//            // create a few dynamic rigidbodies
//            // Re-using the same collision is better for memory usage and performance
//
//            CollisionShape colShape = new BoxShape(new Vector3f(1, 1, 1));
////            CollisionShape colShape = new SphereShape(1f);
//            collisionShapes.add(colShape);
//
//            // Create Dynamic Objects
//            Transform startTransform = new Transform();
//            startTransform.setIdentity();
//
//            float mass = 1f;
//
//            startTransform.origin.set(10, 10, 10);
//
//            add(localCreateRigidBody(mass, startTransform, colShape));
//
//        }

    }

    public void step(float seconds) {
        dynamicsWorld.stepSimulation(seconds);
    }

//    void renderme() {
//        int numObjects = dynamicsWorld.getNumCollisionObjects();
//        for (int i = 0; i < numObjects; i++) {
//            CollisionObject colObj = dynamicsWorld.getCollisionObjectArray().get(i);
//            RigidBody body = RigidBody.upcast(colObj);
//
//            if (body != null && body.getMotionState() != null) {
//                DefaultMotionState myMotionState = (DefaultMotionState) body.getMotionState();
//                m.set(myMotionState.graphicsWorldTrans);
//            } else {
//                colObj.getWorldTransform(m);
//            }
//
//        }
//    }
    public PObject createSphere(float radius, float mass, Point3d pos) {
//        CollisionShape colShape = new BoxShape(halfSizes);
        CollisionShape colShape = new SphereShape(radius);
        collisionShapes.add(colShape);

        // Create Dynamic Objects
        Transform startTransform = new Transform();
        startTransform.setIdentity();
        startTransform.origin.set(pos);

        return localCreateRigidBody(mass, startTransform, colShape);
    }

    public PObject createBox(Vector3f halfSizes, float mass, Point3d pos) {
        CollisionShape colShape = new BoxShape(halfSizes);
//            CollisionShape colShape = new SphereShape(1f);
        collisionShapes.add(colShape);

        // Create Dynamic Objects
        Transform startTransform = new Transform();
        startTransform.setIdentity();
        startTransform.origin.set(pos);

        return localCreateRigidBody(mass, startTransform, colShape);
    }

    public PObject createCapsuleZ(float radius, float height, float mass, Point3d pos) {
        CollisionShape colShape = new CapsuleShapeZ(radius, height);
//            CollisionShape colShape = new SphereShape(1f);
        collisionShapes.add(colShape);

        // Create Dynamic Objects
        Transform startTransform = new Transform();
        startTransform.setIdentity();
        startTransform.origin.set(pos);

        return localCreateRigidBody(mass, startTransform, colShape);
    }

    public PObject localCreateRigidBody(float mass, Transform startTransform, CollisionShape shape) {

        // rigidbody is dynamic if and only if mass is non zero, otherwise static
        boolean isDynamic = (mass != 0f);

        Vector3f localInertia = new Vector3f(0f, 0f, 0f);
        if (isDynamic) {
            shape.calculateLocalInertia(mass, localInertia);
        }

        DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
        RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(mass, myMotionState, shape, localInertia);
        RigidBody body = new RigidBody(cInfo);
        return new PObject(myMotionState, body);
    }

    public void addConstraint(TypedConstraint constraint) {
        dynamicsWorld.addConstraint(constraint);
    }

    public void add(PObject object) {
        dynamicsWorld.addRigidBody(object.body);
    }

    public void remove(PObject object) {
        dynamicsWorld.removeRigidBody(object.body);
    }
}
