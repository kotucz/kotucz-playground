package hypergame.eagleeye;

import hypergame.Entity;
import hypergame.Game;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.Joint;
import robot.input.KeyboardDriving;
import robot.output.DiffWheels;

import java.awt.*;

/**
 * @author Kotuc
 */
public class Robot extends TableEntity implements DiffWheels {

    public static final boolean DRIVE_BY_KEYBOARD = true;
    public static final boolean DRIVE_AUTONOMOUSLY = true;


    Wheel lwheel = new Wheel(this, new Vec2(-0.12f, 0), new Vec2(0, 1));
    Wheel rwheel = new Wheel(this, new Vec2(0.12f, 0), new Vec2(0, 1));
    public Gripper gripper = new Gripper();
    RobotKeyboardController keyboard = new RobotKeyboardController(this);
    PathPlanner pathPlanner = new PathPlanner(this);

    public Robot(Game game) {
        super();
        this.game = game;
        color = new Color(0.8f, 1.0f, 0.8f);
        createPhysic(game.getPhysWorld());
//        game.physWorld.;
    }

    @Override
    public void update(float timestep) {

        if (DRIVE_BY_KEYBOARD) {
            keyboard.act();
        }

//        setSpeedsLR(5, 0.5);

        if (DRIVE_AUTONOMOUSLY) {
            pathPlanner.act();
        }

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
    public void paint(Graphics2D g) {
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
        body.setType(BodyType.DYNAMIC);

        {
            PolygonShape ps = new PolygonShape();
            ps.setAsBox(0.15f, 0.10f);

//        sd.radius = 0.1f;
            FixtureDef sd = new FixtureDef();
            sd.shape = ps;
            sd.density = 100f; // like watter
            sd.friction = 0.5f;

            body.createFixture(sd);
        }

        { // gripper sensor
            PolygonShape pd = new PolygonShape();
            pd.setAsBox(0.05f, 0.05f, new Vec2(0, 0.2f), (float) Math.toRadians(45));
            FixtureDef fd = new FixtureDef();
            fd.shape = pd;
            fd.isSensor = true;
//        sd.radius = 0.1f;
            fd.density = 0f;
            gripper.sensor = body.createFixture(fd);
//            gripper.sensor = body.createFixture(fd);
        }

// TODO mass
//     body.setMassFromShapes();
//        body.resetMassData();
//        body.m_mass = 10;

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

        private Fixture sensor;
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
                            /* TODO block
                            float distance = Distance.distance(x1, x2, sensor, body.getTransform(), entity.body.m_shapeList, entity.body.getTransform());

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
                            */
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

}
