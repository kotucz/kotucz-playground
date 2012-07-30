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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * XODE representation of a World.  Note - doesn't create a world currently.
 * 
 * @author William Denniss
 */
public class XODEGroup extends XODEContainer implements Serializable {

	protected XODEGroup() {
	}
	
	public XODEGroup(String name, Matrix4f transform) {
		super(name, transform);
	}
	
	/**
	 * Creates and parses the XODE group
	 * 
	 * @param world current world
	 * @param space current space
	 * @param parent parent XODE node
	 * @param xodeData XML data
	 */
	public XODEGroup(Node xodeData) {
		this(xodeData,true);
	}
	
	/**
	 * Creates and optionally parses the XODE group.
	 * The data is parsed when this is a <group&gt; node but not
	 * when it is a sub-group such as a <body&gt;.
	 * 
	 * @param world current world
	 * @param space current space
	 * @param parent parent XODE node
	 * @param xodeData XML data
	 * @param parseData boolean to indicate if the XML node data should be parsed
	 */	
	public XODEGroup(Node xodeData, boolean parseData) {
		super(xodeData);
	
		if (parseData) {

			parseTransform(xodeData);
			parseGroup(xodeData);
		}
	}
	
	/**
	 * Iterates though all the child nodes of this NODE and
	 * calls parseGroupChild on each.
	 * 
	 * @param xodeData the XML data to parse
	 */
	protected void parseGroup (Node xodeData) {
		
		
		XODEParserDOM.log.debug("Group!");
		
		NodeList children = xodeData.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
		
			Node currentChild = children.item(i);
		
			parseGroupChild(currentChild);
			
		}
		
	}
	
	/**
	 * Checks to see if the given child node is an XODE object
	 * or container (i.e. body, group, geom, space etc...)
	 * 
	 * @param currentChild the XODE XML node
	 */
	protected void parseGroupChild(Node currentChild) {
		
		if (currentChild.getNodeName().equals("body")) {
			
			addChild(new XODEBody(currentChild));
			
		} else if (currentChild.getNodeName().equals("geom")) {
			
			NodeList children = currentChild.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
			
				String geomType = children.item(i).getNodeName();
				
				if (geomType.equals("box")) {
					addChild(new XODEGeomBox(currentChild));
					return;
				
				} else if (geomType.equals("sphere")) {
					addChild(new XODEGeomSphere(currentChild));
					return;
				
				} else if (geomType.equals("plane")) {
					addChild(new XODEGeomPlane(currentChild));
					return;
				
				} else if (geomType.equals("cylinder")) {
					addChild(new XODEGeomCylinder(currentChild));
					return;
				
				} else if (geomType.equals("cone")) {
					addChild(new XODEGeomCone(currentChild));
					return;
				
				} else if (geomType.equals("cappedCylinder")) {
					addChild(new XODEGeomCappedCylinder(currentChild));
					return;
				
				} else if (geomType.equals("ray")) {
					addChild(new XODEGeomRay(currentChild));
					return;
				
				} else if (geomType.equals("trimesh")) {
					//TODO: Trimesh support	
					throw new XODEException("trimesh isn't supported yet");
				}
			}
			
			throw new XODEException("unable to find child geom object in: " + currentChild.getNodeName());
			
			
			
		} else if (currentChild.getNodeName().equals("joint")) {
			
			addChild(new XODEJoint(currentChild));
			
		} else if (currentChild.getNodeName().equals("group")) {
			
			addChild(new XODEGroup(currentChild));
			
		} else if (currentChild.getNodeName().equals("jointgroup")) {
			
			addChild(new XODEJointGroup(currentChild));
			
		} else if (currentChild.getNodeName().equals("space")) {
			
			addChild(new XODESpace(currentChild));
			
		}
	
	}	

	public Element buildElement(Document doc) {
	    Element element = doc.createElement("group");

	    // attributes
	    element.setAttribute("name", this.getName());
	    
	    // adds transform element
	    addTransformElement(element, doc);

	    // adds child groups
	    addChildElements(element, doc);

	    return element;
	}
	
}
