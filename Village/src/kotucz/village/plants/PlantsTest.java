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

import com.jme3.math.Vector3f;
import kotucz.village.game.MyGame;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kotucz
 */
public class PlantsTest extends MyGame {

    private List<Plant> trees = new ArrayList<Plant>();

    protected void initEntities() {

        {

//            ghostSpider = new Plant(modeler, getPhysicsSpace(), new Vector3f(8 - 4, 8, 0.5f), true);
//            rootNode.attachChild(ghostSpider.getNode());
//
//            spider = new Plant(modeler, getPhysicsSpace(), new Vector3f(8 + 4, 8, 0.5f), false);
//            rootNode.attachChild(spider.getNode());


            Plant tree = new Plant(modeler.matPipes, getPhysicsSpace(), new Vector3f(6, 6, 2));
            rootNode.attachChild(tree.getNode());
            trees.add(tree);

            {
//            Plant tree2 = new Plant(modeler.matPipes, getPhysicsSpace(), new Vector3f(10, 5, 0));
//            rootNode.attachChild(tree2.getNode());
//            trees.add(tree2);
            }

//            createMotorTest();


//            getPhysicsSpace().add(simplePipe.getPhysics());

//            Stem simplePipe2 = new Stem(new Vector3f(4, 2, 5f), new Vector3f(6, 4, 2), modeler.matPipes);
//            rootNode.attachChild(simplePipe2.getSpatial());

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

        for (Plant tree : trees) {

            tree.control(tpf);
        }


//        tree2.control(tpf);


    }

    public static void main(String args[]) {
        MyGame f = new PlantsTest();
        f.start();
    }


}
