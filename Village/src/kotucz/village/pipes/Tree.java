package kotucz.village.pipes;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * @author Kotuc
 */
public class Tree {

    final Node node = new Node();

    public Tree(Material mat, PhysicsSpace physicsSpace) {

        final Vector3f origin = new Vector3f(7, 2, 0);


        SimplePipe leg1 = new SimplePipe(origin.add(new Vector3f(-1, -1, 0)), origin.add(new Vector3f(-1, -1, 1)), mat);
        leg1.getPhysics().setMass(1);
        node.attachChild(leg1.getSpatial());


        SimplePipe leg2 = new SimplePipe(origin.add(new Vector3f(-1, -1, 1)), origin.add(new Vector3f(-1, -1, 2)), mat);
        leg2.getPhysics().setMass(1);
        node.attachChild(leg2.getSpatial());


        {

            leg1.getPhysics().setSleepingThresholds(0, 0);

            physicsSpace.add(leg1.getPhysics());
            physicsSpace.add(leg2.getPhysics());

            HingeJoint hinge = new HingeJoint(leg1.getPhysics(), leg2.getPhysics(), new Vector3f(0, 0, 0.5f), new Vector3f(0f, 0, -0.5f), Vector3f.UNIT_Y, Vector3f.UNIT_Y);
            hinge.setCollisionBetweenLinkedBodys(false);
            hinge.enableMotor(true, 1, 0.1f);
            hinge.setLimit(1, -1);
            physicsSpace.add(hinge);

        }

    }

    public Node getNode() {

        return node;
    }


}
