package kotuc.chaos;

import com.sun.j3d.utils.geometry.Sphere;
import java.util.LinkedList;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *	!!!	create collisionDetector and remove collisionDetected method from doEvry frame
 *
 */


public class Bullet extends PhysicEntity {

    static final double VELOCITY = 0.3;
    private PhysicEntity shooter = null;
    public static java.util.List bullets = new LinkedList();
    private Switch objSwitch;
    private boolean ready = true;

    private boolean isReady() {
        return this.ready;
    }

    public void setReady(boolean ready) {
        if (ready) {
            explosionShape.setGeometry(ExplosionUpdater.createParticles(50));
            objSwitch.setWhichChild(1);
            this.setVelocity(new Vector3d());
            this.hitpoints = 0;
        } else {
            objSwitch.setWhichChild(0);
            this.hitpoints = maxHitpoints;
        }
        
        this.ready = ready;
    }

    /**
     * Method Bullet
     *
     *	DEPRECATED
     */
    private Bullet() {
//		createShotSound();
        objSwitch = new Switch();
        objSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

        objSwitch.addChild(new Sphere(0.1f));

//	expoliding bullet
        explosionShape = new Shape3D(ExplosionUpdater.createParticles(50));
        explosionShape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        explosionShape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        objSwitch.addChild(explosionShape);

        addChild(objSwitch);

        getPhysics().slidingFriction = 0.0;
        bullets.add(this);
    }

    public static void shoot(PhysicEntity shooter) {
        Bullet bull;
        /*		for (int i = 0; i<Bullet.bullets.size(); i++) {
        bull = (Bullet)Bullet.bullets.get(i);
        if (bull.isReady()) {
        //				bull.shotSound.setEnable(true);
        //				bull.say(PhysicEntity.SOUND_GUNSHOT);
        bull.shooter=shooter;
        System.out.println("*************************************");
        System.out.println("shooting: shooter=" + shooter);
        bull.pos=new Point3d(shooter.pos);
        bull.vel = new Vector3d(Math.cos(shooter.rotTransform) * VELOCITY, Math.sin(shooter.rotTransform) * VELOCITY, 0.0);
        bull.setReady(false);
        return;
        //				break;
        }
        //reloading
        //endOfTurn();
        };*/
        bull = new Bullet();
//		bull.shotSound.setEnable(true);
        bull.playSound("gunshot.wav");
        bull.shooter = shooter;
        bull.setPos(new Point3d(shooter.getPos()));
//		bull.vel = new Vector3d(Math.cos(shooter.rotTransform) * VELOCITY, Math.sin(shooter.rotTransform) * VELOCITY, 0/*Math.sin(shooter.getElevation()) * VELOCITY*/);
        bull.setReady(false);

    }

    /*	public void paint(Graphics g) {
    if (location.isPosPermitted(this.pos)) g.drawLine((int) this.pos.x, (int) this.pos.y, (int) (this.pos.x + this.vel.x * VELOCITY), (int) (this.pos.y + this.vel.y * VELOCITY));
    else {
    g.drawOval((int)this.pos.x-3, (int)this.pos.y-3, 6, 6);
    };
    }
     */
    @Override
    public void collisionDetected() {
        throw new UnsupportedOperationException();
//        if (this.isReady()) {
//            return;
//        }
//
//
//        Bounds bounds;
//
////		bounds = new BoundingSphere();
//
////		bounds.transform(this.objAvatar.getBounds(), getTransform());
////		bounds.transform(getTransform());
//
//        bounds = this.getBounds();
//
//        boolean hashit = false;
//
//
////        for (PhysicEntity sold : getLocation().getEntities()) {
//
////            if (this.shooter != sold) {
////                if (this.attack(sold, bounds)) {
////                    System.out.println("shooter: " + this.shooter + " target: " + sold);
////                    hashit = true;
////                    break;
////                }
////
////            }
////        }
//
//
//        if (hashit) {
////			System.out.print("hit");
////			this.setPos(0.0, 0.0, 1.0);
////			this.vel=new Vector3d(0.1, 0.1, 0.0);
//
//            this.setReady(true);
//        }
////		this.setReady(true);
////		entities.remove(this);
    }

    public void endOfTurn() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    Shape3D explosionShape;
    GeometryUpdater updater = new ExplosionUpdater();

    @Override
    public void doEveryFrame() {
        if (!isReady()) {
//			this.pos.add(this.vel);
//            goForward();
            collisionDetected();
        } else {
            ((GeometryArray) this.explosionShape.getGeometry()).updateData(updater);
        }
    }
    /*	Sound shotSound;

    private void createShotSound () {
    shotSound = new PointSound(new MediaContainer("file:gunshot.wav"), 1.0f, new Point3f());
    shotSound.setCapability(Sound.ALLOW_ENABLE_WRITE);
    //		shotSound.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
    shotSound.setEnable(false);

    //		this.addBehavior(shotSound);
    //		this.objAvatar.addChild(shotSound);
    }
     */
}

