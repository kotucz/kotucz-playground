package tradeworld.graphics;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.picking.PickObject;
import com.sun.j3d.utils.universe.ConfiguredUniverse;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.File;
import javax.imageio.ImageIO;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Light;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

public class ChaosFrame {

    private BranchGroup sceneRoot;
    private Canvas3D canvas3D;
    private ConfiguredUniverse universe;
    private PickObject pickObject;
    private TransformGroup vpTrans;

    private BranchGroup createSceneGraph() {
        // Create the root of the branch graph

        BranchGroup sRoot = new BranchGroup();

        sRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        sRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
//        sRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
//        sRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);


//        objAxis = new TransformGroup();
//        objAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        objAxis.addChild(new Axis());
//        objRoot.addChild(objAxis);


//	objRoot.addChild(createBackground());


//        objRoot.addChild(location.getMainGroup());

//    pickObject = new PickObject(canvas3D, location.objLocation);

//  ViewActualizator va = new ViewActualizator();
//	PhysicEntity.addBehavior(va);
//    va.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
//    objRoot.addChild(va);

//	public void configureViewBranch () {
        this.vpTrans = universe.getViewingPlatform().getViewPlatformTransform();

//        universe.getViewer().getPhysicalBody().setLeftEyePosition(new Point3d(0.3, 0, -1));
//        universe.getViewer().getPhysicalBody().setRightEyePosition(new Point3d(0.5, 0, -1));




        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(vpTrans);
        keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
        sRoot.addChild(keyNavBeh);
//		scene.addChild(new MouseRotate(vpTrans));
//		scene.addChild(new MouseZoom(vpTrans));
//		scene.addChild(new MouseTranslate(vpTrans));

//		ViewPlatformAWTBehavior awtBehavior = new ViewPlatformAWTBehavior(ViewPlatformAWTBehavior.KEY_LISTENER);
//		awtBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
//		objRoot.addChild(awtBehavior);

        universe.getViewer().getView().setLocalEyeLightingEnable(true);


//        AudioDevice audioDev = universe.getViewer().createAudioDevice();

//        ViewingPlatform vp = universe.getViewingPlatform();
//        vp.setPlatformGeometry(createPlatformGeometry());

//	}





//    objRoot.compile();

        return sRoot;

    } // end of CreateSceneGraph method of HelloJava3Db

    void createLights() {
        BranchGroup lights = new BranchGroup();
        Light sunshine = new DirectionalLight(
                new Color3f(1.0f, 0.93f, 0.87f),
                new Vector3f(-1, -1, -1));

//            sunshine.setCapability(DirectionalLight.ALLOW_DIRECTION_WRITE);
//            sunshine.setCapability(DirectionalLight.ALLOW_DIRECTION_READ);
//            sunshine.setScope(sRoot, 0);
        sunshine.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000000.0));

        lights.addChild(sunshine);

        AmbientLight ambientLight = new AmbientLight(new Color3f(0.35f, 0.35f, 0.62f));
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000000.0));
//        ambientLight.setScope((Group)getMainGroup(), 0);
        lights.addChild(ambientLight);

        this.add(lights);

    }

    public void setEye(Vector3f vector) {
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(vector);
        vpTrans.setTransform(t3d);
    }

    // Create a simple scene and attach it to the virtual universe
    protected ChaosFrame() {

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
//                    case KeyEvent.VK_O:
//                        showOverview();
//                        break;
//                    case KeyEvent.VK_P:
//                        location.addEntity(new Plant());
//                        break;
//                    case KeyEvent.VK_X:
//                        location.addEntity(new Explosion(new Color3f(Color.getHSBColor((float) Math.random(), 1, 1))));
//                        break;
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

        universe.addBranchGraph(sceneRoot);


        this.add(new Land().getObjLand());

        createLights();

        setEye(new Vector3f(0, 0, 5));


    } // end of HelloJava3Db (constructor)
    //  The following allows this to be run as an application
    //  as well as an applet

    public void add(Group group) {
        this.sceneRoot.addChild(group);
    }

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

//        JFrame frame = new JFrame();
        ChaosFrame frame = new ChaosFrame();
//        frame.add(new ChaosFrame().canvas3D);
//        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
//        frame.setSize(640, 480);
//        frame.setVisible(true);
//        System.out.println("Visible");
//        frame.exportImage();
//        frame.exportImage();
//        frame.exportImage();



    } // end of main (method of HelloJava3Db)
} // end of class ChaosFrame

