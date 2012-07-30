package kotuc.chaos;

import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.io.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.IncorrectFormatException;


public class MorphLoader extends Morph {
	
	GeometryArray[] gArrays = null;
	
	public MorphLoader(String[] filenames, Appearance appear) {
		super(null);
		this.setCapability(Morph.ALLOW_WEIGHTS_READ);
		this.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
		
		this.loadFromFiles(filenames);
		
		this.setAppearance(appear);
	}
		
	public MorphLoader(String[] filenames) {
		super(null);
		
		this.setCapability(Morph.ALLOW_WEIGHTS_READ);
		this.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
		
		this.loadFromFiles(filenames);		
	}
	
	public void loadFromFiles(String[] filenames) {
		Scene s[] = new Scene[filenames.length];
		gArrays = new GeometryArray[filenames.length];
		Shape3D shape[] = new Shape3D[filenames.length];
		ObjectFile loader = new ObjectFile(ObjectFile.RESIZE);
		for(int i=0; i<filenames.length; i++) {
	    	s[i] = null;
	    	gArrays[i] = null;
	 		shape[i] = null;
		}

		for(int i=0; i<filenames.length;i++) {
	    	try {
				s[i] = loader.load(filenames[i]);
	    	}
	    	catch (FileNotFoundException e) {
	      		System.err.println(e);
	      		System.exit(1);
	   		}
	    	catch (ParsingErrorException e) {
	      		System.err.println(e);
	    		System.exit(1);
	    	}
	    	catch (IncorrectFormatException e) {
	      		System.err.println(e);
	     		System.exit(1);
	    	}

        	BranchGroup b = s[i].getSceneGroup();
	    	shape[i] = (Shape3D) b.getChild(0);
	    	gArrays[i] = (GeometryArray) shape[i].getGeometry();
			
	    	shape[i].setGeometry(gArrays[i]);
//	    	objTrans.addChild(b);
		}
		this.setGeometryArrays(gArrays);
	}	
}
