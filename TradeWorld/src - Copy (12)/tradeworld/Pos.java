package tradeworld;

import javax.vecmath.Point3d;

/**
 * Position
 * @author Kotuc
 */
public class Pos extends Point3d {

    /**
     * Returns distance to another position.
     * @param target Target position
     * @return Distance to target
     */

     public Pos() {

         super();
     }

     public Pos(double x, double y, double z) {
         super(x, y, z);
     }

    public double distanceTo(Point3d target) {

        return distance(target);
    }



    

}
