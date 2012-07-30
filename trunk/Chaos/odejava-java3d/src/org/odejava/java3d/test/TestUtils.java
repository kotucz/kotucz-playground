/*
 * Created on 08.09.2003
 *
 * Test routines often re-used in tests
 */
package org.odejava.java3d.test;

import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * @author YVG
 * @author Java 3D port by Paul Byrne
 *
 * Test routines often re-used in tests
 */
public class TestUtils
{

//    public static Geometry createCubeViaTriangles(
//        float x,
//        float y,
//        float z,
//        float size,
//        boolean addColorData) {
//
//        return createCubeViaTriangles(x,y,z,size,addColorData,false,true,1f);
//
//    }
//
//	public static Geometry createCubeViaTriangles(
//		float x,
//		float y,
//		float z,
//		float size,
//		boolean addColorData, boolean addNormals) {
//
//        	return createCubeViaTriangles(x,y,z,size,addColorData,addNormals,true,1f);
//	}

    public static void reverseWinding( GeometryArray ga) {

        float coords[] = new float[ ga.getVertexCount()*3 ];
        ga.getCoordinates( 0, coords );
        for (int i=0;i<coords.length/9;i++) {
            float tempx = coords[i*9+6];
            float tempy = coords[i*9+7];
            float tempz = coords[i*9+8];
            coords[i*9+6] = coords[i*9+0];
            coords[i*9+7] = coords[i*9+1];
            coords[i*9+8] = coords[i*9+2];
            coords[i*9+0] = tempx;
            coords[i*9+1] = tempy;
            coords[i*9+2] = tempz;

        }
        ga.setCoordinate(0,coords);
    }

//	public static Geometry createCubeViaTriangles(
//		float x,
//		float y,
//		float z,
//		float size,
//		boolean addColorData, boolean addNormals, boolean addTextureCoords, float normalScale)
//	{
//		return createRectangleViaTriangles(x,y,z,size,size,size,addColorData,addNormals,addTextureCoords,normalScale);
//	}	
//	public static Geometry createRectangleViaTriangles(
//		float x,
//		float y,
//		float z,
//		float sizex,
//		float sizey,
//		float sizez,
//		boolean addColorData, boolean addNormals, boolean addTextureCoords, float normalScale)
//	{
//		float halfx = sizex / 2f;
//		float halfy = sizey / 2f;
//		float halfz = sizez / 2f;
//
//		Point3f[] coords =
//			new Point3f[] {
//
//                // top
//
//				new Point3f(-halfx + x, halfy + y, halfz + z),
//				new Point3f(halfx + x, halfy + y, halfz + z),
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//				new Point3f(-halfx + x, halfy + y, -halfz + z),
//				new Point3f(-halfx + x, halfy + y, halfz + z),
//
//                // back
//
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//				new Point3f(halfx + x, halfy + y, halfz + z),
//				new Point3f(-halfx + x, halfy + y, halfz + z),
//				new Point3f(-halfx + x, halfy + y, halfz + z),
//				new Point3f(-halfx + x, -halfy + y, halfz + z),
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//
//                // left
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//				new Point3f(halfx + x, halfy + y, halfz + z),
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//				new Point3f(halfx + x, -halfy + y, -halfz + z),
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//
//                // right
//				new Point3f(-halfx + x, -halfy + y, halfz + z),
//				new Point3f(-halfx + x, halfy + y, halfz + z),
//				new Point3f(-halfx + x, halfy + y, -halfz + z),
//				new Point3f(-halfx + x, halfy + y, -halfz + z),
//				new Point3f(-halfx + x, -halfy + y, -halfz + z),
//				new Point3f(-halfx + x, -halfy + y, halfz + z),
//
//                // bottom
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//				new Point3f(-halfx + x, -halfy + y, halfz + z),
//				new Point3f(-halfx + x, -halfy + y, -halfz + z),
//				new Point3f(-halfx + x, -halfy + y, -halfz + z),
//				new Point3f(halfx + x, -halfy + y, -halfz + z),
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//                //front
//
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//				new Point3f(halfx + x, -halfy + y, -halfz + z),
//				new Point3f(-halfx + x, -halfy + y, -halfz + z),
//				new Point3f(-halfx + x, -halfy + y, -halfz + z),
//				new Point3f(-halfx + x, halfy + y, -halfz + z),
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//				};
///*
//        Vector3f up = new Vector3f(0,1,0);
//        Vector3f down = new Vector3f(0,-1,0);
//        Vector3f right = new Vector3f(-1,0,0);
//        Vector3f left = new Vector3f(1,0,0);
//        Vector3f front = new Vector3f(0,-1,0);
//        Vector3f back = new Vector3f(1,0,0);
//*/
//        Vector3f up = new Vector3f(0,normalScale,0);
//        Vector3f down = new Vector3f(0,-normalScale,0);
//        Vector3f right = new Vector3f(-normalScale,0,0);
//        Vector3f left = new Vector3f(normalScale,0,0);
//        Vector3f front = new Vector3f(0,0,-normalScale);
//        Vector3f back = new Vector3f(0,0,normalScale);
//
//        Vector3f[] normals =
//            new Vector3f[] {
//                up,up,up,up,up,up,
//                back,back,back,back,back,back,
//                left,left,left,left,left,left,
//                right,right,right,right,right,right,
//                down,down,down,down,down,down,
//                front,front,front,front,front,front
//            };
//
//		TexCoord2f[] texCoords =
//			new TexCoord2f[] {
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(1f, 0f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(0f, 1f),
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(1f, 0f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(0f, 1f),
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(1f, 0f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(0f, 1f),
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(1f, 0f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(0f, 1f),
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(1f, 0f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(0f, 1f),
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(0f, 0f),
//				new TexCoord2f(1f, 0f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(1f, 1f),
//				new TexCoord2f(0f, 1f),
//				new TexCoord2f(0f, 0f),
//				};
//
//		Color3f[] colors =
//			new Color3f[] {
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				};
//
//		TriangleArray qa = null;
//
//        int flags = GeometryArray.COORDINATES;
//        if (addTextureCoords) flags |= GeometryArray.TEXTURE_COORDINATE_2;
//        if (addColorData) flags |= GeometryArray.COLOR_3;
//        if (addNormals) flags |= GeometryArray.NORMALS;
//
//			qa =
//				new TriangleArray(
//					coords.length, flags);
//		if (addColorData)
//			qa.setColors(0, colors);
//            //qa.setNormals(0,normals);
//
//		qa.setCoordinates(0, coords);
//		if (addTextureCoords) qa.setTextureCoordinates(0, 0, texCoords);
//        if (addNormals) {
//            qa.calculateFaceNormals();
//            float f[] = qa.getNormalData().getFloatData();
//            System.out.println("f size = "+f.length);
//            for (int i=0;i<f.length/3;i++) {
//                Vector3f v = new Vector3f(f[i*3],f[i*3+1],f[i*3+2]);
//                if (!v.equals(normals[i])) {
//                    System.out.println("different normal from calculation : "+v+" != "+normals[i]);
//                }
//            }
//        }
//
//        //if (addNormals) qa.setNormals(0,normals);
//
//		return qa;
//	}

//	public static Geometry createRectangleViaTrianglesT3(
//		float x,
//		float y,
//		float z,
//		float sizex,
//		float sizey,
//		float sizez,
//		boolean addColorData, boolean addNormals, boolean addTextureCoords, float normalScale)
//	{
//		float halfx = sizex / 2f;
//		float halfy = sizey / 2f;
//		float halfz = sizez / 2f;
//
//		Point3f[] coords =
//			new Point3f[] {
//
//                // top
//
//				new Point3f(-halfx + x, halfy + y, halfz + z),
//				new Point3f(halfx + x, halfy + y, halfz + z),
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//				new Point3f(-halfx + x, halfy + y, -halfz + z),
//				new Point3f(-halfx + x, halfy + y, halfz + z),
//
//                // back
//
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//				new Point3f(halfx + x, halfy + y, halfz + z),
//				new Point3f(-halfx + x, halfy + y, halfz + z),
//				new Point3f(-halfx + x, halfy + y, halfz + z),
//				new Point3f(-halfx + x, -halfy + y, halfz + z),
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//
//                // left
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//				new Point3f(halfx + x, halfy + y, halfz + z),
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//				new Point3f(halfx + x, -halfy + y, -halfz + z),
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//
//                // right
//				new Point3f(-halfx + x, -halfy + y, halfz + z),
//				new Point3f(-halfx + x, halfy + y, halfz + z),
//				new Point3f(-halfx + x, halfy + y, -halfz + z),
//				new Point3f(-halfx + x, halfy + y, -halfz + z),
//				new Point3f(-halfx + x, -halfy + y, -halfz + z),
//				new Point3f(-halfx + x, -halfy + y, halfz + z),
//
//                // bottom
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//				new Point3f(-halfx + x, -halfy + y, halfz + z),
//				new Point3f(-halfx + x, -halfy + y, -halfz + z),
//				new Point3f(-halfx + x, -halfy + y, -halfz + z),
//				new Point3f(halfx + x, -halfy + y, -halfz + z),
//				new Point3f(halfx + x, -halfy + y, halfz + z),
//                //front
//
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//				new Point3f(halfx + x, -halfy + y, -halfz + z),
//				new Point3f(-halfx + x, -halfy + y, -halfz + z),
//				new Point3f(-halfx + x, -halfy + y, -halfz + z),
//				new Point3f(-halfx + x, halfy + y, -halfz + z),
//				new Point3f(halfx + x, halfy + y, -halfz + z),
//				};
///*
//        Vector3f up = new Vector3f(0,1,0);
//        Vector3f down = new Vector3f(0,-1,0);
//        Vector3f right = new Vector3f(-1,0,0);
//        Vector3f left = new Vector3f(1,0,0);
//        Vector3f front = new Vector3f(0,-1,0);
//        Vector3f back = new Vector3f(1,0,0);
//*/
//        Vector3f up = new Vector3f(0,normalScale,0);
//        Vector3f down = new Vector3f(0,-normalScale,0);
//        Vector3f right = new Vector3f(-normalScale,0,0);
//        Vector3f left = new Vector3f(normalScale,0,0);
//        Vector3f front = new Vector3f(0,0,-normalScale);
//        Vector3f back = new Vector3f(0,0,normalScale);
//
//        Vector3f[] normals =
//            new Vector3f[] {
//                up,up,up,up,up,up,
//                back,back,back,back,back,back,
//                left,left,left,left,left,left,
//                right,right,right,right,right,right,
//                down,down,down,down,down,down,
//                front,front,front,front,front,front
//            };
//
//		TexCoord3f[] texCoords =
//			new TexCoord3f[] {
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(1f, 0f, 0.5f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(0f, 1f, 0.5f),
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(1f, 0f, 0.5f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(0f, 1f, 0.5f),
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(1f, 0f, 0.5f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(0f, 1f, 0.5f),
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(1f, 0f, 0.5f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(0f, 1f, 0.5f),
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(1f, 0f, 0.5f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(0f, 1f, 0.5f),
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(0f, 0f, 0f),
//				new TexCoord3f(1f, 0f, 0.5f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(1f, 1f, 1f),
//				new TexCoord3f(0f, 1f, 0.5f),
//				new TexCoord3f(0f, 0f, 0f)
//				};           
//
//		Color3f[] colors =
//			new Color3f[] {
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 1f, 1f),
//				new Color3f(1f, 0f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 1f, 0f),
//				new Color3f(0f, 0f, 1f),
//				new Color3f(1f, 1f, 1f),
//				};
//
//		TriangleArray qa = null;
//
//        int flags = GeometryArray.COORDINATES;
//        if (addTextureCoords) flags |= GeometryArray.TEXTURE_COORDINATE_3;
//        if (addColorData) flags |= GeometryArray.COLOR_3;
//        if (addNormals) flags |= GeometryArray.NORMALS;
//
//			qa =
//				new TriangleArray(
//					coords.length, flags);
//		if (addColorData)
//			qa.setColors(0, colors);
//            //qa.setNormals(0,normals);
//
//		qa.setCoordinates(0, coords);
//		if (addTextureCoords) qa.setTextureCoordinates(0, 0, texCoords);
//        if (addNormals) {
//            qa.calculateFaceNormals();
//            float f[] = qa.getNormalData().getFloatData();
//            System.out.println("f size = "+f.length);
//            for (int i=0;i<f.length/3;i++) {
//                Vector3f v = new Vector3f(f[i*3],f[i*3+1],f[i*3+2]);
//                if (!v.equals(normals[i])) {
//                    System.out.println("different normal from calculation : "+v+" != "+normals[i]);
//                }
//            }
//        }
//
//        //if (addNormals) qa.setNormals(0,normals);
//
//		return qa;
//	}

	public static Geometry createQuad(
		Point3f p1,
		Point3f p2,
		Point3f p3,
		Point3f p4,
		float texScaleX,
		float texScaleY)
	{
		// Order of vertexes is important for normal generation
		float[] verts1 = 
		{
			p1.x, p1.y, p1.z,
			p2.x, p2.y, p2.z,
			p3.x, p3.y, p3.z,
			p4.x, p4.y, p4.z
		};

		float[] texCoords = 
		{
			texScaleX, 0f,
			texScaleX, texScaleY,
			0f, texScaleY,
			0f, 0f
		};

		QuadArray rect = new QuadArray(4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
		rect.setCoordinates(0, verts1);
		rect.setTextureCoordinates(0, 0, texCoords);
	
		return rect;
	}

	public static Geometry createPlane(
		float x,
		float y,
		float z,
		float xsize,
		float ysize)
	{
		float xhalf = xsize / 2f;
		float yhalf = ysize / 2f;
		
		// Order of vertexes is important for normal generation
		float[] verts1 = 
		{
			x+xhalf, y-yhalf, z,
			x+xhalf, y+yhalf, z,
			x-xhalf, y+yhalf, z,
			x-xhalf, y-yhalf, z
		};

		float[] texCoords = 
		{
			1f, 0f,
			1f, 1f,
			0f, 1f,
			0f, 0f
		};

		QuadArray rect = new QuadArray(4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
		rect.setCoordinates(0, verts1);
		rect.setTextureCoordinates(0, 0, texCoords);
	
		return rect;
	}

	public static Geometry createPlaneWithNormals(
		float x,
		float y,
		float z,
		float xsize,
		float ysize)
	{
		float xhalf = xsize / 2f;
		float yhalf = ysize / 2f;
		
		// Order of vertexes is important for normal generation
		float[] verts1 = 
		{
			x+xhalf, y-yhalf, z,
			x+xhalf, y+yhalf, z,
			x-xhalf, y+yhalf, z,
			x-xhalf, y-yhalf, z
		};

		float[] texCoords = 
		{
			1f, 0f,
			1f, 1f,
			0f, 1f,
			0f, 0f
		};

		float[] normals = 
		{
			0f, 0f, 1f,
			0f, 0f, 1f,
			0f, 0f, 1f,
			0f, 0f, 1f
		};

		QuadArray rect = new QuadArray(4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2 | QuadArray.NORMALS);
		rect.setCoordinates(0, verts1);
		rect.setTextureCoordinates(0, 0, texCoords);
		rect.setNormals(0, normals);
	
		return rect;
	}

	public static Geometry createNonTexturedPlane(
		float x,
		float y,
		float z,
		float xsize,
		float ysize)
	{
		float xhalf = xsize / 2f;
		float yhalf = ysize / 2f;
		
		// Order of vertexes is important for normal generation
		float[] verts1 = 
		{
			x+xhalf, y-yhalf, z,
			x+xhalf, y+yhalf, z,
			x-xhalf, y+yhalf, z,
			x-xhalf, y-yhalf, z
		};

		QuadArray rect = new QuadArray(4, QuadArray.COORDINATES);
		rect.setCoordinates(0, verts1);
	
		return rect;
	}


	public static Geometry createSphere(float R, int divisions) {
		// This sphere generation is not pretending to be optimal and implemented only for testing purposes
		float sign = 1.0f;
		float[] vertexes = new float[(divisions+1) * (2*divisions+1) * 3];
		float[] normals = new float[(divisions+1) * (2*divisions+1) * 3];
		for (int i = 0; i <= divisions; i++) {
			float rho = (float) ((double) i * Math.PI / (double) divisions);
			for (int j = 0; j <= 2*divisions; j++) {
				float theta = (float) ((double) j * Math.PI / (double) divisions);
				float vx = (float) (Math.cos(theta) * Math.sin(rho));
				float vy = (float) (sign * Math.cos(rho));
				float vz = (float) (Math.sin(theta) * Math.sin(rho));
				vertexes[((i*(2*divisions+1)) + j)*3 + 0] = vx * R;
				vertexes[((i*(2*divisions+1)) + j)*3 + 1] = vy * R;
				vertexes[((i*(2*divisions+1)) + j)*3 + 2] = vz * R;
				normals[((i*(2*divisions+1)) + j)*3 + 0] = vx * sign;
				normals[((i*(2*divisions+1)) + j)*3 + 1] = vy * sign;
				normals[((i*(2*divisions+1)) + j)*3 + 2] = vz * sign;
			}
		}
		int[] vIdx = new int[divisions * 2*divisions * 6];
		int n = 0;
		for (int i = 0; i < divisions; i++) {
			for (int j = 0; j < 2*divisions; j++) {
				int v00 = i*(2*divisions+1) + j;
				int v01 = i*(2*divisions+1) + j+1;
				int v10 = (i+1)*(2*divisions+1) + j;
				int v11 = (i+1)*(2*divisions+1) + j+1;
				vIdx[n++] = v11;
				vIdx[n++] = v10;
				vIdx[n++] = v00;
				vIdx[n++] = v11;
				vIdx[n++] = v00;
				vIdx[n++] = v01;
			}
		}
		IndexedTriangleArray shapeGeom = new IndexedTriangleArray(vertexes.length / 3, TriangleArray.COORDINATES | TriangleArray.NORMALS, vIdx.length);
		shapeGeom.setValidVertexCount(vertexes.length / 3);
		shapeGeom.setCoordinates(0, vertexes);
		shapeGeom.setValidIndexCount(vIdx.length);
		shapeGeom.setCoordinateIndices(0,vIdx);
		shapeGeom.setNormals(0, normals);
		return shapeGeom;
	}

}
