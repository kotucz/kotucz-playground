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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.odejava.Joint;
import org.odejava.ode.Ode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Parses, stores and creates ODE axes.
 * 
 * @author Matteo Migliavacca
 * @author William Denniss
 */
public class XODEJointAxis implements Serializable {

    private static final Set LEGAL_ATTRIBUTES = new HashSet(Arrays.asList(new String [] {"LowStop", "HiStop", "Vel", "FMax", "FudgeFactor", "Bounce", "CFM", "StopERP", "StopCFM", "SuspensionERP", "SuspensionCFM"}));	
	
	private float x, y, z;

	private Map attributes = new HashMap();

	
	protected XODEJointAxis() {
	}
	
	public XODEJointAxis(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public XODEJointAxis(float x, float y, float z, Float LowStop, Float HiStop, Float Vel, Float FMax, Float FudgeFactor, Float Bounce, Float CFM, Float StopERP, Float StopCFM, Float SuspensionERP, Float SuspensionCFM) {
		this(x,y,z);
		
		attributes.put("LowStop", LowStop);
		attributes.put("HiStop", HiStop);
		attributes.put("Vel", Vel);
		attributes.put("FMax", FMax);
		attributes.put("FudgeFactor", FudgeFactor);
		attributes.put("Bounce", Bounce);
		attributes.put("CFM",  CFM);
		attributes.put("StopERP", StopERP);
		attributes.put("StopCFM",  StopCFM);
		attributes.put("SuspensionERP", SuspensionERP);
		attributes.put("SuspensionCFM", SuspensionCFM);

	}


	/**
	 * Parse DOM xml data
	 * @param axisNode
	 */
	public XODEJointAxis(Node axisNode) {
		
		XODETransform transform = null;
		
		// parse angles
		x = DOMUtil.attributeFloat(axisNode,"x");
		y = DOMUtil.attributeFloat(axisNode,"y");
		z = DOMUtil.attributeFloat(axisNode,"z");
		
		Node currentChild =axisNode;
		for (int j = 0; j < currentChild.getAttributes().getLength(); j++) {

			String currentAttribute =currentChild.getAttributes().item(j).getNodeName(); 
			
			Float current = null;
			try {
			    current = new Float(DOMUtil.attributeFloat(currentChild, currentAttribute));
			
			} catch (Exception e) {
			    
			    // checks if the attribute name was legal, and if so complains.
			    if (LEGAL_ATTRIBUTES.contains(currentAttribute)) {
			        throw new RuntimeException("Value for attribute " + currentAttribute + "not a float");
			    }
			    
			    continue;
			}
			
			if (LEGAL_ATTRIBUTES.contains(currentAttribute)) {
			    
				// Adds attribute to attribute hash map
				attributes.put(currentAttribute, current);

			} else {
			    XODEJoint.log.debug("Attribute " + currentAttribute + " is not a legal attribute!");
			}
			
		}
		
		
	
	}
	
	public Element buildElement(Document doc) {
	    Element axisElement = doc.createElement("axis");
	    
	    axisElement.setAttribute("x", this.x +"");
	    axisElement.setAttribute("y", this.y +"");
	    axisElement.setAttribute("z", this.z +"");
	    
	    for (Iterator i = attributes.keySet().iterator(); i.hasNext(); ) {
	        String key = (String) i.next();
	        Float value = (Float) attributes.get(key);
	        if (value != null) {
	            axisElement.setAttribute(key, value.floatValue() +"");
	        }
	    }
	    
	    return axisElement;
	}
	
	/**
	 * Adds this axes data to a joint at the given position
	 * 
	 * @param joint Joint to which axis will be added
	 * @param numAxis Axis position for this axes
	 */
	public void addAxis(Joint joint, int numAxis) {
		
		
		// log the call
		XODEParserDOM.odeLog.debug("setAxis" + numAxis + "(" +
			new Float(x) + ", " + 
			new Float(y) + ", " + 
			new Float(z) + ")");

		if (numAxis == 1) {
		    joint.setAxis1(x, y, z);
		} else if (numAxis == 2) {
		    joint.setAxis2(x, y, z);			    
		} else {
		    assert false : "Axis is not 1 or 2";
		}
			
		
		for (Iterator i = attributes.keySet().iterator(); i.hasNext(); ) {
		    String currentAttribute = (String) i.next();
		    int parameter = axisParamConverter.convert(currentAttribute,numAxis);
		    
		    joint.setParam(parameter, ((Float) attributes.get(currentAttribute)).floatValue());
		}
		

	}
	
	/**
	 * @return Returns the bounce.
	 */
	public Float getBounce() {
		return (Float) attributes.get("Bounce");
	}
	/**
	 * @param bounce The bounce to set.
	 */
	public void setBounce(Float bounce) {
	    attributes.put("Bounce", bounce);
	}
	/**
	 * @return Returns the cFM.
	 */
	public Float getCFM() {
		return (Float) attributes.get("CFM");
	}
	/**
	 * @param cfm The cFM to set.
	 */
	public void setCFM(Float cfm) {
	    attributes.put("CFM", cfm);
	}
	/**
	 * @return Returns the fMax.
	 */
	public Float getFMax() {
		return (Float) attributes.get("FMax");
	}
	/**
	 * @param max The fMax to set.
	 */
	public void setFMax(Float max) {
	    attributes.put("FMax", max);
	}
	/**
	 * @return Returns the fudgeFactor.
	 */
	public Float getFudgeFactor() {
		return (Float) attributes.get("FudgeFactor");
	}
	/**
	 * @param fudgeFactor The fudgeFactor to set.
	 */
	public void setFudgeFactor(Float fudgeFactor) {
	    attributes.put("FudgeFactor", fudgeFactor);
	}
	/**
	 * @return Returns the hiStop.
	 */
	public Float getHiStop() {
		return (Float) attributes.get("HiStop");
	}
	/**
	 * @param hiStop The hiStop to set.
	 */
	public void setHiStop(Float hiStop) {
	    attributes.put("HiStop", hiStop);
	}
	/**
	 * @return Returns the lowStop.
	 */
	public Float getLowStop() {
		return (Float) attributes.get("LowStop");
	}
	/**
	 * @param lowStop The lowStop to set.
	 */
	public void setLowStop(Float lowStop) {
	    attributes.put("LowStop", lowStop);
	}
	/**
	 * @return Returns the stopCFM.
	 */
	public Float getStopCFM() {
		return (Float) attributes.get("StopCFM");
	}
	/**
	 * @param stopCFM The stopCFM to set.
	 */
	public void setStopCFM(Float stopCFM) {
	    attributes.put("StopCFM", stopCFM);
	}
	/**
	 * @return Returns the stopERP.
	 */
	public Float getStopERP() {
		return (Float) attributes.get("StopERP");
	}
	/**
	 * @param stopERP The stopERP to set.
	 */
	public void setStopERP(Float stopERP) {
	    attributes.put("StopERP", stopERP);
	}
	/**
	 * @return Returns the suspensionCFM.
	 */
	public Float getSuspensionCFM() {
		return (Float) attributes.get("SuspensionCFM");
	}
	/**
	 * @param suspensionCFM The suspensionCFM to set.
	 */
	public void setSuspensionCFM(Float suspensionCFM) {
	    attributes.put("SuspensionCFM", suspensionCFM);
	}
	/**
	 * @return Returns the suspensionERP.
	 */
	public Float getSuspensionERP() {
		return (Float) attributes.get("SuspensionERP");
	}
	/**
	 * @param suspensionERP The suspensionERP to set.
	 */
	public void setSuspensionERP(Float suspensionERP) {
	    attributes.put("SuspensionERP", suspensionERP);
	}
	/**
	 * @return Returns the vel.
	 */
	public Float getVel() {
		return (Float) attributes.get("Vel");
	}
	/**
	 * @param vel The vel to set.
	 */
	public void setVel(Float vel) {
	    attributes.put("Vel", vel);
	}
	/**
	 * @return Returns the x.
	 */
	public float getX() {
		return x;
	}
	/**
	 * @param x The x to set.
	 */
	public void setX(float x) {
		this.x = x;
	}
	/**
	 * @return Returns the y.
	 */
	public float getY() {
		return y;
	}
	/**
	 * @param y The y to set.
	 */
	public void setY(float y) {
		this.y = y;
	}
	/**
	 * @return Returns the z.
	 */
	public float getZ() {
		return z;
	}
	/**
	 * @param z The z to set.
	 */
	public void setZ(float z) {
		this.z = z;
	}
	
	
	/**
	*	This class solves the name mismatch between XODE axis name parameters and Ode names and
	*	find the right constant for the right axis  
	*/
	static private class axisParamConverter {
		static String[][] conversionTable = { 
		{"LowStop",			"dParamLoStop"},
		{"HiStop",			"dParamHiStop"},
		{"Vel",				"dParamVel"},
		{"FMax",			"dParamFMax"},
		{"FudgeFactor",		"dParamFudgeFactor"},
		{"Bounce",			"dParamBounce"},
		{"CFM",				"dParamCFM"},
		{"StopERP",			"dParamStopERP"},
		{"StopCFM",			"dParamStopCFM"},
		{"SuspensionERP",	"dParamSuspensionERP"},
		{"SuspensionCFM",	"dParamSuspensionCFM"},
		};
		
		static String convertName(String param) {
			String odeName = null;
			
			for (int i=0;i<conversionTable.length;i++) {
				if (conversionTable[i][0].equals(param))
					odeName = conversionTable[i][1];
			}

			return odeName;
		}
		
		static int convert(String param,int axis) {
			//convert the name
			System.out.println("param: " + param + " : " + axis);
		    String odeName = convertName(param);
			System.out.println(odeName);

			//find the right constant for the right axis
			try {
				return (axis == 1) ? Ode.class.getField(odeName).getInt(null) : Ode.class.getField(odeName+axis).getInt(null) ;
			} catch (Exception e) {
				XODEJoint.log.error("Param: " + param + " does not appear to be a valid ODE axis property");
				throw new XODEException("Param: " + param + " does not appear to be a valid ODE axis property");
			}
		}
	}		
}
