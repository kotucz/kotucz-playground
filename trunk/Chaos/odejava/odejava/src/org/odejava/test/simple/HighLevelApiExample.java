/*
 * Open Dynamics Engine for Java (odejava) Copyright (c) 2004, Jani Laakso, All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution. Neither the name of the odejava nor the
 * names of its contributors may be used to endorse or promote products derived
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
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.odejava.test.simple;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Vector3f;

import org.odejava.Body;
import org.odejava.GeomBox;
import org.odejava.GeomPlane;
import org.odejava.GeomSphere;
import org.odejava.Odejava;
import org.odejava.HashSpace;
import org.odejava.World;
import org.odejava.ode.Ode;
import org.odejava.collision.Contact;
import org.odejava.collision.JavaCollision;

/**
 * Creates simple Odejava simulation. Consists of a static plane (ground), box
 * and sphere object. Box and sphere are placed in the air above the ground, as
 * the simulation goes ahead they fall to ground and collide to each other.
 * Coordinate is selected in such way that Z is up.
 *
 * Note, this example uses high level API. Extend it with low level API as
 * needed.
 *
 * @see http://odejava.dev.java.net
 *
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 *
 */
public class HighLevelApiExample {

    // World that contains our simulation space
    World world;

    // A simulation space that contains objects (geoms and bodies)
    HashSpace space;

    // Store body objects locally as they are accessed frequently
    Body sphere, box;

    // Collision
    JavaCollision collision;
    // Helper class to read / write collision data (DirectBuffer)
    Contact contact;
    Vector3f fdir1 = new Vector3f();
    Vector3f pos = new Vector3f();
    Vector3f normal = new Vector3f();
    int boxId, sphereId;
    int groundId;

    /**
     * Standalone test application that prints simulation results to console.
     */
    public static void main(String[] args) {
        HighLevelApiExample app = new HighLevelApiExample();
        // World is ready, simulate it for 1000 steps
        app.simulate(1000);
        // End application
        app.cleanup();
    }

    /**
     * Create world and objects.
     *
     * Note, this example uses high level API. Extend it with low level API as
     * needed.
     *
     */
    public HighLevelApiExample() {
        // Initialize Odejava
        Odejava.getInstance();
        // Init ode world
        initWorld();
        // Use JavaCollision
        collision = new JavaCollision(world);
        // Setup DirectBuffers reader
        contact =
            new Contact(
                collision.getContactIntBuffer(),
                collision.getContactFloatBuffer());
        // Init static (unmovable) objects
        initEnvironment();

        // Init dynamic objects
        initObjects();
    }

    /**
     * Initialize ODE world.
     *
     */
    private void initWorld() {
        // Create ODE world
        world = new World();
        // Set gravity along Z axis
        world.setGravity(0f, 0f, -0.2f);
        // Set max interactions per step (bigger is more accurate, but slower)
        world.setStepInteractions(10);
        // Set step size (smaller is more accurate, but slower)
        world.setStepSize(0.05f);

        // Create space that contains all of our geoms / bodies
        space = new HashSpace();


        // Setup some surface parameters: maximum friction
        //Odejava.setSurfaceMu(Float.MAX_VALUE);
    }

    /**
     * Initialize static objects. These geoms have no body and therefore
     * simulation steps do not change their position or orientation.
     *
     */
    private void initEnvironment() {
        // Create ground (static plane)
        GeomPlane groundGeom = new GeomPlane(0f, 0f, 1f, 0f);
        groundId = groundGeom.getNativeAddr();
        space.add(groundGeom);
    }

    /**
     * Initialize dynamic objects. These geoms have an body and simulation
     * steps apply various forces to them. Simulation may move these objects or
     * change their orientation as they e.g. collide to each other.
     *
     */
    private void initObjects() {
        // Create a sphere geom and set it to body (dynamic object)
        // Sphere radius is 1. Position sphere above the ground
        sphere = new Body("sphere",world, new GeomSphere(1f));
        space.addBodyGeoms(sphere);
        sphere.setPosition(0f, 0f, 2f);
        sphere.adjustMass(1f);
        sphereId = sphere.getGeom().getNativeAddr();

        // Create a box geom and set it to body (dynamic object)
        // Box x,y,z lengths are 2,1,1. Position box above the sphere, slightly
        // right to sphere
        box = new Body("box",world, new GeomBox(2f, 1f, 1f));
        space.addBodyGeoms(box);
        box.setPosition(0.1f, 0f, 5f);
        box.adjustMass(1f);
        boxId = box.getGeom().getNativeAddr();
    }

    /**
     * Simulate Odejava environment and objects by stepping the world ahead.
     *
     * @param steps
     *            to simulate
     */
    public void simulate(int steps) {
        int step = 0;
        while (step++ < steps) {
            // Step space in ode world
            step();
            // Print object position and quaternion every tenth step
            if ((step % 10) == 0) {
                System.out.println("Step " + step);
                // Print sphere position/quat using sphere class directly
                System.out.print("  Box pos=" + box.getPosition());
                System.out.println(", quaternion=" + box.getQuaternion());
                // Another way of getting position and quaternion data
                System.out.print(
                    "  Sphere pos=" + world.getBody("sphere").getPosition());
                System.out.println(
                    ", quaternion=" + world.getBody("sphere").getQuaternion());
            }
        }
    }

    /**
     * Step simulation ahead.
     *
     */
    public void step() {
        // Collide objects in given space
        collision.collide(space);

        // Read & modify contact information
        iterateContacts();

        // Add all contacts to contact jointGroup
        collision.applyContacts();

        world.stepFast();
    }

    /*
     * Iterate contacts, read and modify
     */
    private void iterateContacts() {
        float depth = 0;
        Vector3f pos = new Vector3f();
        Vector3f normal = new Vector3f();
        for (int i = 0; i < collision.getContactCount(); i++) {
            contact.setIndex(i);

            // Use default surface contact values for any geom that hits
            // ground
            if ((contact.getGeomID1() == groundId)
                || (contact.getGeomID2() == groundId)) {
                continue;
            }

            // Check if contact with chassis
            if ((contact.getGeomID2() == sphereId)) {
                // Found interesting contact
                contact.getPosition(pos);
                contact.getNormal(normal);
                contact.setMode(Ode.dContactBounce | Ode.dContactApprox1);
                contact.setBounce(0.24f);
                contact.setBounceVel(0.2f);
                contact.setMu(0f);
                System.err.println(
                    "A: "
                        + i
                        + " Sphere hits geom "
                        + contact.getGeomID2()
                        + "\n  d="
                        + contact.getDepth()
                        + "\n  pos="
                        + pos
                        + "\n  normal="
                        + normal);
            }

            // Check if contact with chassis
            if ((contact.getGeomID2() == boxId)) {
                // Found interesting contact
                contact.getPosition(pos);
                contact.getNormal(normal);
                // If chassis hits any geom make it bounce hard
                contact.setMode(Ode.dContactBounce | Ode.dContactApprox1);
                contact.setBounce(1.25f);
                contact.setBounceVel(0.2f);
                contact.setMu(0f);

                System.err.println(
                    "B: "
                        + i
                        + " Box hits geom "
                        + contact.getGeomID1()
                        + "\n  d="
                        + contact.getDepth()
                        + "\n  pos="
                        + pos
                        + "\n  normal="
                        + normal);
            }
        }
    }

    /**
     * Clean up native ODE objects. Call this before ending your Java program.
     */
    public void cleanup() {
        space.delete();
        collision.delete();
        world.delete();
        Ode.dCloseODE();
    }

    /**
     * Get geoms. Not used in this class.
     *
     * @return Geom list
     */
    public List getGeoms() {
        return space.getGeoms();
    }

    /**
     * Reset simulation. Not used in this class.
     *
     */
    public void resetSimulation() {
        sphere.setPosition(0f, 0f, 2f);
        sphere.resetRotationAndForces();
        box.setPosition(0.1f, 0f, 5f);
        box.resetRotationAndForces();
    }
}
