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
import java.util.ListIterator;


import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.apache.log4j.Logger;

//import org.apache.xerces.utils.regex.ParseException;
import org.odejava.*;
import org.odejava.ode.Ode;



/**
 * XODE representation of a Joint
 * 
 * @author Matteo Migliavacca
 * @author William Denniss
 */
public class XODEJoint extends XODEObject implements Serializable {

	protected String link1;
	protected String link2;
	protected Vector3f anchor;
	protected List axes = new LinkedList();
	protected String jointType;
	
	public static Logger log = Logger.getLogger("odejava.xode.joint");
	
	protected XODEJoint() {
	}
	
	public XODEJoint(String name, Vector3f anchor, String link1, String link2, String jointType, List axes) {
		super(name);
		this.anchor = anchor;
		this.link1 = link1;
		this.link2 = link2;
		this.jointType = jointType;
		
		
	}
	
	public XODEJoint(Node xodeData) {
		super(xodeData);
		
		log.info("Parsing Joint Node");
		
		NodeList children = xodeData.getChildNodes();
		
		
		for (int i = 0; i < children.getLength(); i++) {
		
			Node currentChild = children.item(i);
		
			if (currentChild.getNodeName().equals("link1")) {
				
				log.debug("parsing link1 element");
				link1 = DOMUtil.attributeString(currentChild,"body");
			}

			if (currentChild.getNodeName().equals("link2")) {
				
				log.debug("parsing link2 element");	
				link2 = DOMUtil.attributeString(currentChild,"body");
				
			}
			
			
			
			/**
			 * Parses varous joint types
			 */
			if (currentChild.getNodeName().equals("ball")) {
				
				XODEParserDOM.odeLog.debug("new JointBall");
				this.jointType = currentChild.getNodeName();

				anchor = parseAnchor(currentChild);
				
				XODEParserDOM.odeLog.debug("JointBall.setAnchor " + anchor);
			}

			if (currentChild.getNodeName().equals("fixed")) {
				
				XODEParserDOM.odeLog.debug("new JointFixed");
				this.jointType = currentChild.getNodeName();
				
			}
			
			if (currentChild.getNodeName().equals("hinge")) {
				
				log.info("Now parsing: Hinge");
				
				XODEParserDOM.odeLog.debug("new JointHinge");
				this.jointType = currentChild.getNodeName();

				
				anchor = parseAnchor(currentChild);
				XODEParserDOM.odeLog.debug("JointHinge.setAnchor " + anchor);
				
				log.debug("parsing axes");
				parseXODEAxis(currentChild);
			
			}

			if (currentChild.getNodeName().equals("slider")) {
				
				log.info("Now parsing: Slider");
				
				XODEParserDOM.odeLog.debug("new JointSlider");
				this.jointType = currentChild.getNodeName();
				
				log.debug("parsing axes");
				parseXODEAxis(currentChild);
			
			}

			if (currentChild.getNodeName().equals("hinge2")) {
				log.info("Now parsing: Hinge2");
				
				XODEParserDOM.odeLog.debug("new JointHinge2");
				this.jointType = currentChild.getNodeName();
				
				
		
				
				anchor = parseAnchor(currentChild);
				
				log.debug("parsing axes");
				parseXODEAxis(currentChild);				
				
			}

		
		}
		
	}

	public void parseXODEAxis(Node xodeData) {
		int numAxis = 1;
		
		NodeList children = xodeData.getChildNodes(); 
		
		for (int i=0; i < children.getLength(); i++) {
			
			Node currentChild = children.item(i);
			
			if (currentChild.getNodeName().equals("axis")) {
				
				this.axes.add(new XODEJointAxis(currentChild));
			}
		}
		
	}
	
	public static Vector3f parseAnchor(Node jointNode) {
		
		Vector3f anchor = new Vector3f(0,0,0);
		
		for (int i = 0; i < jointNode.getChildNodes().getLength(); i++) {
			
			Node currentChild = jointNode.getChildNodes().item(i);
		
			if (currentChild.getNodeName().equals("anchor")) {
		
				anchor.x = DOMUtil.attributeFloat(currentChild,"x");
				anchor.y = DOMUtil.attributeFloat(currentChild,"y");
				anchor.z = DOMUtil.attributeFloat(currentChild,"z");				
				break;
			}
		}
		
		return anchor;
	}
	
	public Vector3f getAnchor() {
		
		
				
				Matrix4f transformMatrix = new Matrix4f();
				transformMatrix.setIdentity();
				transformMatrix.setTranslation(new Vector3f(anchor.x,anchor.y,anchor.z));
				
				Matrix4f transform = this.getParent().getTransform();
				transform.mul(transformMatrix);
				
				XODEParserDOM.log.debug("&&&&&&&&&&&&&&&&&&&&&& Anchor as parsed: " + new Vector3f(anchor.x,anchor.y,anchor.z));
				
				XODEParserDOM.log.debug("parent transform: " + anchor);

				Vector3f anchor = new Vector3f();
				transform.get(anchor);
				
				XODEParserDOM.log.debug("resultant transform: " + anchor);
				
	
		
		return anchor;
	}

	/* (non-Javadoc)
	 * @see org.odejava.xode.XODEObject#buildOde(org.odejava.World, org.odejava.Space, java.lang.String, java.lang.String)
	 */
	public void buildOde(World world, Space space, String namePrefix, List odeList) {
		
//		 Recursivly searches the parent to find the nearest body ancestor
		Body parentBody = this.getParent().getFirstBodyAncestor();
		
		//	Recursivly searches the parent to find the nearest jointgroup ancestor
		JointGroup parentJointGroup = this.getParent().getFirstJointGroupAncestor();
		if (parentJointGroup == null) {
			parentJointGroup = new JointGroup();
		}
		
		Body link1 = null;
		Body link2 = null;
		
		
		{
			
			log.debug("parsing link1 element");
			
			String id = namePrefix + this.link1;
			
			//TODO: now i query OdeJava to find Bodies should we keep an internal list instead?
			//Probably we should - because in Odejava names are not guarenteed to be unique - but it is a requirement
			//of XODE.  There's also the problem of the body being defined after the joint in the file.  This would require
			// a second parse.  Perhaps this should NOT be allowed in the spec - I'll post it to the mailing list to see
			// if this would disadvantage anyone
			
			Iterator iter = world.getBodies().iterator();
			while (iter.hasNext()) {
				Body b = (Body) iter.next();
				if (b.getName() != null) {
					if (b.getName().equals(id)) {
						link1 = b;
						
						log.info("referenced Body found id:" + id);
					}
				}
			}
			
			if (link1 == null) {
				
				log.error("link1 referenced body could NOT be found");
			}
				
		}

		if (link1==null) { 
			link1 = parentBody;//throw new XODEException("link1 body is null - no parent body or no reference found.");
		}

		{
			
			log.debug("parsing link2 element");
							
			String id = namePrefix + this.link2;
			
			Iterator iter = world.getBodies().iterator();
			while (iter.hasNext()) {
				Body b = (Body) iter.next();
				if (b.getName() != null) {
					if (b.getName().equals(id)) {
						link2 = b;
						
						log.debug("referenced Body found id:" + id);
					}
				}
			}
			
			if (link2 == null) {
				log.error("link2 referenced body could NOT be found");
			}	
			
		}
		if (link2 == null) {
			link2 = parentBody;
			//log.error("link2 referenced body could NOT be found");
		}
		if (link1 == link2) {
			throw new XODEException("Not Happy Jan, link1 and link2 are equal!");
			
		}
		log.warn("link1: " + link1.getName() + " link2: " + link2.getName());
		
		/**
		 * Parses varous joint types
		 */
		if (jointType.equals("ball")) {
			
			XODEParserDOM.odeLog.debug("new JointBall");
			JointBall joint = new JointBall(world, parentJointGroup);
			
			XODEParserDOM.odeLog.debug("JointBall.attach");
			joint.attach(link1,link2);
			
			Vector3f anchor = getAnchor();
			XODEParserDOM.odeLog.debug("JointBall.setAnchor " + anchor);
			joint.setAnchor(anchor.x, anchor.y, anchor.z);
			
			this.odeObject = joint;
		}

		if (jointType.equals("fixed")) {
			
			XODEParserDOM.odeLog.debug("new JointFixed");
			JointFixed joint = new JointFixed(world,new JointGroup());
			
			XODEParserDOM.odeLog.debug("JointFixed.attach");
			joint.attach(link1,link2);
			
			XODEParserDOM.odeLog.debug("JointFixed.setFixed");
			joint.setFixed();
			
			this.odeObject = joint;
		
		}
		
		if (jointType.equals("hinge")) {
			
			log.info("Now parsing: Hinge");
			
			XODEParserDOM.odeLog.debug("new JointHinge");
			JointHinge joint = new JointHinge(world,new JointGroup());
			
			XODEParserDOM.odeLog.debug("JointHinge.attach");
			joint.attach(link1,link2);
			
			Vector3f anchor = getAnchor();
			XODEParserDOM.odeLog.debug("JointHinge.setAnchor " + anchor);
			joint.setAnchor(anchor.x, anchor.y, anchor.z);
			
			log.debug("parsing axes");
			addAxes(joint);
		
			log.debug("Param Test - Lowstop value is " + joint.getParam(Ode.dParamLoStop));
			log.debug("Param Test - Highstop value is " + joint.getParam(Ode.dParamHiStop));
			log.debug("Param Test - Axis value is "+joint.getAxis());
			
			log.info("Hinge Parsing Complete");
			
			this.odeObject = joint;
		}

		if (jointType.equals("slider")) {
			
			log.info("Now parsing: Slider");
			
			XODEParserDOM.odeLog.debug("new JointSlider");
			JointSlider joint = new JointSlider(world,new JointGroup());
			
			XODEParserDOM.odeLog.debug("JointSlider.attach");
			joint.attach(link1,link2);

			log.debug("parsing axes");
			addAxes(joint);
		
			log.info("Hinge2 Parsing Complete");
			
			this.odeObject = joint;
		}

		if (jointType.equals("hinge2")) {
			log.info("Now parsing: Hinge2");
			
			
			XODEParserDOM.odeLog.debug("new JointHinge2");
			JointHinge2 joint = new JointHinge2(world,new JointGroup());
			
			
			if (link2 != null) {
				XODEParserDOM.odeLog.debug("JointHinge2.attach");
				joint.attach(link1,link2);
			}
			
					
			Vector3f anchor = getAnchor();

			XODEParserDOM.odeLog.debug("JointHinge2.setAnchor " + anchor);
			joint.setAnchor(anchor.x, anchor.y, anchor.z);
			
			log.debug("parsing axes");
			addAxes(joint);
			log.debug("Param Test - Lowstop value is " + joint.getParam(Ode.dParamLoStop));
			log.debug("Param Test - Highstop value is " + joint.getParam(Ode.dParamHiStop));
			log.debug("Param Test - Axis value is "+joint.getAxis1());
			log.debug("Param Test - Anchor value is "+joint.getAnchor());
			
			
			log.info("Hinge2 Parsing Complete");
			
			this.odeObject = joint;
		}

		if (this.odeObject != null && this.getName() != null) {
			((Joint) this.odeObject).setName(namePrefix + this.getName());
		}
		
		odeList.add(this.odeObject);
		
	}
	
	public void addAxes(Joint joint) {
		int count = 1;
		for (final ListIterator i = this.axes.listIterator(); i.hasNext(); count++) {
			((XODEJointAxis) i.next()).addAxis(joint, count);
		}
		
	}

	
	public Element buildElement(Document doc) {
	    Element element = doc.createElement("joint");
	    
	    // attributes
	    element.setAttribute("name", this.getName());

	    // link 1
	    if (this.link1 != null) {
	        Element link1element = doc.createElement("link1");
	        link1element.setAttribute("body", this.link1);
	        
	        element.appendChild(link1element);
	    }
	    
	    // link 2
	    if (this.link2 != null) {
	        Element link2element = doc.createElement("link2");
	        link2element.setAttribute("body", this.link2);
	        
	        element.appendChild(link2element);
	    }
	    
	    
	    // joint type
	    Element jointTypeElement = doc.createElement(jointType);
	    {
		    if (anchor != null) {
			    Element anchorElement = doc.createElement("anchor");
			    anchorElement.setAttribute("x", anchor.x +"");
			    anchorElement.setAttribute("y", anchor.y +"");
			    anchorElement.setAttribute("z", anchor.z +"");
			    
			    jointTypeElement.appendChild(anchorElement);
		    }
		    
		    for (Iterator i = axes.iterator(); i.hasNext(); ) {
		        jointTypeElement.appendChild(((XODEJointAxis) i.next()).buildElement(doc));
		    }
	    }
        element.appendChild(jointTypeElement);
		    
	    return element;
	    
	}
	
}
