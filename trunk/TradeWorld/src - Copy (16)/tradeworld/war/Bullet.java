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

    private Soldier soldier;
    private final Time last;
    private final Vector3d velocity;
    private final Vector3d acc = new Vector3d(0, 0, -2); // gravity

    public Bullet(Soldier soldier, Point3d pos, Vector3d direction, Time time) {
        this.soldier = soldier;
        this.velocity = direction;
//        this.velocity.normalize();
        this.setPos(pos);
        this.model = new Bullet3D(this);
        last = time;
    }

    public Point3d getPos(Time time) {
        Point3d tpos = new Point3d(pos);
        Vector3d tvec = new Vector3d(velocity);
        tvec.scale(time.sub(last).getSeconds());
        tpos.add(tvec);

        Vector3d tacc = new Vector3d(acc);
        tacc.scale(0.5 * Math.pow(time.sub(last).getSeconds(), 2));
        tpos.add(tacc);

        return tpos;
    }

    @Override
    public void act(Time time) {
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
                    if (this.getPos(time).distanceSquared(sold.getPos(time)) < 1) {
                        sold.damage(35);
                        this.remove();//
                    }
                }
            }
        }

        if (this.getPos(time).z<0) {
            this.remove();
        }

//        this.last = time;
    }
}
