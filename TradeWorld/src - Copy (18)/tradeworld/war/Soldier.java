package tradeworld.war;

import java.io.Serializable;
import javax.media.j3d.Transform3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.Identifiable;
import tradeworld.ObjectId;
import tradeworld.PlayerId;
import tradeworld.Time;
import tradeworld.WorldObject;
import tradeworld.actions.ShootAction;
import tradeworld.actions.ThrowAction;
import tradeworld.actions.UpdatePosAction;
import tradeworld.graphics.Model3D;
import tradeworld.graphics.Notification3D;
import tradeworld.graphics.Soldier3D;

/**
 *
 * @author Kotuc
 */
public class Soldier extends WorldObject implements Identifiable<Soldier>, Serializable {

    private int health = 100;
    public ObjectId<Soldier> id;
    public PlayerId playerid;
    private double pan = 0;
    private double tilt = 0;

    public Soldier(Point3d pos) {
        this.timepos = new TimePos3D(pos);
//        this.setPos(pos);
    }

    @Override
    protected Model3D createModel() {
        return new Soldier3D(this);
    }

    public int getHealth() {
        return health;
    }

    /**
     * adjusts pan
     * @param radians
     */
    public void turnLeft(double radians) {
        this.pan += radians;
    }

    public double getPan() {
        return pan;
    }

    /**
     * adjusts tilt
     * @param radians
     */
    public void turnUp(double radians) {
        this.tilt += radians;
        this.tilt = Math.max(-0.49 * Math.PI, Math.min(tilt, 0.49 * Math.PI));
    }

    public Vector3d getSightVector() {
        return new Vector3d(
                Math.cos(pan) * Math.cos(tilt),
                Math.sin(pan) * Math.cos(tilt),
                Math.sin(tilt));
    }

    /**
     * Walking
     * +x forward, +y right, +z up
     * @param dir
     * @param when 
     */
    public void accelerateFirstPerson(Vector3d dir, Time when) {
        Transform3D t3d = new Transform3D();
        t3d.rotZ(pan - Math.PI / 2);
        Vector3d vec = new Vector3d(dir);
        t3d.transform(vec);
        vec.scale(5);
        go(vec, when);
    }

    void damage(int damage) {
        this.health -= damage;
        final Point3d pos = getPos(getWorld().getGame().getCurrentTime());
        pos.z += 2.1;
        getWorld().showNotification(
                Notification3D.createTextNotification("-" + damage + " HP", new Color3f(1, 0, 0)), pos);
        if (!isAlive()) {
            if (this.equals(getWorld().getGame().soldier)) {
                getWorld().getGame().soldier = null;
            }
            remove();
        }
    }

    /**
     * First person fire where looking if possible.
     * @param when 
     */
    public void fireFP(Time when) {
        Vector3d vec = getSightVector();
        vec.scale(15);
        fire(vec, when);
    }

    public void fire(Vector3d dir, Time when) {
        if (nextShoot == null || when.passed(nextShoot)) {
//            shoot(dir, when);
//            this.nextShoot = when.addSec(0.1);
            granade(dir, when);
            this.nextShoot = when.addSec(3);
        }
    }

//    public void jump(Vector3d dir, Time when) {
//        Point3d point = getPos(when);
//        this.timepos = new TimePos3D(when, point, dir);
////        getWorld().getGame().sendToAll(new UpdatePosAction(this, point, dir, when));
//        getWorld().getGame().sendToAll(new UpdatePosAction(this, timepos));
//    }
    public void go(Vector3d dir, Time when) {
        Point3d point = getPos(when);
        if (onGround) {
            if (dir.z > 0) {
                // jump
                onGround = false;
                this.timepos = new TimePos3D(when, point, dir, gravity);
            } else {
                this.timepos = new TimePos3D(when, point, dir);
            }
        } else {
            dir.z = timepos.getVelocity(when).z;
            this.timepos = new TimePos3D(when, point, dir, gravity);
        }

//        getWorld().getGame().sendToAll(new UpdatePosAction(this, point, dir, when));
        getWorld().getGame().sendToAll(new UpdatePosAction(this, timepos));
    }

    public void shoot(Vector3d dir, Time when) {
        Point3d gunPos = getPos(when); // TODO for sure
        gunPos.z += 1.3; // hand height
        getWorld().getGame().sendToAll(new ShootAction(this, new TimePos3D(when, gunPos, dir, new Vector3d(0, 0, -0.2))));
        ((Soldier3D) getModel()).playSound("gunshot.wav");
    }

    public void granade(Vector3d dir, Time when) {
        Point3d gunPos = getPos(when); // TODO for sure
        gunPos.z += 1.3; // hand height
        getWorld().getGame().sendToAll(new ThrowAction(this, new TimePos3D(when, gunPos, dir, new Vector3d(0, 0, -0.2))));
    }
    private Time nextShoot;
//    private Time nextMove;
    private boolean onGround = true;
    private final Vector3d gravity = new Vector3d(0, 0, -5);
    private final double floor = 0;

    @Override
    public void act(Time now) {
        // moved into game bot
//        // only one instance
//        if (getWorld().getGame().getClientid().equals(this.playerid) &&
//                !this.equals(getWorld().getGame().soldier)) {
//            // move
//            {
//                Vector3d vec = new Vector3d(Math.random() - 0.5, Math.random() - 0.5, 0);
//                vec.scale(2.5);
//                go(vec, now);
//            }
//
//            // shoot
//            for (WorldObject worldObject : getWorld().getObjects()) {
//
//                if (worldObject instanceof Soldier) {
//
//                    Soldier sold = (Soldier) worldObject;
//
//                    if (!sold.equals(this)) {
//
//                        Vector3d vec = new Vector3d(Math.random() - 0.5, Math.random() - 0.5, 0);
//                        vec.sub(sold.getPos(now), this.getPos(now));
//
//                        if (vec.lengthSquared() > 1) {
//                            vec.normalize();
//                            vec.scale(5);
//                            fire(vec, now);
//                            break;
//
//                        }
//
//                    }
//                }
//
//            }
//
//
//        }
    }

    public ObjectId<Soldier> getId() {
        return id;
    }
    public TimePos3D timepos;
    public Time postime;
    public Vector3d velocity;

    /**
     * 
     * @return is simulated int this game instance
     */
    private boolean isLocal() {
        return getWorld().getGame().isLocal(playerid);
    }

    public Point3d getPos(Time when) {
        Point3d pos = timepos.getPos(when);
        if (pos.z < floor && timepos.velocity.z < 0) {
            pos.z = floor;
            onGround = true;
            timepos.velocity.z = 0;
            timepos.acceleration.z = 0;
        }
        return pos;
//        if (postime != null) {
//            Point3d tpos = new Point3d(pos);
//            Vector3d tvec = new Vector3d(velocity);
//            tvec.scale(when.sub(postime).getSeconds());
//            tpos.add(tvec);
//            return tpos;
//        } else {
//            return getPos();
//        }

    }

    public boolean isAlive() {
        return (health > 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Soldier other = (Soldier) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public Point3d getPos() {
        throw new UnsupportedOperationException("obsollete ");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return id + " (" + playerid + ")";
    }
}
