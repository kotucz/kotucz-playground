package kotucz.village.transport;


import com.jme3.math.Vector3f;

import java.util.LinkedList;

/**
 *
 * @author Kotuc
 */
public class Trajectory {

    private final LinkedList<RoadPoint> path;
    private final LinkedList<Vector3f> points;
    private final LinkedList<Vector3f> vectors;
//    private final double hand = 0.25; // hand right positive

    public Trajectory(LinkedList<RoadPoint> path) {
        this(path, 0);
    }

    public Trajectory(LinkedList<RoadPoint> path, float hand) {
        this.path = path;

        this.points = new LinkedList<Vector3f>();
        for (int i = 0; i < path.size(); i++) {
            points.add(toHandPoint(i, hand));
        }

        this.vectors = new LinkedList<Vector3f>();
        for (int i = 0; i < path.size(); i++) {
//            Vector3f pos1 = pointClamp(i - 1);
            Vector3f pos1 = pointClamp(i);
            Vector3f pos2 = pointClamp(i + 1);
            Vector3f vec = new Vector3f(pos2);
            vec.subtract(pos1);
            if (vec.lengthSquared() > 0.001) {
                vec.normalize();
            }
            vectors.add(vec);
        }
    }

    public Vector3f getPoint(float t) {
        int floor = (int) Math.floor(t);

        t -= floor;

        Vector3f pos0 = pointClamp(floor);
        Vector3f pos1 = pointClamp(floor + 1);
        Vector3f vec0 = vecClamp(floor);
        Vector3f vec1 = vecClamp(floor + 1);

        float[] w = getWeights(t);

        return new Vector3f(
                w[0] * pos0.x + w[1] * pos1.x + w[2] * vec0.x + w[3] * vec1.x,
                w[0] * pos0.y + w[1] * pos1.y + w[2] * vec0.y + w[3] * vec1.y,
                w[0] * pos0.z + w[1] * pos1.z + w[2] * vec0.z + w[3] * vec1.z);

        // linear
//        Vector3f pos0 = new Vector3f(pointClamp(floor));
//        Vector3f pos1 = pointClamp(floor + 1);

//        pos0.interpolate(pos1, t);
//        return pos0;
    }

    private Vector3f pointClamp(int i) {
        return points.get(clamp(i));
    }

    private Vector3f vecClamp(int i) {
        return vectors.get(clamp(i));
    }

    /**
     *  @return point shifted to the hand side from path
     */
    Vector3f toHandPoint(int i, float hand) {
        Vector3f pos1 = path.get(clamp(i - 1)).getPos();
        Vector3f pos2 = path.get(clamp(i + 1)).getPos();
        Vector3f vec = new Vector3f(pos2.y - pos1.y, pos1.x - pos2.x, 0);
        Vector3f pos = new Vector3f(path.get(clamp(i)).getPos());
        if (vec.lengthSquared() > 0.1) {
            vec.normalize();
            vec.mult(hand);

            pos.add(vec);
        }
        return pos;
    }

    private int clamp(int i) {
        return Math.max(0, Math.min(i, path.size() - 1));
    }

    public Vector3f getVector(float t) {
        int floor = (int) Math.floor(t);

        Vector3f vec1 = new Vector3f(vectors.get(clamp(floor)));
        Vector3f vec2 = vectors.get(clamp(floor + 1));

        vec1.interpolate(vec2, t - floor);

        return vec1;

    }

    /**
     * t in 0 .. 1
     * @param t
     * @return p0, p1, v0, v1
     */
    private float[] getWeights(float t) {
        // hermit
        return new float[]{
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
