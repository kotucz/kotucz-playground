package cz.kotuc.chaos;
/**
 *	Entity v 0.01;
 *
 *	Tomas Kotula as Kotuc (=disc)
 */


import java.util.*;
import java.io.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public abstract class Entity {
	protected Physics physics = new Physics();
	
	static ConfiguredUniverse universe;
	
	int maxHitpoints=100;
	int hitpoints=maxHitpoints;
	
	static BranchGroup objRoot = null; // the branch where entities are added

	private BranchGroup objAdd = new BranchGroup();
			
	TransformGroup objTrans = null;	// transform of objEntity
	
	private Transform3D tSet = null;	
//	private Transform3D tRot = null;	

	public BranchGroup objEntity = new BranchGroup();			
	
//	public BranchGroup objAvatar = null;
	
	public Node objHPindicator = null;
	
	public static Location location = null;
	
	public static List entities = new LinkedList();
	
	public double getElevation () {
		return this.physics.getElevation();
	}
	
	public Transform3D getTransform() {
//		Transform3D transform=null;
//		objTrans.getTransform(transform);
//		return transform;
//		return tSet;
		return this.physics.getTransform();
	}
	
	public void setTransform(Transform3D transform) {
		this.objTrans.setTransform(transform);
	}
	
	public void setPos(Point3d point) {
		this.physics.setPos(point);
	}
	
	public Point3d getPos() {
		return this.physics.getPos();
	}
	
	public void setVel(Vector3d vel) {
		this.physics.setVel(vel);
	}
	
	public Vector3d getVel() {
		return this.physics.getVel();
	}

	public void setAngle(double angle) {
		this.physics.setOrientation(angle);
	}
	public double getAngle() {
		return this.physics.getOrientation();
	}
	
	/** Sets location for all Entities
	 *	not using this will cause an error
	 *	@param l the Location, in which Entities occurs
	 *	@see Location
	 */
	public static void setLocation(Location l) {
		location=l;
		objRoot = location.objEntities;
//		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
	}
		
	public Entity () {
		if (this.location==null) {
			System.out.println("location not set in Entity\nthis will cause a fatal error\nuse: Entity.setLocation(Location l)");
		};
				
		objTrans = new TransformGroup(new Transform3D());
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
//		objTrans.addChild(createHPindicator());
		
//		objAvatar=createAvatar();
				
/*		objAvatar.setCapability(Node.ALLOW_BOUNDS_READ);
		objAvatar.setCapability(Group.ALLOW_COLLISION_BOUNDS_READ);
//		objAvatar.setBoundsAutoCompute(false);
		objAvatar.setBounds(new BoundingSphere(new Point3d(), 10.0));
//		objEntity.addChild(objAvatar);*/
		
		objEntity.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		
		createHPindicator();
		
		createVisualBounds();
				
		this.addPart(new EntityPart(createAvatar()));		
			
		objTrans.addChild(objEntity);
//		objRoot.addChild(objTrans);
		objAdd.addChild(objTrans);
		objAdd.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objAdd.setCapability(BranchGroup.ALLOW_DETACH);
		objRoot.addChild(objAdd);
		
		
		new EntityBehavior(this);	

		
		this.refreshPosition();
		
//		createTalkingSound();
		
//		location.addEntity(this);		
		entities.add(this);
	}	
	
	public void addPart(EntityPart part) {
		this.objEntity.addChild(part.objRoot);
	}
	
/*	public void createCollisionDetector() {
		new CollisionDetector(this);
	}
*/	
	final public boolean attack(Entity entity, Bounds bounds) {
		return entity.attacked(this, bounds);
	}
	
	/**	this method is called to ensure this entity is hitted
	 *	if true then the hitted method is called
	 *	
	 *	@param entity the attacking entity
	 *	@param bounds the bounds where the attack is performed
	 *
	 *	@returns returns if this entity is hurted
	 *
	 */
	final boolean attacked(Entity entity, Bounds bounds) {
		Bounds thisBounds = this.getBounds();
		if (entity!=this&&this.hitpoints>0&&thisBounds.intersect(bounds)) {
			this.hitpoints-=10; // - entity .strength * .dexterity * .weaponattack
			System.out.println(this + ": \"au\", " + this.hitpoints + " HP left\n");
			System.out.println(bounds + "\nhit " + thisBounds);
			this.hittedCallback(entity);
			return true;
		} else {
//			System.out.println(bounds + "\nmiss " + thisBounds + "\n");
			return false;	
		}
	}
	
	public void hittedCallback(Entity e) {
		this.hitpoints-=5;
		turnRight();
	}
	
	final public Bounds getBounds () {
		Bounds thisBounds = new BoundingSphere();//= this.objAvatar.getBounds();
		thisBounds.transform(this.getTransform());
		return thisBounds;
	}
	
	Appearance hpAppear;
	
	void createHPindicator () {
		hpAppear = new Appearance();
		hpAppear.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);

		objHPindicator = new Sphere(0.1f, hpAppear);
		objHPindicator.setCapability(Primitive.ENABLE_APPEARANCE_MODIFY);
		
		Transform3D tSet = new Transform3D();
		tSet.set(new Vector3f(0.0f, 0.0f, 2.5f));
//		TransformGroup tg = new TransformGroup(tSet);
//		tg.addChild(objHPindicator);
//		return tg;
		
		this.addPart(new EntityPart(objHPindicator, tSet));
	}
	
	/**
	 *	new Entity should be load
	 *	
	 *	create its geometry 
	 *	and appearance
	 *	use some textures to make it good lookin
	 *
	 *	@return new Branchgroup	
	 *
	 */
	public BranchGroup createAvatar() {return new BranchGroup();};
	
	public final void die() {
		System.out.println(this + " dies\n");
		if (this instanceof Soldier3D) new Soldier3D();
		
		this.finalize();
	}
	
	/**
	 *	you can be sure your new entity will
	 *	proceed this metod every frame
	 *	you should do actions by this not to 
	 *	make asynchronization among your entities
	 *
	 */
	
	void doEveryFrame() {};
	
	/**
	 *	what to do if collide
	 *	
	 *	should be overwriten in subclasses
	 */ 
	void collisionDetected() {};

	public void goForward() {
		this.physics.accRelForward();
	}

	public void goRight() {
		this.physics.accRelRight();
	}
	
	public void goLeft() {
		this.physics.accRelLeft();
	}

	public void goBack() {
		this.physics.accRelBack();
	}
	
	public void goUp() {
		this.physics.accRelUp();
	}

	public void goDown() {
		this.physics.accRelDown();
	}

	public void turnRight () {
		this.physics.turnRight();
	}

	public void turnLeft () {
		this.physics.turnLeft();
	}
	
	public void turnUp () {
		this.physics.inclineUp();
	}

	public void turnDown () {
		this.physics.inclineDown();
	}
	
	public void inclineRight () {
		this.physics.inclineRight();
	}

	public void inclineLeft () {
		this.physics.inclineLeft();
	}

	public void refreshPosition() {
//		tSet = new Transform3D();
//		tSet.set(new Vector3f(this.getPos()));
//		Transform3D tRot = new Transform3D();
//		tRot.rotZ(this.getAngle());
//		tSet.mul(tRot);
//		setTransform(tSet);

		this.setTransform(this.physics.getTransform());	
	}
	
	Sound talkingSound;
	
/*	private void createTalkingSound () {
		talkingSound = new PointSound(new MediaContainer("file:gunshot.wav"), 1.0f, new Point3f());
		talkingSound.setCapability(Sound.ALLOW_ENABLE_WRITE);
		talkingSound.setCapability(Sound.ALLOW_SOUND_DATA_WRITE);
		talkingSound.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
		
		talkingSound.setEnable(false);
		
		this.objAvatar.addChild(talkingSound);
	}
*/
/*	static final MediaContainer SOUND_GUNSHOT = new MediaContainer("file:gunshot.wav");

	public void say(MediaContainer mc) {
//		talkingSound.setSoundData(new MediaContainer(filename));
//		talkingSound.setSoundData(new MediaContainer("file:gunshot.wav"));
//		talkingSound.setSoundData(mc);
		talkingSound.setEnable(true);
	}
*/	
	

	public Entity collidingEntity () {
		Iterator it = entities.iterator();
		while (it.hasNext()) {
			Entity ent;
			if (!((ent=(Entity)it.next()).equals(this))
				&&(ent.hitpoints>0)
				&&(!(ent instanceof Bullet)||((((Bullet)ent).shooter)!=this))
				&&(ent.getBounds().intersect(this.getBounds()))
			) {
				return ent;
			};
		};
		return null;
	}

	
	public void performPhysics () {
		this.physics.perform();
	}
	
	List behaviors = new LinkedList();
		
	public void addBehavior (Behavior behavior) {
		behavior.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
//		BranchGroup objAdd = new BranchGroup();
//		objAdd.addChild(behavior);
//		this.objAdd.addChild(objAdd);
//		this.behaviors.add(behavior);
		this.addPart(new EntityPart(behavior));		
	}
	
	public void playSound (String soundName) {
		Sound sound = new PointSound(new MediaContainer("file:sounds\\"+soundName), 1.0f, new Point3f());
		sound.setCapability(Sound.ALLOW_ENABLE_WRITE);
		sound.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
//		BranchGroup objAdd = new BranchGroup();
//		objAdd.addChild(sound);
//		this.objAdd.addChild(objAdd);

//		this.addPart(new EntityPart(sound));

//		this.behaviors.add();

		sound.setEnable(true);
	}

	public void createVisualBounds() {
		Primitive b = new Sphere();
		Appearance appear = b.getAppearance();
		appear.setMaterial(new Material());
		appear.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.3f));
		this.addPart(new EntityPart(b));
	}


	public void finalize () {
// remove bahaviors when not in objAdd
		
		this.physics.finalize();
		Entity.entities.remove(this);
		Entity.objRoot.removeChild(objAdd);
		
//		super.finalize();
	}
	
}
	
