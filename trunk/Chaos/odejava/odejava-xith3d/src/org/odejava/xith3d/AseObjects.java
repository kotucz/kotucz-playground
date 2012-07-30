/*
 * Open Dynamics Engine for Java (odejava) Copyright (c) 2004, Jani Laakso, All
 * rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution. Neither the name of the odejava nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.odejava.xith3d;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.xith3d.loaders.ase.AseFile;
import org.xith3d.loaders.ase.AseGeom;
import org.xith3d.loaders.ase.AseReader;
import com.xith3d.scenegraph.Node;

/**
 * Helper class for getting objects by name from ASE file.
 * 
 * Created 23.12.2003 (dd.mm.yyyy)
 * 
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 * @see http://odejava.dev.java.net
 *  
 */
public class AseObjects {

	private AseFile aseFile;
	private String filename;

	public AseObjects(String filename) {
		this.filename = filename;
		loadObjects();
	}

	private void loadObjects() {
		try {
			aseFile = new AseFile();
			BufferedReader br = null;
			try {
				// Read file directly from the filesystem
				br = new BufferedReader(new FileReader(filename));
			} catch (IOException e) {
				// Read file from jar package
				try {
					URL url =
						this.getClass().getClassLoader().getResource(filename);
					br =
						new BufferedReader(
							new InputStreamReader(url.openStream()));
				} catch (Exception e2) {
					System.err.println("Could not read " + filename + ".");
					throw e2;
				}
			}
			AseReader r = new AseReader(br, aseFile);
			aseFile.parse(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get a node from a parsed ase file.
	 * 
	 * @param nodeName
	 * @return Object
	 */
	public Node getObject(String nodeName) {
		if (aseFile.objects.containsKey(nodeName)) {
			return (Node) ((AseGeom) aseFile.objects.get(nodeName)).getShape(
				aseFile);
		} else {
			System.err.println(
				"Node " + nodeName + " not found from file " + filename);
			return null;
		}
	}
}
