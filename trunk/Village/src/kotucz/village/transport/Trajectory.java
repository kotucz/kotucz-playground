package kotucz.village.transport;


import com.jme3.math.Vector3f;
import kotucz.village.tiles.Pos;

import java.util.LinkedList;

/**
 * @author Kotuc
 */
public class Trajectory {

    private final LinkedList<RoadPoint> path;
    private final LinkedList<Vector3f> points = new LinkedList<Vector3f>();
    private final LinkedList<Vector3f> vectors = new LinkedList<Vector3f>();
    private Vector3f pos;
    private Vector3f vec;
    //    private final double hand = 0.25; // hand right positive

    public Trajectory(LinkedList<RoadPoint> path) {
        this.path = path;


        points.add(new Vector3f(path.getFirst().getPosVector()));
        vectors.add(Vector3f.UNIT_X);

        for (int i = 1; i < path.size() - 1; i++) {

            RoadPoint prev = path.get(i);
            RoadPoint curr = path.get(i);
            RoadPoint next = path.get(i);


            Pos npos = curr.getPos();
            if (prev == null) {
                // first
            } else {

            }


            points.add(new Vector3f(curr.getPosVector()).interpolate(next.getPosVector(), 0.5f));


//            Vector3f pos1 = pointClamp(i - 1);
//            Vector3f pos1 = pointClamp(i);
//            Vector3f pos2 = pointClamp(i + 1);
            Vector3f vec1 = new Vector3f(curr.getPosVector()).subtract(prev.getPosVector());
            Vector3f vec2 = new Vector3f(next.getPosVector()).subtract(curr.getPosVector());
//            if (vec.lengthSquared() > 0.001) {
//                vec = vec.normalize();
//            }
            vectors.add(vec1);


        }

        points.add(new Vector3f(path.getLast().getPosVector()));
        vectors.add(Vector3f.UNIT_X);


    }

    public Vector3f getPoint(float t) {

        int i = Math.round(t);
        vec = Vector3f.UNIT_X;

        if (i == 0) {
              return path.getFirst().getPosVector();
        } else if (i == path.size() - 1) {
            return path.getLast().getPosVector();
        }

//        t -= Math.floor(t);
        t -= i-0.5;

        RoadPoint prev = path.get(i - 1);
        RoadPoint curr = path.get(i);
        RoadPoint next = path.get(i + 1);

        float fi = (float) Math.PI * t / 2;


        Vector3f p01 = prev.getPosVector().interpolate(curr.getPosVector(), 0.5f);
        Vector3f p12 = curr.getPosVector().interpolate(next.getPosVector(), 0.5f);


//            Vector3f pos1 = pointClamp(i - 1);
//            Vector3f pos1 = pointClamp(i);
//            Vector3f pos2 = pointClamp(i + 1);
        Vector3f vec1 = curr.getPosVector().subtract(prev.getPosVector());
        Vector3f vec2 = next.getPosVector().subtract(curr.getPosVector());



        System.out.println("t " + t);
        if ((curr.getPos().x - prev.getPos().x)==(next.getPos().x - curr.getPos().x)&&
                (curr.getPos().y - prev.getPos().y)==(next.getPos().y - curr.getPos().y)
                ) {
            // straingt road
//            pos = p01.interpolate(p12, t);
        } else {
            // turning
//            pos = p01.add(vec1.mult((float) Math.sin(fi) * 0.5f));
        }

        pos = p01.interpolate(p12, t);

//        pos = p01.add(vec1.mult((float) Math.sin(fi) * 0.5f)).add(vec2.mult((float) Math.cos(fi) * 0.5f));


        vec = vec1.interpolate(vec2, t).normalize();

        return pos;

        // linear
//        Vector3f pos0 = new Vector3f(pointClamp(floor));
//        Vector3f pos1 = pointClamp(floor + 1);

//        pos0.interpolate(pos1, t);
//        return pos0;
    }

//    private Vector3f pointClamp(int i) {
//        return points.get(clamp(i));
//    }
//
//    private Vector3f vecClamp(int i) {
//        return vectors.get(clamp(i));
//    }

    private int clamp(int i) {
        return Math.max(0, Math.min(i, path.size() - 1));
    }

    public Vector3f getVector(float t) {
        return vec;
//        int floor = (int) Math.floor(t);
//
//        Vector3f vec1 = new Vector3f(vectors.get(clamp(floor)));
//        Vector3f vec2 = vectors.get(clamp(floor + 1));
//
//        return vec1.interpolate(vec2, t - floor);

//        return vec1;

    }

    double length() {
        return path.size() - 1;
    }

    class Segment {
        float length;


    }


}
