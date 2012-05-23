package hypergame;

import java.awt.Color;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author Kotuc
 */
public class Pawn extends Entity {

    public Pawn(Body body) {
        super(body);
        color = Color.yellow;
        body.setUserData(this);
    }

    @Override
    void update(float timestep) {
        applyFriction();
    }

    private void applyFriction() {
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
