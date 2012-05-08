package tradeworld.war;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


import javax.media.j3d.Canvas3D;
import javax.media.j3d.PickRay;
import javax.media.j3d.Transform3D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import tradeworld.Game;
import tradeworld.World;
import tradeworld.graphics.World3D;

public class WarChaosFrame {

    private final World3D world3d;
    private final World world;
//    private final Player player;
    private final JLayeredPane layeredPane;
    private final Game game;
    public final JLabel nameLabel = new JLabel("player name");
    public final JLabel healthLabel = new JLabel("health ---");
    public final JLabel soldierInstanceLabel = new JLabel("soldier ---");
    public final JLabel timeLabel = new JLabel("time --:--:--");

    protected World3D getWorld3D() {
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
    public WarChaosFrame(Game game, final World world) {
        this.game = game;
        this.world = world;

        this.world3d = world.getWorld3d();
        this.canvas3D = world3d.getCanvas3D();

        {

            JFrame frame = new JFrame("Kill them all!");
            frame.setLayout(new BorderLayout());
            {

                layeredPane = new JLayeredPane();
                layeredPane.addComponentListener(new ComponentAdapter() {

                    @Override
                    public void componentResized(ComponentEvent e) {
                        canvas3D.setSize(layeredPane.getSize());
                    }
                });
//                layeredPane.setPreferredSize(new Dimension(500, 510));
                layeredPane.add(canvas3D, new Integer(-150));
//                layeredPane.add(new JLabel("canvas3D Label"), new Integer(20));
//                splitPane.setRightComponent(layeredPane);
                frame.add(layeredPane, BorderLayout.CENTER);
            }

//            layeredPane.setLayout(new FlowLayout());
//            nameLabel.setLocation(50, 50);
           
//            nameLabel.setOpaque(false);
//            nameLabel.setBackground(new Color(0, 0, 0, 120));
//            soldierInstanceLabel.setLocation(50, 100);
//            healthLabel.setLocation(50, 150);

            layeredPane.add(nameLabel);
            nameLabel.setBounds(00, 00, 100, 20);
            layeredPane.add(soldierInstanceLabel);
            soldierInstanceLabel.setBounds(000, 20, 300, 20);
            layeredPane.add(healthLabel);
            healthLabel.setBounds(300, 00, 100, 20);
            layeredPane.add(timeLabel);
            timeLabel.setBounds(500, 00, 250, 20);

            frame.setSize(700, 500);
//            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

        }

        System.out.println("canvas3D: " + canvas3D);


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

    public void updatePanels() {
        //throw new UnsupportedOperationException("Not yet implemented");
    }
} // end of class ChaosFrame

