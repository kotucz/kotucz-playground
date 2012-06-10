package kotucz.village.transport;


import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import kotucz.village.game.MyGame;
import kotucz.village.tiles.Pos;

import java.util.LinkedList;

/**
 * @author Kotuc
 */
public class Trajectory {

    private final LinkedList<RoadPoint> path;
    private Vector3f pos;
    private Vector3f vec;

    public Trajectory(LinkedList<RoadPoint> path) {
        this.path = path;
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


        // point in half between
        Vector3f p01 = prev.getPosVector().interpolate(curr.getPosVector(), 0.5f);
        Vector3f p12 = curr.getPosVector().interpolate(next.getPosVector(), 0.5f);


        Vector3f vec01 = curr.getPosVector().subtract(prev.getPosVector());
        Vector3f vec12 = next.getPosVector().subtract(curr.getPosVector());


        if ((curr.getPos().x - prev.getPos().x)==(next.getPos().x - curr.getPos().x)&&
                (curr.getPos().y - prev.getPos().y)==(next.getPos().y - curr.getPos().y)
                ) {
            // straingt road
            pos = p01.interpolate(p12, t);
        } else {
            // turning
//            pos = p01.add(vec01.mult((float) Math.sin(fi) * 0.5f));
          pos = p01.add(vec01.mult((float) Math.sin(fi) * 0.5f)).add(vec12.mult((float) (1-Math.cos(fi)) * 0.5f));
        }

        vec = vec01.interpolate(vec12, t).normalize();

        { // interpolate rotation
//            Quaternion rotation01 = new Quaternion();
//            rotation01.fromAxes(vec01, MyGame.UP.cross(vec01), MyGame.UP);
//            Quaternion rotation12 = new Quaternion();
//            rotation12.fromAxes(vec12, MyGame.UP.cross(vec12), MyGame.UP);
//
//            rotation01.interpolate(rotation12, t);

        }
        {
//            double angle01 = Math.atan2(vec01.y, vec01.x);
//            double angle12 = Math.atan2(vec12.y, vec12.x);
//
//            double anglet = an
//
//            vec = new Vector3f()
        }
        return pos;
    }



    public Vector3f getVector(float t) {
        return vec;
    }

    double length() {
        return path.size() - 1;
    }


}
