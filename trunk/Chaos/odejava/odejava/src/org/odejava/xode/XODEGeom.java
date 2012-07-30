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
package org.odejava.xode;

import java.io.Serializable;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.odejava.*;

/**
 * XODE representation of a Geom
 * 
 * @author William Denniss
 */
public abstract class XODEGeom extends XODEGroup implements Serializable {

	/**
	 * Used when deserializing
	 */
	protected XODEGeom() {
	}
	
	/**
	 * Creates the XODE representation of a Geom 
	 * @param name name of Geom
	 * @param parent parent object
	 * @param transform transform
	 * @param shape type of geometry
	 */
	protected XODEGeom(String name, Matrix4f transform) {
		super(name, transform);
	}
	
	public XODEGeom(Node xodeData) {
		super(xodeData, false);
	}
	
	/**
	 * Creates a new ODE Geom object represented by this XODEObject.
	 * 
	 * @return new ODE Geom object represented by this XODEObject.
	 */
	public abstract Geom getGeom();
	
	public void buildOde(final World world, final Space space, final String namePrefix, List odeList) {
		// Recursivly searches the parent to find the nearest body ancestor
		Body parentBody = getFirstBodyAncestor();
	
		
		Geom geom = getGeom();
		geom.setName(namePrefix + this.getName());
		
		XODEParserDOM.log.debug("Processing Geom: " + geom.getName());
		
		XODEParserDOM.log.debug(space);
		
		// adds to parent body (if any)
		if (parentBody != null) {
			
			// Gets the transform with the ancestor body as
			// the root (rather than the XODE root)
			//XODETransform bodyRelTransform1 = this.getBodyRelTransform();
			
			Matrix4f bodyRelTransform = getBodyRelTransform();//bodyRelTransform1.getTransform();
			
			//parsedTransform.set(bodyRelTransform.getTransform());
			
			Matrix4f identity = new Matrix4f();
			identity.setIdentity();
			
			// If there is a transform (ie. it's not the IDENTITY matrix)
			// encapsulate
			if (!bodyRelTransform.equals(identity)) {
				
				GeomTransform tg = new GeomTransform();
				tg.setEncapsulatedGeom((PlaceableGeom) geom);
				
				parentBody.addGeom(tg);
				tg.setName(geom.getName());
				
				space.add(tg);
				
				Vector3f position = new Vector3f();
				bodyRelTransform.get(position);
				
				Quat4f rotation = new Quat4f();
				bodyRelTransform.get(rotation);
				
				//XODEParserDOM.odeLog.debug("Geom.setPosition");				
				((PlaceableGeom) geom).setPosition(position);
				//XODEParserDOM.odeLog.debug("Geom.setQuaternion");				
				((PlaceableGeom) geom).setQuaternion(rotation);
				
				geom = tg;
			} else {
			
				parentBody.addGeom((PlaceableGeom) geom);
				
				// adds to space
				space.add(geom);
			}
		} else {
			space.add(geom);
			
			// transforms into place
			XODEParserDOM.odeLog.debug("Geom.setPosition " + getPosition());				
			((PlaceableGeom)geom).setPosition(getPosition());
			XODEParserDOM.odeLog.debug("Geom.setQuaternion " + getRotation());				
			((PlaceableGeom)geom).setQuaternion(getRotation());
		
		}
		
		
		odeObject = geom;
		
		odeList.add(geom);
		addChildren(world, space, namePrefix, odeList);
		
	}

	public Element buildElement(Document doc) {
		Element element = doc.createElement("geom");
		   
	    // attributes
		element.setAttribute("name", this.getName());
		
	    // adds transform element
	    addTransformElement(element, doc);
		
		return element;
	}

	
}
