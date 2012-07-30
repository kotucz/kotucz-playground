package cz.kotuc.chaos;
/**
 *	DarkBall v 0.04;
 *
 *	Tomas Kotula as Kotuc (=disc)
 */


import java.awt.*;
import java.applet.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;
import com.sun.j3d.utils.image.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;


class DarkBall extends Entity {
	
//	TransformGroup objHeadTG = null;
	
	BranchGroup objBall = null;	


	public DarkBall () {
		super();
		
		setVel(new Vector3d(0.1*(Math.random()-0.5), 0.1*(Math.random()-0.5), 0));
					
//		createCollisionDetector();
	}	

	GeometryArray particles;

	public BranchGroup createAvatar() {
				
		BranchGroup objBall = new BranchGroup();
		
//		objBall.addChild(new Sphere(0.2f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, createDBAppear()));
	
//		LinearFog fog = new LinearFog(0.1f, 0f, 0f);
//		fog.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
//		objBall.addChild(fog);

		PointSound sound = new PointSound(new MediaContainer("file:roar.au"), 1.0f, 0.0f, 0.0f, 0.0f);
		//PointSound sound = new PointSound();
		
		sound.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
		sound.setBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
		sound.setContinuousEnable(false);
		
		sound.setCapability(PointSound.ALLOW_ENABLE_WRITE);
//        sound.setCapability(PointSound.ALLOW_INITIAL_GAIN_WRITE);
//        sound.setCapability(PointSound.ALLOW_SOUND_DATA_WRITE);
//        sound.setCapability(PointSound.ALLOW_SCHEDULING_BOUNDS_WRITE);
//        sound.setCapability(PointSound.ALLOW_CONT_PLAY_WRITE);
//        sound.setCapability(PointSound.ALLOW_RELEASE_WRITE);
//        sound.setCapability(PointSound.ALLOW_DURATION_READ);
//        sound.setCapability(PointSound.ALLOW_IS_PLAYING_READ);
//        sound.setCapability(PointSound.ALLOW_LOOP_WRITE);
		
		sound.setLoop(Sound.INFINITE_LOOPS);
		sound.setEnable(true);	
// sound disabled				
//		objBall.addChild(sound);
		
//	create particles flying around	
		Appearance partsAppear = new Appearance();
//		partsAppear.setPointAttributes(new PointAttributes(3.0f, true));
//		partsAppear.setLineAttributes(new LineAttributes(1.0f, LineAttributes.PATTERN_SOLID, true));
		partsAppear.setColoringAttributes(new ColoringAttributes(0.6f, 0.8f, 1.0f, ColoringAttributes.NICEST));
//		partsAppear.setMaterial(new Material(new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0.0f, 0.3f, 0.6f), new Color3f(0.1f, 0.5f, 1.0f), 1.0f));
//		partsAppear.setMaterial(new Material());
		
		particles = ParticlesUpdater.createParticles(90);
		
		objBall.addChild(new Shape3D(particles, partsAppear));
		
		return objBall;
	}

	public Appearance createDBAppear () {
		Appearance dba = new Appearance();
		
		TextureLoader loader = new TextureLoader("darkball.jpg", null);
    	ImageComponent2D image = loader.getImage();

    	if(image == null) {
            System.out.println("load failed for texture darkball.jpg");
    	}
    	Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());
    	texture.setImage(0, image);
    	dba.setTexture(texture);
		
		TextureAttributes ta = new TextureAttributes();
		ta.setTextureMode(TextureAttributes.MODULATE);
		dba.setTextureAttributes(ta);		
		dba.setTexCoordGeneration(new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR, TexCoordGeneration.TEXTURE_COORDINATE_2));
		
		return dba;
	}
/*	
	public void step () {
//		this.pos.add(this.vel);
				
				double floorZ; 
		if ((floorZ = location.getZatXY(pos.x, pos.y)+0.2)>pos.z) {
			pos.z = floorZ;
			vel.z=0.2;
		} else vel.z-=0.01;
		
//		vel.x*=0.8;
//		vel.y*=0.8;

//		System.out.println("step");
	}
*/

	
//  abstract methods from entity
	
	GeometryUpdater particlesUpdater = new ParticlesUpdater();
	
	public void doEveryFrame() {
		if (this.hitpoints>0) {
			particles.updateData(particlesUpdater);
		
//			step();
		} 
//		if (this.hitpoints<100) hitpoints++;			
	}
/*	
	public void collisionDetected () {
		System.out.println("DarkBall"+location.time);	
		
		vel.x*=-1.0;
		vel.y*=-1.0;
		step();		
		
	}
*/
}
