package kotucz.village.transport;

import com.jme3.math.Vector3f;
import kotucz.village.tiles.Pos;

import java.util.HashSet;
import java.util.Set;

/**
* @author Kotuc
* Date: 9.6.12
* Time: 20:51
*/
public class RoadPoint {

    final Pos pos;
    final Vector3f vector;
    final Set<RoadPoint> incidents = new HashSet<RoadPoint>();

    public RoadPoint(Pos pos, Vector3f posVec) {
        this.pos = pos;
        this.vector = posVec;
    }

    public Pos getPos() {
        return pos;
    }

    public Vector3f getPosVector() {
        return new Vector3f(vector);
    }

    @Override
    public String toString() {
//            return this.vector + "@" + super.toString().split("@")[1];
        return "(" + vector + ")";

    }
}
