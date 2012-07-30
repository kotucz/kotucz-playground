/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotuc.chaos.physx;

import com.JPhysX.JPhysXAdapter;
import com.JPhysX.NxActor;
import com.JPhysX.NxActorDesc;
import com.JPhysX.NxBodyDesc;
import com.JPhysX.NxBoxShapeDesc;
import com.JPhysX.NxJoint;
import com.JPhysX.NxJointDesc;
import com.JPhysX.NxJointLimitDesc;
import com.JPhysX.NxMaterial;
import com.JPhysX.NxParameter;
import com.JPhysX.NxRevoluteJointDesc;
import com.JPhysX.NxScene;
import com.JPhysX.NxSceneDesc;
import com.JPhysX.NxSimulationStatus;
import com.JPhysX.NxSphericalJointDesc;
import com.JPhysX.NxVec3;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3d;
import kotuc.chaos.Box;
import kotuc.chaos.SimpleGame;

/**
 *
 * @author Kotuc
 */
public class PhysXGame2 extends SimpleGame {

    private NxScene physicScene;
    private List<Box> boxes = new LinkedList<Box>();
    private List<NxActor> actors = new LinkedList<NxActor>();
    private int counter = 0;
    private static Matrix3f tempMatrix = new Matrix3f();

    static public void main(String[] args) {
        PhysXGame2 app = new PhysXGame2();
        app.start();
    }

    public PhysXGame2() {
        initPhysics();
    }

    @Override
    protected void simpleInitGame() {
        addBigBox();
    }

    /**
     * Updates scene:
     * <br>
     * <li>adds boxes (on '+' and '*' keys)
     * <li>makes physics "tick"
     * <li>synchronizes position of visual blocks with physic blocks
     */
    @Override
    protected void simpleUpdate() {
        if (counter == 0) {
            if (true) {
                System.out.println("PRESSED");
                if (Math.random() < 0.3) {
                    addBoxDuo(0, 0);
                    counter = 120;
                } else {
                    addBoxAt(0, 0);
                    counter = 80;
                }

            } else if (true) {
                System.out.println("PRESSED");
                for (int i = -3; i < 4; i++) {
                    for (int j = -3; j < 4; j++) {
                        addBoxAt(i, j);
                    }
                }
                counter = 30;
            }
        } else {
            counter--;
        }
        simulatePhysics();
        synchronizeData();

        super.simpleUpdate();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(PhysXGame2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Construction of physics scene (half of normal gravity and other unnecessary parameters)
     */
    private void initPhysics() {
        NxSceneDesc sceneDesc = new NxSceneDesc();
        sceneDesc.setGravity(new NxVec3(0, 0, -4.8f));
        JPhysXAdapter.getPhysicsSDK().setParameter(NxParameter.NX_SKIN_WIDTH, 0.01f);
        physicScene = JPhysXAdapter.getPhysicsSDK().createScene(sceneDesc);

        NxMaterial defaultMaterial = physicScene.getMaterialFromIndex(0);
        defaultMaterial.setRestitution(0.5f);
        defaultMaterial.setStaticFriction(0.5f);
        defaultMaterial.setDynamicFriction(0.5f);
    }

    /**
     * Adds big box (floor) to the scene
     */
    private void addBigBox() {
        Box b;
        NxActor actor;

        b = new Box(5, 5, 0.5f);
        b.setPos(new Point3d(0, 0, -10));
        location.addEntity(b); // Put it in the scene graph
        actor = physicScene.createActor(getBoxStaticActorDescFor(0, 0, -10, 5, 5, 0.5f));
        boxes.add(b);
        actors.add(actor);
    }

    /**
     * Adds little box (that may fall on the floor) at coords (height is constant)
     * @param x coords
     * @param z coords
     */
    private void addBoxAt(float x, float y) {
        Box b = new Box(0.5f, 0.5f, 0.5f);
        b.setPos(new Point3d(x, y, 10));
        location.addEntity(b); // Put it in the scene graph
        NxActor actor = physicScene.createActor(getBoxActorDescFor(x, y, 10, 0.5f, 0.5f, 0.5f));
        boxes.add(b);
        actors.add(actor);
        if (Math.random()<0.1) {
            actor.setLinearVelocity(new NxVec3(0, 0, -50f));
        }
    }

    private void addBoxDuo(float x, float y) {
        Box b1 = new Box(0.5f, 0.5f, 0.5f);
        b1.setPos(new Point3d(x - 0.51f, y - 0.51f, 10));
        location.addEntity(b1); // Put it in the scene graph
        NxActor actor1 = physicScene.createActor(getBoxActorDescFor(x - 0.51f, y - 0.51f, 10, 0.5f, 0.5f, 0.5f));
        boxes.add(b1);
        actors.add(actor1);

        Box b2 = new Box(0.5f, 0.5f, 0.5f);
        b2.setPos(new Point3d(x + 0.51f, y + 0.51f, 10));
        location.addEntity(b2); // Put it in the scene graph
        NxActor actor2 = physicScene.createActor(getBoxActorDescFor(x + 0.51f, y + 0.51f, 10, 0.5f, 0.5f, 0.5f));
        boxes.add(b2);
        actors.add(actor2);

        jointDesc.setToDefault();
        jointDesc.setActor1(actor1);
        jointDesc.setActor2(actor2);
        jointDesc.setGlobalAnchor(new NxVec3(0, 0, 10));
        jointDesc.setGlobalAxis(new NxVec3(0, 0, 1));
        jointDesc.setMaxForce(20);
//        minLimitDesc.setValue(1);
//        jointDesc.getLimit().setLow(minLimitDesc);
//        maxLimitDesc.setValue(2);
//        jointDesc.getLimit().setHigh(maxLimitDesc);
//        jointDesc.setLocalNormal(new NxVec3(1, -1, 0));

        NxJoint joint = physicScene.createJoint(jointDesc);

    }

    /**
     * Physics necessary simulation steps
     */
    private void simulatePhysics() {
        //telling that we begin simulation
        physicScene.simulate(1.0f / 50.0f);
        //flushes all streams (as in PhysX docs)
        physicScene.flushStream();
        //wait for simulation complete
        physicScene.fetchResults(NxSimulationStatus.NX_RIGID_BODY_FINISHED, true);
    }

    /**
     * Synchronize position of visual boxes with their physics
     */
    private void synchronizeData() {
        Iterator<Box> ib = boxes.iterator();
        Iterator<NxActor> ia = actors.iterator();

        while (ia.hasNext()) {
            Box box = ib.next();
            NxActor actor = ia.next();
            float[] vector = actor.getGlobalPosition().get();
            box.setPos(new Point3d(vector[0], vector[1], vector[2]));
            tempMatrix.set(actor.getGlobalOrientation().getRowMajor());
            Transform3D trans = new Transform3D();
            trans.set(tempMatrix);
            box.setRotation(trans);
            if (box.getPos().z < -11) {
                physicScene.releaseActor(actor);
                ia.remove();
                ib.remove();
                location.removeEntity(box);
            } else if (Math.random() < 0.01) {
//                actor.setLinearVelocity(new NxVec3(0, 0, 5));
            }
        }

        if (actors.size() > 20) {
//        if (Math.random()<0.1) {

            NxActor actor = actors.get(2);
            NxVec3 vec = actor.getGlobalPosition();
            float s = -50f;
            actor.addForce(new NxVec3(s * vec.getX(), s * vec.getY(), s * vec.getZ()));
//            actor.setLinearVelocity(new NxVec3(-0.2f*vec.getX(), -0.2f*vec.getY(), -0.2f*vec.getZ()));
//            actor.setLinearVelocity(new NxVec3(0, 0, 5));
        }


    }
    private static final NxActorDesc boxActorDesc = new NxActorDesc();
    private static final NxBoxShapeDesc boxShapeDesc = new NxBoxShapeDesc();
    private static final NxBodyDesc bodyDesc = new NxBodyDesc();
    private static final NxRevoluteJointDesc jointDesc = new NxRevoluteJointDesc();
    private static final NxJointLimitDesc minLimitDesc = new NxJointLimitDesc();
    private static final NxJointLimitDesc maxLimitDesc = new NxJointLimitDesc();

    /**
     * Generates description of the actor, that will be used when creating actor.
     * @param x position in space
     * @param y position in space
     * @param z position in space
     * @param sizeX half of "rad"
     * @param sizeY half of "rad"
     * @param sizeZ half of "rad"
     * @return actor description
     */
    public static NxActorDesc getBoxActorDescFor(float x, float y, float z, float sizeX, float sizeY, float sizeZ) {
        boxShapeDesc.setToDefault();
        boxShapeDesc.getDimensions().set(sizeX, sizeY, sizeZ);
        bodyDesc.setToDefault();

        boxActorDesc.setToDefault();
        boxActorDesc.getShapes().push_back(boxShapeDesc);
        boxActorDesc.setBody(bodyDesc);
        boxActorDesc.setDensity(10);
        boxActorDesc.getGlobalPose().getT().set(x, y, z);
        return boxActorDesc;
    }

    /**
     * Generates description of the actor, that will be used when creating actor. Static actor
     * cannot be pushed away from its place.
     * @param x position in space
     * @param y position in space
     * @param z position in space
     * @param sizeX half of "rad"
     * @param sizeY half of "rad"
     * @param sizeZ half of "rad"
     * @return actor description
     */
    public static NxActorDesc getBoxStaticActorDescFor(float x, float y, float z, float sizeX, float sizeY, float sizeZ) {
        boxShapeDesc.setToDefault();
        boxShapeDesc.getDimensions().set(sizeX, sizeY, sizeZ);

        boxActorDesc.setToDefault();
        boxActorDesc.getShapes().push_back(boxShapeDesc);
        boxActorDesc.setDensity(10);
        boxActorDesc.getGlobalPose().getT().set(x, y, z);
        return boxActorDesc;
    }
}
