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
package org.odejava;

import javax.vecmath.Point3f;

/**
 * 
 * Terrain is an infinite ground based on a height map. It is an contrib
 * module. Contributed by Benoit Chaperot, www.jstarlab.com
 * This module is currently disabled.
 * 
 * Drawing related routines made by William Denniss, these routines are under
 * development.
 * 
 * Created 16.12.2003 (dd.mm.yyyy)
 * 
 * @author Jani Laakso E-mail: jani.laakso@itmill.com, William Denniss
 * @see http://odejava.dev.java.net
 *  
 */
public class GeomTerrain extends Geom {
	private float[] pHeights;
	private float vLength;
	private int numNodesPerSide;

	/**
	 * Create terrain geometry to specific space. Terrain is unmovable.
	 * This module is currently disabled.
	 * @param name
	 * @param spaceId
	 * @param pHeights
	 * @param vLength
	 * @param numNodesPerSide
	 */
	public GeomTerrain(String name, float[] pHeights, float vLength, int numNodesPerSide) {
		super(name);
		this.pHeights = pHeights;
		this.vLength = vLength;
		this.numNodesPerSide = numNodesPerSide;

//		spaceId = Space.SPACEID_ZERO;;
//		geomId =
//			Ode.dCreateTerrain(
//				spaceId,
//				Odejava.createSwigArray(pHeights),
//				vLength,
//				numNodesPerSide);
//		updateReferences();
//		updateNativeAddr();

	}


	public GeomTerrain(float[] pHeights, float vLength, int numNodesPerSide) {
		this(
			null,
			pHeights,
			vLength,
			numNodesPerSide);
	}

	/**
	 * @return Returns the numNodesPerSide.
	 */
	public int getNumNodesPerSide() {
		return numNodesPerSide;
	}

	/**
	 * @return Returns the pHeights.
	 */
	public float[] getPHeights() {
		return pHeights;
	}

	/**
	 * @return Returns the vLength.
	 */
	public float getVLength() {
		return vLength;
	}

	public Point3f[] getGeometry() {
		return getGeometry(0, 0, 1);
	}

	public Point3f[] getGeometry(float x, float y, float vLength) {
		return dsDrawTerrain(
			x,
			y,
			vLength,
			getVLength(),
			getNumNodesPerSide(),
			getPHeights());
	}

	/**
	 * Returns a coordinate array representing this terrain object. This can
	 * then be used to generate geometry for example using Xith3D. The geometry
	 * is not indexed.
	 * 
	 * @param x
	 *            Amount which the terrain will be offset along the x axis
	 * @param y
	 *            Amount which the terrain will be offset along the y axis
	 * @param vLength
	 *            Offset multiplier (the x and y offset is multiplied by this
	 *            amount), if in doubt set to 1
	 * @param vNodeLength
	 *            distance between each vertex
	 * @param nNumNodesPerSide
	 *            number of vertexes per side
	 * @param pHeights
	 *            heightmap array. The length of this array must be equal to
	 *            nNumNodesPerSide squared
	 * @return a coordinate array representing this terrain object
	 */
	public static Point3f[] dsDrawTerrain(
		float x,
		float y,
		float vLength,
		float vNodeLength,
		int nNumNodesPerSide,
		float[] pHeights) {

		if (pHeights.length != nNumNodesPerSide * nNumNodesPerSide) {
			throw new IllegalArgumentException("Length of the heightmap array must be the square of the number of nodes per side!");
		}

		Point3f A = null;
		Point3f B = null;
		Point3f C = null;
		Point3f D = null;

		// x,y offset
		float vx, vy;
		vx = vLength * x;
		vy = vLength * y;

		Point3f[] coords = new Point3f[nNumNodesPerSide * nNumNodesPerSide * 6];
		int count = 0;

		for (int i = 0; i < nNumNodesPerSide; i++) {
			for (int j = 0; j < nNumNodesPerSide; j++) {

				// Calculates the verticies
				A =
					new Point3f(
						i * vNodeLength + vx,
						j * vNodeLength + vy,
						GetHeight(i, j, nNumNodesPerSide, pHeights));
				B =
					new Point3f(
						(i + 1) * vNodeLength + vx,
						j * vNodeLength + vy,
						GetHeight(i + 1, j, nNumNodesPerSide, pHeights));
				C =
					new Point3f(
						i * vNodeLength + vx,
						(j + 1) * vNodeLength + vy,
						GetHeight(i, j + 1, nNumNodesPerSide, pHeights));
				D =
					new Point3f(
						(i + 1) * vNodeLength + vx,
						(j + 1) * vNodeLength + vy,
						GetHeight(i + 1, j + 1, nNumNodesPerSide, pHeights));

				// "Draws" the triangles
				//dsDrawTriangle(pos,R, C,A,B ,1);
				//dsDrawTriangle(pos,R, D,C,B ,1);
				coords[count++] = C;
				coords[count++] = A;
				coords[count++] = B;

				coords[count++] = D;
				coords[count++] = C;
				coords[count++] = B;

			}

		}

		return coords;
	}

	/**
	 * Extracts the z coordinate from the heightmap at the given (x,y)
	 * coordinate
	 *  
	 */
	private static float GetHeight(
		int x,
		int y,
		int nNumNodesPerSide,
		float[] pHeights) {
		int nNumNodesPerSideMask = nNumNodesPerSide - 1;
		return pHeights[(((int) (y) & nNumNodesPerSideMask) * nNumNodesPerSide)
			+ ((int) (x) & nNumNodesPerSideMask)];
	}
	

}
