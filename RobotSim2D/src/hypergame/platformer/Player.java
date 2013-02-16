package hypergame.platformer;

import hypergame.Entity;
import hypergame.Game;
import hypergame.eagleeye.Pawn;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.Joint;
import robot.input.KeyboardDriving;

import java.awt.*;

/**
 * @author Kotuc
 */
public class Player extends Entity {

    public static final boolean DRIVE_BY_KEYBOARD = true;

    KeyboardDriving keyboard = new KeyboardDriving();
    private boolean jumpIntention;
    private boolean grounded;
    private Fixture playerSensorFixture;

    public Player(Game game) {
        super();
        this.game = game;
        color = new Color(0.8f, 1.0f, 0.8f);
        createPhysic(game.getPhysWorld());
//        game.physWorld.;
        grounded = true;
    }

    @Override
    public void update(float timestep) {


//        setSpeedsLR(5, 0.5);


        // clear forces
//        body.m_force.setZero();
//        body.m_torque = 0;

//        if (DRIVE_BY_KEYBOARD) {
        Vec2 v = keyboard.actPlayer();
        System.out.println("v " + v);
//            body.m_force.set(v.mul(10));
//            body.applyForce( new Vec2(-10, 0), body.getWorldCenter());
//        body.applyForce(v.mul(10), body.getWorldCenter());
        body.applyForce(new Vec2(v.x*10, 0), body.getWorldCenter());
//        }

        if (v.y > 0.1) {
            jumpIntention = true;
            jump();
        }

//        lwheel.applyForce((float)(Math.random()-0.5));
//        rwheel.applyForce((float)(Math.random()-0.5));
//        lwheel.applyForce((float)(0.1f));
//        rwheel.applyForce((float)(0.09f));


    }

    public void jump() {
        // jump, but only when grounded
        if (jumpIntention) {
            jumpIntention = false;
            if (grounded) {
                grounded = false;
//                player.setLinearVelocity(vel.x, 0);
                System.out.println("jump before: " + body.getLinearVelocity());
//                player.setTransform(pos.x, pos.y + 0.01f, 0);
                body.applyLinearImpulse(new Vec2(0, 20), body.getWorldCenter());
                System.out.println("jump, " + body.getLinearVelocity());
            }
        }
    }


    private void createPhysic(World world) {
        //CircleDef sd = new CircleDef();

        BodyDef bd = new BodyDef();
//        bd.position.set(-1, 1);
        bd.position.set(5, 5f);
//        bd.linearDamping = 2f;
//        bd.angularDamping = 4f;

        body = world.createBody(bd);
        body.setBullet(true);
        body.setType(BodyType.DYNAMIC);
        body.setFixedRotation(true);

        {
            PolygonShape ps = new PolygonShape();
            ps.setAsBox(0.5f, 1f);

//        sd.radius = 0.1f;
            FixtureDef sd = new FixtureDef();
            sd.shape = ps;
            sd.density = 1f; // like watter
            sd.friction = 0.5f;

            body.createFixture(sd);
        }

        { // gripper sensor
            PolygonShape pd = new PolygonShape();
            pd.setAsBox(0.05f, 0.05f, new Vec2(1, 0), (float) Math.toRadians(45));
            FixtureDef fd = new FixtureDef();
            fd.shape = pd;
            fd.isSensor = true;
//        sd.radius = 0.1f;
            fd.density = 0f;
//            gripper.sensor =
                    body.createFixture(fd);
        }

        {
            CircleShape circle = new CircleShape();
            circle.m_radius= 0.45f;
            circle.m_p.set(0, -1.4f);
            FixtureDef fd = new FixtureDef();
            fd.shape = circle;
            fd.isSensor = true;
//        sd.radius = 0.1f;
            playerSensorFixture = body.createFixture(fd);
        }
// TODO mass
//     body.setMassFromShapes();
//        body.resetMassData();
//        body.m_mass = 10;

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
