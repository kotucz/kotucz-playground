package cz.kotuc.chaos;

import java.awt.event.*;
import java.applet.*;
import java.awt.*;
import javax.swing.*;

import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.picking.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.behaviors.vp.*;

import com.sun.j3d.utils.image.*;

import java.util.*;
import java.io.*;



public class ChaosFrame extends Applet {
    
    static Location location = null;
    
        
    public BranchGroup scene;
    
    Canvas3D canvas3D;
    Canvas3D canvas3D2;
            
    ConfiguredUniverse su;
    
   	PickObject pickObject;
    
    TransformGroup objAxis;
    
    public static BranchGroup behaviorRoot = new BranchGroup();
	
	public BranchGroup createSceneGraph() {
	// Create the root of the branch graph
	
	BranchGroup objRoot = new BranchGroup();

	objAxis = new TransformGroup();
	objAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objAxis.addChild(new Axis());
	objRoot.addChild(objAxis);
	


		
//	objRoot.addChild(createBackground());
	
	
	objRoot.addChild(location.objLocation);	    
    
//    pickObject = new PickObject(canvas3D, location.objLocation);
    
//  ViewActualizator va = new ViewActualizator();
//	Entity.addBehavior(va);
//    va.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
//    objRoot.addChild(va);

//	public void configureViewBranch () {
		TransformGroup vpTrans = su.getViewingPlatform().getViewPlatformTransform();
        Vector3f translate = new Vector3f();
    	Transform3D T3D = new Transform3D();
    	TransformGroup TG = null;
		
    	translate.set(10.0f, 10.0f, 50.0f);
    	T3D.setTranslation(translate);
    	vpTrans.setTransform(T3D);
    	
    	KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(vpTrans);
		keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
		objRoot.addChild(keyNavBeh);
//		scene.addChild(new MouseRotate(vpTrans));
//		scene.addChild(new MouseZoom(vpTrans));
//		scene.addChild(new MouseTranslate(vpTrans));
		
//		ViewPlatformAWTBehavior awtBehavior = new ViewPlatformAWTBehavior(ViewPlatformAWTBehavior.KEY_LISTENER);
//		awtBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
//		objRoot.addChild(awtBehavior);
		
		su.getViewer().getView().setLocalEyeLightingEnable(true);
	
		AudioDevice audioDev = su.getViewer().createAudioDevice();
	
		ViewingPlatform vp = su.getViewingPlatform();
		vp.setPlatformGeometry(createPlatformGeometry());
		
//	}
	
    
//    objRoot.compile();

		return objRoot;
    } // end of CreateSceneGraph method of HelloJava3Db


	PlatformGeometry platformGeometry;
	
	public PlatformGeometry createPlatformGeometry () {
		platformGeometry = new PlatformGeometry();
		
		        // This TransformGroup is used to place the cylinder in the scene.
        // The cylinder will be rotated 90 degrees so it will appear as
        // a circle on the screen (could be made into a nice gun site...).
        // The cylinder is also displaced a little in Z so it is in front
        // of the viewer.
        Transform3D xForm = new Transform3D();
        xForm.rotX(Math.PI/2.0);
        xForm.setTranslation(new Vector3d(0.0, 0.0, -0.7));
        TransformGroup placementTG = new TransformGroup(xForm);
        platformGeometry.addChild(placementTG);

        // Create the cylinder - make it thin and transparent.
        Appearance cylinderAppearance = new Appearance();
        TransparencyAttributes transAttrs =
           new TransparencyAttributes(TransparencyAttributes.NICEST|TransparencyAttributes.SCREEN_DOOR, 0.9f);
	//        cylinderAppearance.setTransparencyAttributes(transAttrs);
        Cylinder aimer = new Cylinder(0.01f, 0.005f, 0, cylinderAppearance);
        placementTG.addChild(aimer);

		
		return platformGeometry;
	}
	
	JFrame overviewFrame;
	
	
	public void showOverview() {
		overviewFrame = new JFrame("Overview");
		overviewFrame.getContentPane().add(new JTable(10, 10));
		
		JTextField jtf = new JTextField("Plant");
		jtf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String className = ((JTextField)e.getSource()).getText();
					System.out.println("new "+className);
					try {
						 Class.forName("cz.kotuc.chaos."+className).newInstance();
						 System.out.println("new Entity");	
					} catch (ClassNotFoundException ex) {
						ex.printStackTrace();
					} catch (InstantiationException ex) {
						ex.printStackTrace();
					} catch (IllegalAccessException ex) {
						ex.printStackTrace();
					} 
				}
			});
//		overviewFrame.getContentPane().add(jtf);
		overviewFrame.getContentPane().add(new JList(Entity.entities.toArray()));
		

//		overviewFrame.pack();	
		overviewFrame.setSize(300, 300);	
		overviewFrame.setLocation(50, 50);
		overviewFrame.setVisible(true);
	}
	
	

    // Create a simple scene and attach it to the virtual universe

    public ChaosFrame() {
        setLayout(new BorderLayout());
/*		GraphicsConfiguration config =
			SimpleUniverse.getPreferredConfiguration();
*/
        canvas3D = new Canvas3D(null);
        
        add("Center", canvas3D);
		
		if (Player.canvases.isEmpty()) {
		   	try {
	    		su = new ConfiguredUniverse(new java.net.URL("file:data/viewsetup.txt"));
			} catch (java.net.MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			canvas3D=((Canvas3D)Player.canvases.get(0));
			su = new ConfiguredUniverse(canvas3D);
//			su = new ConfiguredUniverse((Canvas3D[])Player.canvases.toArray(new Canvas3D[Player.canvases.size()]));
		}
		
		Canvas3D c = su.getCanvas();
		System.out.println(c);
		c.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
//					System.out.println("EVENT: "+e);
					switch (e.getKeyCode()) {
					case KeyEvent.VK_O :
						showOverview();
					break;
					case KeyEvent.VK_P :
						new Plant();
					break;
					case KeyEvent.VK_ESCAPE :
						System.exit(0); 
					break;
					}
				}
			});
		
		Iterator i = Entity.entities.iterator();
		while (i.hasNext()) {
			try {		
				((Player)i.next()).setCanvas(c);
			} catch (ClassCastException e) {
			}
		};
		
			
				
	    Entity.universe=su;
	    
//	    configureViewBranch();

		scene = createSceneGraph();
	
	    BranchGroup bg = new BranchGroup();
	    
	    bg.addChild(scene);
	    behaviorRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
	    bg.addChild(behaviorRoot);

	    	    
	    su.addBranchGraph(bg);

		
    } // end of HelloJava3Db (constructor)
    //  The following allows this to be run as an application
    //  as well as an applet

    public static void main(String[] args) {
		location = new Location();
//		LocationLoader loader = new LocationLoader(location);
//		loader.load("data/location1.xml");

		for (int i = 0; i<args.length; i++) {
			try {
				 Class.forName("cz.kotuc.chaos."+args[i]).newInstance();
				 System.out.println("new Entity");	
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} 
		};	
		Frame frame = new MainFrame(new ChaosFrame(), 256, 256);
		frame.setVisible(false);
    } // end of main (method of HelloJava3Db)

} // end of class ChaosFrame
