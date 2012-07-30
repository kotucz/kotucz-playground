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

import com.xith3d.scenegraph.*;
import javax.vecmath.*;

/**
 * Code initially taken from Xith3d. Modified it to create boxes, not cubes
 * (sizex = sizey = sizez).
 *  
 */
public class BoxGeometry {

	public static Geometry createBoxViaTriangles(
		float x,
		float y,
		float z,
		float sizeX,
		float sizeY,
		float sizeZ,
		boolean addColorData) {

		return createBoxViaTriangles(
			x,
			y,
			z,
			sizeX,
			sizeY,
			sizeZ,
			addColorData,
			false,
			true,
			1f);

	}

	public static Geometry createBoxViaTriangles(
		float x,
		float y,
		float z,
		float sizeX,
		float sizeY,
		float sizeZ,
		boolean addColorData,
		boolean addNormals) {

		return createBoxViaTriangles(
			x,
			y,
			z,
			sizeX,
			sizeY,
			sizeZ,
			addColorData,
			addNormals,
			true,
			1f);
	}

	public static Geometry createBoxViaTriangles(
		float x,
		float y,
		float z,
		float sizeX,
		float sizeY,
		float sizeZ,
		boolean addColorData,
		boolean addNormals,
		boolean addTextureCoords,
		float normalScale) {
		float halfX = sizeX / 2f;
		float halfY = sizeY / 2f;
		float halfZ = sizeZ / 2f;

		Point3f[] coords = new Point3f[] {

			// top
			new Point3f(-halfX + x, halfY + y, halfZ + z),
				new Point3f(halfX + x, halfY + y, halfZ + z),
				new Point3f(halfX + x, halfY + y, -halfZ + z),
				new Point3f(halfX + x, halfY + y, -halfZ + z),
				new Point3f(-halfX + x, halfY + y, -halfZ + z),
				new Point3f(-halfX + x, halfY + y, halfZ + z),

			// back
			new Point3f(halfX + x, -halfY + y, halfZ + z),
				new Point3f(halfX + x, halfY + y, halfZ + z),
				new Point3f(-halfX + x, halfY + y, halfZ + z),
				new Point3f(-halfX + x, halfY + y, halfZ + z),
				new Point3f(-halfX + x, -halfY + y, halfZ + z),
				new Point3f(halfX + x, -halfY + y, halfZ + z),

			// left
			new Point3f(halfX + x, halfY + y, -halfZ + z),
				new Point3f(halfX + x, halfY + y, halfZ + z),
				new Point3f(halfX + x, -halfY + y, halfZ + z),
				new Point3f(halfX + x, -halfY + y, halfZ + z),
				new Point3f(halfX + x, -halfY + y, -halfZ + z),
				new Point3f(halfX + x, halfY + y, -halfZ + z),

			// right
			new Point3f(-halfX + x, -halfY + y, halfZ + z),
				new Point3f(-halfX + x, halfY + y, halfZ + z),
				new Point3f(-halfX + x, halfY + y, -halfZ + z),
				new Point3f(-halfX + x, halfY + y, -halfZ + z),
				new Point3f(-halfX + x, -halfY + y, -halfZ + z),
				new Point3f(-halfX + x, -halfY + y, halfZ + z),

			// bottom
			new Point3f(halfX + x, -halfY + y, halfZ + z),
				new Point3f(-halfX + x, -halfY + y, halfZ + z),
				new Point3f(-halfX + x, -halfY + y, -halfZ + z),
				new Point3f(-halfX + x, -halfY + y, -halfZ + z),
				new Point3f(halfX + x, -halfY + y, -halfZ + z),
				new Point3f(halfX + x, -halfY + y, halfZ + z),
			//front

			new Point3f(halfX + x, halfY + y, -halfZ + z),
				new Point3f(halfX + x, -halfY + y, -halfZ + z),
				new Point3f(-halfX + x, -halfY + y, -halfZ + z),
				new Point3f(-halfX + x, -halfY + y, -halfZ + z),
				new Point3f(-halfX + x, halfY + y, -halfZ + z),
				new Point3f(halfX + x, halfY + y, -halfZ + z),
				};
		/*
		 * Vector3f up = new Vector3f(0,1,0); Vector3f down = new
		 * Vector3f(0,-1,0); Vector3f right = new Vector3f(-1,0,0); Vector3f
		 * left = new Vector3f(1,0,0); Vector3f front = new Vector3f(0,-1,0);
		 * Vector3f back = new Vector3f(1,0,0);
		 */
		Vector3f up = new Vector3f(0, normalScale, 0);
		Vector3f down = new Vector3f(0, -normalScale, 0);
		Vector3f right = new Vector3f(-normalScale, 0, 0);
		Vector3f left = new Vector3f(normalScale, 0, 0);
		Vector3f front = new Vector3f(0, 0, -normalScale);
		Vector3f back = new Vector3f(0, 0, normalScale);

		Vector3f[] normals =
			new Vector3f[] {
				up,
				up,
				up,
				up,
				up,
				up,
				back,
				back,
				back,
				back,
				back,
				back,
				left,
				left,
				left,
				left,
				left,
				left,
				right,
				right,
				right,
				right,
				right,
				right,
				down,
				down,
				down,
				down,
				down,
				down,
				front,
				front,
				front,
				front,
				front,
				front };

		TexCoord2f[] texCoords =
			new TexCoord2f[] {
				new TexCoord2f(0f, 0f),
				new TexCoord2f(1f, 0f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(0f, 1f),
				new TexCoord2f(0f, 0f),
				new TexCoord2f(0f, 0f),
				new TexCoord2f(1f, 0f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(0f, 1f),
				new TexCoord2f(0f, 0f),
				new TexCoord2f(0f, 0f),
				new TexCoord2f(1f, 0f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(0f, 1f),
				new TexCoord2f(0f, 0f),
				new TexCoord2f(0f, 0f),
				new TexCoord2f(1f, 0f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(0f, 1f),
				new TexCoord2f(0f, 0f),
				new TexCoord2f(0f, 0f),
				new TexCoord2f(1f, 0f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(0f, 1f),
				new TexCoord2f(0f, 0f),
				new TexCoord2f(0f, 0f),
				new TexCoord2f(1f, 0f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(1f, 1f),
				new TexCoord2f(0f, 1f),
				new TexCoord2f(0f, 0f),
				};

		Color3f[] colors =
			new Color3f[] {
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 0f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 0f, 1f),
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 0f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 0f, 1f),
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 0f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 0f, 1f),
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 0f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 0f, 1f),
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 0f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 0f, 1f),
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 1f, 1f),
				new Color3f(1f, 0f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 1f, 0f),
				new Color3f(0f, 0f, 1f),
				new Color3f(1f, 1f, 1f),
				};

		TriangleArray qa = null;

		int flags = GeometryArray.COORDINATES;
		if (addTextureCoords)
			flags |= GeometryArray.TEXTURE_COORDINATE_2;
		if (addColorData)
			flags |= GeometryArray.COLOR_3;
		if (addNormals)
			flags |= GeometryArray.NORMALS;

		qa = new TriangleArray(coords.length, flags);
		if (addColorData)
			qa.setColors(0, colors);
		//qa.setNormals(0,normals);

		qa.setCoordinates(0, coords);
		if (addTextureCoords)
			qa.setTextureCoordinates(0, 0, texCoords);
		//if (addNormals) qa.calculateFaceNormals();
		if (addNormals)
			qa.setNormals(0, normals);

		return qa;
	}

}
