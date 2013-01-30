/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package kotucz.village.plants;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import kotucz.village.game.MyGame;
import kotucz.village.pipes.SimplePipe;

/**
 * @author kotucz
 */
public class PlantsTest extends MyGame {


    private Plant ghostSpider;
    private Plant spider;

    protected void initEntities() {

        {

//            ghostSpider = new Plant(modeler, getPhysicsSpace(), new Vector3f(8 - 4, 8, 0.5f), true);
//            rootNode.attachChild(ghostSpider.getNode());
//
//            spider = new Plant(modeler, getPhysicsSpace(), new Vector3f(8 + 4, 8, 0.5f), false);
//            rootNode.attachChild(spider.getNode());


            Tree tree = new Tree(modeler.matPipes, getPhysicsSpace());
            rootNode.attachChild(tree.getNode());

//            createMotorTest();


//            getPhysicsSpace().add(simplePipe.getPhysics());

//            SimplePipe simplePipe2 = new SimplePipe(new Vector3f(4, 2, 5f), new Vector3f(6, 4, 2), modeler.matPipes);
//            rootNode.attachChild(simplePipe2.getSpatial());

        }
    }


    public void createMotorTest() {

        final Vector3f origin = new Vector3f(2, 2, 3);

        SimplePipe leg1 = new SimplePipe(
                origin,
                origin.add(new Vector3f(4, 0, 0)),
                modeler.matPipes);
        leg1.getPhysics().setPhysicsRotation(new Quaternion().fromAngleAxis((float) Math.PI / 2f, Vector3f.UNIT_Y));
        leg1.getPhysics().setMass(1);
        rootNode.attachChild(leg1.getSpatial());
        getPhysicsSpace().add(leg1.getPhysics());

        RigidBodyControl control1;

        {
            final float halfSize = 0.25f;

//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            final float halfSize = 1.25f;

            Box box = new Box(new Vector3f(-halfSize, -halfSize, -halfSize), new Vector3f(halfSize, halfSize, halfSize));
            Geometry geom = new Geometry("torso", box);
            geom.setMaterial(modeler.matPipes);
//        geom.setLocalTranslation(new Vector3f(0, 0, 0));

//            Multitexture mtex2 = new Multitexture(256, 256);
//            box.setTexture(MyBox.FACE_TOP, mtex2.createRealSubtexture(16*(1), 11*16, 16*(1+1f), 12*16));


            geom.setLocalTranslation(origin.add(2, 0, 0));


            geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

            RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(halfSize, halfSize, halfSize)), 0);

            control.setMass(0f);

            geom.addControl(control);


            rootNode.attachChild(geom);
            getPhysicsSpace().add(control);

            control1 = control;

        }

        HingeJoint kneeHinge = new HingeJoint(control1, leg1.getPhysics(), new Vector3f(0, 0, 0.f), new Vector3f(0f, 0, 0f), Vector3f.UNIT_Y, Vector3f.UNIT_Y);
        kneeHinge.setCollisionBetweenLinkedBodys(false);
//        kneeHinge.setLimit(-2.21f, 0.2f);
        kneeHinge.enableMotor(true, 6.14f, 100f);
        getPhysicsSpace().add(kneeHinge);

        {

            final float halfSize = 0.25f;

//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            final float halfSize = 1.25f;

            Box box = new Box(new Vector3f(-halfSize, -halfSize, -halfSize), new Vector3f(halfSize, halfSize, halfSize));
            Geometry geom = new Geometry("torso", box);
            geom.setMaterial(modeler.matPipes);
//        geom.setLocalTranslation(new Vector3f(0, 0, 0));

//            Multitexture mtex2 = new Multitexture(256, 256);
//            box.setTexture(MyBox.FACE_TOP, mtex2.createRealSubtexture(16*(1), 11*16, 16*(1+1f), 12*16));


            geom.setLocalTranslation(origin.add(4, 0, 3));


            geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

            RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(halfSize, halfSize, halfSize)), 0);

            control.setMass(5f);

            geom.addControl(control);


            rootNode.attachChild(geom);
            getPhysicsSpace().add(control);

        }


    }


    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);

//        RigidBodyControl control = ghostSpider.torso.getControl(RigidBodyControl.class);
//        Vector3f physicsLocation = control.getPhysicsLocation();
//        Vector3f joystick = getJoystick();
//        System.out.println("joystick " + joystick);
//        control.setPhysicsLocation(physicsLocation.add(joystick.mult(tpf)));

//        for (int i = 0; i < spider.servos.size(); i++) {
//            spider.servos.get(i).setPos(ghostSpider.servos.get(i).getAngle());
//            System.out.println("G " + ghostSpider.servos.get(i).getAngle() + " S " + spider.servos.get(i).getAngle());
//
//        }


    }

    public static void main(String args[]) {
        MyGame f = new PlantsTest();
        f.start();
    }


}
