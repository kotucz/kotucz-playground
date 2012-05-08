package tradeworld.war;

import java.io.Serializable;
import javax.media.j3d.Transform3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.GetTransform;
import tradeworld.Identifiable;
import tradeworld.ObjectId;
import tradeworld.PlayerId;
import tradeworld.Time;
import tradeworld.World;
import tradeworld.WorldObject;
import tradeworld.graphics.Model3D;
import tradeworld.graphics.Notification3D;
import tradeworld.graphics.Soldier3D;

/**
 *
 * @author Kotuc
 */
public abstract class Soldier extends WorldObject implements Identifiable<Soldier>, Serializable, GetTransform {

    public ObjectId<Soldier> id;
    public PlayerId playerid;
    private double pan = 0;
    private double tilt = 0;
    private final Health health = new Health();
    private Gun gun = new Gun();
    //    private Time nextMove;
    protected boolean onGround = true;
    protected final double floor = 0;

    public Soldier() {
    }

    @Override
    public void addingTo(World world) {
        world.putObject(gun);
    }

    @Override
    protected Model3D createModel() {
        return new Soldier3D(this);
    }

    public Health getHealth() {
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

    public void getLookTransform(Transform3D result) {

        Transform3D headT = new Transform3D();
        Point3d pos = new Point3d(-4, 0, 1.75); // eyes
        Point3d center = new Point3d(pos);
        center.add(getSightVector());
        headT.lookAt(pos, center, new Vector3d(0, 0, 1));
        headT.invert();

        Transform3D bodytrans = new Transform3D();
        getTransform(bodytrans);

        bodytrans.mul(headT);

        result.set(bodytrans);

//        headT.invert();

    }

    /**
     * Walking
     * +x forward, +y right, +z up
     * @param dir
     * @param when 
     */
    public void accelerateFirstPerson(Vector3d dir, Time when) {
        Transform3D t3d = new Transform3D();
//        t3d.rotZ(pan - Math.PI / 2);
        t3d.rotZ(-Math.PI / 2);

        Transform3D bodyt = new Transform3D();
        getTransform(bodyt);
        bodyt.mul(t3d);

        Vector3d vec = new Vector3d(dir);
        bodyt.transform(vec);
        vec.scale(5);

        go(vec, when);
    }

    void damage(int damage) {
        this.health.damage(damage);
        {
            Transform3D t3d = new Transform3D();
            getTransform(t3d);
            Point3d pos = new Point3d(0, 0, 2.1);
            t3d.transform(pos);
            getWorld().showNotification(
                    Notification3D.createTextNotification("-" + damage + " HP", new Color3f(1, 0, 0)), pos);
        }
//        if (!isAlive()) {
//            if (this.equals(getWorld().getGame().soldier)) {
//                getWorld().getGame().soldier = null;
//            }
//            remove();
//        }
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
        Transform3D t3d = new Transform3D();
        getTransform(t3d);
        Point3d gunpos = new Point3d(0, 0, 1.3);
        t3d.transform(gunpos);
        gun.fire(new TimePos3D(when, gunpos, dir));
    }

    public abstract void go(Vector3d dir, Time when);

    public ObjectId<Soldier> getId() {
        return id;
    }

    /**
     * 
     * @return is simulated int this game instance
     */
    private boolean isLocal() {
        return getWorld().getGame().isLocal(playerid);
    }

    public boolean isAlive() {
        return health.isAlive();
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
        Transform3D t3d = new Transform3D();
        getTransform(t3d);
        Point3d footpos = new Point3d(0, 0, 0);
        t3d.transform(footpos);
        return footpos;
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
