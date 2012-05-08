package arachnoid2;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.PickRay;
import javax.media.j3d.Transform3D;
import javax.swing.JFrame;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.graphics.Land3D;
import tradeworld.graphics.World3D;
import tradeworld.physics.PhysicsWorld;

public class ViewFrame {

    private final World3D world3d;

    public World3D getWorld3D() {
        return world3d;
    }

    public void setEye(Vector3d vector) {
        world3d.setEye(vector);
    }

    public Vector3d getEye() {
        return world3d.getEye();
    }
    private final Canvas3D canvas3D;

// Create a simple scene and attach it to the virtual universe
    public ViewFrame(World3D world3d) {
        this.world3d = world3d;

        world3d.addModel(new Land3D(10, 10));

        final StaticModel smodel = new StaticModel();
        smodel.createGraphics(world3d);

        final PhysicsWorld physicsWorld = new PhysicsWorld();

        final DynamicModel dmodel = new DynamicModel(smodel, physicsWorld);
        dmodel.createGraphics(world3d);

        

        Transform3D look = new Transform3D();
        look.lookAt(new Point3d(10, -5, 10), new Point3d(5, 5, 1), new Vector3d(0, 0, 1));
        look.invert();
        world3d.setEye(look);

        this.canvas3D = world3d.getCanvas3D();
        canvas3D.setSize(800, 800);

        canvas3D.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
//					System.out.println("EVENT: "+e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                    default:
                        System.err.println("unhandled key!");
                }


            }
        });

        JFrame frame = new JFrame("Trade World");
        frame.setLayout(new BorderLayout());
        frame.add(canvas3D, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        new Thread() {

            @Override
            public void run() {
                double t = 0;

                SitesConfig goal = new SitesConfig(4);
//                goal.values = new Point3d[]{
//                            new Point3d(4, 6, 2),
//                            new Point3d(5, 4, 2),
//                            new Point3d(6, 5, 1),
//                            new Point3d(5, 6, 3)};

                goal.setTuple(new Point3d(4, 6, 2), 0);
                goal.setTuple(new Point3d(5, 4, 2), 1);
                goal.setTuple(new Point3d(6, 5, 1), 2);
                goal.setTuple(new Point3d(5, 6, 3), 3);

                final GradientOptimizer gradDesc = new GradientOptimizer(new Function(smodel, goal));

                VectorNd dconf = new VectorNd(smodel.joints.size());

                while (true) {

                    smodel.getJoints(dconf);
                    gradDesc.gradientDescentIteration(dconf);
                    smodel.setJoints(dconf);

//                    for (Joint joint : smodel.joints) {
////                        joint.setAngle(Math.sin(t += 0.001) - 0.3);
//                        joint.setAngle(2*Math.sin(t += 0.001));
//                    }

                    physicsWorld.step(0.01f);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ViewFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
//        Iterator i = PhysicEntity.entities.iterator();
//        while (i.hasNext()) {
//            try {
//                ((Player) i.next()).setCanvas(c);
//            } catch (ClassCastException e) {
//            }
//        }
//        
// TODO Uncomment for strategy control
//        MouseAdapter mouseAd = new MouseHandler();
//        canvas3D.addMouseListener(mouseAd);
//        canvas3D.addMouseMotionListener(mouseAd);
//        canvas3D.addMouseWheelListener(mouseAd);
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
} 

