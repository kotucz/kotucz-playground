package hypergame.eagleeye;

import hypergame.Displayer;
import hypergame.Entity;
import org.jbox2d.common.Vec2;
import robot.output.Motor;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * @author Kotuc
 */
class Wheel implements Motor {

    final Vec2 relPos;
    // normalized vector
    final Vec2 relForward;

    // force applied permanenty, may change during time
    float speed = 0;

    // velocity of the wheel
    float angularVelocity = 0;

    // angular force applied to the wheel
    float torque = 0;

    float inertia = 1;
    float wheelFriction;

    // mass on this wheel
    float m = 5;

    // gravity force applied 'kolmo' toward the surface
    float fg = m * TableEntity.g;
    private hypergame.eagleeye.Robot robot;

    /**
     * @param robot
     * @param relPos     position vector relative to the body
     * @param relForward forward vector relative to the body
     */
    public Wheel(hypergame.eagleeye.Robot robot, Vec2 relPos, Vec2 relForward) {
        this.robot = robot;
        this.relPos = relPos;
        this.relForward = relForward;
    }

    /**
     * @see http://www.gamedev.net/reference/programming/features/2dcarphys/page4.asp
     */
    void applyWheelForce(float timestep) {

        torque = speed;

        final Vec2 gForwardVector1 = getAbsDir(); // world forward vector size 1
        Vec2 gSideVector = new Vec2(-gForwardVector1.y, gForwardVector1.x); // world side vector size 1

        // speed of the bottom of the wheel
        Vec2 wheelSpeed = gForwardVector1.mul(-angularVelocity);

        // local velocitiy at the point
        Vec2 globalPointVelocity = robot.body.getLinearVelocityFromLocalPoint(relPos).add(wheelSpeed);

        // forward velocity size
        float forwardVelocitySize = Vec2.dot(globalPointVelocity, gForwardVector1);

        // side velocity size
        float sideVelocitySize = Vec2.dot(globalPointVelocity, gSideVector);

        // lateral force - friction in opposite direction to motion
        Vec2 responseForce = gSideVector.mul(-10f * sideVelocitySize);
//
        responseForce = responseForce.add(gForwardVector1.mul(-forwardVelocitySize)); // forward force
////            responseForce = responseForce.add(gForwardVector1.mul(speed)); // forward force


//            float fricForce = maxFrictionForceMVTF(m, globalPointVelocity.normalize(), timestep, wheelFriction);
//            Vec2 responseForce = globalPointVelocity.mul(fricForce);

        torque += forwardVelocitySize;
//            torque += maxFrictionForceMVTF(m, forwardVelocitySize, timestep, wheelFriction);

        angularVelocity += torque; //*timestep

        // break force
//            if (speed == 0) {
//                float breakforce = 100;
//                if (angularVelocity > 0) {
//                    angularVelocity -= Math.min(angularVelocity, breakforce); // damping
//                }
//                if (angularVelocity < 0) {
//                    angularVelocity += Math.max(-angularVelocity, -breakforce); // damping
//                }
//            }

        Vec2 pos = getAbsPos(); // pos of event
        robot.body.applyForce(responseForce, pos);
    }

    void applyForce() {

        Vec2 force = getAbsDir();
        Vec2 pos = getAbsPos();

        robot.body.applyForce(force.mul(speed), pos);

    }

    Vec2 getAbsDir() {
//            XForm xForm = body.getXForm();
//            xForm.position = new Vec2();
//
//            return XForm.mul(xForm, relForward);
        return new Vec2(robot.body.getWorldVector(relForward));
    }

    Vec2 getAbsPos() {
//            return XForm.mul(body.getXForm(), relPos);
        return new Vec2(robot.body.getWorldPoint(relPos));
    }

    void paintWheel(Displayer g2) {


//        Stroke stroke = g2.getStroke();
//        g2.setStroke(new BasicStroke(0.01f));
        Vec2 pos = getAbsPos();
        {
            Vec2 vec1 = getAbsDir().mul(speed);

            g2.setColor(Color.BLUE);

            g2.drawVector(pos, vec1);
        }
        {
            g2.setColor(Color.RED);
            Vec2 vec2 = robot.body.getLinearVelocityFromLocalPoint(relPos);
            g2.drawVector(pos, vec2);

//                g.setColor(Color.GREEN);
//                Point end3 = toPoint(getAbsPos().add(new Vec2(angularVelocity,0)));
//                g.drawLine(pos.x, pos.y, end3.x, end3.y);
        }

//        g2.setStroke(stroke);

//            Point pos = toPoint(getAbsPos());
//            Point end = toPoint(getAbsDir().mul(speed).add(getAbsPos()));
//
//            g.setColor(Color.BLUE);
//
//            g.drawLine(pos.x, pos.y, end.x, end.y);
//            g.drawLine(pos.x, pos.y, end.x, end.y);
//
//            {
//                g.setColor(Color.RED);
//                Point end2 = toPoint(getAbsPos().add(body.getLinearVelocityFromLocalPoint(relPos)));
//                g.drawLine(pos.x, pos.y, end2.x, end2.y);
//
////                g.setColor(Color.GREEN);
////                Point end3 = toPoint(getAbsPos().add(new Vec2(angularVelocity,0)));
////                g.drawLine(pos.x, pos.y, end3.x, end3.y);
//            }
    }

    public void setSpeed(double speed) {
        this.speed = (float) speed;
    }

    public void stop() {
        setSpeed(0);
    }
}
