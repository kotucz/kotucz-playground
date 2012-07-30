package cz.kotuc.chaos;
//import java.awt.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
;

abstract class GeneticEntity extends Entity {
	
	Genom genom;
	
	public GeneticEntity() {
		super();
		genom = new Genom();
	}	
	
	public void kill() {
		
	}
	
	public void doEveryFrame(java.util.Enumeration enum) {					
	}
	
	Morph handMorph;
/*	
	public BranchGroup createAvatar() {
		BranchGroup bg = new BranchGroup();
		
		String[] fNames = new String[3];
		fNames[0] = "models/hand1.obj";	 		
		fNames[1] = "models/hand2.obj";
		fNames[2] = "models/hand3.obj";
		
		Appearance handAppear = new Appearance();
		handAppear.setMaterial(new Material());
		handMorph = new MorphLoader(fNames, handAppear);
		bg.addChild(handMorph);
		
		this.refreshWeights();
		
		return bg;		 		
 	}	
*/	
	public void setStrength(int i) {
		
	}
	
	public void refreshWeights() {
		double[] w = {0.2, 0.0, 0.8};
		handMorph.setWeights(w);
	}
	
/*	public String toString() {
		return "Animal"+this.hitpoints+"HP\n";
	}*/
}
