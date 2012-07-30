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
import java.util.List;

import javax.vecmath.Matrix4f;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


import org.odejava.*;

/**
 * XODE representation of a Body
 * 
 * @author William Denniss
 */
public class XODEBody extends XODEGroup implements Serializable {

	protected Float finiteRotationMode;
	protected Float finiteRotationAxisX;
	protected Float finiteRotationAxisY;
	protected Float finiteRotationAxisZ;
	
	protected Float totalMass;
	
	protected XODEBody() {
	}
	
	public XODEBody(String name, Matrix4f transform) {
		super(name, transform);
	}
	
	public XODEBody(Node xodeData) {
		super(xodeData, false);
		
		XODEParserDOM.log.debug("Body!");
		

		parseTransform(xodeData);
				
		
		NodeList children = xodeData.getChildNodes();
		
		
		for (int i = 0; i < children.getLength(); i++) {
		
			Node currentChild = children.item(i);
			
			if (currentChild.getNodeName().equals("finiteRotation")) {
				finiteRotationMode = new Float(DOMUtil.attributeFloat(currentChild, "mode"));
				try {
					finiteRotationAxisX = new Float(DOMUtil.attributeFloat(currentChild, "xaxis"));
					finiteRotationAxisY = new Float(DOMUtil.attributeFloat(currentChild, "yaxis"));
					finiteRotationAxisZ = new Float(DOMUtil.attributeFloat(currentChild, "zaxis"));				
				} catch (NumberFormatException e) {
					
				}
			}
			
			if (currentChild.getNodeName().equals("mass")) {
				NodeList massChildren = currentChild.getChildNodes();

				for (int j = 0; j < massChildren.getLength(); j++) {

					Node currentMassChild = massChildren.item(j);
					
						if (currentMassChild.getNodeName().equals("adjust")) {
							Node totalAttr = currentMassChild.getAttributes().getNamedItem("total");
							if (totalAttr != null) {
								XODEParserDOM.odeLog.debug("Body.adjustMass " + Float.parseFloat(totalAttr.getNodeValue()));
								
								totalMass = new Float(totalAttr.getNodeValue());	
							}
							
						}		
				}
				
			}
			
			
			parseGroupChild(currentChild);
			
			XODEParserDOM.odeLog.debug("Body.setPosition " + getPosition());
			
			
			XODEParserDOM.odeLog.debug("Body.setQuaternion " + getRotation());
							
			
			
		
		}
		


	}
	
	/**
	 * Creates an XODEBody based on the given ODE body.
	 * Also adds child geometry as XODEGeom obejcts.
	 * 
	 * @param body the ODE body to use
	 */
	public XODEBody(Body body) {
		this.setName(body.getName());
		
		setAbsolute(true);
		setPosition(body.getPosition());
		setRotation(body.getQuaternion());
		
		for (Iterator i = body.getGeoms().iterator(); i.hasNext(); ) {
			XODESpace.addChild(this, (Geom) i.next());
		}
		
	}

	public void buildOde(final World world, final Space space, final String namePrefix, List odeList) {
		Body boxBody = new Body(namePrefix + this.getName(), world);
		odeObject = boxBody;
		
		if (finiteRotationMode != null) {
			boxBody.setFiniteRotationMode((int) finiteRotationMode.floatValue());
			
				if (finiteRotationAxisX != null && finiteRotationAxisY != null && finiteRotationAxisZ != null) {
					boxBody.setFiniteRotationAxis(finiteRotationAxisX.floatValue(), finiteRotationAxisY.floatValue(), finiteRotationAxisZ.floatValue());
				}
			
		}
		
		
		if (totalMass != null) {
			boxBody.adjustMass(totalMass.floatValue());
		}
		
		boxBody.setPosition(getPosition());
		boxBody.setQuaternion(getRotation());
		
		odeList.add(boxBody);
		
		addChildren(world, space, namePrefix, odeList);
		
		//TODO: remove blatent hack
		boxBody.adjustMass(1f);
		

	}

	public Element buildElement(Document doc) {
	    Element element = doc.createElement("body");
	   
	    // attributes
	    element.setAttribute("name", this.getName());
	    
	    // adds transform element
	    addTransformElement(element, doc);
	    
	    // torque
	    
	    // force
	    
	    // finiteRotation
	    if (finiteRotationMode != null) {
	        Element finateRotationElement = doc.createElement("finiteRotation");
	        finateRotationElement.setAttribute("mode", this.finiteRotationMode.floatValue()+"");
	        finateRotationElement.setAttribute("xaxis", this.finiteRotationAxisX.floatValue()+"");
	        finateRotationElement.setAttribute("yaxis", this.finiteRotationAxisX.floatValue()+"");
	        finateRotationElement.setAttribute("zaxis", this.finiteRotationAxisX.floatValue()+"");
	        
	        element.appendChild(finateRotationElement);
	    }
	    
	    // linearVel
	    // TODO: unimplemented in the importer
	    
	    // angularVel
	    // TODO: unimplemented in importer
	    
	    // child nodes
	    addChildElements(element, doc);
	    
	    // mass
	    System.out.println("totalMass!!! " + totalMass);
	    if (totalMass != null) {
	        Element massElement = doc.createElement("mass");
	        
	        Element adjust = doc.createElement("adjust");
	        adjust.setAttribute("total", this.totalMass.floatValue()+"");
	        
	        massElement.appendChild(adjust);
	        
	        element.appendChild(massElement);
	    }
	    
	    return element;
	    
	}

	public float getTotalMass() {
		return totalMass.floatValue();
	}

	public void setTotalMass(float totalMass) {		
		this.totalMass = new Float(totalMass);
	}
	
}
