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

import org.odejava.Body;
import org.odejava.Geom;
import org.odejava.Joint;
import org.odejava.JointGroup;
import org.odejava.Space;
import org.odejava.World;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import javax.vecmath.Matrix4f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Root XODE node
 * 
 * @author William Denniss
 */
public class XODERoot extends XODEContainer implements Serializable {

	protected XODERoot() {
	}
	
	public XODERoot(String name, Matrix4f transform) {
		super(name, transform);
	}	
	
	public XODERoot(Node xode, Matrix4f parentTransform) {
		super(xode);
		
		NodeList children = xode.getChildNodes();
		
		XODEParserDOM.log.debug("Root!");
		this.relTransform = parentTransform;
		for (int i = 0; i < children.getLength(); i++) {
		
			Node currentChild = children.item(i);
		
			if (currentChild.getNodeName().equals("world")) {
				
				addChild(new XODEWorld(currentChild));
				
			}
			
			
			
		}
	}
	
	/**
	 * Creates an XODE tree based on an ODE scene
	 * 
	 * @param name name of the root node
	 * @param transform root transform
	 * @param world ODE World to parse
	 * @param space ODE Space to parse
	 */
	public XODERoot(String name, Matrix4f transform, World world, Space space) {
		this(name, transform);
		
		addChild(new XODEWorld(world, space));
	}
	
	/**
	 * Creates all the ODE bodies, geoms and joints represented in this XODE tree.
	 * All object names are prefixed with the given String.
	 * 
	 * @param world the World the ODE objects are added to
	 * @param space the Space the ODE objects are added to
	 * @param namePrefix String the names of the objects are prefixed by
	 */
	public List buildODEScene(World world, Space space, String namePrefix) {
		List odeObjects = new LinkedList();
		super.buildOde(world, space, namePrefix, odeObjects);
		return odeObjects;
	}

	/**
	 * Creates all the ODE bodies, geoms and joints represented in this XODE tree.
	 * All object names are prefixed with the given String.
	 * 
	 * @param world the World the ODE objects are added to
	 * @param space the Space the ODE objects are added to
	 * @param namePrefix String the names of the objects are prefixed by
	 * @param transform The transform offset that will be applied to all XODE nodes
	 * 
	 * @see #setRootTransform
	 */
	public List buildODEScene(World world, Space space, String namePrefix, Matrix4f transform) {
	    setRootTransform(transform);
	    return buildODEScene(world, space, namePrefix);
	}	
	
	/**
	 * Changes the root transform and forces child nodes to recalculate their
	 * relitive transforms next time a transform getter is called.
	 * Can be called before ODE objects are built to move the
	 * origin of XODE scene.
	 * 
	 * @param transform the replacement root transform
	 */
	public void setRootTransform(Matrix4f transform) {
		relTransform.set(transform);
		invalidateTransformCache();	
	}
	
	public Document buildDocument() throws ParserConfigurationException {
	    DocumentBuilderFactory factory 
        = DocumentBuilderFactory.newInstance();
       DocumentBuilder builder = factory.newDocumentBuilder();
       DOMImplementation impl = builder.getDOMImplementation();
       
       
       Document xodeDoc = impl.createDocument(
       null, "xode", null
       );  
       
       Element xode = xodeDoc.getDocumentElement();
       
       buildElement(xodeDoc);
       
       xode.setAttribute("version", "1.0r23");
       xode.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
       xode.setAttribute("xsi:noNamespaceSchemaLocation", "http://tanksoftware.com/xode/1.0r23/xode.xsd");
       
       return xodeDoc;
	}
	
	public Element buildElement(Document doc) {
	    Element element = doc.getDocumentElement();

	    // attributes
	    element.setAttribute("name", this.getName());
	    
	    // adds transform element
	    addTransformElement(element, doc);

	    // adds child groups
	    addChildElements(element, doc);

	    return element;
	    
	}
	
	/**
	 * Attempts to remove and delete all given ODE objects from their respective
	 * WorldS and SpaceS.  Use with the returned List from buildODEScene to remove
	 * all created ODE objects.
	 * 
	 * @param odeObjects the list of ODE objects to be removed
	 */
	public static void removeAllODEObjects(List odeObjects) {
	    for (Iterator i = odeObjects.iterator(); i.hasNext(); ) {
	        Object ode = i.next();
	        if (ode instanceof Body) {
	            World world = ((Body) ode).getContainingWorld();
	            if (world.containsBody(((Body) ode).getName())) {
	                world.deleteBody((Body) ode);
	            }
	            
	        } else if (ode instanceof Geom) {
	            ((Geom) ode).delete();
	            
	        } else if (ode instanceof Joint) {
	            ((Joint) ode).delete();
	            
	        } else if (ode instanceof JointGroup) {
	            ((JointGroup) ode).delete();
	        }
	        
	    }
	}
	
}
