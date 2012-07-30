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

import org.odejava.Body;
import org.odejava.Space;
import org.odejava.World;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XODE representation of a World.  Note - doesn't create a world currently.
 * 
 * @author William Denniss
 */
public class XODEWorld extends XODEContainer implements Serializable {
	
	protected XODEWorld() {
	}

	public XODEWorld(Node xodeData) {
		super(xodeData);
		
		


		parseTransform(xodeData);
		
		XODEParserDOM.log.debug("World!");
		
		
		NodeList children = xodeData.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
		
			Node currentChild = children.item(i);
		
			if (currentChild.getNodeName().equals("space")) {
				
				addChild(new XODESpace(currentChild));
				
			}
			

			
		}
		
	}

	/**
	 * Creates an XODEWorld tree based on an ODE scene.
	 * Iterates though all bodies, adding them and any associated joints
	 * and child geometry.  Then iterates through all static geometry, adding them as well.
	 * 
	 * @param world ODE World to parse.
	 * @param wpace ODE Space to parse.
	 */
	public XODEWorld(World world, Space space) {
		XODESpace xSpace = new XODESpace(space);
		addChild(xSpace);
		for (Iterator i = world.getBodies().iterator(); i.hasNext(); ) {
			Body currentBody = (Body) i.next();
			xSpace.addChild(new XODEBody(currentBody));
			
		}
	}

	public Element buildElement(Document doc) {
	    Element element = doc.createElement("world");

	    // attributes
	    element.setAttribute("name", this.getName());
	    
	    // adds transform element
	    addTransformElement(element, doc);

	    // adds child groups
	    addChildElements(element, doc);

	    return element;
	    
	}	
	
}
