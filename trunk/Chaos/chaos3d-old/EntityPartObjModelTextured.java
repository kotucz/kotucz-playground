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

import com.sun.j3d.utils.image.*;

//import com.sun.j3d.utils.behaviors.keyboard.*;

class EntityPartObjModelTextured extends EntityPart {
	
	public EntityPartObjModelTextured (Vector3f pos, String mName, String tName) {
		System.out.println("new Part-"+mName);
		
		ObjectFile f = new ObjectFile();
		Scene s = null;
		
		try {
			s = f.load("models//"+mName);
		} catch (Exception e) {
			e.printStackTrace();
		}
 		
 		Shape3D objShape = (Shape3D)s.getSceneGroup().getChild(0);
 		s.getSceneGroup().removeChild(0);
 		
		Appearance shapeAppear = objShape.getAppearance();
 		
 		Material shapeMaterial = new Material();
		shapeMaterial.setSpecularColor(new Color3f(0, 0, 0));		
 		
 		shapeAppear.setMaterial(shapeMaterial);
 		
 		if (tName!="") {
 		   	TextureLoader tex = new TextureLoader("images//"+tName, null);
        	if (tex != null) 
	    		shapeAppear.setTexture(tex.getTexture());
 		
 			shapeAppear.setTexCoordGeneration(new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR , TexCoordGeneration.TEXTURE_COORDINATE_2));
 		}
 		
 		
 		
 		shapeAppear.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 1));
 		 		 		
 		this.set(objShape);
// 		this.set(s.getSceneGroup());
 		
 		Transform3D tSet = new Transform3D();
		tSet.set(pos);
		
		Transform3D tRotZ90 = new Transform3D();
		tRotZ90.rotZ(Math.PI/2.0);
		
		Transform3D tRotY90 = new Transform3D();
		tRotY90.rotY(Math.PI/2.0);
		
//		Transform3D tScal = new Transform3D();
//		tScal.setScale(0.1);
		
//		tRotZ90.mul(tScal);		
		tRotY90.mul(tRotZ90);
		tSet.mul(tRotY90);
				
		this.setTransform(tSet);
		
	}	

}
	
