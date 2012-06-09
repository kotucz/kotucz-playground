package kotucz.village.transport;

import javax.vecmath.Point3d;
import java.util.HashSet;
import java.util.Set;

/**
* Created with IntelliJ IDEA.
* User: Kotuc
* Date: 9.6.12
* Time: 20:51
* To change this template use File | Settings | File Templates.
*/
public class RoadPoint {

    final Point3d pos;
    Set<RoadPoint> incidents = new HashSet<RoadPoint>();

    public RoadPoint(Point3d pos) {
        this.pos = pos;
    }

    public Point3d getPos() {
        return pos;
    }

    @Override
    public String toString() {
//            return this.pos + "@" + super.toString().split("@")[1];
        return "(" + pos.x + "," + pos.y + ")";

    }
}
