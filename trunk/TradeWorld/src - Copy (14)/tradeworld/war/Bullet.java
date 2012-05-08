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

    private Time last;
    private Soldier soldier;
    private Vector3d velocity;

    public Bullet(Soldier soldier, Point3d pos, Vector3d direction, Time time) {
        this.soldier = soldier;
        this.velocity = direction;
//        this.velocity.normalize();
        this.setPos(pos);
        this.model = new Bullet3D(this);

        last = time;
    }

    @Override
    public void act(Time time) {
        Time dt = time.sub(last);

        Vector3d mov = new Vector3d(velocity);
        mov.scale(dt.getSeconds());
//        mov.scale(0.01);
        this.pos.add(mov);
//        this.pos.x+=0.01;//add(new Point3d());

        for (WorldObject object : getWorld().getObjects()) {
            if (object instanceof Soldier) {
                Soldier sold = (Soldier) object;
                if (!sold.equals(this.soldier)) {
                    if (this.getPos().distanceSquared(sold.getPos()) < 1) {
                        sold.damage(35);
                        this.remove();//
                    }
                }
            }
        }

        this.last = time;
    }
}
