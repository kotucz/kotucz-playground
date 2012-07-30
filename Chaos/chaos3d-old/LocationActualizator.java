package cz.kotuc.chaos;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.picking.*;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.image.*;

public class LocationActualizator extends Behavior {
	WakeupOnElapsedFrames w = new WakeupOnElapsedFrames(0);
	
	Location location;
	
	public LocationActualizator (Location location) {
		this.location=location;
		location.objLocation.addChild(this);
	}
	
	public void initialize () {
		wakeupOn(w);			
	}

	public void processStimulus(java.util.Enumeration enum) {
		location.time++;
					
//	actualization of background
		double sunAngle = (double)location.time/3000.0; //1 radian per minute = 30fps*60s
		
		Transform3D tRot = new Transform3D();
		tRot.rotY(-sunAngle); 
		location.objBgRot.setTransform(tRot);
	
		location.sunshine.setDirection(new Vector3f((float)Math.cos(sunAngle-0.5*Math.PI), 0.0f, (float)Math.sin(sunAngle-0.5*Math.PI)));
		
		wakeupOn(w);					
	}
}
