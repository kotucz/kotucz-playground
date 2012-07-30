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
package org.odejava.test.car;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.odejava.Body;
import org.odejava.Geom;
import org.odejava.GeomBox;
import org.odejava.GeomPlane;
import org.odejava.GeomSphere;
import org.odejava.GeomTriMesh;
import org.odejava.JointGroup;
import org.odejava.JointHinge2;
import org.odejava.Odejava;
import org.odejava.PlaceableGeom;
import org.odejava.HashSpace;
import org.odejava.World;
import org.odejava.collision.Contact;
import org.odejava.collision.JavaCollision;

import org.odejava.ode.*;

/**
 * Creates Open Dynamics Engine world. Plug this demonstration class into any
 * 3d rendered (Xith3d, Openmind, jME, Java3D).
 *
 * @see http://q12.org/ode/ for ODE documentation
 *
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 *
 */
public class CarTerrain {

    boolean inStep;

    World world;
    // This simulation has single space
    HashSpace space;
    JointGroup jointGroup;

    // Store wheel hinges locally as they are accessed frequently
    JointHinge2 hingeFrontLeft;
    JointHinge2 hingeFrontRight;
    JointHinge2 hingeBackLeft;
    JointHinge2 hingeBackRight;

    Vector3f gravity = new Vector3f(0f, 0f, -9.81f);
    Vector4f groundv = new Vector4f(0f, 0f, 1f, 0f);

    // Car parameters
    float carDensity = 1f;
    float wheelLength = 1f;
    float wheelRadius = 4f;
    float chassisSizeX = 15f;
    float chassisSizeY = 30f;
    float chassisSizeZ = 1.5f;
    float wheelChassisDistance = 0.5f;
    float bumperChassisDistance = 2.5f;
    float chassisMass = 2f;
    float wheelMass = 0.25f;
    float suspensionErp = 0.8f;
    float suspensionCfm = 0.08f;

    float gasPedal = 0f;
    float speed = 0f;
    float maxSpeed = 35f;
    float minSpeed = -5f;

    boolean allbrake = false;
    boolean handbrake = false;

    float steeringWheel = 0f;
    float steering = 0f;
    float steeringLimit = (float) Math.PI / 12;
    float steeringDeadzone = 0.00f;

    // Boxes
    int boxCount = 10;
    float boxMass = 0.05f;

    // Spheres
    int sphereCount = 20;
    float sphereRadius = 10f;
    float sphereMass = 0.1f;

    // Collision
    JavaCollision collision;
    // Helper class to read / write collision data (DirectBuffer)
    Contact contact;
    Vector3f fdir1 = new Vector3f();
    Vector3f pos = new Vector3f();
    Vector3f normal = new Vector3f();
    // Store geom ids used by collision checks
    int terrainId, groundId;
    int chassisId;
    int wheelFLId, wheelFRId, wheelBLId, wheelBRId;

    /**
     * Create world and objects.
     *
     */
    public CarTerrain() {
        initWorld();
        initObjects();
        setSimulation();
    }

    /**
     * Initialize ODE world.
     *
     */
    private void initWorld() {
        // Initialize Odejava
        Odejava.init();

        // Create ODE world
        world = new World();
        world.setGravity(gravity.x, gravity.y, gravity.z);
        world.setStepInteractions(10);
        world.setStepSize(0.05f);

        // Use JavaCollision
        collision = new JavaCollision(world);

        // Setup DirectBuffers reader
        contact =
            new Contact(
                collision.getContactIntBuffer(),
                collision.getContactFloatBuffer());

        // Create space that contains all of our geoms / bodies
        space = new HashSpace();

        // Create joint group used to store car's joints
        jointGroup = new JointGroup();

        collision.setSurfaceMu(1.5f);
        collision.setSurfaceBounce(0.24f);
        collision.setSurfaceBounceVel(0.2f);
        collision.setSurfaceMode(Ode.dContactBounce | Ode.dContactApprox1);

        // initialize static objects to world
        initStaticObjects();
    }

    /**
     * Initialize all static objects. These objects have no body.
     *
     */
    private void initStaticObjects() {
        // Create bridge
        createBridge();

        // Create ground (static plane)
        Geom groundGeom =
            new GeomPlane(groundv.x, groundv.y, groundv.z, groundv.w);
        groundId = groundGeom.getNativeAddr();
        space.addGeom(groundGeom);
    }

    public void createTriMesh(String name, float[] vertex, int[] index) {
        GeomTriMesh triMesh = new GeomTriMesh(name, vertex, index);
        //terrainId = triMesh.getNativeAddr();
        space.addGeom(triMesh);
    }

    private void createBridge() {
        float x, y, z;
        PlaceableGeom g;
        g = new GeomBox("bridge", 1500, 150, 1);
        g.setPosition(new Vector3f(-915 + 750, 655 + 75, 381f));
        space.addGeom(g);
    }

    /**
     * Initialize objects.
     *
     */
    private void initObjects() {
        initCar();
        initBoxesAndSpheres();
    }

    /**
     * Initialize car with four wheels. Create geoms with bodies, bodies are
     * dynamic objects that have position and rotation updated after each
     * World.step() call.
     *
     */
    private void initCar() {
        Body b;

        // Create car wheels
        b = new Body("wheelFrontLeft", world, new GeomSphere(wheelRadius));
        b.adjustMass(wheelMass);
        wheelFLId = b.getGeom().getNativeAddr();
        space.addBodyGeoms(b);

        b = new Body("wheelFrontRight", world, new GeomSphere(wheelRadius));
        b.adjustMass(wheelMass);
        wheelFRId = b.getGeom().getNativeAddr();
        space.addBodyGeoms(b);

        b = new Body("wheelBackLeft", world, new GeomSphere(wheelRadius));
        b.adjustMass(wheelMass);
        wheelBLId = b.getGeom().getNativeAddr();
        space.addBodyGeoms(b);

        b = new Body("wheelBackRight",world,  new GeomSphere(wheelRadius));
        b.adjustMass(wheelMass);
        wheelBRId = b.getGeom().getNativeAddr();
        space.addBodyGeoms(b);

        // Create car chassis
        b =
            new Body(
                "chassis",
                world,
                new GeomBox(chassisSizeX, chassisSizeY, chassisSizeZ));
        b.adjustMass(chassisMass);
        chassisId = b.getGeom().getNativeAddr();
        space.addBodyGeoms(b);
    }

    /**
     * Initializes bunch of boxes and few spheres
     *
     */

    private void initBoxesAndSpheres() {
        Body b;
        float sizeX, sizeY, sizeZ;
        for (int i = 0; i < boxCount; i++) {
            // big box
            sizeX = 20;
            sizeY = 20;
            sizeZ = 20;
            b = new Body("box" + i, world, new GeomBox(sizeX, sizeY, sizeZ));
            b.adjustMass(boxMass);
            space.addBodyGeoms(b);
        }
        for (int i = 0; i < sphereCount; i++) {
            b = new Body("sphere" + i, world, new GeomSphere(sphereRadius));
            b.adjustMass(sphereMass);
            space.addBodyGeoms(b);

        }
    }

    /**
     * Set an sample simulation. Also resets existing simulation.
     *
     * @param setup
     */
    public void setSimulation() {
        Body b;

        float curX = 0;
        float curY = 0;
        float curZ = 0;

        steering = 0;
        speed = 0;

        // Reset existing rotation and forces
        Iterator bodiesIterator = (world.getBodies()).iterator();
        while (bodiesIterator.hasNext()) {
            b = (Body) bodiesIterator.next();
            b.resetRotationAndForces();
        }

        // Set objects position and mass
        curX = -800;
        curY = -450;
        curZ = 100;

        // Car chassis
        world.getBody("chassis").setPosition(curX, curY, curZ);

        // Car wheels
        world.getBody("wheelFrontLeft").setPosition(
            curX - chassisSizeX / 2 - wheelRadius - wheelChassisDistance,
            curY + chassisSizeY / 2 - wheelRadius,
            curZ);
        world.getBody("wheelFrontRight").setPosition(
            curX + chassisSizeX / 2 + wheelRadius + wheelChassisDistance,
            curY + chassisSizeY / 2 - wheelRadius,
            curZ);
        world.getBody("wheelBackLeft").setPosition(
            curX - chassisSizeX / 2 - wheelRadius - wheelChassisDistance,
            curY - chassisSizeY / 2 + wheelRadius,
            curZ);
        world.getBody("wheelBackRight").setPosition(
            curX + chassisSizeX / 2 + wheelRadius + wheelChassisDistance,
            curY - chassisSizeY / 2 + wheelRadius,
            curZ);

        // Empty joint group
        jointGroup.empty();

        // Set front hinges (this has steering)
        hingeFrontLeft = new JointHinge2("hingeFrontLeft", world, jointGroup);
        hingeFrontLeft.attach(
            world.getBody("chassis"),
            world.getBody("wheelFrontLeft"));
        hingeFrontLeft.setAnchor(
            curX - chassisSizeX / 2 - wheelRadius - wheelChassisDistance,
            curY + chassisSizeY / 2 - wheelRadius,
            curZ);

        hingeFrontRight = new JointHinge2("hingeFrontRight", world, jointGroup);
        hingeFrontRight.attach(
            world.getBody("chassis"),
            world.getBody("wheelFrontRight"));
        hingeFrontRight.setAnchor(
            curX + chassisSizeX / 2 + wheelRadius + wheelChassisDistance,
            curY + chassisSizeY / 2 - wheelRadius,
            curZ);

        // Set back hinges
        hingeBackLeft = new JointHinge2("hingeBackLeft", world, jointGroup);
        hingeBackLeft.attach(
            world.getBody("chassis"),
            world.getBody("wheelBackLeft"));
        hingeBackLeft.setAnchor(
            curX - chassisSizeX / 2 - wheelRadius - wheelChassisDistance,
            curY - chassisSizeY / 2 + wheelRadius,
            curZ);

        hingeBackRight = new JointHinge2("hingeBackRight", world, jointGroup);
        hingeBackRight.attach(
            world.getBody("chassis"),
            world.getBody("wheelBackRight"));
        hingeBackRight.setAnchor(
            curX + chassisSizeX / 2 + wheelRadius + wheelChassisDistance,
            curY - chassisSizeY / 2 + wheelRadius,
            curZ);

        // Set all hinges axis and parameters
        JointHinge2 joint;
        Iterator iter = jointGroup.getJointList().iterator();
        while (iter.hasNext()) {
            joint = (JointHinge2) iter.next();
            // Set axis
            joint.setAxis1(0, 0, 1);
            joint.setAxis2(1, 0, 0);
            // Set suspension
            joint.setParam(Ode.dParamSuspensionERP, suspensionErp);
            joint.setParam(Ode.dParamSuspensionCFM, suspensionCfm);
            // Set stops to make sure wheels always stay in alignment
            joint.setParam(Ode.dParamLoStop, 0);
            joint.setParam(Ode.dParamHiStop, 0);
            // Set acceleration parameters
            joint.setParam(Ode.dParamFMax, 0);
            joint.setParam(Ode.dParamVel2, 0);
            joint.setParam(Ode.dParamFMax2, maxSpeed);
        }

        // Position random boxes
        curX = -800;
        curY = -350;
        curZ = 0;
        float prevSizeZ = 0;
        for (int i = 0; i < boxCount; i++) {
            b = world.getBody("box" + i);
            curZ += ((GeomBox) b.getGeom()).getLengths()[2] / 2;
            curZ += prevSizeZ / 2;
            b.setPosition(curX, curY, curZ);
            prevSizeZ = ((GeomBox) b.getGeom()).getLengths()[2];
        }

        // Position spheres
        for (int i = 0; i < sphereCount; i++) {
            b = world.getBody("sphere" + i);
            b.setPosition(50 - i * 12, 100 - i * 12, 500f);
        }

    }

    /**
     * Step simulation ahead. Call this step once before rendering results into
     * the screen.
     *
     */
    public void step() {
        inStep = true;

        // Handle engine
        updateEngine();

        // Handle steering
        updateSteering();

        // Collide objects in given space
        collision.collide(space);

        // Read & modify contact information
        iterateContacts();

        // Add all contacts to contact jointGroup
        collision.applyContacts();

        // Step space in ode world based on contact joints
        world.quickStep();

        inStep = false;
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
            // terrain or ground
            if ((contact.getGeomID1() == terrainId)
                || (contact.getGeomID2() == terrainId)
                || (contact.getGeomID2() == groundId)
                || (contact.getGeomID2() == groundId)) {
                continue;
            }

            // Check if contact with chassis
            if ((contact.getGeomID1() == chassisId)) {
                // Found interesting contact
                contact.getPosition(pos);
                contact.getNormal(normal);
                contact.setMode(Ode.dContactBounce | Ode.dContactApprox1);
                // If chassis hits any geom make it bounce hard
                contact.setBounce(1.25f);
                contact.setBounceVel(0.2f);
                contact.setMu(0f);
                System.err.println(
                    "A: "
                        + i
                        + " Chassis hits geom "
                        + contact.getGeomID2()
                        + "\n  d="
                        + contact.getDepth()
                        + "\n  pos="
                        + pos
                        + "\n  normal="
                        + normal);
            }

            // Check if contact with chassis
            if ((contact.getGeomID2() == chassisId)) {
                // Found interesting contact
                contact.getPosition(pos);
                contact.getNormal(normal);
                contact.setMode(Ode.dContactBounce | Ode.dContactApprox1);
                contact.setBounce(0.24f);
                contact.setBounceVel(0.2f);
                contact.setMu(0f);
                System.err.println(
                    "B: "
                        + i
                        + " Chassis got hit by geom "
                        + contact.getGeomID1()
                        + "\n  d="
                        + contact.getDepth()
                        + "\n  pos="
                        + pos
                        + "\n  normal="
                        + normal);
            }

            // Check if contact with wheels
            if ((contact.getGeomID1() == wheelFLId)
                || (contact.getGeomID1() == wheelFRId)
                || (contact.getGeomID1() == wheelBLId)
                || (contact.getGeomID1() == wheelBRId)) {
                // Found interesting contact
                contact.getPosition(pos);
                contact.getNormal(normal);
                contact.setMode(
                    Ode.dContactBounce
                        | Ode.dContactApprox1
                        | Ode.dContactSoftCFM
                        | Ode.dContactSoftERP);
                // Tires are soft, higher penetration allowed
                contact.setSoftCfm(0.05f);
                // Let it penetrate, correct slowly
                contact.setSoftErp(0.1f);
                // If tires hits any geom make it bounce hard
                contact.setBounce(5f);
                contact.setBounceVel(0.2f);
                contact.setMu(0f);
                System.err.println(
                    "C: "
                        + i
                        + " Wheel hits geom "
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
     * Apply engine force by rotating all wheels (4wd). Handbrake is applies to
     * back hinges (wheels).
     *
     */
    private void updateEngine() {
        if (gasPedal != 0) {
            // Gas pedal pushed
            if (speed > 0)
                speed += (speed + 0.5f) * gasPedal;
            else
                speed -= (speed - 0.5f) * gasPedal;
            if (speed > maxSpeed)
                speed = maxSpeed;
            else if (speed < minSpeed)
                speed = minSpeed;
        } else {
            // No gas pedal down, slow down
            if (speed > 0) {
                // Drive ahead
                if (speed < 0.5)
                    speed = 0;
                else
                    speed -= 0.01 * speed;
            } else if (speed < 0) {
                // Reversing
                if (speed > -0.5)
                    speed = 0;
                else
                    speed -= 0.01 * speed;
            }
        }

        if (allbrake) {
            hingeFrontLeft.setParam(Ode.dParamVel2, (float) 0);
            hingeFrontRight.setParam(Ode.dParamVel2, (float) 0);
            hingeBackLeft.setParam(Ode.dParamVel2, (float) 0);
            hingeBackRight.setParam(Ode.dParamVel2, (float) 0);
        } else {
            if (handbrake) {
                // handbrake on back wheels
                hingeBackLeft.setParam(Ode.dParamVel2, (float) 0);
                hingeBackRight.setParam(Ode.dParamVel2, (float) 0);
            } else {
                hingeBackLeft.setParam(Ode.dParamVel2, (float) (this.speed));
                hingeBackRight.setParam(Ode.dParamVel2, (float) (this.speed));
            }
            hingeFrontLeft.setParam(Ode.dParamVel2, (float) (this.speed));
            hingeFrontRight.setParam(Ode.dParamVel2, (float) (this.speed));
        }
    }

    /**
     * Align front wheels along user's steering.
     *
     */
    private void updateSteering() {
        if (steeringWheel != 0) {
            steering += steeringWheel;
            if (steering < -steeringLimit)
                steering = -steeringLimit;
            else if (steering > steeringLimit)
                steering = steeringLimit;
        }
        if ((steering > -steeringDeadzone) && (steering < steeringDeadzone)) {
            // go forward
            hingeFrontLeft.setParam(Ode.dParamLoStop, 0);
            hingeFrontLeft.setParam(Ode.dParamHiStop, 0);
            hingeFrontRight.setParam(Ode.dParamLoStop, 0);
            hingeFrontRight.setParam(Ode.dParamHiStop, 0);
        } else if (steering > 0) {
            // turn right
            hingeFrontLeft.setParam(
                Ode.dParamLoStop,
                (float) (steering * 2 - 0.02));
            hingeFrontLeft.setParam(
                Ode.dParamHiStop,
                (float) (steering * 2 + 0.02));
            hingeFrontRight.setParam(
                Ode.dParamLoStop,
                (float) (steering * 2 - 0.02));
            hingeFrontRight.setParam(
                Ode.dParamHiStop,
                (float) (steering * 2 + 0.02));
            // turn steeringwheel back to center
            steering -= 0.01f;
        } else {
            // turn left
            hingeFrontLeft.setParam(
                Ode.dParamLoStop,
                (float) (steering * 2 - 0.02));
            hingeFrontLeft.setParam(
                Ode.dParamHiStop,
                (float) (steering * 2 + 0.02));
            hingeFrontRight.setParam(
                Ode.dParamLoStop,
                (float) (steering * 2 - 0.02));
            hingeFrontRight.setParam(
                Ode.dParamHiStop,
                (float) (steering * 2 + 0.02));
            // turn steeringwheel back to center
            steering += 0.01f;
        }
    }

    /**
     * @return Returns the gravity.
     */
    public Vector3f getGravity() {
        return gravity;
    }

    /**
     * @param gravity
     *            The gravity to set.
     */
    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
        world.setGravity(gravity.x, gravity.y, gravity.z);
    }

    /**
     * Accelerate (or deaccelerate).
     *
     * @param force
     */
    public void accelerate(float force) {
        gasPedal = force;
    }

    /**
     * Steer car's front wheels.
     *
     * @param steer
     */
    public void steer(float steer) {
        this.steeringWheel = steer;
    }

    /**
     * Test ODE's addForce method to all objects.
     *
     * @param x
     * @param y
     * @param z
     */
    public void rotateObjects(float x, float y, float z) {
        Iterator i = world.getBodies().iterator();
        while (i.hasNext())
             ((Body) i.next()).addForce(x, y, (float) (15 * z + 10));
    }

    /**
     * Test ODE's addForce method to car chassis.
     *
     * @param z
     */
    public void applyForceToCar(float z) {
        world.getBody("chassis").addForce(0f, 0f, (float) (10 * z));
    }

    /**
     * Clean up native ODE objects. Call this before ending your Java program.
     */
    public void cleanup() {
        while (inStep)
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        jointGroup.delete();
        space.delete();
        collision.delete();
        world.delete();
        Ode.dCloseODE();
    }

    /**
     * @return Returns the handbrake.
     */
    public boolean isHandbrake() {
        return handbrake;
    }

    /**
     * @param handbrake
     *            The handbrake to set.
     */
    public void setHandbrake(boolean handbrake) {
        this.handbrake = handbrake;
        System.out.println("Handbrake is " + handbrake);
    }

    public List getBodies() {
        return world.getBodies();
    }

    public Body getBody(String name) {
        return world.getBody(name);
    }

    public List getGeoms() {
        return space.getGeoms();
    }

    public Geom getGeom(String name) {
        return space.getGeom(name);
    }

    /**
     * @return Returns the boxCount.
     */
    public int getBoxCount() {
        return boxCount;
    }

}
