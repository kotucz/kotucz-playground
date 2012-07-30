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
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.w3c.dom.*;

import org.odejava.*;

/**
 * Base XODE container (as defined in the spec)
 * 
 * @author William Denniss
 */
public abstract class XODEContainer extends XODEObject implements Serializable {

	protected List xodeObjects = new LinkedList();
	private XODETransform parsedTransform = null;
	protected Matrix4f relTransform = new Matrix4f();
	protected boolean absoluteTransform = false;
	
	protected transient Matrix4f cachedTransform = null;
	protected transient Matrix4f cachedBodyRelTransform = null;
	protected transient Matrix4f cachedRootTransform = null;
	
	
	
	protected XODEContainer() {
		this.relTransform.setIdentity();
	}
	
	public XODEContainer(String name, Matrix4f transform) {
		super(name);
		
		relTransform.set(transform);
	}
	
	
	public XODEContainer(Node node) {
		super(node);
		this.relTransform.setIdentity();
	}
	
	 
	/**
	 * Parses the transform from the XODE structured XML data
	 * 
	 * @param xodeData the node to parse
	 */
	protected void parseTransform(Node xodeData) {
		
		NodeList children = xodeData.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
		
			Node currentChild = children.item(i);
		
			if (currentChild.getNodeName().equals("transform")) {
					
					// TODO: test for absolute
			    		parsedTransform = XODETransform.parseTransformNode(currentChild);
					relTransform = parsedTransform.getRelTransform();
					
					break;
			}
		}
		
		
	}
	
	/**
	 * Returns the transform of the ancestor XODERoot
	 * or the identity matrix if none.  Result is cached for efficiancy.
	 * 
	 * @return the transform of the ancestor XODERoot
	 * or the identity matrix if none.
	 */
	public Matrix4f getRootTransform() {
		if (cachedRootTransform != null) {
			return cachedRootTransform;
		}
		
		if (this instanceof XODERoot) {
			Matrix4f rootTransform = new Matrix4f();
			rootTransform.set(relTransform);
			return rootTransform;
		}
		
		if (getParent() == null) {
			cachedRootTransform = new Matrix4f();
			cachedRootTransform.setIdentity();
		} else {
			cachedRootTransform = getParent().getRootTransform();
		}
		
		return cachedRootTransform;
	}
	
	/**
	 * Recursivly builds the transform of this container
	 * relative to its parent body.  Used for GeomTransform
	 * where ODE maintains the relationship so one only 
	 * needs to specify the offset from the body (i.e. relative transform).
	 * 
	 * @return the transform of this container relative to its parent body
	 * @throws XODEException if no body ancestor was found
	 */
	public Matrix4f getBodyRelTransform() {
		
		// if cached - return the cached result to avoid recalculating
		if (cachedBodyRelTransform != null) {
			return cachedBodyRelTransform;
		}
		
		if (this instanceof XODEBody) {
			Matrix4f identity = new Matrix4f();
			identity.setIdentity();
			return identity;
		} else if (this instanceof XODERoot) {
			throw new XODEException("Root node reached - no Body ancestor found");
		}
		
		if (getParent() == null) {
			throw new XODEException("Null parent encountered - no Body ancestor found");
		}
		
		Matrix4f parent = getParent().getBodyRelTransform();
		XODEParserDOM.log.debug("^^^ parent " + parent);
		
		Matrix4f child = relTransform;
		XODEParserDOM.log.debug("^^^ child " + getName() + " " + this.getClass().getName() + "\n" + child);
		
		parent.mul(child);

		// cache the result
		cachedBodyRelTransform = new Matrix4f();
		cachedBodyRelTransform.set(parent);
		
		return parent;
		
	}
	
	public void buildOde(final World world, final Space space, final String namePrefix, List odeList) {
		addChildren(world, space, namePrefix, odeList);
	}

	
	public void addChildren(World world, Space space, String namePrefix, List odeList) {
		for (Iterator i = this.xodeObjects.iterator(); i.hasNext() ;) {
			((XODEObject) i.next()).buildOde(world, space, namePrefix, odeList);
		}
	}
	
	/**
	 * Adds a child XODEObject to this container
	 * 
	 * @param toAdd the XODEObject to add
	 */
	public void addChild(XODEObject toAdd) {
		toAdd.setParent(this);
		xodeObjects.add(toAdd);
	}
	
	/**
	 * Removes a child XODEObject from this container
	 * 
	 * @return toRemove the XODEObject to remove
	 */
	public void removeChild(XODEObject toRemove) {
	    toRemove.setParent(null);
	    xodeObjects.remove(toRemove);
	}
	
	
	/**
	 * Returns a List containing all child XODEObjectS
	 * 
	 * @return a List containing all child XODEObjectS
	 */
	public List getChildren() {
	    return new LinkedList(xodeObjects);
	}
	
	/**
	 * Calculates and caches the virtual world transform.
	 * Recursivly calculateTransform on the parent XODEContainer
	 * (if any).
	 *
	 */
	private final void calculateTransform() {
		
		if (cachedTransform == null) {
			cachedTransform = new Matrix4f();
			
			// absolute transforms are relative only to the root node
			if (absoluteTransform) {
				cachedTransform.set(getRootTransform());
			
			// recursivly calculates the parent transform if the node has a parent
			} else if (getParent() != null) {
			    getParent().getTransform(cachedTransform);
			
			// if no parent, use the identity matrix
			} else {
				cachedTransform.setIdentity();
			}
			
			// multiplies the parent transform by this object's transform
			cachedTransform.mul(this.relTransform);
		}
		
	}	
	
	/**
	 * Gets the virtual world transform of this object.
	 * 
	 * @return the virtual world transform of this object.
	 */
	public final Matrix4f getTransform() {
		return getTransform(new Matrix4f());
	}
	
	/**
	 * Gets the virtual world transform of this object.
	 * 
	 * @param transform the Matrix to store the transform in (will be overwritten and returned)
	 * @return the virtual world transform of this object.
	 */
	public final Matrix4f getTransform(Matrix4f transform) {
		
		calculateTransform();
		
		// returns the transform
		transform.set(cachedTransform);
		return transform;
	}
	
	/**
	 * Gets a Vector representing the virtual world position of this object
	 * 
	 * @return the virtual world position of this object
	 */
	public Vector3f getPosition() {
		calculateTransform();
		
		Vector3f result = new Vector3f();
		cachedTransform.get(result);
		return result;
	}
	
	/**
	 * Gets a Quaternion representing the virtual world rotation of this object
	 * 
	 * @return the virtual world rotation of this object
	 */
	public Quat4f getRotation() {
		calculateTransform();
		
		Quat4f result = new Quat4f();
		cachedTransform.get(result);
		return result;
	}
	
	/**
	 * Sets the position of this object.
	 * If the transform mode is relative, it is relative to the
	 * parent XODEContainer (if any), else the transform is absolute
	 * and is relative only to the master transform of the XODERoot.
	 * 
	 * @param position the new position of this object.
	 * @see #setAbsolute
	 */
	public void setPosition(Vector3f position) {
		relTransform.setTranslation(position);
		
		invalidateTransformCache();
	}
	
	/**
	 * Sets the rotation of this object.
	 * If the transform mode is relative, it is relative to the
	 * parent XODEContainer (if any), else the transform is absolute
	 * and is relative only to the master transform of the XODERoot.
	 * 
	 * @param rotation the new rotation of this object.
	 * @see #setAbsolute
	 */
	public void setRotation(Quat4f rotation) {
		relTransform.setRotation(rotation);
		
		invalidateTransformCache();
	}
	
	/**
	 * Sets the transform of this object.
	 * If the transform mode is relative, it is relative to the
	 * parent XODEContainer (if any), else the transform is absolute
	 * and is relative only to the master transform of the XODERoot.
	 * 
	 * @param transform the new transform of this object.
	 * @see #setAbsolute
	 */
	public void setTransform(Matrix4f transform) {
		relTransform.set(transform);
		
		invalidateTransformCache();
	}
	
	/**
	 * Sets the transform mode.  If absolute, this objects transform
	 * is relative only to the XODERoot ancestor (if any).  Else, it is relative
	 * to the parent XODEContainer (if any) 
	 * 
	 * @param absolute true for absolute mode, false for relative mode
	 * @see #isAbsoluteTransformMode()
	 */
	public void setAbsolute(boolean absolute) {
		this.absoluteTransform = absolute;
	}
	
	/**
	 * Returns true if the transform mode for this object is absolute
	 * 
	 * @return true if the transform mode for this object is absolute
	 * @see #setTransform
	 */
	public boolean isAbsoluteTransformMode() {
		return absoluteTransform;
	}
	
	
	/**
	 * Forces recomputation of all transforms in the XODE tree next time
	 * getTransform, getPosition or getRotation is called.  Called when
	 * a new parent transform is set in XODERoot.
	 * 
	 * @see XODERoot#setRootTransform
	 */
	protected void invalidateTransformCache() {
		cachedTransform = null;
		cachedBodyRelTransform = null;
		cachedRootTransform = null;
		
		for (Iterator i = this.xodeObjects.iterator(); i.hasNext() ;) {
			XODEObject currentChild = (XODEObject) i.next();
			if (currentChild instanceof XODEContainer) {
				((XODEContainer) currentChild).invalidateTransformCache();
			}
		}
	}
	
	/**
	 * Dereferences the cached ODE object (if any) for this XODEContainer and all
	 * child XODEContainers and XODEObjects.
	 * 
	 * @see XODEObject#odeObject
	 */
	public void scrapODEObjectCache() {
		odeObject = null;
		
		for (Iterator i = this.xodeObjects.iterator(); i.hasNext() ;) {
			XODEObject currentChild = (XODEObject) i.next();
			if (currentChild instanceof XODEContainer) {
				((XODEContainer) currentChild).scrapODEObjectCache();
			} else {
				((XODEObject) currentChild).odeObject = null;
			}
		}
	}
	
	/**
	 * Dereferences the cached XML Node object (if any) for this XODEContainer and all
	 * child XODEContainers and XODEObjects.
	 * 
	 * @see XODEObject#node
	 */
	public void scrapDOMObjectCache() {
		node = null;
		
		for (Iterator i = this.xodeObjects.iterator(); i.hasNext() ;) {
			XODEObject currentChild = (XODEObject) i.next();
			if (currentChild instanceof XODEContainer) {
				((XODEContainer) currentChild).scrapDOMObjectCache();
			} else {
				((XODEObject) currentChild).node = null;
			}
		} 
	}
	
	/**
	 * Adds a new Element representing the transform of this container
	 * to the passed Element.
	 * 
	 * @param element the element to which the transform element will be added
	 * @param doc the Document this Element will be associated with
	 */
	protected void addTransformElement(Element element, Document doc) {
		XODETransform transform = getXODETransform();
		if (transform != null) {
		    element.appendChild(getXODETransform().buildElement(doc));
		}
	}
	
	protected void addChildElements(Element parent, Document doc) {
	    
	    // TODO must export in the order geom/body/group/joint/jointgroup/mass
	    for (Iterator i = xodeObjects.iterator(); i.hasNext(); ) {
	        parent.appendChild(((XODEObject) i.next()).buildElement(doc));
	    }
	}
	

	/**
	 * Sets the parent of this object. and invalidates the transform cache for
	 * this branch of the XODE tree.  Should only be called by the <code>addChild()</code> method
	 * of <code>XODEContainer</code>.
	 * 
	 * @param parent the parent of this object or null if there isn't one
	 * @throws XODEException if this XODEObject has a parent and a non-null parent was passed
	 * 
	 * @see XODEContainer#addChild(XODEObject)
	 */
	protected void setParent(XODEContainer parent) {
		super.setParent(parent);
		invalidateTransformCache();
	}

	protected XODETransform getXODETransform() {
	    return parsedTransform;
	}
	
}
