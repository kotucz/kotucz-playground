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

import com.xith3d.scenegraph.Geometry;
import com.xith3d.scenegraph.IndexedTriangleArray;
import com.xith3d.scenegraph.TriangleArray;

/**
 * Code initially taken from Xith3d.
 * 
 * Created 21.12.2003 (dd.mm.yyyy)
 * 
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 * @see http://odejava.dev.java.net
 *  
 */
public class SphereGeometry {
	public static Geometry createSphere(float R, int divisions) {
		// This sphere generation is not pretending to be optimal and
		// implemented only for testing purposes
		float sign = 1.0f;
		float[] vertexes = new float[(divisions + 1) * (2 * divisions + 1) * 3];
		float[] normals = new float[(divisions + 1) * (2 * divisions + 1) * 3];
		for (int i = 0; i <= divisions; i++) {
			float rho = (float) ((double) i * Math.PI / (double) divisions);
			for (int j = 0; j <= 2 * divisions; j++) {
				float theta =
					(float) ((double) j * Math.PI / (double) divisions);
				float vx = (float) (Math.cos(theta) * Math.sin(rho));
				float vy = (float) (sign * Math.cos(rho));
				float vz = (float) (Math.sin(theta) * Math.sin(rho));
				vertexes[((i * (2 * divisions + 1)) + j) * 3 + 0] = vx * R;
				vertexes[((i * (2 * divisions + 1)) + j) * 3 + 1] = vy * R;
				vertexes[((i * (2 * divisions + 1)) + j) * 3 + 2] = vz * R;
				normals[((i * (2 * divisions + 1)) + j) * 3 + 0] = vx * sign;
				normals[((i * (2 * divisions + 1)) + j) * 3 + 1] = vy * sign;
				normals[((i * (2 * divisions + 1)) + j) * 3 + 2] = vz * sign;
			}
		}
		int[] vIdx = new int[divisions * 2 * divisions * 6];
		int n = 0;
		for (int i = 0; i < divisions; i++) {
			for (int j = 0; j < 2 * divisions; j++) {
				int v00 = i * (2 * divisions + 1) + j;
				int v01 = i * (2 * divisions + 1) + j + 1;
				int v10 = (i + 1) * (2 * divisions + 1) + j;
				int v11 = (i + 1) * (2 * divisions + 1) + j + 1;
				vIdx[n++] = v11;
				vIdx[n++] = v10;
				vIdx[n++] = v00;
				vIdx[n++] = v11;
				vIdx[n++] = v00;
				vIdx[n++] = v01;
			}
		}
		IndexedTriangleArray shapeGeom =
			new IndexedTriangleArray(
				vertexes.length / 3,
				TriangleArray.COORDINATES | TriangleArray.NORMALS,
				vIdx.length);
		shapeGeom.setValidVertexCount(vertexes.length / 3);
		shapeGeom.setCoordinates(0, vertexes);
		shapeGeom.setValidIndexCount(vIdx.length);
		shapeGeom.setIndex(vIdx);
		shapeGeom.setNormals(0, normals);
		return shapeGeom;
	}
}
