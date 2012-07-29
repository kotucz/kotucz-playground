package kotucz.village.pipes;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.bullet.joints.PhysicsJoint;
import com.jme3.bullet.joints.Point2PointJoint;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import kotucz.village.common.Entities;
import kotucz.village.common.MyBox;
import kotucz.village.game.Modeler;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Multitexture1;

/**
 * @author Kotuc
 */
public class Animal {

    final Node node = new Node();

    public Animal(Modeler mat, PhysicsSpace physicsSpace) {

        final Vector3f origin = new Vector3f(10, 2, 0);

        Spatial torso = createBody(mat.matPipes, origin.add(0, 0, 2));

        node.attachChild(torso);
        physicsSpace.add(torso);

        Vector3f[] vector3fs = {new Vector3f(-1, -1, 0), new Vector3f(1, -1, 0), new Vector3f(1, 1, 0), new Vector3f(-1, 1, 0)};


        for (int i = 0; i < 4; i++) {
            SimplePipe leg1 = new SimplePipe(
                    origin.add(vector3fs[i]),
                    origin.add(vector3fs[i]).add(new Vector3f(0, 0, 1)),
                    mat.matPipes);
            leg1.getPhysics().setMass(1);
            node.attachChild(leg1.getSpatial());


            SimplePipe leg2 = new SimplePipe(
                    origin.add(vector3fs[i]).add(new Vector3f(0, 0, 1)),
                    origin.add(vector3fs[i]).add(new Vector3f(0, 0, 2)),
                    mat.matPipes);
            leg2.getPhysics().setMass(1);
            node.attachChild(leg2.getSpatial());


            leg1.getPhysics().setSleepingThresholds(0, 0);

            physicsSpace.add(leg1.getPhysics());
            physicsSpace.add(leg2.getPhysics());

            HingeJoint kneeHinge = new HingeJoint(leg1.getPhysics(), leg2.getPhysics(), new Vector3f(0, 0, 0.5f), new Vector3f(0f, 0, -0.5f), Vector3f.UNIT_Y, Vector3f.UNIT_Y);
            kneeHinge.setCollisionBetweenLinkedBodys(false);
//            kneeHinge.enableMotor(true, 1, 0.1f);
            kneeHinge.setLimit(-.1f, 0.2f);
            physicsSpace.add(kneeHinge);

            PhysicsJoint hipHinge = new Point2PointJoint(leg2.getPhysics(), torso.getControl(RigidBodyControl.class), new Vector3f(0, 0, 0.5f), vector3fs[i]);//, Vector3f.UNIT_Y, Vector3f.UNIT_Y);
            hipHinge.setCollisionBetweenLinkedBodys(false);
//            hipHinge.enableMotor(true, 1, 0.1f);
//            hipHinge.setLimit(-.1f, 2);
            physicsSpace.add(hipHinge);


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

        control.setMass(4);

        geom.addControl(control);


        return geom;

    }

    public Node getNode() {

        return node;
    }


}
