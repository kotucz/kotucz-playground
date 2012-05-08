package tradeworld.war;

import java.io.Serializable;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.Time;

/**
 *
 * @author Kotuc
 */
public class TimePos3D implements Serializable {

    public final Time time;
    public final Point3d position;
    public final Vector3d velocity;
    public final Vector3d acceleration;

    public TimePos3D(Point3d position) {
        this(new Time(0), position, new Vector3d(), new Vector3d());
    }

    public TimePos3D(Time time, Point3d position, Vector3d velocity) {
        this(time, position, velocity, new Vector3d());
    }

    public TimePos3D(Time time, Point3d position, Vector3d velocity, Vector3d acceleration) {
        this.time = time;
        this.position = new Point3d(position);
        this.velocity = new Vector3d(velocity);
        this.acceleration = new Vector3d(acceleration);
    }

    public Point3d getPos(Time when) {

        Point3d tpos = new Point3d(position);

        Vector3d tvec = new Vector3d(velocity);
        tvec.scale(when.sub(time).getSeconds());
        tpos.add(tvec);

        Vector3d tacc = new Vector3d(acceleration);
        tacc.scale(0.5 * Math.pow(when.sub(time).getSeconds(), 2));
        tpos.add(tacc);

        return tpos;
    }

    public Vector3d getVelocity(Time when) {

        Vector3d tvel = new Vector3d(velocity);

        Vector3d tacc = new Vector3d(acceleration);
        tacc.scale(when.sub(time).getSeconds());
        tvel.add(tacc);

        return tvel;
    }
}
