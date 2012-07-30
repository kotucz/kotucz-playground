/**
 *	Entity v 0.01;
 *
 *	Tomas Kotula as Kotuc (=disc)
 */
package cz.kotuc.chaos;

import java.util.*;
import java.io.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

//import com.sun.j3d.utils.behaviors.keyboard.*;

class EntityBehavior extends Behavior {
	
	//private Transform3D tSet = null;		// move obj entity, which is
//	private Transform3D tRot = null;		// its avatar,
											// to its current position
	Entity entity = null;
	
	
	public EntityBehavior (Entity entity) {
		this.entity=entity;
//		this.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
//		entity.objAdd.addChild(this);
		entity.addBehavior(this);		
	}	
	
	WakeupOnElapsedFrames w = new WakeupOnElapsedFrames(0);
							
	public void initialize () {
		wakeupOn(w);			
	}

	/**
	 *	doing actualizations of 3D view 
	 */
	
	public void processStimulus(java.util.Enumeration enum) {
//		System.out.println("physics");		
		entity.performPhysics();

//		System.out.println("behavior");		
		entity.doEveryFrame();
		
//		objHPindicator
		entity.hpAppear.setColoringAttributes(new ColoringAttributes(1.0f - (float)entity.hitpoints/(float)entity.maxHitpoints, (float)entity.hitpoints/(float)entity.maxHitpoints, 0.0f, ColoringAttributes.NICEST));
		
		entity.refreshPosition();
		
		if (entity.hitpoints>0) {
//			System.out.println("next");
			wakeupOn(w);
		} else {
			entity.die();	
//			wakeupOn(w);					
		};

	}
	
}
	