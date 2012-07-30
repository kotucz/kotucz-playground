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
package org.odejava.xith3d;

import java.util.*;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.odejava.Placeable;
import org.odejava.display.*;

import com.xith3d.render.jogl.CanvasPeerImpl;
import com.xith3d.scenegraph.IllegalSceneGraphOperation;
import com.xith3d.scenegraph.TransformGroup;
import com.xith3d.scenegraph.Group;
import com.xith3d.scenegraph.Transform3D;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Xith3D implementation of the DisplayTransformable object used in the abstract
 * org.odejava.display package to bind display objects to ODE bodies and geometry.
 *
 * @author William Denniss
 */
public class Xith3DDisplayObject implements DisplayTransformable {
    private static Log log = LogFactory.getLog(Xith3DDisplayObject.class);
	
	/**
	 * The linked TransformGroup that this Xith3DDisplayObject represents
	 */
	protected TransformGroup tg;
	
	/**
	 * Locally stored object for efficiancy
	 */
	private static Transform3D t3d = new Transform3D ();
	
	/**
	 * Creates a Xith3DDisplayObject that represents the given TransformGroup
	 *
	 * @param tg The TransformGroup to be represented
	 */
	public Xith3DDisplayObject (TransformGroup tg) {
		if (tg == null) {
			throw new IllegalArgumentException("TransformGroup can not be null");
		}
		
		this.tg = tg;
	}
	
	/**
	 * Updates the represented TransformGroup with the given position and quaternion.
	 *
	 */
	public void setTransform(Vector3f position, Quat4f quaternion) {
		
		t3d.set(quaternion);
		t3d.setTranslation(position);
		
		tg.setTransform(t3d);
	}
	
	/**
	 * Returns the represented TransformGroup.
	 *
	 * @return the represented TransformGroup.
	 */
	public TransformGroup getTransformGroup () {
		return tg;
	}
	
	/**
	 * Conveniance method to add all the TransformGroups registered in a DisplayBin
	 * to the given scene.
	 *
	 * @param boundObjects DisplayBin whose bound TransformGroupS will be added to the given scene
	 * @param scene the Xith3D group that the DisplayBinS TransofrmGroupS will be added to
	 *
	 */
	public static void addToScene (DisplayBin boundObjects, Group scene) {
		
		log.info("Adding bound objects to Xith3D scene.");
		
		LinkedList tgs = new LinkedList ();
		for (Iterator i = boundObjects.iterator(); i.hasNext() ; ) {
			BoundDisplayObject displayObject = ((BoundDisplayObject)i.next());
			Xith3DDisplayObject current = (Xith3DDisplayObject) displayObject.getDisplayTransformable();
			tgs.add(current.getTransformGroup());
			
			
			Placeable currentOdeTransformable = displayObject.getOdeTransformable();
			log.debug("adding bound object to list (OdeTransformable id: " + currentOdeTransformable + " class: " + currentOdeTransformable.getClass().getName() + " name: " + currentOdeTransformable.getName() + ", Xith3D TransformGroup id: " + current.getTransformGroup() + " name:  " + current.getTransformGroup().getName());
		}
		
		for (Iterator i = tgs.iterator(); i.hasNext() ; ) {
			TransformGroup tg = (TransformGroup) i.next();
			try {
				log.error("Adding TG id:" + tg + " parentid:" + tg.getParent() + " name: " + tg.getName() + " to scene.");
			//	tg.removeFromParentGroup();
                System.out.println("!" + tg.getParent());
				scene.addChild(tg);

			} catch (IllegalSceneGraphOperation e) {
		
				log.error("IllegalSceneGraphOperation - " + tg, e);
			}
		}
		
	}
	/**
	 * Removes the Xith3D objects boundin the DisplayBin from the given Group
	 * 
	 * @param boundObjects DisplayBin whose bound TransformGroupS will be added to the given scene
	 * @param scene the Xith3D group that the DisplayBinS TransofrmGroupS will be added to
	 */
	public static void removeFromScene(DisplayBin boundObjects, Group scene) {
	    for (Iterator i = boundObjects.iterator(); i.hasNext(); ) {
	        
	        Xith3DDisplayObject current = (Xith3DDisplayObject) ((BoundDisplayObject)i.next()).getDisplayTransformable();
	        scene.removeChild(current.getTransformGroup());
	    }
	}
}
