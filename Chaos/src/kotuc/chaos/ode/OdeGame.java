package kotuc.chaos.ode;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import kotuc.chaos.Box;
import kotuc.chaos.SimpleGame;
import org.odejava.Body;
import org.odejava.GeomBox;
import org.odejava.GeomPlane;
import org.odejava.HashSpace;
import org.odejava.Odejava;
import org.odejava.PlaceableGeom;
import org.odejava.World;
import org.odejava.collision.Contact;
import org.odejava.collision.JavaCollision;
import org.odejava.ode.Ode;

/**
 *
 * @author Kotuc
 */
public class OdeGame extends SimpleGame {

    private World world;
    private HashSpace collSpace;
    private JavaCollision collCalcs;
    private Contact contactInfo;
    private HashMap<Body, Box> boxes = new HashMap<Body, Box>();
    private int counter = 0;
    private static Matrix3f tempMatrix = new Matrix3f();

    static public void main(String[] args) {
        OdeGame app = new OdeGame();
        app.start();
    }

    public OdeGame() {
    }

    @Override
    protected void simpleInitGame() {
        Odejava.getInstance();
        initWorld();
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
            addBox();
            counter = 80;
//                else if (true) {
//                System.out.println("PRESSED");
//                for (int i = -3; i < 4; i++) {
//                    for (int j = -3; j < 4; j++) {
//                        addBoxAt(i, j);
//                    }
//                }
//                counter = 30;
//            }
        } else {
            counter--;
        }
        physicsStep();
        synchronizeData();

        super.simpleUpdate();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(OdeGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Construction of physics scene (half of normal gravity and other unnecessary parameters)
     */
    private void initWorld() {

        world = new World();
//        world.setGravity(0, 0, -4.8f);
        world.setGravity(0, -2, 0);

        world.setStepInteractions(10);

        world.setStepSize(0.05f);

        collSpace = new HashSpace();

        collCalcs = new JavaCollision(world);

        contactInfo = new Contact(collCalcs.getContactIntBuffer(), collCalcs.getContactFloatBuffer());

        GeomPlane geomPlane = new GeomPlane(0, 1, 0, 0);
        collSpace.add(geomPlane);

//        addBox();

        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(0, 0, 20));
        location.getViewTranformGroup().setTransform(trans);

    }

    private void addBox() {

//        GeomSphere geom = new GeomSphere(1);
        PlaceableGeom geom = new GeomBox(1, 1, 1);
        Body body = new Body("box", world, geom);
        body.adjustMass(1);

        body.setPosition(0, 4, 0);
        body.setAngularVel(0, 0, -1);

        collSpace.addBodyGeoms(body);
//        collSpace.addGeom(geomSphere);

        Box box = new Box(0.5f, 0.5f, 0.5f);
        location.addEntity(box);

        boxes.put(body, box);

    }

    /**
     * Physics necessary simulation steps
     */
    private void physicsStep() {
//        world.step();

        collCalcs.collide(collSpace);
        // examine

//        for (int i = 0; i < collCalcs.getContactCount(); i++) {
//            contactInfo.setIndex(0);
//
////        contactInfo.ge
//
////        contactInfo.setMode(Ode.dContactBounce);
//            contactInfo.setMode(Ode.dContactMotion1);
//            contactInfo.setBounce(0.f);
//            contactInfo.setBounceVel(0.1f);
//            contactInfo.setMu(00000);
////        contactInfo.set
//        }


        collCalcs.applyContacts();

        world.stepFast();
    }

    /**
     * Synchronize position of visual boxes with their physics
     */
    private void synchronizeData() {

        for (Body body : boxes.keySet()) {
            Box box = boxes.get(body);

            Vector3f vec = body.getPosition();
            Quat4f quat = body.getQuaternion();

            System.out.println("pos " + vec);

            box.setPos(new Point3d(vec));
            box.setRotation(new Quat4d(quat));

//            if (box.getPos().z < -11) {
//                physicScene.releaseActor(actor);
//                ia.remove();
//                ib.remove();
//                location.removeEntity(box);
//            }
        }
    }
    /**
     * Adds little box (that may fall on the floor) at coords (height is constant)
     * @param x coords
     * @param z coords
     */
//    private void addBoxAt(float x, float y) {
//        Box b = new Box(0.5f, 0.5f, 0.5f);
//        b.setPos(new Point3d(x, y, 10));
//        location.addEntity(b); // Put it in the scene graph
//        NxActor actor = physicScene.createActor(getBoxActorDescFor(x, y, 10, 0.5f, 0.5f, 0.5f));
//        boxes.add(b);
//        actors.add(actor);
//        if (Math.random() < 0.1) {
//            actor.setLinearVelocity(new NxVec3(0, 0, -50f));
//        }
//    }
//    private NxFluid fluid;
//    private void addFluid() {
//        Box b = new Box(0.1f, 0.1f, 0.1f);
//        NxFluidDesc fluidDesc = new NxFluidDesc();
//        fluidDesc.setToDefault();
//        NxParticleData particles = new NxParticleData();
//        NxFluidEmitterDesc fe = new NxFluidEmitterDesc();
//        fluidDesc.getEmitters()[1] =;
//        SWIGTYPE_p_float ff = particles.getBufferPos();
////        particles.setBufferPos(value);
//        fluidDesc.setParticlesWriteData(particles);
////        fluidDesc.setsetMaxParticles(100);
//        if (fluidDesc.isValid()) {
//            System.err.println("fluidDesc is valid!");
//        }
//        fluid = physicScene.createFluid(fluidDesc);
//        NxFluidEmitterDesc emitterDesc = new NxFluidEmitterDesc();
//        fluid.createEmitter(emitterDesc);
//        location.addEntity(b); // Put it in the scene graph
//    }
//    private NxCloth cloth;
//
//    private void addCloth() {
//        Box b = new Box(0.1f, 0.1f, 0.1f);
//        NxClothDesc clothDesc = new NxClothDesc();
//        clothDesc.setToDefault();
//        final NxMeshData meshData = new NxMeshData();
//        meshData.setMaxVertices(100);
//        clothDesc.setMeshData(meshData);
//        cloth = physicScene.createCloth(clothDesc);
//        System.out.println("Cloth: " + cloth + " valid: " + clothDesc.isValid());
//        location.addEntity(b); // Put it in the scene graph
//    }
//    private void addBoxDuo(float x, float y) {
//        Box b1 = new Box(0.5f, 0.5f, 0.5f);
//        b1.setPos(new Point3d(x - 0.51f, y - 0.51f, 10));
//        location.addEntity(b1); // Put it in the scene graph
//        NxActor actor1 = physicScene.createActor(getBoxActorDescFor(x - 0.51f, y - 0.51f, 10, 0.5f, 0.5f, 0.5f));
//        boxes.add(b1);
//        actors.add(actor1);
//
//        Box b2 = new Box(0.5f, 0.5f, 0.5f);
//        b2.setPos(new Point3d(x + 0.51f, y + 0.51f, 10));
//        location.addEntity(b2); // Put it in the scene graph
//        NxActor actor2 = physicScene.createActor(getBoxActorDescFor(x + 0.51f, y + 0.51f, 10, 0.5f, 0.5f, 0.5f));
//        boxes.add(b2);
//        actors.add(actor2);
//
//        jointDesc.setToDefault();
//        jointDesc.setActor1(actor1);
//        jointDesc.setActor2(actor2);
//        jointDesc.setGlobalAnchor(new NxVec3(0, 0, 10));
//        jointDesc.setGlobalAxis(new NxVec3(0, 0, 1));
//        jointDesc.setMaxForce(20);
////        minLimitDesc.setValue(1);
////        jointDesc.getLimit().setLow(minLimitDesc);
////        maxLimitDesc.setValue(2);
////        jointDesc.getLimit().setHigh(maxLimitDesc);
////        jointDesc.setLocalNormal(new NxVec3(1, -1, 0));
//
//        NxJoint joint = physicScene.createJoint(jointDesc);
//
//    }
//        if (actors.size() > 20) {
////        if (Math.random()<0.1) {
//
//            NxActor actor = actors.get(2);
//            NxVec3 vec = actor.getGlobalPosition();
//            float s = -50f;
//            actor.addForce(new NxVec3(s * vec.getX(), s * vec.getY(), s * vec.getZ()));
////            actor.setLinearVelocity(new NxVec3(-0.2f*vec.getX(), -0.2f*vec.getY(), -0.2f*vec.getZ()));
////            actor.setLinearVelocity(new NxVec3(0, 0, 5));
//        }
//        if (fluid!=null) {
//            System.out.println("fluid");
//        }
//    private static final NxActorDesc boxActorDesc = new NxActorDesc();
//    private static final NxBoxShapeDesc boxShapeDesc = new NxBoxShapeDesc();
//    private static final NxBodyDesc bodyDesc = new NxBodyDesc();
//    private static final NxRevoluteJointDesc jointDesc = new NxRevoluteJointDesc();
//    private static final NxJointLimitDesc minLimitDesc = new NxJointLimitDesc();
//    private static final NxJointLimitDesc maxLimitDesc = new NxJointLimitDesc();
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
//    public static NxActorDesc getBoxActorDescFor(float x, float y, float z, float sizeX, float sizeY, float sizeZ) {
//        boxShapeDesc.setToDefault();
//        boxShapeDesc.getDimensions().set(sizeX, sizeY, sizeZ);
//        bodyDesc.setToDefault();
//
//        boxActorDesc.setToDefault();
//        boxActorDesc.getShapes().push_back(boxShapeDesc);
//        boxActorDesc.setBody(bodyDesc);
//        boxActorDesc.setDensity(10);
//        boxActorDesc.getGlobalPose().getT().set(x, y, z);
//        return boxActorDesc;
//    }
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
//    public static NxActorDesc getBoxStaticActorDescFor(float x, float y, float z, float sizeX, float sizeY, float sizeZ) {
//        boxShapeDesc.setToDefault();
//        boxShapeDesc.getDimensions().set(sizeX, sizeY, sizeZ);
//
//        boxActorDesc.setToDefault();
//        boxActorDesc.getShapes().push_back(boxShapeDesc);
//        boxActorDesc.setDensity(10);
//        boxActorDesc.getGlobalPose().getT().set(x, y, z);
//        return boxActorDesc;
//    }
}
