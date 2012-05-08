package tradeworld;

import java.util.LinkedList;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.PathNetwork.RoadPoint;

/**
 *
 * @author Kotuc
 */
public class Trajectory {

    private final LinkedList<RoadPoint> path;
    private final LinkedList<Point3d> points;
    private final LinkedList<Vector3d> vectors;
//    private final double hand = 0.25; // hand right positive

    public Trajectory(LinkedList<RoadPoint> path) {
        this(path, 0);
    }

    public Trajectory(LinkedList<RoadPoint> path, double hand) {
        this.path = path;

        this.points = new LinkedList<Point3d>();
        for (int i = 0; i < path.size(); i++) {
            points.add(toHandPoint(i, hand));
        }

        this.vectors = new LinkedList<Vector3d>();
        for (int i = 0; i < path.size(); i++) {
            Point3d pos1 = pointClamp(i - 1);
            Point3d pos2 = pointClamp(i + 1);
            Vector3d vec = new Vector3d(pos2);
            vec.sub(pos1);
            if (vec.lengthSquared() > 0.001) {
                vec.normalize();
            }
            vectors.add(vec);
        }
    }

    public Point3d getPoint(double t) {
        int floor = (int) Math.floor(t);

        t -= floor;

        Point3d pos0 = pointClamp(floor);
        Point3d pos1 = pointClamp(floor + 1);
        Vector3d vec0 = vecClamp(floor);
        Vector3d vec1 = vecClamp(floor + 1);

        double[] w = getWeights(t);

        return new Point3d(
                w[0] * pos0.x + w[1] * pos1.x + w[2] * vec0.x + w[3] * vec1.x,
                w[0] * pos0.y + w[1] * pos1.y + w[2] * vec0.y + w[3] * vec1.y,
                w[0] * pos0.z + w[1] * pos1.z + w[2] * vec0.z + w[3] * vec1.z);

        // linear
//        Point3d pos0 = new Point3d(pointClamp(floor));
//        Point3d pos1 = pointClamp(floor + 1);

//        pos0.interpolate(pos1, t);
//        return pos0;
    }

    private Point3d pointClamp(int i) {
        return points.get(clamp(i));
    }

    private Vector3d vecClamp(int i) {
        return vectors.get(clamp(i));
    }

    /**
     *  @return point shifted to the hand side from path
     */
    Point3d toHandPoint(int i, double hand) {
        Point3d pos1 = path.get(clamp(i - 1)).getPos();
        Point3d pos2 = path.get(clamp(i + 1)).getPos();
        Vector3d vec = new Vector3d(pos2.y - pos1.y, pos1.x - pos2.x, 0);
        Point3d pos = new Point3d(path.get(clamp(i)).getPos());
        if (vec.lengthSquared() > 0.1) {
            vec.normalize();
            vec.scale(hand);

            pos.add(vec);
        }
        return pos;
    }

    private int clamp(int i) {
        return Math.max(0, Math.min(i, path.size() - 1));
    }

    public Vector3d getVector(double t) {
        int floor = (int) Math.floor(t);

        Vector3d vec1 = new Vector3d(vectors.get(clamp(floor)));
        Vector3d vec2 = vectors.get(clamp(floor + 1));

        vec1.interpolate(vec2, t - floor);

        return vec1;

    }

    /**
     * t in 0 .. 1
     * @param t
     * @return p0, p1, v0, v1
     */
    private double[] getWeights(double t) {
        // hermit
        return new double[]{
                    2 * t * t * t - 3 * t * t + 1,
                    -2 * t * t * t + 3 * t * t,
                    1 * t * t * t - 2 * t * t + 1 * t,
                    1 * t * t * t - 1 * t * t
                };
//        return new double[]{
//                    1 + t * (t * (-3 + t * (2))),
//                    t * (t * (3 + t * (-2))),
//                    t * (1 + t * (- 2 + t * (1))),
//                    t * (t * (-1 + t * (1)))
//                };

    }

    double length() {
        return path.size();
    }
}
