package tradeworld.war;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.Time;
import tradeworld.actions.UpdatePosAction;
import tradeworld.graphics.Transforms;

/**
 *
 * @author Kotuc
 */
public class SoldierGhost extends Soldier {

    public TimePos3D timepos;
    private final Vector3d gravity = new Vector3d(0, 0, -10);

    public SoldierGhost(Point3d pos) {
        this.timepos = new TimePos3D(pos);
    }

    @Override
    public void act(Time time) {
        go(moveVector, time);
    }

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
//        getWorld().getGame().sendToAll(new UpdatePosAction(this, timepos));
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

    public void getTransform(Transform3D transform) {
        Transforms.setTransform(transform, getPos(getWorld().getGame().getCurrentTime()), getPan());
    }
  
}
