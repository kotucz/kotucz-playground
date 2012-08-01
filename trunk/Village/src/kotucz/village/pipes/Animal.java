package kotucz.village.pipes;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import kotucz.village.game.Modeler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kotuc
 */
public class Animal {

    final float EPS = 0.00001f;

    final Node node = new Node();

    final List<Servo> servos = new ArrayList<Servo>();

    final List<Leg> legs = new ArrayList<Leg>();
    final Spatial torso;

    public Animal(Modeler mat, PhysicsSpace physicsSpace, Vector3f origin, boolean ghost) {

        torso = createBody(mat.matPipes, origin.add(0, 0, 2));

        if (ghost) {
            torso.getControl(RigidBodyControl.class).setMass(0); // kinematic
        }

        node.attachChild(torso);
        physicsSpace.add(torso);

        Vector3f[] vector3fs = {new Vector3f(-1, -1, 0), new Vector3f(1, -1, 0), new Vector3f(1, 1, 0), new Vector3f(-1, 1, 0)};


        for (int i = 0; i < 4; i++) {

            Leg leg = new Leg();

            final Quaternion rot = new Quaternion();
            final float angle = (float) ((0.25 + i * 0.5) * Math.PI);
//            rot.fromAngleAxis(angle, MyGame.UP);
            rot.fromAngleAxis(angle, Vector3f.UNIT_Z);

            SimplePipe leg1 = new SimplePipe(
                    origin.add(vector3fs[i]),
                    origin.add(vector3fs[i]).add(new Vector3f(0, 0, 1)),
                    mat.matPipes);
            leg1.getPhysics().setPhysicsRotation(rot);
            leg1.getPhysics().setMass(1);
            node.attachChild(leg1.getSpatial());


            SimplePipe leg2 = new SimplePipe(
                    origin.add(vector3fs[i]).add(new Vector3f(0, 0, 1)),
                    origin.add(vector3fs[i]).add(new Vector3f(0, 0, 2)),
                    mat.matPipes);
            leg2.getPhysics().setPhysicsRotation(rot);
            leg2.getPhysics().setMass(1);
            node.attachChild(leg2.getSpatial());

            SimplePipe leg3 = new SimplePipe(
                    origin.add(vector3fs[i]).add(new Vector3f(0, 0, 1.8f)),
                    origin.add(vector3fs[i]).add(new Vector3f(0, 0, 2.2f)),
                    mat.matPipes);
//            leg3.getPhysics().setPhysicsRotation(rot);
            leg3.getPhysics().setPhysicsRotation(new Quaternion());
            leg3.getPhysics().setMass(1);
            node.attachChild(leg3.getSpatial());


            leg1.getPhysics().setSleepingThresholds(0, 0);

            physicsSpace.add(leg1.getPhysics());
            physicsSpace.add(leg2.getPhysics());
            physicsSpace.add(leg3.getPhysics());

            HingeJoint kneeHinge = new HingeJoint(leg1.getPhysics(), leg2.getPhysics(), new Vector3f(0, 0, 0.5f), new Vector3f(0f, 0, -0.5f), Vector3f.UNIT_Y, Vector3f.UNIT_Y);
            kneeHinge.setCollisionBetweenLinkedBodys(false);
            kneeHinge.setLimit(-2.21f, 0.2f);
//            kneeHinge.enableMotor(true, -1, 0.1f);
            physicsSpace.add(kneeHinge);


            servos.add(leg.s1 = new Servo(kneeHinge));

            Transform t = new Transform();
            t.loadIdentity();
            t.setRotation(rot);


            HingeJoint hinge = new HingeJoint(leg2.getPhysics(), leg3.getPhysics(), new Vector3f(0, 0, 0.5f), new Vector3f(0f, 0, 0.f), Vector3f.UNIT_Y, t.transformVector(Vector3f.UNIT_Y, null));
//            HingeJoint hinge = new HingeJoint(leg2.getPhysics(), leg3.getPhysics(), new Vector3f(0, 0, 0.5f), new Vector3f(0f, 0, 0.f), Vector3f.UNIT_Y, new Vector3f( (float)Math.cos(angle+0.5*Math.PI), (float)Math.sin(angle + 0.5 * Math.PI), 0));
            hinge.setCollisionBetweenLinkedBodys(false);
            hinge.setLimit(-0.1f, 2.1f);
//            hinge.enableMotor(true, 1, 0.1f);
            physicsSpace.add(hinge);

            System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR " + i + " " + leg2.getPhysics().getPhysicsRotation() + "  - " + leg3.getPhysics().getPhysicsRotation());

            servos.add(leg.s2 = new Servo(hinge));


            HingeJoint hipHinge = new HingeJoint(leg3.getPhysics(), torso.getControl(RigidBodyControl.class), new Vector3f(0, 0, 0.0f), vector3fs[i], Vector3f.UNIT_Z, Vector3f.UNIT_Z);
            hipHinge.setCollisionBetweenLinkedBodys(false);
//            hipHinge.setLimit(-1f, 1);
            hipHinge.setLimit(-EPS, EPS);
//            hipHinge.enableMotor(true, 1, 0.1f);
            physicsSpace.add(hipHinge);

//            Servo e = new Servo(hipHinge);
//            e.zero = angle;
//            leg.s3 = e;
//            servos.add(e);


            legs.add(leg);

        }

        if (!ghost) {
            node.addControl(new AbstractControl() {

                @Override
                protected void controlUpdate(float tpf) {
                    control(tpf);
                }

                @Override
                protected void controlRender(RenderManager rm, ViewPort vp) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public Control cloneForSpatial(Spatial spatial) {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
        }

    }

    float off = 0;

    void control(float tpf) {

        off += tpf;

//        System.out.println("Animal " + off);

//        int i = 0;
//
//        for (Leg leg : legs) {
//
//            boolean even = (i % 2 != 0);
//
//            leg.s1.setPos((float) Math.sin(off));
//            leg.s2.setPos((float) Math.sin(off) + 1);
//            leg.s3.setPos((float) Math.sin(off) * (even ? 1 : -1));
//
//            i++;
//        }


        for (Servo servo : servos) {


            servo.control(tpf);


        }

    }


    Spatial createBody(Material mat, Vector3f posVector) {
//        this.dir = dir;

//        mat = mat.clone();
//        mat.setColor("Color", new ColorRGBA((float)Math.random(), (float)Math.random(),(float)Math.random(), 1f));

//        id = "Cube" + idGen.incrementAndGet();

        final float halfSize = 0.25f;

//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            final float halfSize = 1.25f;

        Box box = new Box(new Vector3f(-halfSize, -halfSize, -halfSize), new Vector3f(halfSize, halfSize, halfSize));
        Geometry geom = new Geometry("torso", box);
        geom.setMaterial(mat);
        geom.setLocalTranslation(new Vector3f(0, 0, 0));

//            Multitexture mtex2 = new Multitexture(256, 256);
//            box.setTexture(MyBox.FACE_TOP, mtex2.createRealSubtexture(16*(1), 11*16, 16*(1+1f), 12*16));


        geom.setLocalTranslation(posVector);


        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(halfSize, halfSize, halfSize)), 0);

        control.setMass(0.4f);

        geom.addControl(control);


        return geom;

    }

    public Node getNode() {


        return node;
    }


    class Servo {

        final HingeJoint hinge;

//        float zero;

        Float req = null;

        Servo(HingeJoint joint) {
            this.hinge = joint;
        }


        void control(float tpf) {

            if (req == null) {
                hinge.enableMotor(false, 0, 0);
            } else {

                float hingeAngle = getAngle();
                float diff = req - hingeAngle;

                if (diff>EPS) {


                    hinge.setLimit(req-EPS, req+EPS);

//            System.out.println("hinge " + hingeAngle + " " + diff);

                   //hinge.enableMotor(true, 12 * diff, 100.f);
                } else {
                    hinge.enableMotor(false, 0, 0);
                }
            }
        }

        public void setPos(Float pos) {
            this.req = pos;
        }

        public float getAngle() {
            return hinge.getHingeAngle();
        }


    }

    class Leg {
        Servo s1;
        Servo s2;
        Servo s3;


    }


}
