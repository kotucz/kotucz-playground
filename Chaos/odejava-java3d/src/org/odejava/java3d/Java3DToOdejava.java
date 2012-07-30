/*
 * Open Dynamics Engine for Java (odejava) Copyright (c) 2004, Odejava Project
 * Group, All rights reserved.
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
package org.odejava.java3d;

import javax.vecmath.Point3f;
import javax.media.j3d.IndexedTriangleArray;
import org.odejava.GeomTriMesh;

/**
 * Counterpart to OdejavaToXith3D, converts a Xith3D scene into Ode
 *
 * @author William Denniss
 * @author Java 3D port by Paul Byrne
 */
public class Java3DToOdejava {
	
	/**
	 * Converts an IndexedTriangleArray into a GeomTriMesh
	 *
	 */
	public static GeomTriMesh createTriMesh (IndexedTriangleArray ita) {
		
		// Gets the indices
		int [] indices = new int [ita.getIndexCount()];
		ita.getCoordinateIndices(0, indices);
		
		// Gets the vertices
		Point3f [] coords = new Point3f [ita.getVertexCount()];
		ita.getCoordinates(0, coords);
		
		// Converts Point3fs to floats
		float [] vertices = new float [coords.length * 3];
		
		int vertex = 0;
		for (int i = 0; i < coords.length; i++) {
			vertices[vertex++] = coords[i].x;
			vertices[vertex++] = coords[i].y;
			vertices[vertex++] = coords[i].z;
		}
		
		// Returns built TriMesh
		return new GeomTriMesh ("Java3DToOdeJava", vertices, indices);
		
	}
	
	
}
