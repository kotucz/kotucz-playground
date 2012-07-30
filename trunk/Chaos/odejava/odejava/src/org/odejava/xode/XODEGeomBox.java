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
import org.odejava.GeomBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * A box.
 * 
 * @author William Denniss
 */
public class XODEGeomBox extends XODEGeom implements Serializable {

	private float sizex;
	private float sizey;
	private float sizez;
	
	protected XODEGeomBox() {
	}
	
	public XODEGeomBox(final float sizex, final float sizey, final float sizez) {
		this.sizex = sizex;
		this.sizey = sizey;
		this.sizez = sizez;
	}
	
	public XODEGeomBox(Node xodeData) {
		super(xodeData);
		
				
		try {
		
			XODEParserDOM.log.debug("Geom!");
			
			parseTransform(xodeData);
			
			NodeList children = xodeData.getChildNodes();
			
			for (int i = 0; i < children.getLength(); i++) {
			
				Node currentChild = children.item(i);
			
				//box
				if (currentChild.getNodeName().equals("box")) {
					sizex = Float.parseFloat(currentChild.getAttributes().getNamedItem("sizex").getNodeValue());
					sizey = Float.parseFloat(currentChild.getAttributes().getNamedItem("sizey").getNodeValue());
					sizez = Float.parseFloat(currentChild.getAttributes().getNamedItem("sizez").getNodeValue());
					
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
	public XODEGeomBox(Geom currentGeom, Matrix4f transform) {
		// TODO extract Geom properties
		throw new XODEException("unimplemented");
	}

	public Geom getGeom() {
		return new GeomBox(sizex, sizey, sizez);
	}
	
	public Element buildElement(Document doc) {
	    
	    // creates parent <geom> element
	    Element geom = super.buildElement(doc);
	    
	    // creates the box element
	    Element box = doc.createElement("box");
	    box.setAttribute("sizex", sizex +"");
	    box.setAttribute("sizey", sizey +"");
	    box.setAttribute("sizez", sizez +"");
	    geom.appendChild(box);
	    
	    // adds child nodes to parent geom element
		addChildElements(geom, doc);
		
		return geom;
	}

	public float getSizex() {
		return sizex;
	}

	public float getSizey() {
		return sizey;
	}

	public float getSizez() {
		return sizez;
	}

	public void setSizex(float sizex) {
		this.sizex = sizex;
	}

	public void setSizey(float sizey) {
		this.sizey = sizey;
	}

	public void setSizez(float sizez) {
		this.sizez = sizez;
	}
	
}
