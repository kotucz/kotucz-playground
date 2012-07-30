package kotuc.chaos;

import java.util.*;
import javax.media.j3d.*;

/**
 *	@deprecated
 */
public class CollisionDetector extends Behavior {
	private WakeupOnCollisionEntry wEntry;
	private WakeupOnCollisionMovement wMove;
	
	private PhysicEntity entity;

	public CollisionDetector(PhysicEntity entity) {
		this.entity = entity;
		wEntry = new WakeupOnCollisionEntry(entity.getMainNode(), WakeupOnCollisionEntry.USE_GEOMETRY);
		wMove = new WakeupOnCollisionMovement(entity.getMainNode(), WakeupOnCollisionMovement.USE_BOUNDS);
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
//    	System.out.print("PhysicEntity collision entry: ");
//	   	SceneGraphPath sgp = wMove.getTriggeringPath();
//		System.out.print(sgp);
    	    	
    	entity.collisionDetected();
//    	wakeupOn(wEntry);
		wakeupOn(wMove);
		
   	}
   	

}
	