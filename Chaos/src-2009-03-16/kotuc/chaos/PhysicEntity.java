package kotuc.chaos;

/**
 *	PhysicEntity v 0.01;
 *
 *	Tomas Kotula as Kotuc (=disc)
 */
import java.util.*;

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public abstract class PhysicEntity extends Entity {

    private Physics physics = new Physics();
//    private Transform3D tSet = null;
//	private Transform3D tRot = null;	
//	public BranchGroup objAvatar = null;
    private Node objHPindicator = null;

    public PhysicEntity() {
        createHPindicator();

        createVisualBounds();

//		createTalkingSound();

//		location.addEntity(this);		
//        location.addEntity(this);
    }


    /*	public void createCollisionDetector() {
    new CollisionDetector(this);
    }
     */
    final public boolean attack(PhysicEntity entity, Bounds bounds) {
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
    final boolean attacked(PhysicEntity entity, Bounds bounds) {
        Bounds thisBounds = this.getBounds();
        if (entity != this && this.hitpoints > 0 && thisBounds.intersect(bounds)) {
            this.hitpoints -= 10; // - entity .strength * .dexterity * .weaponattack
            System.out.println(this + ": \"au\", " + this.hitpoints + " HP left\n");
            System.out.println(bounds + "\nhit " + thisBounds);
            return true;
        } else {
//			System.out.println(bounds + "\nmiss " + thisBounds + "\n");
            return false;
        }
    }

    final public Bounds getBounds() {
        Bounds thisBounds = new BoundingSphere();//= this.objAvatar.getBounds();
        thisBounds.transform(this.getTransform());
        return thisBounds;
    }
    private Appearance hpAppear;

    void createHPindicator() {
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
     *	what to do if collide
     *
     *	should be overwriten in subclasses
     */
    void collisionDetected() {
    }
    private Sound talkingSound;

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
    public Entity collidingEntity() {
        throw new UnsupportedOperationException();
//        for (Entity entity : getLocation().getEntities()) {
//            if (!(entity.equals(this)) && (entity.hitpoints > 0) && (!(entity instanceof Bullet) || ((((Bullet) entity).shooter) != this)) && (entity.getBounds().intersect(this.getBounds()))) {
//                return entity;
//            }
//
//        }
//        return null;
    }

    public void performPhysics() {
        this.physics.perform();
    }
    List behaviors = new LinkedList();

    public void playSound(String soundName) {
        Sound sound = new PointSound(new MediaContainer("file:sounds\\" + soundName), 1.0f, new Point3f());
        sound.setCapability(Sound.ALLOW_ENABLE_WRITE);
        sound.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
//		BranchGroup objAdd = new BranchGroup();
//		objAdd.addChild(sound);
//		this.objAdd.addChild(objAdd);

//		this.addPart(new EntityPart(sound));

//		this.behaviors.add();

        sound.setEnable(true);
    }

    private void createVisualBounds() {
        Primitive b = new Sphere();
        Appearance appear = b.getAppearance();
        appear.setMaterial(new Material());
        appear.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.3f));
        this.addPart(new EntityPart(b));
    }

    public double getElevation() {
        return this.physics.getElevation();
    }

    public Vector3d getVel() {
        return this.physics.getVel();
    }

    Physics getPhysics() {
        return this.physics;
    }
}
	
