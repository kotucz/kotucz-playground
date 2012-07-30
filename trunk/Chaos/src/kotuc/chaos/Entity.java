package kotuc.chaos;

import javax.media.j3d.BadTransformException;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Kotuc
 */
public class Entity {

    public int maxHitpoints = 100;
    public int hitpoints = maxHitpoints;
    private Location location;
    private BranchGroup objAdd = new BranchGroup();
    /**
     *  add all 3d objects everything to this group
     */
    private final BranchGroup group = new BranchGroup();
    private final TransformGroup transformGroup;
    private final Transform3D transform;

    protected Entity() {
        this.transform = new Transform3D();
        this.transformGroup = new TransformGroup(transform);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

//		objTrans.addChild(createHPindicator());

//		objAvatar=createAvatar();

        /*		objAvatar.setCapability(Node.ALLOW_BOUNDS_READ);
        objAvatar.setCapability(Group.ALLOW_COLLISION_BOUNDS_READ);
        //		objAvatar.setBoundsAutoCompute(false);
        objAvatar.setBounds(new BoundingSphere(new Point3d(), 10.0));
        //		objEntity.addChild(objAvatar);*/

        transformGroup.addChild(group);
//		objRoot.addChild(objTrans);
        objAdd.addChild(transformGroup);
        objAdd.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        objAdd.setCapability(BranchGroup.ALLOW_DETACH);

        group.setCapability(Group.ALLOW_CHILDREN_EXTEND);

        addBehavior(new EntityBehavior());

        this.refreshPosition();


    }

    protected void addBehavior(Behavior behavior) {
        behavior.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
//		BranchGroup objAdd = new BranchGroup();
//		objAdd.addChild(behavior);
//		this.objAdd.addChild(objAdd);
//		this.behaviors.add(behavior);
        this.addPart(new EntityPart(behavior));
    }

//    public final void die() {
//        location.removeEntity(this);
//
//    }
    /**
     *	you can be sure your new entity will
     *	proceed this metod every frame
     *	you should do actions by this not to
     *	make asynchronization among your entities
     *
     */
    /**
     * you can be sure your new entity will
     * proceed this metod every frame
     * you should do actions by this not to
     * make asynchronization among your entities
     *
     */
    public void doEveryFrame() {
    }

    public Location getLocation() {
        return location;
    }

    Group getMainNode() {
        return objAdd;
    }

    public Point3d getPos() {
        Vector3d pos = new Vector3d();
        transform.get(pos);
        return new Point3d(pos);
    }

    public Transform3D getRot() {
        Quat4d rotation = new Quat4d();
        this.transform.get(rotation);

        Transform3D newRot = new Transform3D();
        newRot.set(rotation);
        return newRot;
//        return this.rotTransform;
    }

    public synchronized void setPos(Point3d point) {
//        this.pos.set(point);
        Quat4d quat = new Quat4d();
        transform.get(quat);

        this.transform.set(quat, new Vector3d(point), 1);

        try {
            this.transformGroup.setTransform(transform);
        } catch (BadTransformException ex) {
            ex.printStackTrace();
            System.out.println("" + transform);
        }

    }

    public synchronized void setRotation(Transform3D trans) {
        Quat4d quat = new Quat4d();
        trans.get(quat);
        quat.normalize();
        this.setRotation(quat);
    }

    public synchronized void setRotation(Quat4d quat) {
        this.transform.set(quat, new Vector3d(getPos()), 1);
        this.transformGroup.setTransform(transform);
    }

    private boolean isAlive() {
        return hitpoints > 0;
    }

//
//    class EntityThread implements Runnable {
//
//        public void run() {
//            while (true) {
//                doEveryFrame();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(PhysicEntity.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//    }
    public void kill() {
        hitpoints = -1000;
        getLocation().removeEntity(this);
    }

    public void refreshPosition() {
//		tSet = new Transform3D();
//		tSet.set(new Vector3f(this.getPos()));
//		Transform3D tRot = new Transform3D();
//		tRot.rotZ(this.getAngle());
//		tSet.mul(tRot);
//		setTransform(tSet);

        this.setTransform(getTransform());
    }
    //protected Transform3D rotTransform = new Transform3D();

    private void setTransform(Transform3D trans) {
        this.transformGroup.setTransform(trans);
    }

    public Transform3D getTransform() {
        return this.transform;
//        Transform3D tSet = new Transform3D();
//        tSet.set(new Vector3f(this.pos));
////		Transform3D tRot = new Transform3D();
////		tRot.rotZ(this.orientation);
////		Transform3D tElev = new Transform3D();
////		tElev.rotY(-this.elevation);
////		Transform3D tIncl = new Transform3D();
////		tIncl.rotX(this.inclination);
//
////		tElev.mul(tIncl);
////		tRot.mul(tElev);
////		tSet.mul(tRot);
//        throw new UnsupportedOperationException();
////        tSet.mul(this.rotTransform);
//////		tSet = new Transform3D(this.rotTransform, new Vector3d(this.pos), 1.0);
////        return tSet;
    }

    /**
     *	current possition in 3D space
     *	x, y, z
     *	@unit m
     */
    //protected Point3d pos = new Point3d();//Math.random() * 20.0, Math.random() * 20.0, /*Math.random()*2*/ 0.0);
    /**
     * Sets location for all Entities
     * not using this will cause an error
     * @param loc
     * @see Location
     */
    public void setLocation(Location loc) {
        this.location = loc;
    }
    private Vector3d vel;

    public void setVelocity(Vector3d vel) {
        this.vel = vel;
    }

    protected void addChild(Node node) {
        this.group.addChild(node);
    }

    public void addPart(EntityPart part) {
        this.group.addChild(part.objRoot);
    }

    class EntityBehavior extends Behavior {

        //private Transform3D tSet = null;		// move obj entity, which is
//	private Transform3D tRot = null;		// its avatar,
        // to its current position
        private WakeupOnElapsedFrames w = new WakeupOnElapsedFrames(0);

        public void initialize() {
            wakeupOn(w);
        }

        /**
         *	doing actualizations of 3D view
         */
        public void processStimulus(java.util.Enumeration en) {
//		System.out.println("physics");
//            performPhysics();

//		System.out.println("behavior");
            doEveryFrame();

//		objHPindicator
//            hpAppear.setColoringAttributes(new ColoringAttributes(1.0f - (float) hitpoints / (float) maxHitpoints, (float) hitpoints / (float) maxHitpoints, 0.0f, ColoringAttributes.NICEST));

//            refreshPosition();

            if (isAlive()) {
                wakeupOn(w);
            } else {
                kill();
            }
        }
    }
}
