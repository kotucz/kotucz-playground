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

public class ExplosionUpdater implements GeometryUpdater {
	public void updateData (Geometry geometry) {
		GeometryArray geometryArray = (GeometryArray)geometry;
		float[] coords = geometryArray.getCoordRefFloat();
		
		int N = geometryArray.getValidVertexCount();
		
		for (int i = 0; i<N; i+=2) {
			 if (coords[i*3+2+3]>-1f) {
				float vx = coords[i*3+0+3]-coords[i*3+0];
				float vy = coords[i*3+1+3]-coords[i*3+1];
				float vz = coords[i*3+2+3]-coords[i*3+2];
								
				coords[i*3+0] += vx;
				coords[i*3+1] += vy;
				coords[i*3+2] += vz;
			
				coords[i*3+0+3] += vx;
				coords[i*3+1+3] += vy;
				coords[i*3+2+3] += vz-0.01f;
			}
		}
	} 
	
	/**	Creates GeometryArray, that will look like explosion
	 *	if ExplosionUpdater is used
	 *	
	 *	@param N the count of particles to be created
	 *	when N is too high, your graphic card may not be able to paint them
	 *	when N is too small, it wouldn�t look like an explosion
	 *
	 *	@return new GeometryArray
	 */
	public static GeometryArray createParticles (int N) {
		N*=3;

		GeometryArray particles = new LineArray(N*2, GeometryArray.COORDINATES|GeometryArray.BY_REFERENCE);
		
		particles.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		particles.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
		particles.setCapability(GeometryArray.ALLOW_COUNT_READ);
		
		float[] coordinates = new float[N*2*3];
		
		for (int i = 0; i<N; i+=2) {
			coordinates[i*3+0]=0f;		//	x
			coordinates[i*3+1]=0f;		//	y
			coordinates[i*3+2]=0f;		//	z
			
			coordinates[i*3+0+3]=(float)Math.random()*0.2f-0.1f;		//	x
			coordinates[i*3+1+3]=(float)Math.random()*0.2f-0.1f;		//	y
			coordinates[i*3+2+3]=(float)Math.random()*0.2f-0.05f;		//	z
		}
		
		particles.setCoordRefFloat(coordinates);
		
		return particles;
	}
}
	