/**
 *	Player v 0.03;
 *
 *	Tomas Kotula as Kotuc (=disc)
 */
package cz.kotuc.chaos;

import java.awt.*;
import java.applet.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.keyboard.*;

class Obstacle {
	
	public double smer = 0;
	
	Point3d pos;
	
	Location location;

	public Obstacle (Location location) {
		this.location=location;
	}	

	
	public BranchGroup createGeometry() {
		
		ObjectFile f = new ObjectFile();
		Scene s = null;
		
		BranchGroup objPlayer = new BranchGroup();
		
		try {
			s = f.load("head1.obj");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
 		
 		Transform3D tHeadSet = new Transform3D();
		tHeadSet.set(new Vector3f(0f,0f,1f));
				
		Transform3D tHeadRot = new Transform3D();
		tHeadRot.rotZ(0);
		tHeadRot.mul(tHeadSet);
		
		TransformGroup objHeadTG = new TransformGroup(tHeadRot);
		objHeadTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objHeadTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		objPlayer.addChild(objHeadTG);
 		objHeadTG.addChild(s.getSceneGroup());
 		
 		objPlayer.addChild(new ColorCube(0.4));
	
		return objPlayer;
	}

	
	public void step () {
	}

//  abstract methods from entity

	public void doEveryFrame(java.util.Enumeration enum) {
		step();					
	}
	
	public void collisionDetected (Enumeration enum) {
		System.out.println("Player"+location.time);	
		step();
	}
}
	