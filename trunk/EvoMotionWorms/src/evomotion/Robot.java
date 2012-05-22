package evomotion;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.RotationalJointAxis;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tomas
 */
public class Robot implements Comparable<Robot> {

    final PhysicsSpace space;
    final Node rootNode;
    List<DynamicPhysicsNode> limbs;
    List<Joint> joints;
    DynamicPhysicsNode head;
    Brain brain = new Brain();

    public Robot(PhysicsSpace space, Node rootNode) {
        this.space = space;
        this.rootNode = rootNode;

    }

    void act(float robottime, float time) {
//        braintime += time;
//        if (braintime >= 6) {
//            braintime -= 6;
//        }
//        System.out.println("braintime " + braintime);
//        bendJoint(0, (float)(Math.sin(braintime)*3000f), time);
//        bendJoint(1, (float)(Math.sin(braintime+1)*3000f), time);
//        bendJoint(2, (float)(Math.sin(braintime+2)*3000f), time);
        int frame = Math.round(robottime) % 6;
        bendJoint(0, brain.get(0, frame) * 5000f, time);
        bendJoint(1, brain.get(1, frame) * 5000f, time);
        bendJoint(2, brain.get(2, frame) * 5000f, time);
    }

    void evaluate() {
        Vector3f center = head.getLocalTranslation();
        brain.score = Math.abs(center.x);//length();

        System.out.println(brain.score + ":" + brain);

        destroyRobot();
    }

    void createRobot(float z) {

        limbs = new ArrayList<DynamicPhysicsNode>();
        joints = new ArrayList<Joint>();

        head = space.createDynamicNode();

        rootNode.attachChild(head);
        head.createBox("torso");
        head.getLocalTranslation().set(0, 2, z);

        DynamicPhysicsNode limb2 = createLimb(head, 0, 2, z);
        limbs.add(limb2);
        DynamicPhysicsNode limb3 = createLimb(limb2, 2, 2, z);
        limbs.add(limb3);
        DynamicPhysicsNode limb4 = createLimb(limb3, 4, 2, z);
        limbs.add(limb4);
        DynamicPhysicsNode limb5 = createLimb(limb4, 6, 2, z);
        limbs.add(limb5);

    }

    void destroyRobot() {

        head.delete();

        for (DynamicPhysicsNode limb : limbs) {
            limb.delete();
        }
        for (Joint joint : joints) {
            joint.detach();
        }

    }

    private DynamicPhysicsNode createLimb(DynamicPhysicsNode attach, float jointx, float length, float z) {
        DynamicPhysicsNode limb = space.createDynamicNode();
        rootNode.attachChild(limb);
        //limb2.createSphere("limb2");
        limb.createBox("limb");
        //wheel1.createBox("limb2");
        limb.getLocalTranslation().set(jointx + length / 2, 2, z);
        limb.setLocalScale(new Vector3f(length, 1, 1));

        final Joint limbJoint = space.createJoint();
        joints.add(limbJoint);
        final RotationalJointAxis rotAxis = limbJoint.createRotationalAxis();
        rotAxis.setDirection(new Vector3f(0, 0, 10));
        //limbJoint.setSpring(0, 1);
        limbJoint.setSpring(1000, 1000);
        limbJoint.attach(limb, attach);
        limbJoint.setAnchor(new Vector3f(-length / 2f, 0, 0));
        return limb;
    }

    void bendJoint(int limbid, float force, float time) {
        final Vector3f direction = new Vector3f(0, 0, 1);
        final Vector3f appliedForce = new Vector3f();

        appliedForce.set(direction).multLocal(time * force);
        limbs.get(limbid).addTorque(appliedForce);

        appliedForce.set(direction).multLocal(-time * force);
        limbs.get(limbid + 1).addTorque(appliedForce);
    }

    public int compareTo(Robot o) {
        if (this.equals(o)) {
            return 0;
        } else {
            return (int) Math.signum(o.brain.score - brain.score);
        }
    }

    @Override
    public String toString() {
        return "" + brain.score;
    }
}
