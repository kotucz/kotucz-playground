package org.odejava.java3d;

import javax.media.j3d.*;

/**
 * Code initially taken from Xith3d.
 * 
 * Created 26.12.2003 (dd.mm.yyyy)
 * 
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 * @author Java 3D port by Paul Byrne
 * @see http://odejava.dev.java.net
 *  
 */
public class CylinderGeometry {
	public static Geometry createCylinder(float R0, float R1, float H, int N) {
		float[] vertexes = new float[N * 2 * 3];
		float[] normals = new float[N * 2 * 3];
		int[] indicies = new int[N * 2 * 3];
		for (int i = 0; i < N; i++) {
			float a = ((float) i * 360f / (float) N) * (float) Math.PI / 180f;
			vertexes[i * 2 * 3 + 0] = -H / 2f;
			vertexes[i * 2 * 3 + 1] = (float) Math.cos(a) * R0;
			vertexes[i * 2 * 3 + 2] = (float) Math.sin(a) * R0;
			vertexes[i * 2 * 3 + 3] = H / 2f;
			vertexes[i * 2 * 3 + 4] = (float) Math.cos(a) * R1;
			vertexes[i * 2 * 3 + 5] = (float) Math.sin(a) * R1;
			normals[i * 2 * 3 + 0] = 0f;
			normals[i * 2 * 3 + 1] = (float) Math.cos(a);
			normals[i * 2 * 3 + 2] = (float) Math.sin(a);
			normals[i * 2 * 3 + 3] = 0f;
			normals[i * 2 * 3 + 4] = (float) Math.cos(a);
			normals[i * 2 * 3 + 5] = (float) Math.sin(a);
			indicies[i * 2 * 3 + 0] = (i * 2 + 3) % (N * 2);
			indicies[i * 2 * 3 + 1] = (i * 2 + 1) % (N * 2);
			indicies[i * 2 * 3 + 2] = (i * 2 + 2) % (N * 2);
			indicies[i * 2 * 3 + 3] = (i * 2 + 1) % (N * 2);
			indicies[i * 2 * 3 + 4] = (i * 2 + 0) % (N * 2);
			indicies[i * 2 * 3 + 5] = (i * 2 + 2) % (N * 2);
		}
		IndexedTriangleArray rect =
			new IndexedTriangleArray(
				N * 2,
				TriangleArray.COORDINATES | TriangleArray.NORMALS,
				N * 2 * 3);
		rect.setCoordinates(0, vertexes);
		rect.setValidIndexCount(N * 2 * 3);
		rect.setCoordinateIndices(0, indicies);
		rect.setNormals(0, normals);
		return rect;
	}
}
