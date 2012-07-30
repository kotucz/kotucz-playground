package cz.kotuc.chaos;

import java.awt.*;
import java.applet.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class ParticlesUpdater implements GeometryUpdater {
	public void updateData (Geometry geometry) {
		GeometryArray geometryArray = (GeometryArray)geometry;
		float[] coords = geometryArray.getCoordRefFloat();
		
		int N = geometryArray.getValidVertexCount();
		
		for (int i = 0; i<N; i+=2) {
			float vx = coords[i*3+0+3]-coords[i*3+0];
			float vy = coords[i*3+1+3]-coords[i*3+1];
			float vz = coords[i*3+2+3]-coords[i*3+2];
								
			coords[i*3+0] += vx;
			coords[i*3+1] += vy;
			coords[i*3+2] += vz;
			
			coords[i*3+0+3] += vx + ((0<coords[i*3+0])?-0.01f:+0.01f);;
			coords[i*3+1+3] += vy + ((0<coords[i*3+1])?-0.01f:+0.01f);;
			coords[i*3+2+3] += vz + ((0<coords[i*3+2])?-0.01f:+0.01f);;
		}
			
	} 
	
	public static GeometryArray createParticles(int N) {
		N*=3;  // must be 3 dividable
		
		GeometryArray particles = new LineArray(N*2, LineArray.COORDINATES|LineArray.BY_REFERENCE);
		
		particles.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		particles.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
		particles.setCapability(GeometryArray.ALLOW_COUNT_READ);
		
		float[] coordinates = new float[N*2*3];
		
		for (int i = 0; i<N; i+=2) {
			coordinates[i*3+0]=(float)Math.random()*0.2f-0.1f;		//	x
			coordinates[i*3+1]=(float)Math.random()*0.2f-0.1f;		//	y
			coordinates[i*3+2]=(float)Math.random()*0.2f-0.1f;		//	z
			
			coordinates[i*3+0+3]=(float)Math.random()*0.4f-0.2f;		//	x
			coordinates[i*3+1+3]=(float)Math.random()*0.4f-0.2f;		//	y
			coordinates[i*3+2+3]=(float)Math.random()*0.4f-0.2f;		//	z
		}
		
		particles.setCoordRefFloat(coordinates);
		
		
		return particles;
	}
}
	