package tradeworld.graphics;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.universe.ConfiguredUniverse;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.File;
import javax.imageio.ImageIO;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.PickRay;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class ChaosFrame {

    private World3D world;
    private Canvas3D canvas3D;
    private ConfiguredUniverse universe;
    private TransformGroup vpTrans;

//    private BranchGroup createSceneGraph() {
//        // Create the root of the branch graph
//
//        BranchGroup sRoot = new BranchGroup();
//
//        sRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
//        sRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
//
////        sRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
////        sRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
//
//
////        objAxis = new TransformGroup();
////        objAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
////        objAxis.addChild(new Axis());
////        objRoot.addChild(objAxis);
//
//
////	objRoot.addChild(createBackground());
//
//
////        objRoot.addChild(location.getMainGroup());
//
////    pickObject = new PickObject(canvas3D, location.objLocation);
//
////  ViewActualizator va = new ViewActualizator();
////	PhysicEntity.addBehavior(va);
////    va.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
////    objRoot.addChild(va);
//
////	public void configureViewBranch () {
//        final ViewingPlatform viewingPlatform = universe.getViewingPlatform();
//
//        this.vpTrans = viewingPlatform.getViewPlatformTransform();
//
////        universe.getViewer().getPhysicalBody().setLeftEyePosition(new Point3d(0.3, 0, -1));
////        universe.getViewer().getPhysicalBody().setRightEyePosition(new Point3d(0.5, 0, -1));
//
//
//
//
//        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(vpTrans);
//        keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000000.0));
//        sRoot.addChild(keyNavBeh);
//
////		scene.addChild(new MouseRotate(vpTrans));
////		scene.addChild(new MouseZoom(vpTrans));
////		scene.addChild(new MouseTranslate(vpTrans));
//
////		ViewPlatformAWTBehavior awtBehavior = new ViewPlatformAWTBehavior(ViewPlatformAWTBehavior.KEY_LISTENER);
////		awtBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
////		objRoot.addChild(awtBehavior);
//
//        final View view = universe.getViewer().getView();
//        view.setLocalEyeLightingEnable(true);
//        view.setBackClipDistance(1000000);
//
//
////        AudioDevice audioDev = universe.getViewer().createAudioDevice();
//
////        ViewingPlatform vp = universe.getViewingPlatform();
////        vp.setPlatformGeometry(createPlatformGeometry());
//
////	}
//
//
//
//
//
////    objRoot.compile();
//
//        return sRoot;
//
//    } // end of CreateSceneGraph method of HelloJava3Db
    public void setEye(Vector3d vector) {
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(vector);
        vpTrans.setTransform(t3d);
    }

    public Vector3d getEye() {
        Transform3D t3d = new Transform3D();
        vpTrans.getTransform(t3d);
        Vector3d vector = new Vector3d();
        t3d.get(vector);
        return vector;
    }

    enum Actions {

        BUILD_FACTORY,
        SHOW_NOTIFICATION,
        BUILD_ROAD;
    }
    private Actions selectedAction = Actions.BUILD_ROAD;

    private void selectAction(Actions action) {
        this.selectedAction = action;
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
                    case KeyEvent.VK_R:
                        selectAction(Actions.BUILD_ROAD);
                        break;
                    case KeyEvent.VK_F:
                        selectAction(Actions.BUILD_FACTORY);
                        break;
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
//        
        final ViewingPlatform viewingPlatform = universe.getViewingPlatform();

        this.vpTrans = viewingPlatform.getViewPlatformTransform();

        this.world = new World3D();

        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(vpTrans);
        keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000000.0));
        world.getRoot().addChild(keyNavBeh);

        final View view = universe.getViewer().getView();
        view.setLocalEyeLightingEnable(true);
        view.setBackClipDistance(1000000);

        universe.addBranchGraph(world.getRoot());

        setEye(new Vector3d(10, 10, 30));

        MouseAdapter mouseAd = new MouseAdapter() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                Vector3d eye = getEye();

                eye.z *= 1 + (e.getWheelRotation()) / 5.0;
                eye.z = Math.max(1.0, eye.z);

                setEye(eye);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
//                System.out.println("move");
                Point point = e.getPoint();
                Vector3d eye = getEye();
                int bound = 10; // min 1
                double amount = 0.01 * eye.z;
                if (point.x < bound) {
                    eye.x -= amount;
                }
                if (point.x > canvas3D.getWidth() - 1 - bound) {
                    eye.x += amount;
                }
                if (point.y < bound) {
                    eye.y += amount;
                }
                if (point.y > canvas3D.getHeight() - 1 - bound) {
                    eye.y -= amount;
                }
                setEye(eye);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("click " + e.getPoint());
                PickRay ray = createPickRay(canvas3D, e.getX(), e.getY());

                SceneGraphPath[] paths =
                        universe.getLocale().pickAll(ray);

                Point3d pick = world.getLand().pick(ray);
                System.out.println("Land pick: " + pick);

                switch (selectedAction) {
                    case BUILD_ROAD:
                        int x = (int) Math.round(pick.x);
                        int y = (int) Math.round(pick.y);
                        world.getRoadNetwork().addPoint((int) Math.round(pick.x), (int) Math.round(pick.y));
                        world.getLand().correctRoadTile(x, y);
                        world.getLand().correctRoadTile(x - 1, y);
                        world.getLand().correctRoadTile(x - 1, y - 1);
                        world.getLand().correctRoadTile(x, y - 1);
                        break;
                    case BUILD_FACTORY:
                        final Building3D build = new Building3D(3, 2, 1);
                        build.setPos(pick);
                        build.refresh();
                        world.add(build);
                        break;
                    case SHOW_NOTIFICATION:
                        world.add(new Notification3D(pick));
                        break;
                    default:
                        System.err.println("Unspecified action " + selectedAction);
                }

                for (SceneGraphPath sceneGraphPath : paths) {
                    System.out.println("object: " + sceneGraphPath.getObject());
                }

            }
        };

        canvas3D.addMouseListener(mouseAd);
        canvas3D.addMouseMotionListener(mouseAd);
        canvas3D.addMouseWheelListener(mouseAd);


    } // end of HelloJava3Db (constructor)
    //  The following allows this to be run as an application
    //  as well as an applet

    /**
     * Creates mouse pick ray
     * @param canvas
     * @param x
     * @param y
     * @return
     */
    private PickRay createPickRay(Canvas3D canvas, int x, int y) {
        Point3d eye_pos = new Point3d();
        Point3d mouse_pos = new Point3d();

        canvas.getCenterEyeInImagePlate(eye_pos);
        canvas.getPixelLocationInImagePlate(x, y, mouse_pos);

        Transform3D motion = new Transform3D();
        canvas.getImagePlateToVworld(motion);
        motion.transform(eye_pos);
        motion.transform(mouse_pos);

        Vector3d direction = new Vector3d(mouse_pos);
        direction.sub(eye_pos);

        return new PickRay(eye_pos, direction);
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

