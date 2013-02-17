package hypergame.platformer;

import hypergame.Entity;
import hypergame.Game;
import hypergame.eagleeye.Pawn;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.ManifoldPoint;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import robot.input.KeyboardDriving;

import java.awt.*;
import java.util.Arrays;


/**
 * @author Kotuc
 */
public class Player extends Entity implements ContactListener {

    public static final boolean DRIVE_BY_KEYBOARD = true;


    private boolean jumpIntention;
    private Fixture legsFixture;
    private Vec2 goDir = new Vec2();

    public Player(Game game, Vec2 pos) {
        super();
        this.game = game;
        color = new Color(0.8f, 1.0f, 0.8f);
        createPhysic(game.getPhysWorld(), pos);
//        game.physWorld.;
    }

    @Override
    public void update(float timestep) {


//        setSpeedsLR(5, 0.5);


        // clear forces
//        body.m_force.setZero();
//        body.m_torque = 0;

//        if (DRIVE_BY_KEYBOARD) {

        System.out.println("v " + goDir);
//            body.m_force.set(v.mul(10));
//            body.applyForce( new Vec2(-10, 0), body.getWorldCenter());
//        body.applyForce(v.mul(10), body.getWorldCenter());

//        }

        if (Math.abs(goDir.x) > 0.1) {
            legsFixture.setFriction(0);
        } else {
            legsFixture.setFriction(0.2f);
        }
        if (goDir.y > 0.1) {
            jumpIntention = true;
        }
//        System.out.println(body.isAwake());
//        legsFixture.setSensor(v.y < 0.1);

//        lwheel.applyForce((float)(Math.random()-0.5));
//        rwheel.applyForce((float)(Math.random()-0.5));
//        lwheel.applyForce((float)(0.1f));
//        rwheel.applyForce((float)(0.09f));


        if (grounded()) {
            color = new Color(0.8f, 1.0f, 0.8f);
        } else {
            color = new Color(0.6f, .8f, 0.6f);
        }

    }

    public void jump() {
        // jump, but only when grounded
        if (jumpIntention) {
            jumpIntention = false;
            if (grounded()) {
//                player.setLinearVelocity(vel.x, 0);
                // apply action
                System.out.println("jump before: " + body.getLinearVelocity());
//                player.setTransform(pos.x, pos.y + 0.01f, 0);
                Vec2 impulse = new Vec2(0, 20);
                body.applyLinearImpulse(impulse, body.getWorldCenter());
                System.out.println("jump, " + body.getLinearVelocity());

                // apply reaction
                ground.applyLinearImpulse(impulse.negate(), body.getWorldCenter());
//                ground = null;
            }
        }
    }

    private boolean grounded() {
        return ground != null;
    }


    private void createPhysic(World world, Vec2 pos) {
        //CircleDef sd = new CircleDef();

        BodyDef bd = new BodyDef();
//        bd.position.set(-1, 1);
        bd.position.set(pos);
//        bd.linearDamping = 2f;
//        bd.angularDamping = 4f;

        body = world.createBody(bd);
        body.setBullet(true);
        body.setSleepingAllowed(false);
        body.setType(BodyType.DYNAMIC);
        body.setFixedRotation(true);

        {
            PolygonShape ps = new PolygonShape();
            ps.setAsBox(0.45f, 0.5f);

//        sd.radius = 0.1f;
            FixtureDef sd = new FixtureDef();
            sd.shape = ps;
            sd.density = 1f; // like watter
//            sd.friction = 0.5f;

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
            circle.m_radius = 0.5f;
            circle.m_p.set(0, -0.5f);
            FixtureDef fd = new FixtureDef();
            fd.shape = circle;
//            fd.isSensor = true;
            fd.density = 1f;
//        sd.radius = 0.1f;
            legsFixture = body.createFixture(fd);
        }
// TODO mass
//     body.setMassFromShapes();
//        body.resetMassData();
//        body.m_mass = 10;


    }


//    private boolean isPlayerGrounded(float deltaTime) {
//        groundedPlatform = null;
//        List<Contact> contactList = world.getContactList();
//        for(int i = 0; i < contactList.size(); i++) {
//            Contact contact = contactList.get(i);
//            if(contact.isTouching() && (contact.getFixtureA() == legsFixture ||
//                    contact.getFixtureB() == legsFixture)) {
//
//                Vec2 pos = body.getPosition();
//                WorldManifold manifold = contact.getWorldManifold();WorldManifold();
//                boolean below = true;
//                for(int j = 0; j < manifold.points.length; j++) {
//                    below &= (manifold.points[j].y < pos.y - 1.5f);
//                }

//                if(below) {
//                    if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("p")) {
//                        groundedPlatform = (MovingPlatform)contact.getFixtureA().getBody().getUserData();
//                    }
//
//                    if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("p")) {
//                        groundedPlatform = (MovingPlatform)contact.getFixtureB().getBody().getUserData();
//                    }
//                    return true;
//                }
//
//                return false;
//            }
//        }
//        return false;
//    }

    Body ground = null;

    @Override
    public void beginContact(Contact contact) {
//        Fixture other;
//        if (contact.getFixtureA() == legsFixture) {
//            other = contact.getFixtureB();
//        } else if (contact.getFixtureB() == legsFixture) {
//            other = contact.getFixtureA();
//        } else {
//            return; // not interested
//        }
//        System.out.println("Contact legs begin " + contact.isTouching());
//        ground = other.getBody();
    }

    @Override
    public void endContact(Contact contact) {
//        Fixture other;
//        if (contact.getFixtureA() == legsFixture) {
//            other = contact.getFixtureB();
//        } else if (contact.getFixtureB() == legsFixture) {
//            other = contact.getFixtureA();
//        } else {
//            return; // not interested
//        }
//        System.out.println("Contact legs end " + contact.isTouching());
//        ground = null;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture other;
//        if (contact.getFixtureA().getBody() == body) {
        if (contact.getFixtureA() == legsFixture) {
            System.out.println("Contact legs A");
            other = contact.getFixtureB();
        } else if (contact.getFixtureB() == legsFixture) {
//        } else if (contact.getFixtureB().getBody() == body) {
            System.out.println("Contact legs B");
            other = contact.getFixtureA();
        } else {
            return; // not interested
        }
        System.out.println("Contact legs " + contact + " " + other);
        ground = other.getBody();

        // walking


        ManifoldPoint[] points = contact.getManifold().points;

//        System.out.println("points " + Arrays.toString(points));
        System.out.println("points ");

        for (ManifoldPoint point : points) {
            System.out.print(point.localPoint + ", ");
        }


        if (points.length > 0) {
            Vec2 force = new Vec2(goDir.x * 20, 0);
//            points[0].tangentImpulse = force.x;
            body.applyForce(force, body.getWorldCenter());


            // TODO when jbox2d release 2.3 use Contact.setTangentVelocity
//            contact.setTan
//            ground.applyForce(force.negate(), points[0].);
            ground.applyForce(force.negate(), body.getWorldCenter());
        }


        jump();

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setTimeFrame(TimeFrame timeFrame) {
        this.goDir.set(timeFrame.goVec);
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
//                            } else {
                            //                              color = Color.PINK;
                            //                        }
                                          */

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
