/*
 * Copyright (c) 2004, William Denniss. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 *
 * Neither the name of William Denniss nor the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) A
 * RISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE
 *
 */
package org.odejava.test.xode;

import java.util.List;
import javax.vecmath.*;

import org.odejava.*;
import org.odejava.collision.*;
import org.odejava.ode.Ode;

import org.odejava.test.Demonstration;

/**
 *
 * @author William Denniss
 */
public class NormalTest implements Demonstration {

    private World world;
    private HashSpace space;
    private JavaCollision collision;
    public static final Vector3f gravity = new Vector3f(0f,-9.82f,0f);

    // Loads the Odejava library (optional but will though an error
    // sooner if it can't be found.
    static {
        Odejava.getInstance();
    }

    Contact contact;

    public NormalTest() throws Exception {

        /*
         * Setup Odejava Scene
         */

        //Odejava.getInstance();

        // Sets up the Odejava World
        world = new World();
        world.setGravity(gravity.x, gravity.y, gravity.z);
        world.setStepInteractions(10);
        world.setStepSize(0.05f);

        // Sets up the collision space
        space = new HashSpace();

        // Sets up collision properties
        collision = new JavaCollision(world);

        // Initialise a contact object.  This object is a collection of
        // collision data, populated everytime the collision system
        // processes the collisions.  It can be used to give feedback
        // to the user when a collision occures (for example the avatar
        // is hit.
        contact =
            new Contact(
                collision.getContactIntBuffer(),
                collision.getContactFloatBuffer());


        // Sets the surface properties - see the javadocs for more info
        collision.setSurfaceMu(1f);
        collision.setSurfaceBounce(0.14f);
        collision.setSurfaceBounceVel(0.1f);
        collision.setSurfaceMode(Ode.dContactBounce | Ode.dContactApprox1);
        collision.setSurfaceMode(0);
        collision.setSurfaceMu(Float.MAX_VALUE);


        Body boxBody = new Body("box", world, new GeomBox(4f, 4f, 4f));
        boxBody.setPosition(0.1f, 4f, 2f);
        boxBody.adjustMass(1f);
        space.addBodyGeoms(boxBody);

        Body sphereBody = new Body("sphere", world, new GeomSphere(2f));                sphereBody.setPosition(0.5f, 1.8f, 2.3f);
        sphereBody.adjustMass(1f);
        space.addBodyGeoms(sphereBody);

        // Static ground geometry
        PlaceableGeom planeGeom = new GeomBox("plane", 100f, 1f, 100f);
        planeGeom.setPosition(0.1f, -7f, 0f);
        space.add(planeGeom);





    }

    public void step () {

    //  Body boxBody = world.getBody("box");

        // Calculate the collissions
        collision.collide(space);

        for (int i=0; i < collision.getContactCount(); i++) {
            contact.setIndex(i);

            //if ((contact.getGeomID2()
        }

        // process collisions
        collision.applyContacts();

        // Advance the physics world one step
        world.step();

    //  System.out.println(boxBody.getPosition());
//      System.out.println(space.getGeom("geombox").getPosition());



    }

    public List getGeoms() {
        return space.getGeoms();
    }

    public final static void main(String [] args) throws Exception {
        NormalTest demo = new NormalTest();
        for (;;) {

            demo.step();

            // Sleep for a bit
            try {
                Thread.sleep(50);
            } catch (Exception e) {}
        }
    }

}
