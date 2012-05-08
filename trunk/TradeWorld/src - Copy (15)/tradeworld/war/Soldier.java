package tradeworld.war;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.Identifiable;
import tradeworld.ObjectId;
import tradeworld.Time;
import tradeworld.WorldObject;
import tradeworld.actions.ShootAction;
import tradeworld.actions.UpdatePosAction;
import tradeworld.graphics.Notification3D;
import tradeworld.graphics.Soldier3D;
import tradeworld.multi.ClientId;

/**
 *
 * @author Kotuc
 */
public class Soldier extends WorldObject implements Identifiable<Soldier> {

    private int health = 100;
    public ObjectId<Soldier> id;
    public ClientId clientid;

    public Soldier(Point3d pos) {
        this.model = new Soldier3D(this);
        this.setPos(pos);
    }

    void damage(int damage) {
        this.health -= damage;
        world.showNotification(
                Notification3D.createTextNotification("-" + damage + " HP", new Color3f(1, 0, 0)),
                pos);
        if (health <= 0) {
             if (this.equals(world.getGame().soldier)) {
                 world.getGame().soldier = null;
             }
            remove();
        }
    }

    public void fire(Vector3d dir, Time when) {
        if (nextShoot == null || when.passed(nextShoot)) {

            shoot(dir, when);

            this.nextShoot = when.addSec(1);
        }

    }

    public void go(Vector3d dir, Time when) {
        getWorld().getGame().scheduleAction(new UpdatePosAction(this, getPos(when), dir, when));
    }

    public void shoot(Vector3d dir, Time when) {

        getWorld().getGame().scheduleAction(new ShootAction(this, getPos(when), when, dir));
    }
    private Time nextShoot;
//    private Time nextMove;

    @Override
    public void act(Time now) {
        // only one instance
        if (world.getGame().getClientid().equals(this.clientid) &&
                !this.equals(world.getGame().soldier)) {
            // move
            {
                Vector3d vec = new Vector3d(Math.random() - 0.5, Math.random() - 0.5, 0);
                vec.scale(2.5);
                go(vec, now);
            }

            // shoot
            for (WorldObject worldObject : getWorld().getObjects()) {

                if (worldObject instanceof Soldier) {

                    Soldier sold = (Soldier) worldObject;

                    if (!sold.equals(this)) {

                        Vector3d vec = new Vector3d(Math.random() - 0.5, Math.random() - 0.5, 0);
                        vec.sub(sold.getPos(now), this.getPos(now));

                        if (vec.lengthSquared() > 1) {
                            vec.normalize();
                            vec.scale(5);
                            fire(vec, now);
                            break;

                        }

                    }
                }

            }


        }
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
