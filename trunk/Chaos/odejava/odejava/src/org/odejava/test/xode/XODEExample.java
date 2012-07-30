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
 * - Neither the name of William Denniss nor the names of
 *   contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
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

import java.io.IOException;
import java.util.List;
import javax.vecmath.*;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.odejava.*;
import org.odejava.collision.*;
import org.odejava.ode.Ode;

import org.odejava.test.Demonstration;
import org.odejava.xode.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XODEExample implements Demonstration {

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

    public XODEExample() throws Exception {

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

        XODEParserDOM xodepdom = new XODEParserDOM(false);
        XODERoot root = xodepdom.parse(new InputSource(XODEExample.class.getClassLoader().getResourceAsStream("data/truck.xode")));
        root.buildODEScene(world, space, "1");

        XODERoot rootGround = xodepdom.parse(new InputSource(XODEExample.class.getClassLoader().getResourceAsStream("data/ground.xode")));
        rootGround.buildODEScene(world, space, "1");

        
        Vector3f position = new Vector3f(1f,3f,25f);
        Matrix4f m1 = new Matrix4f ();
        m1.setIdentity();
        m1.setTranslation(position);
        root.setRootTransform(m1);
        root.buildODEScene(world, space, "2");

        System.out.println("\n====================\n\n\n");
        
        Document document = root.buildDocument();
        
        try {
            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            XMLSerializer output = new XMLSerializer(System.out, format);
            output.serialize(document);
          }
          catch (IOException e) {
            System.err.println(e);
          }
        
          
          System.out.println("\n\n\n====================\n\n\n");
          
        //Body boxBody = world.getBody("chassis");
        //boxBody.setPosition(new Vector3f(0,10,0));
    }

    public void step () {

        Body boxBody = world.getBody("1chassis");

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

//        System.out.println(boxBody.getPosition());
//      System.out.println(space.getGeom("geombox").getPosition());



    }

    public List getGeoms() {
        return space.getGeoms();
    }

    public final static void main(String [] args) throws Exception {
        XODEExample demo = new XODEExample();
        for (;;) {

            demo.step();

            // Sleep for a bit
            try {
                Thread.sleep(50);
            } catch (Exception e) {}
        }
    }

}
