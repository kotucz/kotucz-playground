package hypergame;

import java.awt.Color;
import org.jbox2d.common.Vec2;
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
        body.m_force.setZero();
        applyFriction(timestep);
        color = Color.yellow;
        for (ScoringArea scoringArea : game.filterEntities(ScoringArea.class)) {
//            System.out.println(""+scoringArea);
            if (scoringArea.contains(this)) {
                color = scoringArea.color;
            }
        }
    }

    private void applyFriction(float timestep) {
        Vec2 linearVelocity = body.getLinearVelocity();
        float v = linearVelocity.normalize();
        float force = maxFrictionForceMVTF(body.getMass(), v, timestep, body.getShapeList().getFriction());
        body.applyForce(linearVelocity.mul(force), body.getPosition());
    }
}
