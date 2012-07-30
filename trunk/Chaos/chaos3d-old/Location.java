package cz.kotuc.chaos;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.image.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import java.util.*;

import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

public class Location {
	final float EDGE = 1.0f;
	
	int widthx=20, widthy=20;
	
	public int[][][] phaseMap = new int[widthx][widthy][1];
	
	public static final int AIR = 1;
	public static final int GAS = AIR;
	
	public static final int WATER = 2;
	public static final int LIQUID = WATER;
	
	public static final int SOLID = 4;
	
	public static long time = 0;
	
	BranchGroup objLocation = null;
	
	BranchGroup objLand = null;
	
	BranchGroup objEntities = null;
	
	Point3d[][] verts = null;	
	
    DirectionalLight sunshine = null;
    
    TransformGroup objBgRot = null;

	
//	Player player = null;
	
//	Vector entities = new Vector();
	
/*	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}
*/	
/*	Vector obstacles = new Vector();
	
	public void addObstacle(Obstacle obstacle) {
		this.obstacles.add(obstacle);
	}
*/			
	public Location() {
		objLocation = new BranchGroup();
		objLand = new BranchGroup();
		objLand.setPickable(false);
		objLocation.addChild(objLand);
		
		objLocation.addChild(Arrow.getDefaultRoot());
		objLocation.addChild(Explosion.getDefaultRoot());
				
		objEntities = new BranchGroup();
		objEntities.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objEntities.setCapability(Group.ALLOW_CHILDREN_WRITE);
		objLocation.addChild(objEntities);
//		initPhaseMap();
//		objLand.addChild(objPhaseMap);
		initLandVerts();
//		new KotucCommander();
//		new PetrCommander();
		createLand();	
		new LocationActualizator(this);	
		
		Entity.setLocation(this);
		
//		new Player();
//		new Player();
//		new Soldier3D();
//		new Soldier3D();
//		new Animal();
//		new Plant();	
	}
	
	public void createTree (double x, double y) {
		Transform3D t3D = new Transform3D();
		t3D.set(new Vector3f((float)x, (float)y, 2));
		Transform3D t3Dr = new Transform3D();
		t3Dr.rotX(0.5*Math.PI);
		t3D.mul(t3Dr); 
		TransformGroup objTree = new TransformGroup(t3D);
		
		objTree.addChild(new Cylinder(0.1f, 4.0f));
		objLand.addChild(objTree);		
	}
	
	public void createHouse(double x, double y, double rotZ) {
		
		ObjectFile f = new ObjectFile();
		Scene s = null;
				
		try {
			s = f.load("models/house1.obj");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
 		
 		Transform3D tHouseSet = new Transform3D();
		tHouseSet.set(new Vector3d(x, y, 0.0));
				
		Transform3D tHouseRot = new Transform3D();
		tHouseRot.rotZ(rotZ);
		
		tHouseSet.mul(tHouseRot);
						
		TransformGroup objHouse = new TransformGroup(tHouseSet);
		objHouse.addChild(s.getSceneGroup());
 		
		objLand.addChild(objHouse);
	}

	public void createGalleon(double x, double y, double rotZ) {
		
		ObjectFile f = new ObjectFile();
		Scene s = null;
				
		try {
			s = f.load("models/galleon.obj");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
 		
 		Transform3D tGalleonSet = new Transform3D();
		tGalleonSet.set(new Vector3d(x, y, 0.0));
				
		Transform3D tGalleonRot = new Transform3D();
		tGalleonRot.rotZ(rotZ);
		
		tGalleonSet.mul(tGalleonRot);
						
		TransformGroup objGalleon = new TransformGroup(tGalleonSet);
		objGalleon.addChild(s.getSceneGroup());
 		
		objLand.addChild(objGalleon);
	}



	public void createGrass(double x, double y) {
		
		Transform3D tGrassSet = new Transform3D();
		tGrassSet.set(new Vector3d(x, y, 0));
		
		TexturedPlane texPlane = new TexturedPlane("images/grass.jpg");
				
		TransformGroup objGrass = new TransformGroup(tGrassSet);
 		//objGrass.addChild(new OrientedShape3D(gGeom, gAppear, OrientedShape3D.ROTATE_ABOUT_POINT, new Vector3f(0.0f, 0.0f, 1.0f));
 		objGrass.addChild(new OrientedShape3D(texPlane.createGeometry(), texPlane.createAppearance("images/grass.jpg"), OrientedShape3D.ROTATE_ABOUT_POINT, new Vector3f(0.0f, 0.0f, 1.0f)));
 		
 		
		objLand.addChild(objGrass);
	}

	
/*
	public double getZatXY (double x, double y) {
		int ix, iy;
		
		double z;		
				
		if ((x>0)&&(x<widthx*EDGE)&&(y>0)&&(y<widthy*EDGE)) {
			ix = (int)Math.floor(x);	//EDGE == 1
			iy = (int)Math.floor(y);
			
			z = Math.max(Math.max(verts[ix][iy].z, verts[ix+1][iy].z),
			    		 Math.max(verts[ix+1][iy+1].z, verts[ix][iy+1].z));
			
		
			return z+0.2;
		}
		return 0.2;
	}
*/	
/**********************
 *	
 *	creating  Land
 *
 **********************/
	GeometryArray landGeometry;

    public void createLand(){

//   		objLand.addChild(new Shape3D(createLandGeometry(), new Appearance()));
/*    	Node objFloor = new Shape3D(createLandGeometry(), createLandAppearance());
//    	objFloor.setCollidable(false);
    	objLand.addChild(objFloor);
*/


        objLand.addChild(createBox());

//		objLand.addChild(createEarth());

		createLights();
		
		objLocation.addChild(createBackground());
        
//        createTree(5.0, 6.0);
        
//        createTree(11.0, 6.0);
        
//        createHouse(10.0, 10.0, 0.0);
        
//        new DarkBall(this);
//        createGrass(2.0, 2.0);
    
//    	createGalleon(20.0, 20.0, 0.0);
    
    }

	public TransformGroup createBox () {
	    Appearance boxAppear = new Appearance();
        TextureLoader tex = new TextureLoader("images/metal068.jpg", null);
//        if (tex != null) 
//	    	boxAppear.setTexture(tex.getTexture());
	    boxAppear.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_FRONT, 0.0f));
        boxAppear.setMaterial(new Material());
        
        Box objBox = new Box(10.0f, 10.0f, 10.0f, Box.GENERATE_NORMALS | Box.GENERATE_NORMALS_INWARD | Box.GENERATE_TEXTURE_COORDS, boxAppear, 256);
                
        Transform3D ts = new Transform3D();
        ts.set(new Vector3f(10.0f, 10.0f, 10.0f));
        TransformGroup tg = new TransformGroup(ts);
        
        tg.addChild(objBox);
        
        return tg;
    }

	public void initLandVerts () {
		verts = new Point3d[widthx+1][widthy+1];
		for (int y=0; y<this.widthy+1; y++) {
			for (int x=0; x<this.widthx+1; x++) {
				verts[x][y] = new Point3d(
					(double)(x*EDGE),
					(double)(y*EDGE),
					(double)(Math.random()*EDGE/3.0)					
				);
			}
		}
	}
	
	public void initLandVerts (double random) {
		verts = new Point3d[widthx+1][widthy+1];
		for (int y=0; y<this.widthy+1; y++) {
			for (int x=0; x<this.widthx+1; x++) {
				verts[x][y] = new Point3d(
					(double)(x*EDGE),
					(double)(y*EDGE),
					(double)(Math.random()*random)					
				);
			}
		}
	}


	public GeometryArray createLandGeometry () {
		int N = (widthx+1)*(widthy+1)*4;
		
		landGeometry = new QuadArray(N, 
        									GeometryArray.COORDINATES
                                            | GeometryArray.COLOR_3 
//                                            | GeometryArray.NORMALS
                                            | GeometryArray.BY_REFERENCE
//                                            | GeometryArray.TEXTURE_COORDINATE_2
                                            );
        
        int i = 0;     
        
        float[] coords = new float[N*3];
                
        for (int y=0; y<this.widthy; y++) {
			for (int x=0; x<this.widthx; x++) {
//				Point3f v = new Point3f(verts[x][y]);
				coords[i*3] = (float)x;
				coords[i*3+1] = (float)y;
				coords[i*3+2] = 0.0f;
				i++;
//				v = new Point3f(verts[x+1][y]);
				coords[i*3] = (float)x+1.0f;
				coords[i*3+1] = (float)y+0.0f;
				coords[i*3+2] = 0.0f;
				i++;
//				v = new Point3f(verts[x+1][y+1]);
				coords[i*3] = (float)x+1.0f;
				coords[i*3+1] = (float)y+1.0f;
				coords[i*3+2] = 0.0f;
				i++;
//				v = new Point3f(verts[x][y+1]);
				coords[i*3] = (float)x+0.0f;
				coords[i*3+1] = (float)y+1.0f;
				coords[i*3+2] = 0.0f;
				i++;
			};
		};
        
        landGeometry.setCoordRefFloat(coords);
        landGeometry.setColorRefFloat(new float[N*3]);

//      landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
//		landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
//		landGeometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
//      landGeometry.setCapability(GeometryArray.BY_REFERENCE);

//		GeometryInfo gi = new GeometryInfo(landGeometry);                
                
//        NormalGenerator ng = new NormalGenerator();
//        ng.generateNormals(gi);
      
//        landGeometry = gi.getGeometryArray();
        
        landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
		landGeometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
// 	     landGeometry.setCapability(GeometryArray.BY_REFERENCE);

        return landGeometry;
	}

	public Appearance createLandAppearance () {
	
        Appearance landAppearance = new Appearance();
        
        Material ma = new Material();
        
        ma.setAmbientColor(new Color3f(0.32f, 0.32f, 0.32f));
        ma.setSpecularColor(new Color3f(0.2f, 0.2f, 0.2f));
        ma.setEmissiveColor(new Color3f(0.41f, 0.41f, 0.41f));
        ma.setDiffuseColor(new Color3f(1.0f, 1.0f, 1.0f));
        ma.setShininess(1.0f);
        
        landAppearance.setMaterial(ma);
        
//  landTexturing
        
//    	NewTextureLoader loader = new NewTextureLoader("grass.jpg");
// 	  	ImageComponent2D image = loader.getImage();
//		if (image==null) System.out.println("no image");
//		Texture2D texture = new Texture2D();
//     	texture.setImage(0, image);
//   	landAppearance.setTexture(texture);
		
		TextureLoader loader = new TextureLoader("images/grass.jpg", null);
    	ImageComponent2D image = loader.getImage();

    	if(image == null) {
            System.out.println("load failed for texture grass.jpg");
    	}
    	Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());
    	texture.setImage(0, image);
    	landAppearance.setTexture(texture);
		
		TextureAttributes ta = new TextureAttributes();
		ta.setTextureMode(TextureAttributes.MODULATE);
		landAppearance.setTextureAttributes(ta);		
		landAppearance.setTexCoordGeneration(new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR, TexCoordGeneration.TEXTURE_COORDINATE_2));	
	
	
//		return new Appearance();
			
		return landAppearance;	
	}
	
	GeometryUpdater waterUpdater = new WaterUpdater();
	

/*	
	void initPhaseMap() {
		phaseMap=new int[widthx][widthy][];
		for (int x=0; x<widthx; x++) {
			for (int y=0; y<widthy; y++) {
				if (true) {
					phaseMap[x][y]=new int[1];	
					phaseMap[x][y][0]=SOLID;
				} else {
					phaseMap[x][y]=new int[5];
					phaseMap[x][y][0]=SOLID;
				}
			}
		}
		
		createPhaseMap();
		
	}
*/
/*	
	BranchGroup objPhaseMap;
	
	public BranchGroup createPhaseMap () {
					
		objPhaseMap = new BranchGroup();
					
		for (int x=0; x<phaseMap.length; x++) {
			for (int y=0; y<phaseMap[x].length; y++) {
				for (int z=0; z<phaseMap[x][y].length; z++) {
					
					Appearance phaseAppear = new Appearance(); 
					phaseAppear.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE, 0.0f));						
					
					switch (phaseMap[x][y][z]) {
					case SOLID :
						phaseAppear.setColoringAttributes(new ColoringAttributes(0.0f, 1.0f, 0.0f, ColoringAttributes.FASTEST));	
					break;
					case WATER :
						phaseAppear.setColoringAttributes(new ColoringAttributes(0.0f, 0.0f, 1.0f, ColoringAttributes.FASTEST));
					break;
					case AIR : 
						phaseAppear.setColoringAttributes(new ColoringAttributes(1.0f, 1.0f, 1.0f, ColoringAttributes.FASTEST));
					break;
					default :
					break;
					}
					
					Transform3D t3dSet = new Transform3D();
					t3dSet.set(new Vector3f((float)x+0.5f, (float)y+0.5f, (float)z-0.5f));						
					TransformGroup objTrans = new TransformGroup(t3dSet);
											
					objTrans.addChild(new Box(0.49f, 0.49f, 0.49f, phaseAppear));
					objPhaseMap.addChild(objTrans);
				}
			}	
		}

		objLand.addChild(objPhaseMap);

		return objPhaseMap;
	}
	
	int getPhase (Point3d point) {
		int x= (int)Math.floor(point.x);
		int y= (int)Math.floor(point.y);
		int z= (int)Math.floor(point.z)-1;
				
		if ((x<phaseMap.length)&&x>=0&&(y<phaseMap[x].length)&&y>=0&&(z<phaseMap[x][y].length)&&z>=0)
			return phaseMap[x][y][z];
		return AIR;
	}
*/

	
/**
 *	creating Background
 */
	public Background createBackground () {
		BranchGroup objBg = new BranchGroup();
		
// background		
		Background bg = new Background();
		
		bg.setApplicationBounds(new BoundingSphere(new Point3d(),1000.0));
		Sphere sphereObj = new Sphere(1.0f, Sphere.GENERATE_NORMALS |
			          Sphere.GENERATE_NORMALS_INWARD |
				  Sphere.GENERATE_TEXTURE_COORDS, 45);
        Appearance backgroundApp = sphereObj.getAppearance();
                
        TextureLoader tex = new TextureLoader("images/bg.jpg", null);
        if (tex != null) 
	    	backgroundApp.setTexture(tex.getTexture());
			
//	background rotation showing time
		 				
		Transform3D tBgRot = new Transform3D();
		objBgRot = new TransformGroup(tBgRot);
		objBgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objBgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        
        bg.setGeometry(objBg);
        objBg.addChild(objBgRot);
        objBgRot.addChild(sphereObj);
	
		return bg;
	} // end of createBackground method

/**
 *	creating Earth with gravitation center
 */
	public TransformGroup createEarth () {
		System.out.print("creating earth .. ");
		
		double eRadius = 100.0;
		
		Sphere sphereObj = new Sphere((float)eRadius, Sphere.GENERATE_NORMALS |
				  Sphere.GENERATE_TEXTURE_COORDS, 200);
        Appearance earthAppear = sphereObj.getAppearance();
        earthAppear.setMaterial(new Material());
                
        TextureLoader tex = new TextureLoader("images/earth.jpg", null);
        if (tex != null) 
	    	earthAppear.setTexture(tex.getTexture());
		
		
		Physics.gravityCenter = new Point3d(0.0, 0.0, -eRadius);
		
		Physics earth = new Physics();
		earth.setBounds(new BoundingSphere(new Point3d(), eRadius));
		earth.setPos(Physics.gravityCenter);
						 				
//		Transform3D tBgRot = new Transform3D();
//		obj = new TransformGroup(tBgRot);
//		objBgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//		objBgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        
//      bg.setGeometry(objBg);
//      objBg.addChild(objBgRot);

		Transform3D t3d = new Transform3D();
		t3d.set(new Vector3f(Physics.gravityCenter));
		TransformGroup objEarth = new TransformGroup(t3d);
		
		objEarth.addChild(sphereObj);
		
		System.out.println("created");	
		
		return objEarth;
	} // end of createBackground method


	public void createLights () {
		sunshine = new DirectionalLight(
			new Color3f(1.0f, 0.93f, 0.87f), 
			new Vector3f(0, 0, -1)
		);
		
		sunshine.setCapability(DirectionalLight.ALLOW_DIRECTION_WRITE);
		sunshine.setCapability(DirectionalLight.ALLOW_DIRECTION_READ);
		sunshine.setInfluencingBounds(new BoundingSphere(new Point3d(),1000.0));		

		objLocation.addChild(sunshine);
	
		AmbientLight ambientLight = new AmbientLight(new Color3f(0.35f, 0.35f, 0.62f));
		ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(),1000.0));		
		objLocation.addChild(ambientLight);

	}

}
