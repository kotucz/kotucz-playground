/*
 * Open Dynamics Engine for Java (odejava) Copyright (c) 2004, Odejava Project
 * Group, All rights reserved.
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
package org.odejava.display;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.odejava.Placeable;

/**
 * Represents an abstract Display Object that is bound to
 * an Ode Body or Geom (the OdeTransformable).
 *
 * @author William Denniss
 */
public class BoundDisplayObject {

	/**
	 * True if the object's display needs updating.
	 * When a fixed object is updated, this is set to false
	 * so the fixed object is only updated once.
	 */
	private boolean updateNeeded = true;
	
    /**
     * The abstract display object.
     */
    protected DisplayTransformable displayObject;
    
    /**
     * The abstract ODE object.
     */
    protected Placeable odeObject;

    /**
     * Creates a new binding of a transform-capable Display object
     * and a transform-capable Ode object.  The typical use is for the Ode object
     * to be updated by the ODE engine and for the Display object to be synched to
     * that (using BoundDisplayObjectS).
     *
     */
    public BoundDisplayObject (DisplayTransformable displayObject,    
                               Placeable odeObject) {
    		if (displayObject == null) {
    			throw new IllegalArgumentException("Display object cannot be null");
    		}
    		if (odeObject == null) {
    			throw new IllegalArgumentException("Display object cannot be null");
    		}
        this.displayObject = displayObject;
        this.odeObject = odeObject;
    }


    private Vector3f positionCache = new Vector3f();
    private Quat4f rotationCache = new Quat4f();
    private Vector3f prevPosition = new Vector3f();
    private Quat4f prevRotation = new Quat4f();
    
    /** 
     * Updates the transform of the Display Object, synching it to that of the ODE object.
     */
    public void update () {
        if (updateNeeded) {
            odeObject.getPosition(positionCache);
            odeObject.getQuaternion(rotationCache);
            
            if (!(prevPosition.equals(positionCache) && prevRotation.equals(rotationCache))) {
            
	            displayObject.setTransform(odeObject.getPosition(positionCache), odeObject.getQuaternion(rotationCache));
	            if (odeObject.fixed()) {
	            		updateNeeded = false;
	            }
            
	            prevPosition.set(positionCache);
	            prevRotation.set(rotationCache);
            }
        }
    }

    
    /**
     * Returns the bound DisplayTransformable
     *
     * @return the bound DisplayTransformable
     */
    public DisplayTransformable getDisplayTransformable () {
        return displayObject;
    }
    
    /**
     * Changes the display object that is kept in sync with the ODE Placeable.
     * Note: This does NOT alter the 3d scene in any way. The given DisplayTransformable
     * should already be visible in the scene if this is desired.
     * 
     * @param displayObject The displayObject to set.
     */
    public void setDisplayTransformable(DisplayTransformable displayObject) {
        this.displayObject = displayObject;
        forceUpdate();
    }
    
    /**
     * Returns the bound OdeTransformable
     *
     * @return the bound OdeTransformable
     */
    public Placeable getOdeTransformable () {
        return odeObject;
    }
   
    /**
     * Changes the Placeable which the display object keeps in sync with.
     * 
     * @param odeObject The new Placeable object to use.
     */
    public void setOdeTransformable(Placeable odeObject) {
        this.odeObject = odeObject;
        forceUpdate();
    }   
    
    /**
     * Forces an update of the display object's position next time update() is
     * called
     */
    public void forceUpdate() {
    		updateNeeded = true;
    		prevPosition.x = 0;
    		prevPosition.y = 0;
    		prevPosition.z = 0;
    		
    		prevRotation.w = 0; 
    		prevRotation.x = 0; 
    		prevRotation.y  = 0;
    		prevRotation.z  = 0;
    }
} 
