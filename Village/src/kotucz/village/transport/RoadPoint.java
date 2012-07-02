package kotucz.village.transport;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.jme3.math.Vector3f;
import com.sun.istack.internal.Nullable;
import kotucz.village.tiles.Pos;
import kotucz.village.transport.paths.Edge;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kotuc
 *         Date: 9.6.12
 *         Time: 20:51
 */
public class RoadPoint {

    final Pos pos;
    final Vector3f vector;
    //    final Set<RoadPoint> incidents = new HashSet<RoadPoint>();
    private final Set<RoadPoint> nexts = new HashSet<RoadPoint>();
    private final Set<RoadPoint> prevs = new HashSet<RoadPoint>();

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

    public void linkTogether(RoadPoint other) {
        this.linkNext(other);
        other.linkNext(this);
    }

    public void linkNext(RoadPoint next) {
        this.nexts.add(next);
        next.prevs.add(this);
    }

    public void unlinkNext(RoadPoint next) {
        this.nexts.remove(next);
        next.prevs.remove(this);
    }

    public void unlinkTogether(RoadPoint other) {
        this.unlinkNext(other);
        other.unlinkNext(this);
    }

    public void unlinkAll() {
        for (RoadPoint nxt : nexts) {
            this.unlinkNext(nxt);
        }

        for (RoadPoint prv : prevs) {
            prv.unlinkNext(this);
        }


    }

    public Set<RoadPoint> getPrevs() {
        return prevs;
    }

    public Set<RoadPoint> getNexts() {
        return nexts;
    }


    public Iterable<Edge<RoadPoint>> getEdges() {
        return Iterables.transform(getNexts(), new Function<RoadPoint, Edge<RoadPoint>>() {
            @Override
            public Edge<RoadPoint> apply(@Nullable RoadPoint roadPoint) {
                return new Edge<RoadPoint>(roadPoint, 1.0);

            }
        });
    }

}
