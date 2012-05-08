package tradeworld.war;

import java.io.Serializable;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.Identifiable;
import tradeworld.ObjectId;
import tradeworld.PlayerId;
import tradeworld.Time;
import tradeworld.WorldObject;
import tradeworld.actions.ShootAction;
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

    public Soldier(Point3d pos) {
    
        this.setPos(pos);
    }

    @Override
    protected Model3D createModel() {
        return new Soldier3D(this);
    }

    void damage(int damage) {
        this.health -= damage;
        getWorld().showNotification(
                Notification3D.createTextNotification("-" + damage + " HP", new Color3f(1, 0, 0)),
                pos);
        if (!isAlive()) {
            if (this.equals(getWorld().getGame().soldier)) {
                getWorld().getGame().soldier = null;
            }
            remove();
        }
    }

    public void fire(Vector3d dir, Time when) {
        if (nextShoot == null || when.passed(nextShoot)) {

            shoot(dir, when);

            this.nextShoot = when.addSec(0.1);
        }

    }

    public void go(Vector3d dir, Time when) {
        Point3d pos = getPos(when);
//        pos.z += 1;
        getWorld().getGame().sendToAll(new UpdatePosAction(this, pos, dir, when));
    }

    public void shoot(Vector3d dir, Time when) {
        Point3d gunPos = getPos(when);
        gunPos.z += 1;
        getWorld().getGame().sendToAll(new ShootAction(this, gunPos, when, dir));
        ((Soldier3D) getModel()).playSound("gunshot.wav");
    }
    private Time nextShoot;
//    private Time nextMove;

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
    public Time postime;
    public Vector3d velocity;

    public Point3d getPos(Time time) {
        if (postime != null) {
            Point3d tpos = new Point3d(pos);
            Vector3d tvec = new Vector3d(velocity);
            tvec.scale(time.sub(postime).getSeconds());
            tpos.add(tvec);
            return tpos;
        } else {
            return getPos();
        }

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
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
