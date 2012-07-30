package cz.kotuc.chaos;

import javax.vecmath.*;

public class Leaf extends PlantPart {
	/**
	* Method Leaf
	*
	*
	*/
	
	public Leaf(Branch part) {
		this.depth = part.depth + 1;
		this.plant = part.plant;
	    this.directionAngle = plant.genom.getGrowDirectionAngle(Genom.LEAF) + part.directionAngle;
	    this.elevationAngle = plant.genom.getGrowElevationAngle(Genom.LEAF);
	    
	    this.pos = part.endpos;
	    endpos = new Point3d();
	    
	    size = 0;
	    
	    maxSize = plant.genom.getGrowLength(Genom.LEAF);
	    System.out.println(maxSize);
	    plant.addPart(this);
	}	

	public void grow() {
		this.plant.energy += this.size * 2;
		if (this.plant.energy > 2) {
		
			if (size < maxSize) {
			
				plant.energy--;
				System.out.println("rostu");
				size++;
				
			};
		};
		
		
		
	}
	
	public void nodeActivity () {
				
	}

		
}
