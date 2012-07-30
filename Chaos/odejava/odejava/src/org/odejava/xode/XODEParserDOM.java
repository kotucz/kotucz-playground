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

import java.util.List;

import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException; 
import org.xml.sax.SAXNotSupportedException;  
import java.io.IOException;

import javax.vecmath.Matrix4f;

import org.xml.sax.InputSource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import org.odejava.*;

import org.apache.log4j.Logger;

/**
 * XODE DOM Parser
 * 
 * @author William Denniss
 */
public class XODEParserDOM {
	
	public static Logger log = Logger.getLogger("odejava.xode");
	public static Logger odeLog = Logger.getLogger("odejava.xode.call");
	
	
	private List geoms;
	private List bodies;
	private List joints;
	private List jointGroups;
	private List groups;
	
	private DOMParser parser;
        private Document doc;
	
	/**
	 *
	 * @param validate causes the XML to be validated if set.
	 */
	public XODEParserDOM(boolean validate) {
		
		parser = new DOMParser();
		
		if (validate) {
			
			try {  
				parser.setFeature("http://xml.org/sax/features/validation",true); 
				parser.setFeature("http://xml.org/sax/features/validation", true); 
				parser.setFeature("http://apache.org/xml/features/validation/schema",true); 
			} catch (SAXNotRecognizedException e) { 
				log.error("Unrecognized feature: http://xml.org/sax/features/validation");
			} catch (SAXNotSupportedException e) {  
				log.error("Unrecognized feature: http://xml.org/sax/features/validation");  
			} 
		}
		
	}

	
	/**
	 * Parses the XODE file, builds the ODE objects and returns the XODE root.
	 * 
	 * @param world
	 * @param space
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	public XODERoot parse(World world, Space space, InputSource file) throws IOException, SAXException {
		Matrix4f identity = new Matrix4f();
		identity.setIdentity();
		return this.parse(world, space, file, identity);
	}
	
	/**
	 * Parses the XODE file so it can be later added to the scene
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * 
	 * @see XODERoot.buildODEScene
	 * @see XODERoot.setRootTransform
	 */
	public XODERoot parse(InputSource file) throws IOException, SAXException {
		Matrix4f identity = new Matrix4f();
		identity.setIdentity();
		return this.parse(file, identity);
	}
	/**
	 * @param world the world which the bodies will be added.
	 * @param space the space to which the GeomS will be added.  If null,
	 *              then they are not added to any Space.
	 * @param file the XODE file to parse.
	 * 
	 */
	public XODERoot parse(World world, Space space, InputSource file, Matrix4f parentTransform) throws IOException, SAXException {
		XODERoot root = parse(file, parentTransform);
		root.buildODEScene(world, space, "");
		
		return root;
	}
	
	/**
	 * Parses the XODE file so it can be later added to the scene
	 * @param file
	 * @param parentTransform
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	public XODERoot parse(InputSource file, Matrix4f parentTransform) throws IOException, SAXException {
		
		// Parses the XML
		parser.parse(file);
		doc = parser.getDocument();
		
		XODERoot root = new XODERoot(doc.getFirstChild(), parentTransform);
		
		// Garbage colelct the no longer needed XML data
		System.gc();
		
		return root;
	}


	
	public static void display (Node start) {
		
		if (start.getNodeType() == Node.ELEMENT_NODE) {
			
			System.out.print("<"+start.getNodeName());       
			NamedNodeMap startAttr = start.getAttributes();   
			
			for (int i = 0; i < startAttr.getLength(); i++) {   
				Node attr = startAttr.item(i);   
				System.out.print(" "+attr.getNodeName()+   
				"=\""+attr.getNodeValue()+"\"");   
			}       
			System.out.print(">");
			
		} else if (start.getNodeType() == Node.TEXT_NODE)  {
			System.out.print(start.getNodeValue());       
		}   
		
		for (Node child = start.getFirstChild(); child != null; child = child.getNextSibling()) {   
			display(child);   
		}
			
		if (start.getNodeType() == Node.ELEMENT_NODE) {       
			System.out.print("</"+start.getNodeName()+">");   
		}
	}
	
	public XODERoot parse(World world, Space space, String file, Matrix4f parentTransform)  throws IOException, SAXException {
		return parse(world, space, new InputSource(file), parentTransform);
	}
	public XODERoot parse(World world, Space space, String file)  throws IOException, SAXException {
		return parse(world, space, new InputSource(file));
	}
	
	
	
	public List getGeoms() {
		return geoms;
	}
	
	public List getBodies() {
		return bodies;
	}
	
	public List getJoints() {
		return joints;
	}
	
	public List getJointGroups() {
		return jointGroups;
	}

	public Document getDocument() {
		return doc;
	}
	
	public static final void main (String [] args) throws Exception {
		
		new XODEParserDOM(true).parse(null, null, "data/box.xml");
	}
}
