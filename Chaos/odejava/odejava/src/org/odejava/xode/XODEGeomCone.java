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

import javax.vecmath.Matrix4f;

import org.odejava.Geom;
import org.odejava.GeomCone;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An immutable Cone
 * 
 * @author William Denniss
 */
public class XODEGeomCone extends XODEGeom implements Serializable {

	private float radius, length;
	
	protected XODEGeomCone() {
	}
	
	public XODEGeomCone(final float radius, final float length) {
		this.radius = radius;
		this.length = length;
	}

	public XODEGeomCone(Node xodeData) {
		super(xodeData);
		
		try {
		
			XODEParserDOM.log.debug("Geom!");
			
			parseTransform(xodeData);
			
			NodeList children = xodeData.getChildNodes();
			
			for (int i = 0; i < children.getLength(); i++) {
			
				Node currentChild = children.item(i);
			
				
				
				/*
				 * Geom shapes (box, sphere)
				 */
				
				
				//cone
				if (currentChild.getNodeName().equals("cone")) {
					radius = DOMUtil.attributeFloat(currentChild, "radius");
					length = DOMUtil.attributeFloat(currentChild, "length");
					

				} else {
					
					this.parseGroupChild(currentChild);
				}
				
			}
						
		} catch (NumberFormatException e) {
			throw new XODEException("Error parsing numeric values", e);
		}
		
		
	}	
	/**
	 * @param currentGeom
	 * @param transform
	 */
	public XODEGeomCone(Geom currentGeom, Matrix4f transform) {
		// TODO extract Geom properties
		throw new XODEException("unimplemented");

	}

	public Geom getGeom() {
		return new GeomCone(radius, length);
	}
	
	public Element buildElement(Document doc) {
	    
	    // creates parent <geom> element
	    Element geom = super.buildElement(doc);
	    
	    // creates the box element
	    Element cone = doc.createElement("cone");
	    cone.setAttribute("radius", radius +"");
	    cone.setAttribute("length", length +"");
	    geom.appendChild(cone);
	    
	    // adds child nodes to parent geom element
		addChildElements(geom, doc);
		
		return geom;
	}	

	/**
	 * @return Returns the length.
	 */
	public float getLength() {
		return length;
	}
	/**
	 * @return Returns the radius.
	 */
	public float getRadius() {
		return radius;
	}
}
