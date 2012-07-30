package kotuc.chaos.portal;

import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import kotuc.chaos.Box;
import kotuc.chaos.Location;

/**
 *
 * @author Tomas
 */
public class PortalView {

    private TransformGroup viewTG;
    private PhysicalBody physBody;
    private Canvas3D canvas;

    public PortalView() {

        VirtualUniverse universe = new VirtualUniverse();
        Locale locale = new Locale(universe);

        Location location = new Location();
        //location.addEntity(new Box(0.1f, 0.1f, 0.1f));
        {
            Box leftupbox = new Box(0.1f, 0.1f, 0.1f);
            leftupbox.setColor(new Color3f(1, 0.7f, 0.7f));
            leftupbox.setPos(new Point3d(-0.2, 0.15, 0));
            location.addEntity(leftupbox);
        }
        {
            float s = 4.7f;
            Box box1 = new Box(s * 0.16f, s * 0.08f, 0.1f);
            box1.setColor(new Color3f(0.7f, 1f, 0.7f));
            box1.setPos(new Point3d(0, 0, -0.1));
            location.addEntity(box1);
        }
        {
            Box box2 = new Box(0.1f, 0.1f, 0.1f);
            box2.setColor(new Color3f(0.7f, 0.7f, 1f));
            box2.setPos(new Point3d(0, 0, -1));
            location.addEntity(box2);
        }

        //location.getMainGroup();
        locale.addBranchGraph(location.getMainGroup());

        BranchGroup viewBG = new BranchGroup();
        viewTG = new TransformGroup();
        viewBG.addChild(viewTG);
        viewTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Transform3D t3d = new Transform3D();
//        t3d.setTranslation(new Vector3f(0f, 0.0f, 1f));
        viewTG.setTransform(t3d);

        ViewPlatform viewPlatform = new ViewPlatform();
        viewTG.addChild(viewPlatform);

//        Canvas3D canvas = new Canvas3D(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.getScreen3D().setPhysicalScreenWidth(0.33);
        canvas.getScreen3D().setPhysicalScreenHeight(0.21);

        canvas.setMonoscopicViewPolicy(View.LEFT_EYE_VIEW);
        
        Point3d pos = new Point3d();
        canvas.getLeftManualEyeInImagePlate(pos);
        System.out.println("Left man eye " + pos);
        
//        canvas.getLeftEyeInImagePlate(pos);
//        System.out.println("Left eye " + pos);
        
//        LeftManualEyeInImagePlate(new Point3d(0.17, 0.1, 1));
//        canvas.setLeftManualEyeInImagePlate(new Point3d(0.17, 0.1, 1));
//        canvas.setRightManualEyeInImagePlate(new Point3d(0, 0, 2));
        View view = new View();
        view.addCanvas3D(canvas);
        view.attachViewPlatform(viewPlatform);
        view.setFrontClipDistance(0.1);
        view.setFrontClipPolicy(View.PHYSICAL_EYE);

//        view.setWindowEyepointPolicy(View.RELATIVE_TO_COEXISTENCE);
        view.setWindowEyepointPolicy(View.RELATIVE_TO_SCREEN);
//        view.setWindowEyepointPolicy(View.RELATIVE_TO_WINDOW);
        physBody = new PhysicalBody(new Point3d(), new Point3d());

        view.setPhysicalBody(physBody);

        PhysicalEnvironment physEnv = new PhysicalEnvironment();
        view.setPhysicalEnvironment(physEnv);

        locale.addBranchGraph(viewBG);

        final JFrame frame = new JFrame("Window");
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);

        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        canvas.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("click");
                final Point3d eye = new Point3d((e.getX() - 500) / 1000.0, (e.getY() - 400) / 1000.0, 1);
                System.out.println("" + eye);
                setHead(eye);
                frame.repaint();
            }
        });
    }

    private void setHead(Point3d eye) {
        double q = 2;
        canvas.setLeftManualEyeInImagePlate(new Point3d(0.17 + eye.x, 0.1 - eye.y, q * 1));
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3d(-2.8 * eye.x / q, 2.8 * eye.y / q, 0));
//        t3d.setTranslation(new Vector3d(-1.8*eye.x/q, 1.8*eye.y/q, 0));
//        t3d.setTranslation(new Vector3d(0, 0, 1));
//        t3d.lookAt(new Point3d(eye), new Point3d(), new Vector3d(0, 1, 0));
//        t3d.invert();
        viewTG.setTransform(t3d);
//        physBody.setLeftEyePosition(eye);
//        physBody.setRightEyePosition(eye);
    }

    public static void main(String[] args) {
        new PortalView();
    }
}
