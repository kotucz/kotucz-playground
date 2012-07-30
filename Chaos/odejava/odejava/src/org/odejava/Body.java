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
package org.odejava;

import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.AxisAngle4f;

import org.odejava.ode.Ode;
import org.odejava.ode.SWIGTYPE_p_dBodyID;
import org.odejava.ode.SWIGTYPE_p_dMass;
import org.odejava.ode.SWIGTYPE_p_dWorldID;
import org.odejava.ode.SWIGTYPE_p_float;
import org.odejava.ode.dMass;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Representation of a body that can be used in a rigid body simulation. A
 * rigid body has various properties from the point of view of the
 * simulation. Some properties change over time:
 * <p>
 * Position vector (x,y,z) of the body's point of reference. Currently the
 * point of reference must correspond to the body's center of mass. Linear
 * velocity of the point of reference, a vector (vx,vy,vz). Orientation of a
 * body, represented by a quaternion (qs,qx,qy,qz) or a 3x3 rotation matrix.
 * Angular velocity vector (wx,wy,wz) which describes how the orientation
 * changes over time.
 * <p>
 * Other body properties are usually constant over time:
 * <p>
 *
 * Mass of the body. Position of the center of mass with respect to the point
 * of reference. In the current implementation the center of mass and the point
 * of reference must coincide. Inertia matrix. This is a 3x3 matrix that
 * describes how the body's mass is distributed around the center of mass.
 * <p>
 *
 * Conceptually each body has an x-y-z coordinate frame embedded in it, that
 * moves and rotates with the body:
 * <p>
 *
 * The origin of this coordinate frame is the body's point of reference. Some
 * values in ODE (vectors, matrices etc) are relative to the body coordinate
 * frame, and others are relative to the global coordinate frame.
 * <p>
 *
 * Note that the shape of a rigid body is not a dynamic property (except
 * insofar as it influences the various mass properties). It is only collision
 * detection that cares about the detailed shape of the body.
 * <p>
 *
 * <b>Unimplemented</b>
 * <p>
 * <ul>
 * <li>Auto disable of all forms. Getter methods return default values</li>
 * <li>Contact surface thickness</li>
 * <li>Correction velocity</li>
 * </ul>
 *
 *
 * Created 16.12.2003 (dd.mm.yyyy)
 *
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 * @author William Denniss
 * @see http://odejava.dev.java.net
 *
 */
public class Body implements Placeable {
    // Unique name
    private String name;
    private SWIGTYPE_p_dBodyID bodyId;
    private int nativeAdr = 0;
    private SWIGTYPE_p_dWorldID worldId;
    private dMass mass;
    private SWIGTYPE_p_dMass massId;

    /**
     * Flag indicating that this body has been requested to be deleted. After
     * this is set to true, none of the methods should allow further calls to
     * ODE as the values are invalid, and may well cause a crash of the library
     * or other strange error.
     */
    private boolean deleted;

    /** Allows the user to attach arbituary data */
    private Object userData;

    /** The reference to the world that this body is a member of */
    private World world;

    LinkedList geoms = new LinkedList();

    // TODO get rid of SWIGTYPE's by extending ODE's methods
    // References to position, quaternion and rotation objects
    SWIGTYPE_p_float posArray, quatArray, rotArray, angularVelArray,
            forceArray, linearVelArray, torqueArray;

    static final float DEFAULT_MASS_DENSITY = 1.0f;

    /** Temporary array for fetching rotation quats and turning into axisAngles */
    private Quat4f tmpQuat;

    /**
     * Create an unnamed body that belongs to the given world, and does not
     * contain any geometry. The world reference must be non-null.
     *
     * @param world The parent world reference
     * @throws NullPointerException The world reference was not null
     */
    public Body(World world) {
        this(null, world, null);
    }

    /**
     * Create a named body that belongs to the given world, and does not
     * contain any geometry. The world reference must be non-null and the
     * name string is optional.
     *
     * @param world The parent world reference
     * @param name A name string to associate with this body
     * @throws NullPointerException The world reference was not null
     */
    public Body(String name, World world) {
        this(name, world, null);
    }

    /**
     * Create a named ODE body with an initial geometry.
     * The body's default mass is based on geometry.
     *
     * Unnamed geoms are named after their body's name.
     *
     * @param world The parent world reference
     * @param name A name string to associate with this body
     * @param geom An initial geometry to associate with the body
     * @throws NullPointerException The world reference was not null
     */
    public Body(String name, World world, PlaceableGeom geom) {
        this.worldId = world.getId();
        this.name = name;
        this.world = world;

        deleted = false;

        // Create body
        bodyId = Ode.dBodyCreate(worldId);

        //if (geom != null) {
        // Set geom for this body
        //  setGeom(geom);
        // Name unnamed geoms by body
        //  if ((this.name != null) && (geom.getName() == null)) {
        //      geom.setName(name);
        //      space.getGeomMap().put(geom.getName(), geom);
        //  }
        //  space.addGeom(geom);
        //}

        // Create mass
        mass   = new dMass();
        massId = mass.getCPtr();

        if (geom != null) {
            setGeom(geom);
        }

        world.addBody(this);

        // Get references
        posArray = Ode.dBodyGetPosition(bodyId);
        quatArray = Ode.dBodyGetQuaternion(bodyId);
        rotArray = Ode.dBodyGetRotation(bodyId);
        angularVelArray = Ode.dBodyGetAngularVel(bodyId);
        forceArray = Ode.dBodyGetForce(bodyId);
        linearVelArray = Ode.dBodyGetLinearVel(bodyId);
        torqueArray = Ode.dBodyGetTorque(bodyId);
        updateNativeAddr();
    }

    /**
     * Adds a geom to this body.  Alias for addGeom.
     */
    public void setGeom(PlaceableGeom geom) {
        addGeom(geom);
    }

    /**
     * Adds a geom to this body.
     * An exception will be thrown if the geom is already in this body.
     */
    public void addGeom(PlaceableGeom geom) {

        assert !geoms.contains(geom) : "Geom already exists in the Body!";

        // adds to geom list
        geoms.add(geom);

        // sets the geom to be this body (by calling Ode.dGeomSetBody)
        geom.setBody(this);

        if ((this.name != null) && (geom.getName() == null)) {
            geom.setName(name);
        }

        if (massId != null) {
            //TODO:  is this correct??
            setDefaultMass(geom);
        }
    }

    /**
     * Removes a geom from this body.
     * An exception will be thrown if the geom isn't in the body to begin with.
     */
    public void removeGeom(PlaceableGeom geom) {

        assert geoms.contains(geom) : "Geom doesn't exist in the Body!";

        // removes from the geom list
        geoms.remove(geom);

        // sets Body to null on Geom
        geom.setBody(null);
    }

    /**
     * Returns the last added geom
     *
     * @return the last added geom
     */
    public Geom getGeom() {
        return (Geom) geoms.getLast();
    }

    /**
     * Returns a list of geoms
     */
    public List getGeoms() {
        return geoms;
    }

    /**
     * Set default mass parameters based on geometry. Density is
     * DEFAULT_MASS_DENSITY.
     */
    public void setDefaultMass(Geom geom) {
        if (geom instanceof GeomBox) {
            float size[] = ((GeomBox) geom).getLengths();
            Ode.dMassSetBox(massId, DEFAULT_MASS_DENSITY, size[0], size[1],
                            size[2]);
            Ode.dBodySetMass(bodyId, massId);
        } 
        else if (geom instanceof GeomSphere) {
            float radius = ((GeomSphere) geom).getRadius();
            Ode.dMassSetSphere(massId, DEFAULT_MASS_DENSITY, radius);
            Ode.dBodySetMass(bodyId, massId);
        }
        else {
            Ode.dMassSetParameters(massId, DEFAULT_MASS_DENSITY,
                                   0.0f, 0.0f, 0.0f,
                                   DEFAULT_MASS_DENSITY, DEFAULT_MASS_DENSITY,
                                   DEFAULT_MASS_DENSITY, 0.0f, 0.0f, 0.0f);
            Ode.dBodySetMass(bodyId, massId);
        }
    }

    /**
     * Set the inertial parameters of this body based on box density
     * and dimensions.
     */
    public void setBoxMass(float density, float lx, float ly, float lz) {
        Ode.dMassSetBox(massId, density, lx, ly, lz);
        Ode.dBodySetMass(bodyId, massId);
    }

    /**
     * Set the inertial parameters of this body based on box mass
     * and dimensions.
     */
    public void setBoxMassTotal(float mass, float lx, float ly, float lz) {
        Ode.dMassSetBoxTotal(massId, mass, lx, ly, lz);
        Ode.dBodySetMass(bodyId, massId);
    }

    /**
     * Set the inertial parameters of this body based on sphere density
     * and dimensions.
     */
    public void setSphereMass(float density, float radius) {
        Ode.dMassSetSphere(massId, density, radius);
        Ode.dBodySetMass(bodyId, massId);
    }

    /**
     * Set the inertial parameters of this body based on box mass
     * and dimensions.
     */
    public void setSphereMassTotal(float mass, float radius) {
        Ode.dMassSetSphereTotal(massId, mass, radius);
        Ode.dBodySetMass(bodyId, massId);
    }

    public void adjustMass(float mass) {
        if (geoms.size() == 0) {
            Odejava.log.warn("Odejava - warn: geoms should be added before the mass is adjusted");
        }

        Ode.dMassAdjust(massId, mass);
        Ode.dBodySetMass(bodyId, massId);
    }

    public void resetRotationAndForces() {
        // TODO, why setRotation does not work in all cases, quat has no
        // problems
        //setRotation(new Matrix3f());
        setQuaternion(new Quat4f());
        setAngularVel(0f, 0f, 0f);
        setLinearVel(0f, 0f, 0f);
        setForce(0f, 0f, 0f);
        setTorque(0f, 0f, 0f);
    }

    /**
     * Get the location in world space of the body. A new Vector3f instance
     * will be created for each request. This is identical to calling
     * <code>getPosition(null)</code>.
     *
     * @return A new vector object containing the position values
     */
    public Vector3f getPosition() {
        return getPosition((Vector3f)null);
    }

    /**
     * Get the position of the body and place it in the user-provided data
     * structure. If the user-provided data structure is null, then a new
     * instance is created and returned, otherwise the user provided structure
     * is used as the return value.
     *
     * @param result An object to place the values into or null
     * @return Either the result parameter or a new object
     */
    public Vector3f getPosition(Vector3f result) {
        if (result == null)
            result = new Vector3f();

        result.x = Ode.floatArray_getitem(posArray, 0);
        result.y = Ode.floatArray_getitem(posArray, 1);
        result.z = Ode.floatArray_getitem(posArray, 2);

        return result;
    }

    /**
     * Get the position of the body and place it in the user-provided array.
     *
     * @param result An object to place the values into
     */
    public void getPosition(float[] result) {
        result[0] = Ode.floatArray_getitem(posArray, 0);
        result[1] = Ode.floatArray_getitem(posArray, 1);
        result[2] = Ode.floatArray_getitem(posArray, 2);
    }

    /**
     * Set the position to a new value, using a vector.
     *
     * @param position A vector holding the position
     */
    public void setPosition(Vector3f position) {
        Ode.dBodySetPosition(bodyId, position.x, position.y, position.z);
    }

    /**
     * Set the force to a new value, using individual values.
     *
     * @param x The x component of the the position
     * @param y The y component of the the position
     * @param z The z component of the the position
     */
    public void setPosition(float x, float y, float z) {
        Ode.dBodySetPosition(bodyId, x, y, z);
    }

    /**
     * Get the rotation expressed as a axis/angle pair for the body. A new
     * AxisAngle4f instance will be created for each request. This is identical
     * to calling <code>getAxisAngle(null)</code>.
     *
     * @return A new quaternion object containing the axis values
     */
    public AxisAngle4f getAxisAngle () {
        return getAxisAngle((AxisAngle4f)null);
    }

    /**
     * Get the rotation expressed as a axis/angle for the body and place it in
     * the user-provided data structure. If the user-provided data structure is
     * null, then a new instance is created and returned, otherwise the user
     * provided structure is used as the return value.
     *
     * @param result An object to place the values into or null
     * @return Either the result parameter or a new object
     */
    public AxisAngle4f getAxisAngle(AxisAngle4f result) {
        if (result == null)
            result = new AxisAngle4f();

        if(tmpQuat == null)
            tmpQuat = new Quat4f();

        tmpQuat.x = Ode.floatArray_getitem(quatArray, 1);
        tmpQuat.y = Ode.floatArray_getitem(quatArray, 2);
        tmpQuat.z = Ode.floatArray_getitem(quatArray, 3);
        tmpQuat.w = Ode.floatArray_getitem(quatArray, 0);

        result.set(tmpQuat);

        return result;
    }

    public void setAxisAndAngle (AxisAngle4f axisAngle) {
        setAxisAndAngle(axisAngle.x, axisAngle.y, axisAngle.z, axisAngle.angle);
    }

    public void setAxisAndAngle(float ax, float ay, float az, float angle) {
        SWIGTYPE_p_float tmpArray = Ode.new_floatArray(4);
        Ode.dQFromAxisAndAngle(tmpArray, ax, ay, az, angle);
        Ode.dBodySetQuaternion(bodyId, tmpArray);
        Ode.delete_floatArray(tmpArray);
    }

    /**
     * Get the rotation expressed as a quaternion for the body. A new Quat4f
     * instance will be created for each request. This is identical to calling
     * <code>getQuaternion(null)</code>.
     *
     * @return A new quaternion object containing the axis values
     */
    public Quat4f getQuaternion() {
        return getQuaternion((Quat4f)null);
    }

    /**
     * Get the rotation expressed as a quaternion for the body and place it in
     * the user-provided data structure. If the user-provided data structure is
     * null, then a new instance is created and returned, otherwise the user
     * provided structure is used as the return value.
     *
     * @param result An object to place the values into or null
     * @return Either the result parameter or a new object
     */
    public Quat4f getQuaternion(Quat4f result) {
        if (result == null)
            result = new Quat4f();

        result.x = Ode.floatArray_getitem(quatArray, 1);
        result.y = Ode.floatArray_getitem(quatArray, 2);
        result.z = Ode.floatArray_getitem(quatArray, 3);
        result.w = Ode.floatArray_getitem(quatArray, 0);

        return result;
    }

    public void setQuaternion(Quat4f quaternion) {
        SWIGTYPE_p_float tmpArray = Ode.new_floatArray(4);
        Ode.floatArray_setitem(tmpArray, 0, quaternion.w);
        Ode.floatArray_setitem(tmpArray, 1, quaternion.x);
        Ode.floatArray_setitem(tmpArray, 2, quaternion.y);
        Ode.floatArray_setitem(tmpArray, 3, quaternion.z);
        Ode.dBodySetQuaternion(bodyId, tmpArray);
        Ode.delete_floatArray(tmpArray);
    }

    /**
     * Get the rotation of the body expressed as a quaternion and place it in
     * the user-provided array.
     *
     * @param result An object to place the values into
     */
    public void getQuaternion(float[] result) {
        result[0] = Ode.floatArray_getitem(quatArray, 1);
        result[1] = Ode.floatArray_getitem(quatArray, 2);
        result[2] = Ode.floatArray_getitem(quatArray, 3);
        result[3] = Ode.floatArray_getitem(quatArray, 0);
    }

    /**
     * Get the rotation matrix for the body. A new matrix instance will be created
     * for each request. This is identical to calling
     * <code>getMatrix(null)</code>.
     *
     * @return A new matrix object containing the axis values
     */
    public Matrix3f getRotation() {
        return getRotation((Matrix3f)null);
    }

    /**
     * Get the rotation matrix for the body and place it in the user-provided
     * data structure. If the user-provided data structure is null, then a
     * new instance is created and returned, otherwise the user provided
     * structure is used as the return value.
     *
     * @param result An object to place the values into or null
     * @return Either the result parameter or a new object
     */
    public Matrix3f getRotation(Matrix3f result) {
        if(result == null)
            result = new Matrix3f();

        // Note that the ODE representation is a 4x3 matrix and we only need a
        // 3x3 for return.
        result.m00 = Ode.floatArray_getitem(rotArray, 0);
        result.m01 = Ode.floatArray_getitem(rotArray, 1);
        result.m02 = Ode.floatArray_getitem(rotArray, 2);
        result.m10 = Ode.floatArray_getitem(rotArray, 4);
        result.m11 = Ode.floatArray_getitem(rotArray, 5);
        result.m12 = Ode.floatArray_getitem(rotArray, 6);
        result.m20 = Ode.floatArray_getitem(rotArray, 8);
        result.m21 = Ode.floatArray_getitem(rotArray, 9);
        result.m22 = Ode.floatArray_getitem(rotArray, 10);
        return result;
    }

    /**
     * Get the rotation of the body expressed as a 3x3 matrix and place it in
     * the user-provided array. The array must be of length 9 and is row-major.
     *
     * @param result An object to place the values into
     */
    public void getRotation(float[] result) {
        // Note that the ODE representation is a 4x3 matrix and we only need a
        // 3x3 for return.
        result[0] = Ode.floatArray_getitem(rotArray, 0);
        result[1] = Ode.floatArray_getitem(rotArray, 1);
        result[2] = Ode.floatArray_getitem(rotArray, 2);
        result[3] = Ode.floatArray_getitem(rotArray, 4);
        result[4] = Ode.floatArray_getitem(rotArray, 5);
        result[5] = Ode.floatArray_getitem(rotArray, 6);
        result[6] = Ode.floatArray_getitem(rotArray, 8);
        result[7] = Ode.floatArray_getitem(rotArray, 9);
        result[8] = Ode.floatArray_getitem(rotArray, 10);
    }

    /**
     * Use of setQuaternion is preferred instead of setRotation as this method
     * might have some problems in some cases.
     *
     * @param r
     */
    public void setRotation(Matrix3f r) {
        SWIGTYPE_p_float tmpArray = Ode.new_floatArray(12);
        Ode.floatArray_setitem(tmpArray, 0, r.getElement(0, 0));
        Ode.floatArray_setitem(tmpArray, 1, r.getElement(0, 1));
        Ode.floatArray_setitem(tmpArray, 2, r.getElement(0, 2));
        Ode.floatArray_setitem(tmpArray, 4, r.getElement(1, 0));
        Ode.floatArray_setitem(tmpArray, 5, r.getElement(1, 1));
        Ode.floatArray_setitem(tmpArray, 6, r.getElement(1, 2));
        Ode.floatArray_setitem(tmpArray, 8, r.getElement(2, 0));
        Ode.floatArray_setitem(tmpArray, 9, r.getElement(2, 1));
        Ode.floatArray_setitem(tmpArray, 10, r.getElement(2, 2));
        Ode.dBodySetRotation(bodyId, tmpArray);
        Ode.delete_floatArray(tmpArray);
    }

    /**
     * Get the mass of this Body.
     */
    public float getMass() { return mass.getMass(); }

    /**
     * Get the center of mass of this Body.
     */
    public Vector3f getCenterOfMass(Vector3f v) {
        if (v == null)
            v = new Vector3f();

        SWIGTYPE_p_float tmp = mass.getC();
        v.x = Ode.floatArray_getitem(tmp, 0);
        v.y = Ode.floatArray_getitem(tmp, 1);
        v.z = Ode.floatArray_getitem(tmp, 2);
        return v;
    }

    /**
     * Get the center of mass of this Body.
     */
    public Vector3f getCenterOfMass() { return getCenterOfMass(null); }

    /**
     * Get the inertia tensor of this Body.
     */
    public Matrix3f getInertiaTensor(Matrix3f m) {
        if (m == null)
            m = new Matrix3f();

        // Note that the ODE representation is a 4x3 matrix and we only need a
        // 3x3 for return.
        SWIGTYPE_p_float tmp = mass.getI();
        m.m00 = Ode.floatArray_getitem(tmp, 0);
        m.m01 = Ode.floatArray_getitem(tmp, 1);
        m.m02 = Ode.floatArray_getitem(tmp, 2);
        m.m10 = Ode.floatArray_getitem(tmp, 4);
        m.m11 = Ode.floatArray_getitem(tmp, 5);
        m.m12 = Ode.floatArray_getitem(tmp, 6);
        m.m20 = Ode.floatArray_getitem(tmp, 8);
        m.m21 = Ode.floatArray_getitem(tmp, 9);
        m.m22 = Ode.floatArray_getitem(tmp, 10);
        return m;
    }

    /**
     * Get the inertial tensor of this Body.
     */
    public Matrix3f getInertialTensor() { return getInertialTensor(); }

    /**
     * Get the current mode describing how global gravity will effect this
     * body. A value of 1 indicates gravity will effect it, a value of zero
     * indicates it will not be effected by gravity.
     *
     * @return A value of 0 or 1
     */
    public int getGravityMode() {
        return Ode.dBodyGetGravityMode(bodyId);
    }

    /**
     * Change the mode of how gravity effects this geometry. A value of 1 will
     * tell the body to obey the world's local gravity, a value of 0 will
     * ignore it.
     *
     * @param mode A value of 1 for global gravity, 0 for no gravity
     */
    public void setGravityMode(int mode) {
        Ode.dBodySetGravityMode(bodyId, mode);
    }

    public void setEnabled(boolean enabled) {
        if (enabled)
            Ode.dBodyEnable(bodyId);
        else
            Ode.dBodyDisable(bodyId);
    }

    public boolean isEnabled() {
        if (Ode.dBodyIsEnabled(bodyId) == 0)
            return false;
        else
            return true;
    }

    public void addForce(float x, float y, float z) {
        Ode.dBodyAddForce(bodyId, x, y, z);
    }

    /**
     * Convienience method, just calls addForce(x,y,z)
     *
     * @param force
     */
    public void addForce(Vector3f force) {
        addForce(force.x, force.y, force.z);
    }

    public void addForceAtPos(float fx, float fy, float fz, float px, float py,
            float pz) {
        Ode.dBodyAddForceAtPos(bodyId, fx, fy, fz, px, py, pz);
    }

    public void addForceAtRelPos(float fx, float fy, float fz, float px,
            float py, float pz) {
        Ode.dBodyAddForceAtRelPos(bodyId, fx, fy, fz, px, py, pz);
    }

    public void addRelForce(float fx, float fy, float fz) {
        Ode.dBodyAddRelForce(bodyId, fx, fy, fz);
    }

    public void addRelForceAtPos(float fx, float fy, float fz, float px,
            float py, float pz) {
        Ode.dBodyAddRelForceAtPos(bodyId, fx, fy, fz, px, py, pz);
    }

    public void addRelForceAtRelPos(float fx, float fy, float fz, float px,
            float py, float pz) {
        Ode.dBodyAddRelForceAtRelPos(bodyId, fx, fy, fz, px, py, pz);
    }

    public void addTorque(float x, float y, float z) {
        Ode.dBodyAddTorque(bodyId, x, y, z);
    }

    /**
     * Convenience method, just calls addTorque(x,y,z)
     *
     * @param torque
     */
    public void addTorque(Vector3f torque) {
        addTorque(torque.x, torque.y, torque.z);
    }

    public void addRelTorque(float x, float y, float z) {
        Ode.dBodyAddRelTorque(bodyId, x, y, z);
    }

    /**
     * Convenience method, just calls addRelTorque(x,y,z)
     *
     * @param torque
     */
    public void addRelTorque(Vector3f torque) {
        addRelTorque(torque.x, torque.y, torque.z);
    }

    /**
     * Return number of joints attached to this body.
     *
     * @return
     */
    public int getNumOfJoints() {
        return Ode.dBodyGetNumJoints(bodyId);
    }

    /**
     * Return a list of joints attached to this body, or null if none. A new
     * list is created each time this method is called.
     *
     * @return A new list of the current joints
     */
    public List getJoints() {
        return getJoints(null);
    }

    /**
     * Return a list of joints attached to this body, copying them into the
     * given list. This method will first clear the given list, even if there
     * are no joints current. If the passed list is null, a new list instance
     * will be created.
     *
     * @param result A list to put the joints into
     * @return A list of the current joints or null if no joints
     */
    public List getJoints(List result) {

        if(getNumOfJoints() == 0)
            return null;

        if(result == null)
            result = new ArrayList();
        else
            result.clear();

        if(getNumOfJoints() > 0) {
            for (int i = 0; i < getNumOfJoints(); i++)
                result.add(Ode.dBodyGetJoint(bodyId, i));
        }

        return result;
    }

    /**
     * @return Returns the bodyId.
     */
    public SWIGTYPE_p_dBodyID getId() {
        return bodyId;
    }

    /**
     * @param bodyId The bodyId to set.
     */
    public void setId(SWIGTYPE_p_dBodyID bodyId) {
        this.bodyId = bodyId;
    }

    /**
     * Fet the currently associated name string. If none is set, return null.
     *
     * @return The current name string or null.
     */
    public String getName() {
        return name;
    }

    /**
     * Set a new name string to associate with this body.
     *
     * @param name The new name string to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Deletes the body.  This is performed automatically
     * by World.deletebody and should not be called directly.
     *
     * @ode dBodyDestroy destroys the body
     * @see World#deleteBody(Body)
     */
    protected void delete() {
        if (deleted) {
            Odejava.log.warn("Body " + name + " already deleted.");
            return;
        }

        for (Iterator i = new LinkedList(geoms).iterator(); i.hasNext(); ) {
            ((Geom) i.next()).delete();
        }

        Ode.dBodyDestroy(bodyId);
        world.removeBody(this);

        deleted = true;

        world = null;
        geoms = null;
        userData = null;
    }
    
    /**
     * Removes this Body from its World and deletes it.
     */
    public void deleteFromWorld() {
        world.deleteBody(this);
    }
    
    /**
     * Returns the world that contains this Body.
     * 
     * @return the world that contains this Body.
     */
    public World getContainingWorld() {
        return world;
    }

    /**
     * Set the force to a new value, using a vector.
     *
     * @param force A vector holding the force
     */
    public void setForce(Vector3f force) {
        setForce(force.x, force.y, force.z);
    }

    /**
     * Set the force to a new value, using individual values.
     *
     * @param x The x component of the the force
     * @param y The y component of the the force
     * @param z The z component of the the force
     */
    public void setForce(float x, float y, float z) {
        Ode.dBodySetForce(bodyId, x, y, z);
    }

    /**
     * Get the force of the body. A new Vector3f instance will be
     * created for each request. This is identical to calling
     * <code>getForce(null)</code>.
     *
     * @return A new vector object containing the velocity values
     */
    public Vector3f getForce() {
        return getForce((Vector3f)null);
    }

    /**
     * Get the force for the body and place it in the user-provided
     * data structure. If the user-provided data structure is null, then a
     * new instance is created and returned, otherwise the user provided
     * structure is used as the return value.
     *
     * @param result An object to place the values into or null
     * @return Either the result parameter or a new object
     */
    public Vector3f getForce(Vector3f result) {
        if(result == null)
            result = new Vector3f();

        result.x = Ode.floatArray_getitem(forceArray, 0);
        result.y = Ode.floatArray_getitem(forceArray, 1);
        result.z = Ode.floatArray_getitem(forceArray, 2);

        return result;
    }

    /**
     * Get the force of the body and place it in the user-provided array.
     *
     * @param result An object to place the values into
     */
    public void getForce(float[] result) {
        result[0] = Ode.floatArray_getitem(forceArray, 0);
        result[1] = Ode.floatArray_getitem(forceArray, 1);
        result[2] = Ode.floatArray_getitem(forceArray, 2);
    }

    /**
     * Set the angular velocity to a new value, using a vector.
     *
     * @param angularVel A vector holding the angular velocity
     */
    public void setAngularVel(Vector3f angularVel) {
        setAngularVel(angularVel.x, angularVel.y, angularVel.z);
    }

    /**
     * Set the angular velocity to a new value, using individual values.
     *
     * @param x The x component of the the angular velocity
     * @param y The y component of the the angular velocity
     * @param z The z component of the the angular velocity
     */
    public void setAngularVel(float x, float y, float z) {
        Ode.dBodySetAngularVel(bodyId, x, y, z);
    }

    /**
     * Get the angular velocity of the body. A new Vector3f instance will be
     * created for each request. This is identical to calling
     * <code>getAngularVel(null)</code>.
     *
     * @return A new vector object containing the velocity values
     */
    public Vector3f getAngularVel() {
        return getAngularVel((Vector3f)null);
    }

    /**
     * Get the angular velocity for the body and place it in the user-provided
     * data structure. If the user-provided data structure is null, then a
     * new instance is created and returned, otherwise the user provided
     * structure is used as the return value.
     *
     * @param result An object to place the values into or null
     * @return Either the result parameter or a new object
     */
    public Vector3f getAngularVel(Vector3f result) {
        if(result == null)
            result = new Vector3f();

        result.x = Ode.floatArray_getitem(angularVelArray, 0);
        result.y = Ode.floatArray_getitem(angularVelArray, 1);
        result.z = Ode.floatArray_getitem(angularVelArray, 2);

        return result;
    }

    /**
     * Get the angular velocity of the body and place it in the user-provided array.
     *
     * @param result An object to place the values into
     */
    public void getAngularVel(float[] result) {
        result[0] = Ode.floatArray_getitem(angularVelArray, 0);
        result[1] = Ode.floatArray_getitem(angularVelArray, 1);
        result[2] = Ode.floatArray_getitem(angularVelArray, 2);
    }

    /**
     * Set the linear velocity to a new value, using a vector.
     *
     * @param linearVel A vector holding the linear velocity
     */
    public void setLinearVel(Vector3f linearVel) {
        setLinearVel(linearVel.x, linearVel.y, linearVel.z);
    }

    /**
     * Set the linear velocity to a new value, using individual values.
     *
     * @param x The x component of the the linear velocity
     * @param y The y component of the the linear velocity
     * @param z The z component of the the linear velocity
     */
    public void setLinearVel(float x, float y, float z) {
        Ode.dBodySetLinearVel(bodyId, x, y, z);
    }

    /**
     * Get the linear velocity of the body. A new Vector3f instance will be
     * created for each request. This is identical to calling
     * <code>getLinearVel(null)</code>.
     *
     * @return A new vector object containing the velocity values
     */
    public Vector3f getLinearVel() {
        return getLinearVel((Vector3f)null);
    }

    /**
     * Get the linear velocity for the body and place it in the user-provided
     * data structure. If the user-provided data structure is null, then a
     * new instance is created and returned, otherwise the user provided
     * structure is used as the return value.
     *
     * @param result An object to place the values into or null
     * @return Either the result parameter or a new object
     */
    public Vector3f getLinearVel(Vector3f result) {
        if(result == null)
            result = new Vector3f();

        result.x = Ode.floatArray_getitem(linearVelArray, 0);
        result.y = Ode.floatArray_getitem(linearVelArray, 1);
        result.z = Ode.floatArray_getitem(linearVelArray, 2);

        return result;
    }

    /**
     * Get the linear velocity of the body and place it in the user-provided array.
     *
     * @param result An object to place the values into
     */
    public void getLinearVel(float[] result) {
        result[0] = Ode.floatArray_getitem(linearVelArray, 0);
        result[1] = Ode.floatArray_getitem(linearVelArray, 1);
        result[2] = Ode.floatArray_getitem(linearVelArray, 2);
    }

    /**
     * Set the torque to a new value, using a vector.
     *
     * @param torque A vector holding the torque
     */
    public void setTorque(Vector3f torque) {
        setTorque(torque.x, torque.y, torque.z);
    }

    /**
     * Set the torque to a new value, using individual values.
     *
     * @param x The x component of the the torque
     * @param y The y component of the the torque
     * @param z The z component of the the torque
     */
    public void setTorque(float x, float y, float z) {
        Ode.dBodySetTorque(bodyId, x, y, z);
    }

    /**
     * Get the torque of the body. A new Vector3f instance will be
     * created for each request. This is identical to calling
     * <code>getTorque(null)</code>.
     *
     * @return A new vector object containing the velocity values
     */
    public Vector3f getTorque() {
        return getTorque((Vector3f)null);
    }

    /**
     * Get the torque for the body and place it in the user-provided
     * data structure. If the user-provided data structure is null, then a
     * new instance is created and returned, otherwise the user provided
     * structure is used as the return value.
     *
     * @param result An object to place the values into or null
     * @return Either the result parameter or a new object
     */
    public Vector3f getTorque(Vector3f result) {
        if(result == null)
            result = new Vector3f();

        result.x = Ode.floatArray_getitem(torqueArray, 0);
        result.y = Ode.floatArray_getitem(torqueArray, 1);
        result.z = Ode.floatArray_getitem(torqueArray, 2);

        return result;
    }

    /**
     * Get the torque of the body and place it in the user-provided array.
     *
     * @param result An object to place the values into
     */
    public void getTorque(float[] result) {
        result[0] = Ode.floatArray_getitem(torqueArray, 0);
        result[1] = Ode.floatArray_getitem(torqueArray, 1);
        result[2] = Ode.floatArray_getitem(torqueArray, 2);
    }

    /**
     * Set the finiteRotationAxis to a new value, using a vector.
     *
     * @param finiteRotationAxis A vector holding the finiteRotationAxis
     */
    public void setFiniteRotationAxis(Vector3f finiteRotationAxis) {
        setFiniteRotationAxis(finiteRotationAxis.x,
                              finiteRotationAxis.y,
                              finiteRotationAxis.z);
    }

    /**
     * Set the finiteRotationAxis to a new value, using individual values.
     *
     * @param x The x component of the the rotation axis
     * @param y The y component of the the rotation axis
     * @param z The z component of the the rotation axis
     */
    public void setFiniteRotationAxis(float x, float y, float z) {
        Ode.dBodySetFiniteRotationAxis(bodyId, x, y, z);
    }

    /**
     * Get the finiteRotationAxis of the body. A new Vector3f instance will be
     * created for each request. This is identical to calling
     * <code>getFiniteRotationAxis(null)</code>.
     *
     * @return A new vector object containing the velocity values
     */
    public Vector3f getFiniteRotationAxis() {
        return getFiniteRotationAxis((Vector3f)null);
    }

    /**
     * Get the finiteRotationAxis for the body and place it in the user-provided
     * data structure. If the user-provided data structure is null, then a
     * new instance is created and returned, otherwise the user provided
     * structure is used as the return value.
     *
     * @param result An object to place the values into or null
     * @return Either the result parameter or a new object
     */
    public Vector3f getFiniteRotationAxis(Vector3f result) {
        if(result == null)
            result = new Vector3f();

        SWIGTYPE_p_float tmpArray = Ode.new_floatArray(4);
        Ode.dBodyGetFiniteRotationAxis(bodyId, tmpArray);

        result.x = Ode.floatArray_getitem(tmpArray, 0);
        result.y = Ode.floatArray_getitem(tmpArray, 1);
        result.z = Ode.floatArray_getitem(tmpArray, 2);
        Ode.delete_floatArray(tmpArray);

        return result;
    }

    /**
     * Get the finiteRotationAxis of the body and place it in the user-provided
     * array.
     *
     * @param result An object to place the values into
     */
    public void getFiniteRotationAxis(float[] result) {
        SWIGTYPE_p_float tmpArray = Ode.new_floatArray(4);
        Ode.dBodyGetFiniteRotationAxis(bodyId, tmpArray);

        result[0] = Ode.floatArray_getitem(tmpArray, 0);
        result[1] = Ode.floatArray_getitem(tmpArray, 1);
        result[2] = Ode.floatArray_getitem(tmpArray, 2);
        Ode.delete_floatArray(tmpArray);
    }

    public void setFiniteRotationMode(int mode) {
        Ode.dBodySetFiniteRotationMode(bodyId, mode);
    }

    public int getFiniteRotationMode() {
        return Ode.dBodyGetFiniteRotationMode(bodyId);
    }

    /**
     * <p>Returns the SWIG representation of this body identifier.</p>
     * 
     * <p><b>UNSUPPORTED</b> - use of this method is better avoided.  No guarantees
     * are made that this method won't change or even exist in future versions.
     * </p>
     *
     * @return The SWIG representation of this Body identifier
     */
    public SWIGTYPE_p_dBodyID getBodyId() {
        return bodyId;
    }

    protected void updateNativeAddr() {
        // Get native address based on Swig C pointer
        // This can be used with dContact.getGeom() structure that
        // contains native address instead of Swig C pointer values
        nativeAdr = Odejava.getNativeAddr(bodyId.getSwigCPtr());
    }

    /**
     * <p>Fetch the native address that ODE uses internally. Care should be taken
     * making use of this, but it will at least be needed for interacting with
     * the collisions system contact information.</p>
     * 
     * <p><b>UNSUPPORTED</b> - use of this method is better avoided.  No guarantees
     * are made that this method won't change or even exist in future versions.
     * </p>
     * 
     *
     * @return The native address that ODE uses
     */
    public int getNativeAddr() {
        return nativeAdr;
    }

    /**
     * Set the mass parameters to the given values. themass is the mass of the
     * body. (cx,cy,cz) is the center of gravity position in the body frame.
     * The Ixx values are the elements of the inertia matrix:
     * <p>[ I11 I12 I13 ]<br />[ I12 I22 I23 ]<br />[ I13 I23 I33 ]<br />
     * </p>
     *
     * @param themass
     * @param cgx
     * @param cgy
     * @param cgz
     * @param I11
     * @param I22
     * @param I33
     * @param I12
     * @param I13
     * @param I23
     */
    public void setMassParameters(float themass, float cgx, float cgy,
            float cgz, float I11, float I22, float I33, float I12, float I13,
            float I23) {
        Ode.dMassSetParameters(massId, themass, cgx, cgy, cgz, I11, I22, I33,
                I12, I13, I23);
        Ode.dBodySetMass(bodyId, massId);
    }

    public boolean fixed() {
        return false;
    }

    /**
     * @return Returns the userData.
     */
    public Object getUserData() {
        return userData;
    }
    /**
     * @param userData The userData to set.
     */
    public void setUserData(Object userData) {
        this.userData = userData;
    }

    /**
     * Control whether the world should allow auto-disable of this body. A
     * value of true will enable the auto disable ability. Whether this body
     * is disabled or not depends on the settings of the individual
     * thresholds (which can be set by other methods).
     *
     * @param state True to enable auto disabling, false to disable
     */
    public void setAutoDisable(boolean state) {
//        Ode.dBodySetAutoDisableFlag(bodyId, state ? 1 : 0);
    }

    /**
     * Check to see the current state of the auto disable functionality.
     *
     * @return true if the auto-disable mode is on, false otherwise
     */
    public boolean isAutoDisabling() {
        boolean ret = false;

//        int flag = Ode.dBodyGetAutoDisableFlag(bodyId);
//      ret = flag == 1 ? true : false;

        return ret;
    }

    /**
     * Set the threshold for the linear velocity that will cause a body to be
     * disabled.  Once the velocity falls below this value, the body will be
     * subject to being disabled. The threshold is only used if the auto
     * disable capability is enabled.
     *
     * @param vel The speed below which the body is disabled
     */
    public void setLinearVelocityDisableThreshold(float vel) {
//        Ode.dBodySetAutoDisableLinearThreshold(bodyId, vel);
    }

    /**
     * Get the threshold for linear velocity at which a body will be
     * automatically disabled.
     *
     * @return The current threshold value
     */
    public float getLinearVelocityDisableThreshold() {
        return 0.01f;
//        return Ode.dBodyGetAutoDisableLinearThreshold(bodyId);
    }

    /**
     * Set the threshold for the angular velocity that will cause a body to be
     * disabled.  Once the velocity falls below this value, the body will be
     * subject to being disabled. The threshold is only used if the auto
     * disable capability is enabled.
     *
     * @param vel The speed below which the body is disabled
     */
    public void setAngularVelocityDisableThreshold(float vel) {
//        Ode.dBodySetAutoDisableAngularThreshold(bodyId, vel);
    }

    /**
     * Get the threshold for angular velocity at which a body will be
     * automatically disabled.
     *
     * @return The current threshold value
     */
    public float getAngularVelocityDisableThreshold() {
        return 0.01f;
//        return Ode.dBodyGetAutoDisableAngularThreshold(bodyId);
    }

    /**
     * Set the number of evaluation steps before an umoving body is disabled.
     * If the body has not moved in this number of steps, it is automatically
     * disabled. This setting is only used if the auto disable capabilities is
     * enabled. If the number of steps is negative or zero, bodies cannot be
     * disabled using this way.
     *
     * @param steps The number of evaluation steps to use or negative to disable
     */
    public void setStepDisableThreshold(int steps) {
//        Ode.dBodySetAutoDisableSteps(bodyId, steps);
    }

    /**
     * Get the threshold for the number of steps at which a body will be
     * automatically disabled.
     *
     * @return The current threshold value
     */
    public int getStepDisableThreshold() {
        return 10;
//        return Ode.dBodyGetAutoDisableSteps(bodyId);
    }

    /**
     * Set the total amount of evaluation time an umoving body is disabled.
     * If the body has not moved in this time, it is automatically
     * disabled. This setting is only used if the auto disable capabilities is
     * enabled. If the time is negative or zero, bodies cannot be
     * disabled using this way.
     *
     * @param time The amount of time in seconds or negative to disable
     */
    public void setTimeDisableThreshold(float time) {
//        Ode.dBodySetAutoDisableSteps(bodyId, time);
    }

    /**
     * Get the threshold for the evaluation time at which a body will be
     * automatically disabled.
     *
     * @return The current threshold value
     */
    public float getTimeDisableThreshold() {
        return 0;
//        return Ode.dBodyGetAutoDisableTime(bodyId);
    }

}
