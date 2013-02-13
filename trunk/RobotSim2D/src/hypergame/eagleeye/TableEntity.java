package hypergame.eagleeye;

import hypergame.Entity;
import org.jbox2d.dynamics.Body;

/**
 * Adds tools for computations in eagle eye world.
 *
 * @author Kotuc
 *
 */
public class TableEntity extends Entity {

    public static final float g = 9.81F; // gravity m/s2

    public TableEntity(Body body) {
        super(body);
    }

    public TableEntity() {
        super();
    }

    /**
     * force in the direction of v - applying in opposite direction
     */
    protected static float maxFrictionForceMVTF(float m, float v, float timestep, float friction) {
        //        f = m*a = m*v/t;
        //        v = a*t;
        float maxForce = -m * v / timestep; // force to stop
        float frictionForce = -m * g * friction;
        // not to accelerate in the opposite direction
        return (Math.abs(maxForce) < Math.abs(frictionForce)) ? maxForce : frictionForce;
    }

}
