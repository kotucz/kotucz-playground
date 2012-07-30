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
import com.JPhysX.NxMaterial;
import com.JPhysX.NxParameter;
import com.JPhysX.NxScene;
import com.JPhysX.NxSceneDesc;
import com.JPhysX.NxSimulationStatus;
import com.JPhysX.NxVec3;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3d;
import kotuc.chaos.Box;
import kotuc.chaos.SimpleGame;

/**
 *
 * @author Kotuc
 */
public class PhysXGame1 extends SimpleGame {

    private NxScene physicScene;
    private List<Box> boxes = new LinkedList<Box>();
    private List<NxActor> actors = new LinkedList<NxActor>();
    private int counter = 0;
    private static Matrix3f tempMatrix = new Matrix3f();

    static public void main(String[] args) {
        PhysXGame1 app = new PhysXGame1();
        app.start();
    }

    public PhysXGame1() {
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
                addBoxAt(0, 0);
                counter = 30;
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
            }
        }




    }
    private static final NxActorDesc boxActorDesc = new NxActorDesc();
    private static final NxBoxShapeDesc boxShapeDesc = new NxBoxShapeDesc();
    private static final NxBodyDesc bodyDesc = new NxBodyDesc();

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
