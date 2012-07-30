package kotuc.chaos;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.picking.PickObject;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.universe.ConfiguredUniverse;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.File;
import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import kotuc.chaos.flowers.Plant;

public class ChaosFrame /* extends JFrame */ {

    protected final Location location;
    private BranchGroup sceneRoot;
    private Canvas3D canvas3D;
//    Canvas3D canvas3D2;
    private ConfiguredUniverse universe;
    private PickObject pickObject;
    private TransformGroup objAxis;
    private BranchGroup behaviorRoot = new BranchGroup();

    private BranchGroup createSceneGraph() {
        // Create the root of the branch graph

        BranchGroup objRoot = new BranchGroup();

        objAxis = new TransformGroup();
        objAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objAxis.addChild(new Axis());
        objRoot.addChild(objAxis);


//	objRoot.addChild(createBackground());


        objRoot.addChild(location.getMainGroup());

//    pickObject = new PickObject(canvas3D, location.objLocation);

//  ViewActualizator va = new ViewActualizator();
//	PhysicEntity.addBehavior(va);
//    va.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
//    objRoot.addChild(va);

//	public void configureViewBranch () {
        TransformGroup vpTrans = universe.getViewingPlatform().getViewPlatformTransform();
        location.setViewTransformGroup(vpTrans);

        universe.getViewer().getPhysicalBody().setLeftEyePosition(new Point3d(0.3, 0, -1));
        universe.getViewer().getPhysicalBody().setRightEyePosition(new Point3d(0.5, 0, -1));

        Transform3D t3d = new Transform3D();              
        t3d.setTranslation(new Vector3f(3.0f, 3.0f, 5.0f));
        vpTrans.setTransform(t3d);

        

        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(vpTrans);
        keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
        objRoot.addChild(keyNavBeh);
//		scene.addChild(new MouseRotate(vpTrans));
//		scene.addChild(new MouseZoom(vpTrans));
//		scene.addChild(new MouseTranslate(vpTrans));

//		ViewPlatformAWTBehavior awtBehavior = new ViewPlatformAWTBehavior(ViewPlatformAWTBehavior.KEY_LISTENER);
//		awtBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
//		objRoot.addChild(awtBehavior);

        universe.getViewer().getView().setLocalEyeLightingEnable(true);

        
//        AudioDevice audioDev = universe.getViewer().createAudioDevice();

        ViewingPlatform vp = universe.getViewingPlatform();
        vp.setPlatformGeometry(createPlatformGeometry());

//	}


//    objRoot.compile();

        return objRoot;
    } // end of CreateSceneGraph method of HelloJava3Db
    private PlatformGeometry platformGeometry;

    private PlatformGeometry createPlatformGeometry() {
        platformGeometry = new PlatformGeometry();

        // This TransformGroup is used to place the cylinder in the scene.
        // The cylinder will be rotated 90 degrees so it will appear as
        // a circle on the screen (could be made into a nice gun site...).
        // The cylinder is also displaced a little in Z so it is in front
        // of the viewer.
        Transform3D xForm = new Transform3D();
        xForm.rotX(Math.PI / 2.0);
        xForm.setTranslation(new Vector3d(0.0, 0.0, -0.7));
        TransformGroup placementTG = new TransformGroup(xForm);
        platformGeometry.addChild(placementTG);

        // Create the cylinder - make it thin and transparent.
        Appearance cylinderAppearance = new Appearance();
        TransparencyAttributes transAttrs =
                new TransparencyAttributes(TransparencyAttributes.NICEST | TransparencyAttributes.SCREEN_DOOR, 0.9f);
        //        cylinderAppearance.setTransparencyAttributes(transAttrs);
        Cylinder aimer = new Cylinder(0.01f, 0.005f, 0, cylinderAppearance);
        placementTG.addChild(aimer);


        return platformGeometry;
    }
    JFrame overviewFrame;

    private void showOverview() {
        overviewFrame = new JFrame("Overview");
        overviewFrame.getContentPane().add(new JTable(10, 10));

        JTextField jtf = new JTextField("Plant");
        jtf.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String className = ((JTextField) e.getSource()).getText();
                System.out.println("new " + className);
                try {
                    Class.forName("cz.kotuc.chaos." + className).newInstance();
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
        overviewFrame.getContentPane().add(new JList(location.getEntities().toArray()));


//		overviewFrame.pack();	
        overviewFrame.setSize(300, 300);
        overviewFrame.setLocation(50, 50);
        overviewFrame.setVisible(true);
    }

    // Create a simple scene and attach it to the virtual universe
    protected ChaosFrame() {
        this.location = new Location();
//        super("ChaosFrame");

//        setLayout(new BorderLayout());

        if (false) {
            GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            GraphicsConfiguration config2 = SimpleUniverse.getPreferredConfiguration();
            System.out.println("config1: " + config);
            System.out.println("config2: " + config2);
            canvas3D = new Canvas3D(config2, true);
//            add(canvas3D);
            universe = new ConfiguredUniverse(canvas3D);
        } else {
            universe = new ConfiguredUniverse(getClass().getResource("/data/viewsetup.txt"));
            canvas3D = universe.getCanvas();
        }


        System.out.println("canvas3D: " + canvas3D);
        canvas3D.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
//					System.out.println("EVENT: "+e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_O:
                        showOverview();
                        break;
                    case KeyEvent.VK_P:
                        location.addEntity(new Plant());
                        break;
                    case KeyEvent.VK_X:
                        location.addEntity(new Explosion(new Color3f(Color.getHSBColor((float) Math.random(), 1, 1))));
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
            }
        });

//        Iterator i = PhysicEntity.entities.iterator();
//        while (i.hasNext()) {
//            try {
//                ((Player) i.next()).setCanvas(c);
//            } catch (ClassCastException e) {
//            }
//        }
//        ;



//        configureViewBranch();

        sceneRoot = createSceneGraph();

        BranchGroup bg = new BranchGroup();

        bg.addChild(sceneRoot);
        behaviorRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        bg.addChild(behaviorRoot);


        universe.addBranchGraph(bg);



    } // end of HelloJava3Db (constructor)
    //  The following allows this to be run as an application
    //  as well as an applet

    private void exportImage() {
        try {
            canvas3D.setOffScreenBuffer(new ImageComponent2D(ImageComponent2D.FORMAT_RGB, 320, 240));
//            canvas3D.setSize(320, 240);
            canvas3D.getScreen3D().setSize(320, 240);
            canvas3D.getScreen3D().setPhysicalScreenHeight(0.5);
            canvas3D.getScreen3D().setPhysicalScreenWidth(0.5);

            System.out.println("screen3D: " + canvas3D.getScreen3D());
            canvas3D.renderOffScreenBuffer();
            canvas3D.waitForOffScreenRendering();
            ImageComponent2D offScreenBuffer = canvas3D.getOffScreenBuffer();
            System.out.println("buffer: " + offScreenBuffer);
            ImageIO.write(offScreenBuffer.getImage(), "png", new File("obrazek.png"));
            System.out.println("Obrazek out");
        } catch (IOException ex) {
            Logger.getLogger(ChaosFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

//		LocationLoader loader = new LocationLoader(location);
//		loader.load("/data/location1.xml");

//        for (int i = 0; i < args.length; i++) {
//            try {
//                Class.forName("kotuc.chaos." + args[i]).newInstance();
//                System.out.println("new Entity");
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }

//        Frame frame = new MainFrame(new ChaosFrame(), 256, 256);
//        frame.setVisible(false);
        ChaosFrame frame = new ChaosFrame();
//        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
//        frame.setSize(640, 480);
//        frame.setVisible(true);
        System.out.println("Visible");
//        frame.exportImage();
//        frame.exportImage();
//        frame.exportImage();



    } // end of main (method of HelloJava3Db)
} // end of class ChaosFrame
