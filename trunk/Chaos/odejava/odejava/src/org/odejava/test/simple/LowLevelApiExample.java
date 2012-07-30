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

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.odejava.ode.*;

/**
 * Creates simple Odejava simulation. Consists of a static plane (ground), box
 * and sphere object. Box and sphere are placed in the air above the ground, as
 * the simulation goes ahead they fall to ground and collide to each other.
 * Coordinate is selected in such way that Z is up.
 * 
 * Note, this example uses low level API. Consider using high level API and
 * extend it with low level API as needed.
 * 
 * @see http://odejava.dev.java.net
 * 
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 *  
 */
public class LowLevelApiExample {

	// Load odejava native library
	static {
		try {
			System.loadLibrary("odejava");
			System.out.println(
				"Odejava library version " + Ode.ODEJAVA_VERSION + ".");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load. " + e);
			System.exit(1);
		}
	}

	// World that contains our simulation space
	SWIGTYPE_p_dWorldID worldId;
	// A simulation space that contains objects (geoms and bodies)
	SWIGTYPE_p_dSpaceID spaceId;
	// ContactGroup is used by ODE internally for collision information
	SWIGTYPE_p_dJointGroupID contactGroupId;

	// Body specifies e.g. position and direction of an object
	SWIGTYPE_p_dBodyID boxBodyId, sphereBodyId;
	// Body's mass parameters
	SWIGTYPE_p_dMass boxMass, sphereMass;
	// Body's geom
	SWIGTYPE_p_dGeomID boxGeomId;
	SWIGTYPE_p_dGeomID sphereGeomId;

	// Helper for converting swig float arrays into java arrays
	SWIGTYPE_p_float tmpFloatArray;

	/**
	 * Standalone test application that prints simulation results to console.
	 */
	public static void main(String[] args) {
		LowLevelApiExample simple = new LowLevelApiExample();
		simple.cleanup();
	}

	/**
	 * Create world and objects.
	 * 
	 * Note, this example uses low level API. Consider using high level API and
	 * extend it with low level API as needed.
	 *  
	 */
	public LowLevelApiExample() {
		// Init ode world
		initWorld();
		// Init static (unmovable) objects
		initStaticObjects();
		// Init dynamic objects
		initObjects();
		// World is ready, simulate it for 1000 steps
		simulate(1000);
	}

	/**
	 * Initialize ODE world.
	 *  
	 */
	private void initWorld() {
		// Create ODE world
		worldId = Ode.dWorldCreate();
		// Create space that contains all of our geoms / bodies
		spaceId = Ode.dSimpleSpaceCreate(Ode.getPARENTSPACEID_ZERO());
		// Create contact group used by collide functions
		contactGroupId = Ode.dJointGroupCreate(0);
		// Set gravity along Z axis
		Ode.dWorldSetGravity(worldId, 0, 0, -0.2f);
		// Setup callback function related parameters
		Ode.setWorldID(worldId);
		Ode.setContactGroupID(contactGroupId);
		// Setup some surface parameters: maximum friction
		Ode.setSurfaceMu(Float.MAX_VALUE);
	}

	/**
	 * Initialize static objects. These geoms have no body and therefore
	 * simulation steps do not change their position or orientation.
	 *  
	 */
	private void initStaticObjects() {
		// Create ground (static plane)
		Ode.dCreatePlane(spaceId, 0, 0, 1, 0);
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
		sphereBodyId = Ode.dBodyCreate(worldId);
		Ode.dBodySetPosition(sphereBodyId, 0f, 0f, 2f);
		dMass mass = new dMass();
		Ode.dMassSetSphere(mass.getCPtr(), 1f, 1f);
		Ode.dMassAdjust(mass.getCPtr(), 1.45f);
		Ode.dBodySetMass(sphereBodyId, mass.getCPtr());
		sphereGeomId = Ode.dCreateSphere(spaceId, 1f);
		Ode.dGeomSetBody(sphereGeomId, sphereBodyId);

		// Create a box geom and set it to body (dynamic object)
		// Box x,y,z lengths are 2,1,1. Position box above the sphere, slightly
		// right to sphere
		boxBodyId = Ode.dBodyCreate(worldId);
		Ode.dBodySetPosition(boxBodyId, 0.1f, 0f, 5f);
		dMass mass2 = new dMass();
		Ode.dMassSetBox(mass2.getCPtr(), 1f, 2f, 1f, 1f);
		Ode.dMassAdjust(mass2.getCPtr(), 1f);
		Ode.dBodySetMass(boxBodyId, mass2.getCPtr());
		boxGeomId = Ode.dCreateBox(spaceId, 2f, 1f, 1f);
		Ode.dGeomSetBody(boxGeomId, boxBodyId);

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
				System.out.print("  Box pos=" + getBodyPosition(boxBodyId));
				System.out.println(
					", quaternion=" + getBodyQuaternion(boxBodyId));
				System.out.print(
					"  Sphere pos=" + getBodyPosition(sphereBodyId));
				System.out.println(
					", quaternion=" + getBodyQuaternion(sphereBodyId));
			}
		}
	}

	/**
	 * Step simulation ahead. Call this step once before rendering results into
	 * the screen.
	 *  
	 */
	public void step() {
		// Call collision methods for given space
		//Ode.spaceCollide(spaceId);
		// Max interactions per step is 10, bigger is more accurate but
		// slower. Step size is 0.05, smaller is more accurate but slower.
		Ode.dWorldStepFast1(worldId, 0.05f, 10);
		// Empty step contact information
		Ode.dJointGroupEmpty(contactGroupId);
	}

	/**
	 * Helper method for converting internal ODE array into Java object
	 * 
	 * @return Returns the bodyPos.
	 */
	public Vector3f getBodyPosition(SWIGTYPE_p_dBodyID body) {
		tmpFloatArray = Ode.dBodyGetPosition(body);
		return new Vector3f(
			Ode.floatArray_getitem(tmpFloatArray, 0),
			Ode.floatArray_getitem(tmpFloatArray, 1),
			Ode.floatArray_getitem(tmpFloatArray, 2));
	}

	/**
	 * Helper method for converting internal ODE array into Java object
	 * 
	 * @return Returns the body orientation using quatertion.
	 */
	public Quat4f getBodyQuaternion(SWIGTYPE_p_dBodyID body) {
		tmpFloatArray = Ode.dBodyGetQuaternion(body);
		return new Quat4f(
			Ode.floatArray_getitem(tmpFloatArray, 1),
			Ode.floatArray_getitem(tmpFloatArray, 2),
			Ode.floatArray_getitem(tmpFloatArray, 3),
			Ode.floatArray_getitem(tmpFloatArray, 0));
	}

	/**
	 * Clean up native ODE objects. Call this before ending your Java program.
	 */
	public void cleanup() {
		Ode.dGeomDestroy(boxGeomId);
		Ode.dGeomDestroy(sphereGeomId);
		Ode.dJointGroupDestroy(contactGroupId);
		Ode.dSpaceDestroy(spaceId);
		Ode.dWorldDestroy(worldId);
		Ode.dCloseODE();
	}
}
