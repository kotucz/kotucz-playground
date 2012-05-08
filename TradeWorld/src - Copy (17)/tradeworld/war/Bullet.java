package tradeworld.war;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.Time;
import tradeworld.WorldObject;
import tradeworld.graphics.Bullet3D;

/**
 *
 * @author Kotuc
 */
public class Bullet extends WorldObject {

    private final Soldier soldier;
    private final TimePos3D timepos;

    public Bullet(Soldier soldier, TimePos3D timepos) {
        this.soldier = soldier;
        this.timepos = timepos;
//        this.timepos = new TimePos3D(time, pos, direction, new Vector3d(0, 0, -0.2));

//        this.velocity = direction;
//        this.velocity.normalize();
//
//        this.setPos(pos);

//        last = time;
        this.model = new Bullet3D(this);
    }

    public Point3d getPos(Time when) {
        return timepos.getPos(when);
//        Point3d tpos = new Point3d(pos);
//        Vector3d tvec = new Vector3d(velocity);
//        tvec.scale(when.sub(last).getSeconds());
//        tpos.add(tvec);
//
//        Vector3d tacc = new Vector3d(acc);
//        tacc.scale(0.5 * Math.pow(when.sub(last).getSeconds(), 2));
//        tpos.add(tacc);
//
//        return tpos;

    }

    @Override
    public Point3d getPos() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Vector3d getDirection() {
        return timepos.velocity;
    }

    @Override
    public void act(Time now) {
//        Time dt = time.sub(last);

//        Vector3d mov = new Vector3d(velocity);
//        mov.scale(dt.getSeconds());
//        mov.scale(0.01);
//        this.pos.add(mov);
//        this.pos.x+=0.01;//add(new Point3d());

//        Point3d tpos = getPos(time);

        for (WorldObject object : getWorld().getObjects()) {
            if (object instanceof Soldier) {
                Soldier sold = (Soldier) object;
                if (!sold.equals(this.soldier)) {
                    if (this.getPos(now).distanceSquared(sold.getPos(now)) < 1) {
                        sold.damage(35);
                        this.remove();//
                    }
                }
            }
        }

        if (this.getPos(now).z < 0) {
            this.remove();
        }

//        this.last = time;
    }
}
