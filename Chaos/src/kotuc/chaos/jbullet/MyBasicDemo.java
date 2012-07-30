/*
 * Java port of Bullet (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * Bullet Continuous Collision Detection and Physics Library
 * Copyright (c) 2003-2008 Erwin Coumans  http://www.bulletphysics.com/
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose, 
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */
package kotuc.chaos.jbullet;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import java.util.ArrayList;
import java.util.List;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.demos.opengl.IGL;
import com.bulletphysics.demos.opengl.LWJGL;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Clock;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import kotuc.chaos.Box;
import kotuc.chaos.SimpleGame;
import org.lwjgl.LWJGLException;

/**
 * BasicDemo is good starting point for learning the code base and porting.
 * 
 * @author jezek2
 */
public class MyBasicDemo extends SimpleGame {

    // create 125 (5x5x5) dynamic object
    private static final int ARRAY_SIZE_X = 5;
    private static final int ARRAY_SIZE_Y = 5;
    private static final int ARRAY_SIZE_Z = 5;
    // maximum number of objects (and allow user to shoot additional boxes)
    private static final int MAX_PROXIES = (ARRAY_SIZE_X * ARRAY_SIZE_Y * ARRAY_SIZE_Z + 1024);
    private static final int START_POS_X = -5;
    private static final int START_POS_Y = -5;
    private static final int START_POS_Z = -3;
    // keep the collision shapes, for deletion/cleanup
    private List<CollisionShape> collisionShapes = new ArrayList<CollisionShape>();
    private BroadphaseInterface overlappingPairCache;
    private CollisionDispatcher dispatcher;
    private ConstraintSolver solver;
    private DefaultCollisionConfiguration collisionConfiguration;
    private DynamicsWorld dynamicsWorld;
    private Map<RigidBody, Box> binding = new HashMap<RigidBody, Box>();

    public MyBasicDemo(IGL gl) {
    }

    @Override
    protected void simpleInitGame() {
        initPhysics();
    }

    @Override
    protected void simpleUpdate() {
        clientMoveAndDisplay();
    }
    protected Clock clock = new Clock();

    public float getDeltaTimeMicroseconds() {
        //#ifdef USE_BT_CLOCK
        float dt = clock.getTimeMicroseconds();
        clock.reset();
        return dt;
        //#else
        //return btScalar(16666.);
        //#endif
    }

    public void clientMoveAndDisplay() {

        if (Math.random()<0.01) {
            shoot();
        }

        // simple dynamics world doesn't handle fixed-time-stepping
        float ms = getDeltaTimeMicroseconds();

        // step the simulation
        if (dynamicsWorld != null) {
            dynamicsWorld.stepSimulation(ms / 1000000f);
            // optional but useful: debug drawing
//            dynamicsWorld.debugDrawWorld();
        }

        renderme();

        //glFlush();
        //glutSwapBuffers();
    }

    public void displayCallback() {

        renderme();

        // optional but useful: debug drawing to detect problems
        if (dynamicsWorld != null) {
            dynamicsWorld.debugDrawWorld();
        }




        //glFlush();
        //glutSwapBuffers();
    }

    public void initPhysics() {
//        setCameraDistance(50f);

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
        //overlappingPairCache = new SimpleBroadphase(MAX_PROXIES);

        // the default constraint solver. For parallel processing you can use a different solver (see Extras/BulletMultiThreaded)
        SequentialImpulseConstraintSolver sol = new SequentialImpulseConstraintSolver();
        solver = sol;

        // TODO: needed for SimpleDynamicsWorld
        //sol.setSolverMode(sol.getSolverMode() & ~SolverMode.SOLVER_CACHE_FRIENDLY.getMask());

        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);
        //dynamicsWorld = new SimpleDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);

        dynamicsWorld.setGravity(new Vector3f(0f, -10f, 0f));

        // create a few basic rigid bodies
        //CollisionShape groundShape = new BoxShape(new Vector3f(50f, 50f, 50f));
        CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 50);

        collisionShapes.add(groundShape);

        Transform groundTransform = new Transform();
        groundTransform.setIdentity();
        groundTransform.origin.set(0, -56, 0);

        // We can also use DemoApplication::localCreateRigidBody, but for clarity it is provided here:
        {
            float mass = 0f;

            // rigidbody is dynamic if and only if mass is non zero, otherwise static
            boolean isDynamic = (mass != 0f);

            Vector3f localInertia = new Vector3f(0, 0, 0);
            if (isDynamic) {
                groundShape.calculateLocalInertia(mass, localInertia);
            }

            // using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
            DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
            RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, groundShape, localInertia);
            RigidBody body = new RigidBody(rbInfo);



            // add the body to the dynamics world
            dynamicsWorld.addRigidBody(body);

        }

        {
            // create a few dynamic rigidbodies
            // Re-using the same collision is better for memory usage and performance

            CollisionShape colShape = new BoxShape(new Vector3f(1, 1, 1));
            //CollisionShape colShape = new SphereShape(1f);
            collisionShapes.add(colShape);

            // Create Dynamic Objects
            Transform startTransform = new Transform();
            startTransform.setIdentity();

            float mass = 1f;

            // rigidbody is dynamic if and only if mass is non zero, otherwise static
            boolean isDynamic = (mass != 0f);

            Vector3f localInertia = new Vector3f(0, 0, 0);
            if (isDynamic) {
                colShape.calculateLocalInertia(mass, localInertia);
            }

            float start_x = START_POS_X - ARRAY_SIZE_X / 2;
            float start_y = START_POS_Y;
            float start_z = START_POS_Z - ARRAY_SIZE_Z / 2;

            for (int k = 0; k < ARRAY_SIZE_Y; k++) {
                for (int i = 0; i < ARRAY_SIZE_X; i++) {
                    for (int j = 0; j < ARRAY_SIZE_Z; j++) {
                        startTransform.origin.set(
                                2f * i + start_x,
                                2f * k + start_y,
                                2f * j + start_z);

                        // using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
                        DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
                        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, colShape, localInertia);
                        RigidBody body = new RigidBody(rbInfo);

                        dynamicsWorld.addRigidBody(body);


                        Box box = new Box();

                        binding.put(body, box);
                        location.addEntity(box);

                    }
                }
            }
        }


    }

    public static void main(String[] args) throws LWJGLException {
        MyBasicDemo ccdDemo = new MyBasicDemo(LWJGL.getGL());
        ccdDemo.initPhysics();
        ccdDemo.start();
//        ccdDemo.getDynamicsWorld().setDebugDrawer(new GLDebugDrawer(LWJGL.getGL()));

//        LWJGL.main(args, 800, 600, "Bullet Physics Demo. http://bullet.sf.net", ccdDemo);
    }

    public void renderme() {

        Transform m = new Transform();
        Vector3f wireColor = new Vector3f();
        if (dynamicsWorld != null) {
            int numObjects = dynamicsWorld.getNumCollisionObjects();
            wireColor.set(1f, 0f, 0f);
            for (int i = 0; i < numObjects; i++) {
                CollisionObject colObj = dynamicsWorld.getCollisionObjectArray().get(i);
                RigidBody body = RigidBody.upcast(colObj);

                if (body != null && body.getMotionState() != null) {
                    DefaultMotionState myMotionState = (DefaultMotionState) body.getMotionState();
                    m.set(myMotionState.graphicsWorldTrans);
                } else {
                    colObj.getWorldTransform(m);
                }



                wireColor.set(1f, 1f, 0.5f); // wants deactivation
                if ((i & 1) != 0) {
                    wireColor.set(0f, 0f, 1f);
                }

                // color differently for active, sleeping, wantsdeactivation states
                if (colObj.getActivationState() == 1) // active
                {
                    if ((i & 1) != 0) {
                        //wireColor.add(new Vector3f(1f, 0f, 0f));
                        wireColor.x += 1f;
                    } else {
                        //wireColor.add(new Vector3f(0.5f, 0f, 0f));
                        wireColor.x += 0.5f;
                    }
                }
                if (colObj.getActivationState() == 2) // ISLAND_SLEEPING
                {
                    if ((i & 1) != 0) {
                        //wireColor.add(new Vector3f(0f, 1f, 0f));
                        wireColor.y += 1f;
                    } else {
                        //wireColor.add(new Vector3f(0f, 0.5f, 0f));
                        wireColor.y += 0.5f;
                    }
                }

                Box box = binding.get(body);
                if (box != null) {
                    Quat4f quat = new Quat4f();
                    m.getRotation(quat);
                    box.setRotation(new Quat4d(quat));

                    box.setPos(new Point3d(m.origin));
//                    System.out.println("update");

                    box.setColor(new Color3f(wireColor));
                }

//                GLShapeDrawer.drawOpenGL(gl, m, colObj.getCollisionShape(), wireColor, getDebugMode());
            }
        }
    }

    private void shoot() {
        System.out.println("shoot");
        CollisionShape colShape = new BoxShape(new Vector3f(1, 1, 1));
        //CollisionShape colShape = new SphereShape(1f);
        collisionShapes.add(colShape);

        // Create Dynamic Objects
        Transform startTransform = new Transform();
        startTransform.setIdentity();
//        startTransform.origin.y = 6;

        float mass = 1f;

        // rigidbody is dynamic if and only if mass is non zero, otherwise static
        boolean isDynamic = (mass != 0f);

        Vector3f localInertia = new Vector3f(0, 0, 0);
        if (isDynamic) {
            colShape.calculateLocalInertia(mass, localInertia);
        }

        DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, colShape, localInertia);
        RigidBody body = new RigidBody(rbInfo);

        dynamicsWorld.addRigidBody(body);

        

        Box box = new Box();

        binding.put(body, box);
        location.addEntity(box);

    }
}
