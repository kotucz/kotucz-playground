package hypergame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import org.jbox2d.collision.Distance;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;
import robot.input.KeyboardDriving;
import robot.output.DiffWheels;
import robot.output.Motor;

/**
 *
 * @author Kotuc
 */
public class Robot extends Entity implements DiffWheels {

    Wheel lwheel = new Wheel(new Vec2(-0.12f, 0), new Vec2(0, 1));
    Wheel rwheel = new Wheel(new Vec2(0.12f, 0), new Vec2(0, 1));
    public Gripper gripper = new Gripper();
    KeyboardDriving keyboard = new KeyboardDriving(this);
    PathPlanner pathPlanner = new PathPlanner(this);

    public Robot(Game game) {
        super();
        this.game = game;
        color = new Color(0.8f, 1.0f, 0.8f);
        createPhysic(game.getPhysWorld());
//        game.physWorld.;
    }

    @Override
    void update(float timestep) {

        keyboard.act();

//        setSpeedsLR(5, 0.5);

        pathPlanner.act();

        // clear forces
        body.m_force.setZero();
        body.m_torque = 0;


//        lwheel.applyForce((float)(Math.random()-0.5));
//        rwheel.applyForce((float)(Math.random()-0.5));
//        lwheel.applyForce((float)(0.1f));
//        rwheel.applyForce((float)(0.09f));


        lwheel.applyWheelForce(timestep);
        rwheel.applyWheelForce(timestep);


    }

    @Override
    void paint(Graphics g) {
        super.paint(g);
        lwheel.paintWheel(g);
        rwheel.paintWheel(g);
        pathPlanner.paint(g);
    }

    private void createPhysic(World world) {
        //CircleDef sd = new CircleDef();

        BodyDef bd = new BodyDef();
//        bd.position.set(-1, 1);
        bd.position.set(0, 0.35f);
//        bd.linearDamping = 2f;
//        bd.angularDamping = 4f;

        body = world.createBody(bd);
        body.setBullet(true);

        {
            PolygonDef sd = new PolygonDef();
            sd.setAsBox(0.15f, 0.10f);

//        sd.radius = 0.1f;
            sd.density = 100f; // like watter
            sd.friction = 0.5f;

            body.createShape(sd);
        }

        { // gripper sensor
            PolygonDef sd = new PolygonDef();
            sd.setAsBox(0.05f, 0.05f, new Vec2(0, 0.2f), (float) Math.toRadians(45));
            sd.isSensor = true;
//        sd.radius = 0.1f;
            sd.density = 0f;
            gripper.sensor = body.createShape(sd);
        }

        body.setMassFromShapes();

    }

    public void setSpeedsLR(double leftSpeed, double rightSpeed) {
        lwheel.setSpeed(leftSpeed);
        rwheel.setSpeed(rightSpeed);
    }

    public void stop() {
        lwheel.stop();
        rwheel.stop();
    }

    public class Gripper {

        private Shape sensor;
        private Entity hold = null;
        private Joint createJoint = null;
        private boolean gripped = false;

        public void grip(boolean grip) {
            if (grip) {
                if (!gripped) {
                    for (Entity entity : game.entities) {
                        if (entity instanceof Pawn) {
                            Vec2 x1 = new Vec2();
                            Vec2 x2 = new Vec2();
                            float distance = Distance.distance(x1, x2, sensor, body.getXForm(), entity.body.m_shapeList, entity.body.getXForm());

                            if (distance < 0.1) {
//                                color = Color.RED;
//                                if (hold == null) {
                                // join
                                DistanceJointDef jd = new DistanceJointDef();
                                jd.body1 = body;
                                jd.body2 = entity.body;
                                jd.localAnchor1 = new Vec2(0, 0.21f);
                                jd.localAnchor2 = new Vec2();
                                jd.length = 0;
                                createJoint = game.physWorld.createJoint(jd);
                                hold = entity;
                                break;
                            }
//                            } else {
                            //                              color = Color.PINK;
                            //                        }



//                Contact contact = Contact.createContact(sensor, entity.body.m_shapeList);
//                if (contact != null) {
//                    color = Color.RED;
//                }
                        }

                    }
                }
                gripped = true;
            } else {
                // disjoin
                if (hold != null) {
                    game.physWorld.destroyJoint(createJoint);
                    createJoint = null;
                    hold = null;
                }
                gripped = false;
            }
        }
    }

    class Wheel implements Motor {

        final Vec2 relPos;
        // normalized vector
        final Vec2 relForward;
        float spd = 0;
        float angvelocity = 0;
        float torque = 0;
        float inertia = 1;
        float wheelFriction;
        // mass on this wheel
        float m = 5;
        float fg = m * g;

        public Wheel(Vec2 relPos, Vec2 relForward) {
            this.relPos = relPos;
            this.relForward = relForward;
        }

        /**
         *
         * @see http://www.gamedev.net/reference/programming/features/2dcarphys/page4.asp
         */
        void applyWheelForce(float timestep) {

            torque = spd;

            Vec2 forwardv = getAbsDir(); // world forward vector size 1
            Vec2 sidev = new Vec2(-forwardv.y, forwardv.x); // world side vector size 1

            Vec2 pos = getAbsPos(); // pos of event

            Vec2 wheelSpeed = forwardv.mul(-angvelocity);

            Vec2 vel = body.getLinearVelocityFromLocalPoint(relPos).add(wheelSpeed);

            float fwvel = Vec2.dot(vel, forwardv);

            float sidevel = Vec2.dot(vel, sidev);

            // lateral force - friction in opposite direction to motion
            Vec2 responseForce = sidev.mul(-10f * sidevel);
//
            responseForce = responseForce.add(forwardv.mul(-fwvel)); // forward force
////            responseForce = responseForce.add(forwardv.mul(spd)); // forward force


//            float fricForce = maxFrictionForceMVTF(m, vel.normalize(), timestep, wheelFriction);
//            Vec2 responseForce = vel.mul(fricForce);

            torque += fwvel;
//            torque += maxFrictionForceMVTF(m, fwvel, timestep, wheelFriction);

            angvelocity += torque; //*timestep

            // break force
//            if (spd == 0) {
//                float breakforce = 100;
//                if (angvelocity > 0) {
//                    angvelocity -= Math.min(angvelocity, breakforce); // damping
//                }
//                if (angvelocity < 0) {
//                    angvelocity += Math.max(-angvelocity, -breakforce); // damping
//                }
//            }

            body.applyForce(responseForce, pos);
        }

        void applyForce() {

            Vec2 force = getAbsDir();
            Vec2 pos = getAbsPos();

            body.applyForce(force.mul(spd), pos);

        }

        Vec2 getAbsDir() {
//            XForm xForm = body.getXForm();
//            xForm.position = new Vec2();
//
//            return XForm.mul(xForm, relForward);
            return body.getWorldVector(relForward);
        }

        Vec2 getAbsPos() {
//            return XForm.mul(body.getXForm(), relPos);
            return body.getWorldPoint(relPos);
        }

        void paintWheel(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            Stroke stroke = g2.getStroke();
            g2.setStroke(new BasicStroke(0.01f));

            Vec2 pos = getAbsPos();
            Vec2 end = getAbsDir().mul(spd).add(getAbsPos());

            g.setColor(Color.BLUE);

            g2.draw(new Line2D.Float(pos.x, pos.y, end.x, end.y));

            {
                g.setColor(Color.RED);
                Vec2 end2 = (getAbsPos().add(body.getLinearVelocityFromLocalPoint(relPos)));
                g2.draw(new Line2D.Float(pos.x, pos.y, end2.x, end2.y));

//                g.setColor(Color.GREEN);
//                Point end3 = toPoint(getAbsPos().add(new Vec2(angvelocity,0)));
//                g.drawLine(pos.x, pos.y, end3.x, end3.y);
            }

            g2.setStroke(stroke);

//            Point pos = toPoint(getAbsPos());
//            Point end = toPoint(getAbsDir().mul(spd).add(getAbsPos()));
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
////                Point end3 = toPoint(getAbsPos().add(new Vec2(angvelocity,0)));
////                g.drawLine(pos.x, pos.y, end3.x, end3.y);
//            }
        }

        public void setSpeed(double speed) {
            this.spd = (float) speed;
        }

        public void stop() {
            setSpeed(0);
        }
    }
}
