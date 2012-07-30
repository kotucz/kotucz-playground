package cz.kotuc.chaos;
/**
 *	Entity v 0.01;
 *
 *	Tomas Kotula as Kotuc (=disc)
 */


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

public class EntityPart {
	
	BranchGroup objRoot = new BranchGroup();
	
	TransformGroup objTrans = new TransformGroup();
	
	Node objPart;
	
	public EntityPart () {
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		
		objRoot.addChild(objTrans);
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objRoot.setCapability(BranchGroup.ALLOW_DETACH);
	}	

	public EntityPart (Node node) {
		objPart = node;
				
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		objTrans.addChild(objPart);
		objRoot.addChild(objTrans);
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objRoot.setCapability(BranchGroup.ALLOW_DETACH);

	}	

	public EntityPart (Node node, Transform3D transform) {
		this(node);
		objTrans.setTransform(transform);
	}
	
/*	final public Bounds getBounds () {
		Bounds thisBounds = this.objPart.getBounds();
		thisBounds.transform(this.getTransform());
		return thisBounds;
	}
*/	

	public static Node createDefaultPart() {
		return new ColorCube();	
	};

	public void set(Node node) {
//		this.objPart = node;
		objTrans.addChild(node);
	}		
	
	public void setTransform(Transform3D t) {
		this.objTrans.setTransform(t);
	}
	
}
	
