package cz.kotuc.chaos;

import javax.vecmath.*;

import javax.media.j3d.*;

import java.util.*;


/**
 *	before using, getDefaultRoot() value must be added to scene graph so explosions could display
 */
public class Explosion extends BranchGroup implements Runnable {
	
	/**
	 *	current possition in 3D space
	 *	x, y, z
	 *	@unit m
	 */
	protected Point3d pos = new Point3d(Math.random()*20.0, Math.random()*20.0, /*Math.random()*2*/1.0);

	public void setPos(Point3d p) {
		this.pos = p;
		double[] c = ga.getCoordRefDouble();
		c[0]=pos.x;
		c[1]=pos.y;
		c[2]=pos.z;
	}
	
	public void setPos(Transform3D t, Point3d point) {
		Point3d p = new Point3d(point);
		t.transform(p);
		setPos(p);
	}
	
	public Point3d getPos() {
		return this.pos;
	}

	/**
	 *	group, in which arrows are added 
	 *	should have no transform to local world
	 */
	protected static Group objExplosions;
	
	/**
	 *	add this method return value to your scene graph, to allow Arrows be visible
	 */
	public static Group getDefaultRoot () {
		objExplosions = new BranchGroup();
		objExplosions.setCapability(ALLOW_CHILDREN_EXTEND); 
		objExplosions.setCapability(ALLOW_CHILDREN_WRITE);
		return objExplosions;
	}

	public static void setDefaultRoot (Group objRoot) {
		if (!objRoot.getCapability(ALLOW_CHILDREN_EXTEND)) {
			System.err.println("Invalid root group: "
				+ "ALLOW_CHILDREN_EXTEND capability not set");
			return;
		}
		objExplosions = objRoot;
	}

	private static List explosions = new LinkedList();
		
	static GeometryUpdater updater = new ExplosionUpdater();
		
	public Explosion() {
		super.setCapability(ALLOW_DETACH);
		super.addChild(this.getExplosionObject());
		new ExplosionBehavior(this);
		objExplosions.addChild(this);
		explosions.add(this);
		System.out.print(". new Explosion .");
		setTimeout(15000);
	}
	
	public Explosion(Color3f color) {
		this();
//		System.out.print(shape.getAppearance());
		shape.getAppearance().setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.FASTEST));
	}
	
	private long ms = 0;
	
	private void setTimeout (long ms) {
		this.ms = ms;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run () {
		try {
			Thread.sleep(ms);
			this.finalize();	
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	GeometryArray ga;
	private Shape3D shape;
	
	private Node getExplosionObject() {
/*		if (ga==null) {
			ga = new LineArray(2, GeometryArray.COORDINATES|GeometryArray.BY_REFERENCE);
			ga.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
			ga.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
			
			ga.setCoordRefDouble(new double[6]);
		}
*/
		ga = ExplosionUpdater.createParticles(50);
		this.shape = new Shape3D(ga, new Appearance());
		Appearance appear = this.shape.getAppearance();
		appear.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		appear.setPointAttributes(new PointAttributes(3f, true));
		
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		
		Transform3D t = new Transform3D();
		t.set(new Vector3f(this.pos));
		TransformGroup tg = new TransformGroup(t);
		tg.addChild(shape);		
		return tg;
	}
	
/*	public Object clone() {
		System.out.println("clone Physics");
		return this.clone();
	}	*/
	
	
	public void finalize() {
		objExplosions.removeChild(this);
		explosions.remove(this);
	}
}

class ExplosionUpdater implements GeometryUpdater {
	public void updateData (Geometry geometry) {
		GeometryArray geometryArray = (GeometryArray)geometry;
		float[] coords = geometryArray.getCoordRefFloat();
		
		int N = geometryArray.getValidVertexCount();
		
		for (int i = 0; i<N; i+=2) {
			 if (coords[i*3+2+3]>-1.0f) {
				float vx = coords[i*3+0+3]-coords[i*3+0];
				float vy = coords[i*3+1+3]-coords[i*3+1];
				float vz = coords[i*3+2+3]-coords[i*3+2];
								
				coords[i*3+0] += vx;
				coords[i*3+1] += vy;
				coords[i*3+2] += vz;
			
				coords[i*3+0+3] += vx;
				coords[i*3+1+3] += vy;
				coords[i*3+2+3] += vz-0.01f;
			}
		}
	} 
	
	/**	Creates GeometryArray, that will look like explosion
	 *	if ExplosionUpdater is used
	 *	
	 *	@param N the count of particles to be created
	 *	when N is too high, your graphic card may not be able to paint them
	 *	when N is too small, it wouldn´t look like an explosion
	 *
	 *	@return new GeometryArray
	 */
	public static GeometryArray createParticles (int N) {
		N*=3;

		GeometryArray particles = new LineArray(N*2, GeometryArray.COORDINATES|GeometryArray.BY_REFERENCE);
		
		particles.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		particles.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
		particles.setCapability(GeometryArray.ALLOW_COUNT_READ);
		
		float[] coordinates = new float[N*2*3];
		
		for (int i = 0; i<N; i+=2) {
			coordinates[i*3+0]=0f;		//	x
			coordinates[i*3+1]=0f;		//	y
			coordinates[i*3+2]=0f;		//	z
			
			coordinates[i*3+0+3]=(float)Math.random()*0.2f-0.1f;		//	x
			coordinates[i*3+1+3]=(float)Math.random()*0.2f-0.1f;		//	y
			coordinates[i*3+2+3]=(float)Math.random()*0.2f-0.05f;		//	z
		}
		
		particles.setCoordRefFloat(coordinates);
		
		return particles;
	}
}
	
/**
 *	Entity v 0.01;
 *
 *	Tomas Kotula as Kotuc (=disc)
 */
class ExplosionBehavior extends Behavior {
	
	Explosion explosion = null;
	
	
	public ExplosionBehavior (Explosion e) {
		this.explosion=e;
		this.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
		e.addChild(this);		
	}	
	
	WakeupOnElapsedFrames w = new WakeupOnElapsedFrames(0);
							
	public void initialize () {
		wakeupOn(w);			
	}

	/**
	 *	doing actualizations of 3D view 
	 */
	
	public void processStimulus(java.util.Enumeration enum) {
		explosion.updater.updateData(explosion.ga);
		wakeupOn(w);					
	}
	
}
	