package kotucz.village.plants;

import com.bulletphysics.dynamics.constraintsolver.ConeTwistConstraint;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.joints.ConeJoint;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kotuc
 */
public class Plant {

    final Node node = new Node();

    final List<Stem> stems = new ArrayList<Stem>();
    private final Material mat;
    private final PhysicsSpace physicsSpace;


    float off = -1;
    int next = 1;


    public Plant(Material mat, PhysicsSpace physicsSpace, Vector3f vector3f) {
        this.mat = mat;
        this.physicsSpace = physicsSpace;

        final Vector3f origin = vector3f;


        Transform transform = new Transform();
        transform.setTranslation(vector3f);
        transform.setRotation(new Quaternion().fromAngleAxis((float)Math.PI*0.5f, new Vector3f(0, 1, 0)));

//        Stem leg1 = new Stem(origin.add(new Vector3f(0, 0, 0)), origin.add(new Vector3f(0, 0, 1)), mat);
        Stem leg1 = new Stem(transform, 1, mat);
        leg1.getPhysics().setMass(0);  // static
//        leg1.getPhysics().setMass(1);  // dynamic
        node.attachChild(leg1.getSpatial());
        stems.add(leg1);
        physicsSpace.add(leg1.getPhysics());


//        Stem leg2 = new Stem(origin.add(new Vector3f(0, 0, 1)), origin.add(new Vector3f(0, 0, 2)), mat);
        Stem leg2 = leg1.extend(new Vector3f());
        leg2.getPhysics().setMass(1);
        node.attachChild(leg2.getSpatial());
        stems.add(leg2);
        physicsSpace.add(leg2.getPhysics());

        joint(leg1, leg2);





//        Stem legx = new Stem(transform, 2, mat);
//        legx.getPhysics().setMass(1);
//        node.attachChild(legx.getSpatial());
//        stems.add(legx);


//        Stem leg3 = leg2.extend(new Vector3f(1, 0, 0));
//        leg3.getPhysics().setMass(1);
//        node.attachChild(leg3.getSpatial());
//
//        Stem leg4 = leg3.extend(new Vector3f(1, 0, 0));
//        leg4.getPhysics().setMass(1);
//        node.attachChild(leg4.getSpatial());
//
//        Stem leg5 = leg4.extend(new Vector3f(1, 0, 0));
//        leg5.getPhysics().setMass(1);
//        node.attachChild(leg5.getSpatial());

        {

//            leg1.getPhysics().setSleepingThresholds(0, 0);
//
//
//            HingeJoint hinge = new HingeJoint(leg1.getPhysics(), leg2.getPhysics(), new Vector3f(0, 0, 0.5f), new Vector3f(0f, 0, -0.5f), Vector3f.UNIT_Y, Vector3f.UNIT_Y);
//            hinge.setCollisionBetweenLinkedBodys(false);
////            hinge.enableMotor(true, 1, 0.1f);
//            hinge.setLimit(-0.1f, 0.1f);
//            physicsSpace.add(hinge);

        }

        {
            // connect to ground
//            HingeJoint hinge = new HingeJoint(null, leg1.getPhysics(), new Vector3f(6, 1, 0), new Vector3f(0f, 0, -0.5f), Vector3f.UNIT_Y, Vector3f.UNIT_Y);
//            hinge.setCollisionBetweenLinkedBodys(false);
////            hinge.enableMotor(true, 1, 0.1f);
//            hinge.setLimit(-0.1f, 0.1f);
//            physicsSpace.add(hinge);
        }

    }

    public Node getNode() {
        return node;
    }



    void control(float tpf) {

        off += tpf;




//        System.out.println("Plant " + off);

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

        if (next < off) {
            grow();
            next += 1;
        }


    }

    private void grow() {
//        Stem lastStem = stems.get(stems.size() - 1);
        Stem lastStem = stems.get(next);

        Stem stem1 = growBranch(lastStem, new Vector3f(0.00001f, 0.0f, 0));
        joint(lastStem, stem1);
//        Stem stem1 = growBranch(lastStem, new Vector3f(0.2f, 0.2f, 1));
//        growBranch(lastStem, new Vector3f(-0.2f, -0.2f, -1));
    }

    private Stem growBranch(Stem lastStem, Vector3f localVector) {
        Stem newStem = lastStem.extend(localVector);
        newStem.getPhysics().setMass(1);
        node.attachChild(newStem.getSpatial());
        stems.add(newStem);
        physicsSpace.add(newStem.getPhysics());
        return newStem;
    }


    void joint(Stem leg1, Stem leg2) {
        {

            leg1.getPhysics().setSleepingThresholds(0, 0);

            HingeJoint joint = new HingeJoint(leg1.getPhysics(), leg2.getPhysics(), new Vector3f(0, 0, 0.5f), new Vector3f(0f, 0, -0.5f), Vector3f.UNIT_Y, Vector3f.UNIT_Y);
//            ConeJoint joint = new ConeJoint(leg1.getPhysics(), leg2.getPhysics(), new Vector3f(0, 0, 0.5f), new Vector3f(0f, 0, -0.5f));
            joint.setCollisionBetweenLinkedBodys(false);
//            joint.enableMotor(true, 1, 0.1f);
            joint.setLimit(-0.01f, 0.01f);
//            joint.setLimit(0.1f, 0.1f, 0.1f);
//            ((ConeTwistConstraint)joint.getObjectId()).setLimit(0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.9f);
            physicsSpace.add(joint);

        }
    }


}
