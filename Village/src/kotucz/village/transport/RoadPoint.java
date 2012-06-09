package kotucz.village.transport;

import com.jme3.math.Vector3f;

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

    final Vector3f pos;
    Set<RoadPoint> incidents = new HashSet<RoadPoint>();

    public RoadPoint(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getPos() {
        return pos;
    }

    @Override
    public String toString() {
//            return this.pos + "@" + super.toString().split("@")[1];
        return "(" + pos + ")";

    }
}
