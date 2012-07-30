package cz.kotuc.chaos;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;
import java.util.*;
import java.io.*;

public class WaterUpdater implements GeometryUpdater {
	Vector sources;
	float time;
	
	public WaterUpdater () {
		sources = new Vector();
		sources.add(new Point3f(0.0f, 0.0f, 0.5f));
//		sources.add(new Point3f(20.0f, 10.0f, 0.5f));
//		sources.add(new Point3f(20.0f, 20.0f, 0.5f));
//		sources.add(new Point3f(0.0f, 20.0f, 0.5f));
	
	
		time = 0.0f;	
	}
			
	public void updateData(Geometry geometry) {
		GeometryArray geometryArray = (GeometryArray)geometry;
		float[] coords = geometryArray.getCoordRefFloat();
		float[] colors = geometryArray.getColorRefFloat();
		
		int N = geometryArray.getValidVertexCount();
						
		for (int i = 0; i<N; i++) {
			float height = 0.0f;
			
			for (int j = 0; j<sources.size(); j++) {
				Point3f src = (Point3f)sources.get(j);
				float range = (float)Math.sqrt(Math.pow(coords[i*3+0]-src.x, 2)+Math.pow(coords[i*3+1]-src.y, 2));
//				height += Math.sin(range+System.currentTimeMillis());
				height += Math.sin(range-time);
			};
			
			coords[i*3+2] = height;
			colors[i*3] = height;
			colors[i*3+1] = -height;
			colors[i*3+2] = 1.0f;
		}
		
//			GeometryInfo gi = new GeometryInfo(geometryArray);                
               
//	       	NormalGenerator ng = new NormalGenerator();
//        	ng.generateNormals(gi);
       
//     		geometryArray = gi.getGeometryArray();
		
		time+=0.07f;
	}
}
	