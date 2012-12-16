package cz.kotuc.chaos;
//import java.awt.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import java.util.*;

public class Plant extends GeneticEntity {
	
	List parts = new LinkedList();
		
	public Plant() {
		super();
		
		this.maxHitpoints = 500;
		this.hitpoints = 500;
		new Branch(this);
	}	
	
	public Plant(Plant plant) {
		for (int i=0; i<Entity.entities.size()-1; i++) {
			Entity ent;
			if ((ent=(Entity)Entity.entities.get(i)).getPos().distance(this.getPos())<2.0) this.die(); 
		}
		
		
		this.genom = new Genom(plant.genom);
		this.maxHitpoints = 500;
		this.hitpoints = 500;
		new Branch(this);
	}
	
	public void addPart (PlantPart part) {
		this.parts.add(part);
	}
	
	
	long energy = 1500;
	
	public void doEveryFrame() {
							
		this.hitpoints = (int) energy;
		if (this.energy > 5000) this.die();
			
			if (Location.time%1==0) {
				energy -= (int) (Math.random() * 5);
				List parts2 = (List)((LinkedList)parts).clone();
				Iterator li = parts2.iterator();
				while (li.hasNext()) {
					((PlantPart)li.next()).grow();
			
				};
				refreshParts();
	
		};
		
			
	}
	
//	public void die() {
//		this.parts.clear();
//	}
	
	Shape3D shape;
	
	public BranchGroup createAvatar() {
		BranchGroup bg = new BranchGroup();
		/*
		String[] fNames = new String[3];
		fNames[0] = "models/hand1.obj";	 		
		fNames[1] = "models/hand2.obj";
		fNames[2] = "models/hand3.obj";
		
		Appearance handAppear = new Appearance();
		handAppear.setMaterial(new Material());
		handMorph = new MorphLoader(fNames, handAppear);
		bg.addChild(handMorph);
		
		this.refreshWeights();
		*/
		Appearance app = new Appearance();
		app.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL , PolygonAttributes.CULL_NONE, 0f));
		shape = new Shape3D(new LineArray(2, LineArray.COORDINATES), app);
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
//		refreshParts();
		bg.addChild(shape);
//		bg.addChild(new ColorCube());
		
		return bg;		 		
 	}	
	
	public void refreshParts() {
		int N=parts.size();
		
		GeometryArray particles = new TriangleArray(N*3, 
			GeometryArray.COORDINATES
			|GeometryArray.BY_REFERENCE
			|GeometryArray.COLOR_3
		);
		
//		particles.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
//		particles.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
//		particles.setCapability(GeometryArray.ALLOW_COUNT_READ);
		
		float[] coordinates = new float[N*3*3];
		float[] colors = new float[N*3*3];
//		float[] coordinates = particles.getColorRefFloat();
//		float[] colors = particles.getColorRefFloat();
		
		for (int i = 0; i<N; i++) {
			PlantPart pp = ((PlantPart)parts.get(i));
			
			coordinates[i*9+0]=(float)pp.pos.x;		//	x
			coordinates[i*9+1]=(float)pp.pos.y;		//	y
			coordinates[i*9+2]=(float)pp.pos.z;		//	z
			
			pp.endpos();
			if (pp instanceof Branch) {
			
				coordinates[i*9+0+3]=(float)pp.endpos.x-0.1f;		//	x
				coordinates[i*9+1+3]=(float)pp.endpos.y;		//	y
				coordinates[i*9+2+3]=(float)pp.endpos.z;		//	z
			
				coordinates[i*9+0+6]=(float)pp.endpos.x+0.1f;		//	x
				coordinates[i*9+1+6]=(float)pp.endpos.y;		//	y
				coordinates[i*9+2+6]=(float)pp.endpos.z;		//	z
			} else {
				
				coordinates[i*9+0+3]=(float)(pp.endpos.x) - (float) (Math.sin(pp.directionAngle) * pp.size / 50f);		//	x
				coordinates[i*9+1+3]=(float)(pp.endpos.y) + (float) (Math.cos(pp.directionAngle) * pp.size / 50f);		//	y
				coordinates[i*9+2+3]=(float)pp.endpos.z;		//	z
			
				coordinates[i*9+0+6]=(float)(pp.endpos.x) + (float) (Math.sin(pp.directionAngle) * pp.size / 50f);		//	x
				coordinates[i*9+1+6]=(float)(pp.endpos.y) - (float) (Math.cos(pp.directionAngle) * pp.size / 50f);		//	y
				coordinates[i*9+2+6]=(float)pp.endpos.z;		//	z
			}
			
			for (int j = 0; j<3; j++) {
				if (pp instanceof Leaf) {
					colors[9*i+j*3+0] = 0f;	// red
					colors[9*i+j*3+1] = 1f;	// green
					colors[9*i+j*3+2] = 0f;	// blue
					break;
				}
				if (pp instanceof Branch) {
					colors[9*i+j*3+0] = 0.8f;	// red
					colors[9*i+j*3+1] = 0.4f;	// green
					colors[9*i+j*3+2] = 0.3f;	// blue
					break;
				}
				if (pp instanceof Seed) {
					colors[9*i+j*3+0] = 1f;	// red
					colors[9*i+j*3+1] = 0.2f;	// green
					colors[9*i+j*3+2] = 0.4f;	// blue
					break;
				}
				
					colors[9*i+j*3+0] = 1f;	// red
					colors[9*i+j*3+1] = 1f;	// green
					colors[9*i+j*3+2] = 1f;	// blue
					break;
				
			}
		}
		
		
		particles.setCoordRefFloat(coordinates);
		particles.setColorRefFloat(colors);
				
		this.shape.setGeometry(particles);
		
	}
	
	public String toString() {
		return "ENERGY : " + this.energy;
	}

	

	
}