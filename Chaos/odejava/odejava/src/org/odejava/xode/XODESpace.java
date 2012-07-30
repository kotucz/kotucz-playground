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
import java.util.Iterator;

import javax.vecmath.Matrix4f;

import org.odejava.*;
import org.odejava.Space;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XODE representation of a World.  Note - doesn't create a world currently.
 * 
 * @author William Denniss
 */
public class XODESpace extends XODEGroup implements Serializable {

	protected XODESpace() {
	}

	public XODESpace(String name, Matrix4f transform) {
		super(name, transform);
	}

	
	public XODESpace(Node xodeData) {
		super(xodeData, false);
		
		NodeList children = xodeData.getChildNodes();
		
		//transform = XODETransform.IDENTITY;
		
		XODEParserDOM.log.debug("SPAce!");
		

		parseTransform(xodeData);
		parseGroup(xodeData);
		
	}

	/**
	 * @param space
	 */
	public XODESpace(Space space) {
		
		for (Iterator i = space.getGeoms().iterator(); i.hasNext(); ) {
			XODESpace.addChild(this, (Geom) i.next());
		}
	}
	
	public static void addChild(XODEContainer parent, Geom currentGeom) {
		Matrix4f identity = new Matrix4f();
		identity.setIdentity();
		addChild(parent, currentGeom, identity);
	}
	public static void addChild(XODEContainer parent, Geom currentGeom, Matrix4f transform) {
		if (currentGeom instanceof GeomBox) {
			parent.addChild(new XODEGeomBox(currentGeom, transform));
		
		} else if (currentGeom instanceof GeomCappedCylinder) {
			parent.addChild(new XODEGeomCappedCylinder(currentGeom, transform));
		
		} else if (currentGeom instanceof GeomCone) {
			parent.addChild(new XODEGeomCone(currentGeom, transform));
		
		} else if (currentGeom instanceof GeomCylinder) {
			parent.addChild(new XODEGeomCylinder(currentGeom, transform));
		
		} else if (currentGeom instanceof GeomPlane) {
			parent.addChild(new XODEGeomPlane(currentGeom, transform));
		
		} else if (currentGeom instanceof GeomRay) {
			parent.addChild(new XODEGeomRay(currentGeom, transform));
		
		} else if (currentGeom instanceof GeomSphere) {
			parent.addChild(new XODEGeomSphere(currentGeom, transform));
		
		} else if (currentGeom instanceof GeomTriMesh) {
			//addChild(new XODEGeomSphere(currentGeom));
			throw new XODEException("Trimesh unsupported currently");
		
		} else if (currentGeom instanceof GeomTransform) {
			
			Matrix4f encapsulatedTransform = new Matrix4f();
			transform.setIdentity();
			PlaceableGeom encapsulatedGeom = ((PlaceableGeom) ((GeomTransform) currentGeom).getEncapsulatedGeom());
			transform.setRotation(encapsulatedGeom.getQuaternion());
			transform.setTranslation(encapsulatedGeom.getPosition());
			
			addChild(parent, encapsulatedGeom, encapsulatedTransform);
		
		} else {
			throw new XODEException("Unknown geom type: " + currentGeom.getClass().getName());
		}		
	}

	public Element buildElement(Document doc) {
	    Element element = doc.createElement("space");

	    // attributes
	    element.setAttribute("name", this.getName());
	    
	    // adds transform element
	    addTransformElement(element, doc);

	    // adds child groups
	    addChildElements(element, doc);

	    return element;
	}
	
	
}
