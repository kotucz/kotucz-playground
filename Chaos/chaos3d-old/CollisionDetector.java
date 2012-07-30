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

/**
 *	@deprecated
 */
public class CollisionDetector extends Behavior {
	private WakeupOnCollisionEntry wEntry;
	private WakeupOnCollisionMovement wMove;
	
	private Entity entity;

	public CollisionDetector(Entity entity) {
		this.entity = entity;
		wEntry = new WakeupOnCollisionEntry(entity.objEntity, WakeupOnCollisionEntry.USE_GEOMETRY);
		wMove = new WakeupOnCollisionMovement(entity.objEntity, WakeupOnCollisionMovement.USE_BOUNDS);
//		entity.objEntity.addChild(this);
//		this.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));	
		entity.addBehavior(this);
	}

  	public void initialize() {
//		wakeupOn(wEntry);
		System.out.println("coldet initialized");
		wakeupOn(wMove);
   	}

	public void processStimulus(Enumeration criteria) {
//    	System.out.print("Entity collision entry: ");
//	   	SceneGraphPath sgp = wMove.getTriggeringPath();
//		System.out.print(sgp);
    	    	
    	entity.collisionDetected();
//    	wakeupOn(wEntry);
		wakeupOn(wMove);
		
   	}
   	

}
	