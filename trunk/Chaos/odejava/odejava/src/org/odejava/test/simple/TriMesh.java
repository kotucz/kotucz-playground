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
import org.odejava.Geom;
import org.odejava.GeomBox;
import org.odejava.GeomPlane;
import org.odejava.GeomSphere;
import org.odejava.GeomTriMesh;
import org.odejava.Odejava;
import org.odejava.HashSpace;
import org.odejava.World;
import org.odejava.ode.Ode;

import org.odejava.collision.Contact;
import org.odejava.collision.JavaCollision;

/**
 * Creates simple Odejava simulation. Consists of a static plane (ground), box,
 * four spheres and trimesh object. TriMesh object is like a pyramid, but it is
 * upside down and it's bottom is open. Box and spheres are placed in the air
 * above the ground and trimesh object, as the simulation goes ahead they fall
 * inside the pyramid. Coordinate is selected in such way that Z is up.
 *
 * Note, this example uses high level API. Extend it with low level API as
 * needed.
 *
 * @see http://odejava.dev.java.net
 *
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 *
 */
public class TriMesh {

    // World that contains our simulation space
    World world;

    // A simulation space that contains objects (geoms and bodies)
    HashSpace space;

    // Store body objects locally as they are accessed frequently
    public Body sphere1, sphere2, sphere3, sphere4, box;

    GeomTriMesh pyramidGeom;
    GeomPlane groundGeom;

    // Collision
    JavaCollision collision;
    // Helper class to read / write collision data (DirectBuffer)
    Contact contact;
    Vector3f fdir1 = new Vector3f();
    Vector3f pos = new Vector3f();
    Vector3f normal = new Vector3f();

    /**
     * Create world and objects.
     *
     * Note, this example uses high level API. Extend it with low level API as
     * needed.
     *
     */
    public TriMesh() {
        // Initialize Odejava
        Odejava.getInstance();
        // Init ode world
        initWorld();
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

        // Use JavaCollision
        collision = new JavaCollision(world);
        // Setup DirectBuffers reader
        contact =
            new Contact(
                collision.getContactIntBuffer(),
                collision.getContactFloatBuffer());

        // Create space that contains all of our geomes / bodies
        space = new HashSpace();

        // Setup some surface parameters
        collision.setSurfaceMode(0);
        collision.setSurfaceMu(Float.MAX_VALUE);
    }

    /**
     * Initialize static objects. These geoms have no body and therefore
     * simulation steps do not change their position or orientation.
     *
     */
    private void initEnvironment() {
        // Create ground (static plane)
        groundGeom = new GeomPlane("ground", 0f, 0f, 1f, 0f);
        space.addGeom(groundGeom);
        //System.err.println("ground geomId=" + ground.nativeAdr);

        // Create TriMesh object from four triangles
        // This is like a pyramid but it's upside down and it's bottom is open

        // 5 points
        int vertexCount = 5 * 3;

        // 12 indexes
        int indexCount = 12;

        float[] vertices = new float[vertexCount];
        int[] indices = new int[indexCount];
        float size[] = new float[] { 25f, 25f, 20f };

        vertices[0 * 3 + 0] = -size[0];
        vertices[0 * 3 + 1] = -size[1];
        vertices[0 * 3 + 2] = size[2];

        vertices[1 * 3 + 0] = size[0];
        vertices[1 * 3 + 1] = -size[1];
        vertices[1 * 3 + 2] = size[2];

        vertices[2 * 3 + 0] = size[0];
        vertices[2 * 3 + 1] = size[1];
        vertices[2 * 3 + 2] = size[2];

        vertices[3 * 3 + 0] = -size[0];
        vertices[3 * 3 + 1] = size[1];
        vertices[3 * 3 + 2] = size[2];

        vertices[4 * 3 + 0] = 0;
        vertices[4 * 3 + 1] = 0;
        vertices[4 * 3 + 2] = 0;

        int i = 0;
        // 2
        indices[i++] = 1;
        indices[i++] = 2;
        indices[i++] = 4;

        // 1
        indices[i++] = 0;
        indices[i++] = 1;
        indices[i++] = 4;

        // 4
        indices[i++] = 3;
        indices[i++] = 0;
        indices[i++] = 4;

        // 3
        indices[i++] = 2;
        indices[i++] = 3;
        indices[i++] = 4;

        pyramidGeom = new GeomTriMesh("pyramid", vertices, indices);
        pyramidGeom.setPosition(0, 0, -2.5f);
        space.addGeom(pyramidGeom);
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

        // Create a box geom and set it to body (dynamic object)
        // Box x,y,z lengths are 2,1,1. Position box above the sphere, slightly
        // right to sphere
        float x = 4f, y = 4f;

        box = new Body("box", world, new GeomBox(2f, 1f, 1f));
        box.setPosition(x + 0f, y + 0f, 20f);
        box.adjustMass(1f);
        space.addBodyGeoms(box);

        // Create four spheres
        sphere1 = new Body("sphere1", world,new GeomSphere(3f));
        sphere1.setPosition(x + 2f, y + -1.01f, 30f);
        sphere1.adjustMass(0.2f);
        space.addBodyGeoms(sphere1);

        sphere2 = new Body("sphere2", world,new GeomSphere(1f));
        sphere2.setPosition(x + 1.01f, y + -1.01f, 25f);
        sphere2.adjustMass(1f);
        space.addBodyGeoms(sphere2);

        sphere3 = new Body("sphere3", world,new GeomSphere(1f));
        sphere3.setPosition(x + -1.01f, y + 1.01f, 25f);
        sphere3.adjustMass(1f);
        space.addBodyGeoms(sphere3);

        sphere4 = new Body("sphere4", world,new GeomSphere(1f));
        sphere4.setPosition(x + 1.01f, y + 1.01f, 35f);
        sphere4.adjustMass(1f);
        space.addBodyGeoms(sphere4);

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

        // Step space in ode world based on contact joints
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
            // ground or pyramid
            if (((contact.getGeomID1() == groundGeom.getNativeAddr())
                || (contact.getGeomID2() == groundGeom.getNativeAddr()))
                || ((contact.getGeomID1() == pyramidGeom.getNativeAddr())
                    || (contact.getGeomID2() == pyramidGeom.getNativeAddr()))) {
                continue;
            }

            // Check if contact with sphere1

            if ((contact.getGeomID2() == sphere1.getGeom().getNativeAddr())) {
                // Found interesting contact
                contact.getPosition(pos);
                contact.getNormal(normal);
                // Sphere1 is bouncy and teflon coated
                contact.setMode(Ode.dContactBounce | Ode.dContactApprox1);
                contact.setBounce(1.8f);
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
     * Get geoms list.
     *
     * @return Geoms list
     */
    public List getGeoms() {
        return space.getGeoms();
    }

    public Geom getGeom(String name) {
        return space.getGeom(name);
    }

    /**
     * Reset simulation. Not used in this class.
     *
     */
    public void resetSimulation() {
        box.setPosition(
            0 + (float) Math.random(),
            0 + (float) Math.random(),
            20f + (float) Math.random() * 5);
        box.resetRotationAndForces();

        sphere1.setPosition(
            0 + (float) Math.random(),
            0 + (float) Math.random(),
            30);
        sphere1.resetRotationAndForces();
        sphere2.setPosition(
            1 + (float) Math.random(),
            5 + (float) Math.random(),
            25);
        sphere2.resetRotationAndForces();
        sphere3.setPosition(
            2 + (float) Math.random(),
            (float) Math.random(),
            25);
        sphere3.resetRotationAndForces();
        sphere4.setPosition(
            3 + (float) Math.random(),
            (float) Math.random(),
            35);

    }
}
