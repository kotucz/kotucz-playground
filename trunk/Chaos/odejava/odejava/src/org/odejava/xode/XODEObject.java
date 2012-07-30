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

import org.odejava.Body;
import org.odejava.JointGroup;
import org.odejava.Space;
import org.odejava.World;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Base XODE object (as defined in the spec)
 * 
 * @author William Denniss
 */
public abstract class XODEObject implements Serializable {

	private XODEContainer parent;
	private String name;
	
	/**
	 * The ODE objects created last time buildOde was called.
	 * Used to aid calculations of relitive transforms while building the
	 * ODE representation of the XODE tree.  Outside the buildOde method
	 * should be used for debugging use only.
	 */
	protected transient Object odeObject;
	
	/**
	 * The DOM objects that represents this XODEObject generated
	 * while importing/exporting to/from XML.
	 * Debugging use only, not designed to be be accessed in normal operation.
	 */
	protected transient Node node;
	
	protected XODEObject() {
	}
	
	public XODEObject(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the world and space variables, and parses the 'name' attribute.
	 * 
	 * @param parent the parent container
	 * @param node the XML data
	 */
	protected XODEObject(Node node) {
		this.node = node;
		
		// parses the node name
		if (node != null) {
			XODEParserDOM.log.debug("Name attribute parsed: " + name);
			this.name = DOMUtil.attributeString(node, "name");
		}
	}


	/**
	 * Recursivly locates then returns the Body of the first XODEBody 
	 * ancestor or null if there is none.  An ancestor is a parent
	 * or a parent of a parent ad infinitum.  Used in the buildOde
	 * method to aid in calculations of relitive transforms.
	 * 
	 * @return the Body of the first XODEBody ancestor or null if 
	 * there is none
	 */
	protected Body getFirstBodyAncestor() {
		
		if (this instanceof XODERoot) {
			return null;
		} else if (this instanceof XODEBody) {
			return (Body) ((XODEBody) this).getODEObject(); 
		} else {
			return this.parent.getFirstBodyAncestor();
		}
	}

	/**
	 * Recursivly locates then returns the JointGroup of the first XODEJointGroup 
	 * ancestor or null if there is none.  An ancestor is a parent
	 * or a parent of a parent ad infinitum.
	 * 
	 * @return the JointGroup of the first XODEJointGroup ancestor or null if 
	 * there is none
	 */
	protected JointGroup getFirstJointGroupAncestor() {
		
		if (this instanceof XODERoot) {
			return null;
		} else if (this instanceof XODEJointGroup) { 
			return (JointGroup) ((XODEJointGroup) this).getODEObject(); 			
		} else {
			return this.parent.getFirstJointGroupAncestor();
		}
	}

	/**
	 * Recursivly locates then returns the Space of the first XODESpace 
	 * ancestor or null if there is none.  An ancestor is a parent
	 * or a parent of a parent ad infinitum.
	 * 
	 * @return the Space of the first XODESpace ancestor or null if 
	 * there is none
	 */
	protected Space getFirstSpaceAncestor() {
		
		if (this instanceof XODERoot) {
			return null;
		} else if (this instanceof XODESpace) { 
			return (Space) ((XODESpace) this).getODEObject();
		} else {
			return this.parent.getFirstSpaceAncestor();
		}
	}
	
	/**
	 * Returns the Odejava object represented by this XODEObject or
	 * null if no Odejava object has been created, or the cache was
	 * scrapped.
	 * 
	 * @return the Odejava object represented by this XODEObject
	 */
	public Object getODEObject() {
		return odeObject;
	}

	/**
	 * Returns DOM node represented by this XODEObject or null
	 * if no XML import/export functions have been performed
	 * on this XODEObject, or the cache was lost by purging
	 * or serialization.  Not suggestd to be used for
	 * non-debugging reasons.
	 *
	 * @return the DOM node represented by this XODEObject
	 */
	public Node getDOMNode() {
		return node;
	}
	
	/**
	 * Constructs the ODE scene based on this XODE structure
	 * 
	 * @param world the World to add created bodies
	 * @param space the Space to add created geometry
	 * @param namePrefix string to prefix all names
	 * @param odeList list of all created ODE files (will be added to)
	 */
	public abstract void buildOde(World world, Space space, String namePrefix, List odeList);

	/**
	 * Sets the parent of this object. Should only be called by the <code>addChild()</code> method
	 * of <code>XODEContainer</code>.
	 * 
	 * @param parent the parent of this object or null if there isn't one
	 * @throws XODEException if this XODEObject has a parent and a non-null parent was passed
	 * 
	 * @see XODEContainer#addChild(XODEObject)
	 */
	protected void setParent(XODEContainer parent) {
		if (this.parent == null || parent == null) {
			this.parent = parent;
		} else {
			throw new XODEException("Cannot assign a new parent (name: " + parent.getName() + ", class: " + parent.getClass().getName() + ") to this object (name: " + getName() + ", class: " + getClass().getName() + ") as it already has a parent and cannot have two parents (this would violate the directed acyclic graph constraint of the XODE tree).");
		}
				
	}
	
	/**
	 * Builds an XML DOM representation of this XODEObject
	 * 
	 * @param doc the Document this Element will be associated with
	 * @return and XML DOM representation of this XODEObject
	 */
	protected abstract Element buildElement(Document doc);

	/**
	 * Returns the parent XODEContainer of this XODEObject
	 * or null if it does not have a parent.
	 * 
	 * @return this object's parent container
	 * 
	 * @see XODEContainer#addChild(XODEObject)
	 */
	public XODEContainer getParent() {
	    return parent;
	}
	
	/**
	 * Returns the name of this XODEObject or null
	 * if it has no name.
	 * 
	 * @return the name of this XODEObject
	 */
	public String getName() {
	    return name;
	}
	
	/**
	 * Sets the name to the given new name.
	 * 
	 * @param name the new name to use
	 */
	public void setName(String name) {
	    this.name = name;
	}
	
	/**
	 * Returns a deep copy of this XODEObject
	 *
	 * @return a deep copy of this XODEObject
	 */
	public Object clone() {

		XODEObject object = null;
		
		try {
			object = (XODEObject)DOMUtil.deepCopy(this);		
		} catch(Exception e1) {
			System.out.println(e1);
		}
		
		object.setParent(null);
		return object;
	
	}
}
