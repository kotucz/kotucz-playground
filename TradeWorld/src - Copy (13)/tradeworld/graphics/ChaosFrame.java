package tradeworld.graphics;

import tradeworld.actions.BuildAction;
import tradeworld.actions.BuildRoad;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.PickRay;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import net.java.games.input.ControllerEnvironment;
import tradeworld.Building;
import tradeworld.Building.Type;
import tradeworld.Game;
import tradeworld.actions.GameAction;
import tradeworld.Vehicle;
import tradeworld.World;
import tradeworld.gui.MainPanel2;
import tradeworld.gui.StatusPanel;
import tradeworld.gui.SuperPanel;
import tradeworld.gui.VehiclePanel;

public class ChaosFrame {

    private final StatusPanel statusPanel;
    private final World3D world3d;
    private final World world;
    private final Canvas3D canvas3D;
    private final SimpleUniverse universe;
    private final TransformGroup vpTrans;
//    private final Player player;
    private final JLayeredPane layeredPane;
    private Cursor3D cursor;
    private final ControllerEnvironment controllerEnvironment;
    private final Set<SuperPanel> superPanels = new HashSet<SuperPanel>();
    private final Game game;

    public World3D getWorld3D() {
        return world3d;
    }

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

    public abstract class UserAction {

        public void update(Point3d point) {
        }

        public void update(PickRay ray) {
        }

        public void cancel() {
        }

        public void submit() {
            scheduleAction(selectedAction.createAction());
        }

        public GameAction createAction() {
            return null;
        }
    }

    /**
     * Blocking method for interactive selecting building
     * @return first selected building when clicked
     *  null when canceled
     */
    public Building selectBuilding() {
        final SelectBuildingAction selectBuildingAction = new SelectBuildingAction(Type.LUMBERJACK_CAMP);
        selectAction(selectBuildingAction);
        return selectBuildingAction.selectBuilding();
    }

    class SelectBuildingAction extends UserAction {

        private PickRay ray;
        private Building.Type selectedBuildingType;
        private Building selectedBuilding;
        private UserAction follower;

        public SelectBuildingAction(Type selectedBuildingType) {
            this.selectedBuildingType = selectedBuildingType;
        }

        @Override
        public void update(PickRay ray) {
            this.ray = ray;
        }

        @Override
        public void submit() {
            Building pickBuilding = world3d.pickBuilding(ray);
            if (pickBuilding != null) {
                System.out.println("Picked building " + pickBuilding);
//                showPanel(pickBuilding.getPanel());
                this.selectedBuilding = pickBuilding;
                synchronized (this) {
                    this.notifyAll();
                }
            } else {
                System.out.println("Pick building missed");
            }
        }

        /**
         * Blocking method for interactive selecting building
         * @return first selected building when clicked
         *  null when canceled
         */
        private Building selectBuilding() {
            synchronized (this) {
                while (selectedBuilding == null ||
                        selectedAction != this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChaosFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return selectedBuilding;
        }
    }

    class BuildFactoryAction extends UserAction {

        private Point3d center;
        private Building.Type selectedBuildingType;

        public BuildFactoryAction(Type selectedBuildingType) {
            this.selectedBuildingType = selectedBuildingType;
        }

        @Override
        public GameAction createAction() {
            return new BuildAction(center, selectedBuildingType, game.getCurrentPlayer());
        }

        @Override
        public void update(Point3d point) {
            this.center = new Point3d(
                    0.5 * selectedBuildingType.getWidthx() + Math.round(point.x - 0.5 * selectedBuildingType.getWidthx()),
                    0.5 * selectedBuildingType.getWidthy() + Math.round(point.y - 0.5 * selectedBuildingType.getWidthy()), 0);
            cursor.setPos(center);
            cursor.setColor(
                    (world.getRoadNetwork().isAreaFree(
                    center.x, center.y,
                    selectedBuildingType.getWidthx(), selectedBuildingType.getWidthy()))
                    ? new Color3f(0.0f, 1f, 0.0f) : new Color3f(1f, 0f, 0.0f));
            cursor.setSize(selectedBuildingType.getWidthx(), selectedBuildingType.getWidthy());
            cursor.refresh();
        }
    }

    class BuildRoadAction extends UserAction {

        private int x;
        private int y;

        BuildRoadAction() {
            super();
        }

        @Override
        public GameAction createAction() {
            return new BuildRoad(x, y, game.getCurrentPlayer());
        }

        @Override
        public void update(Point3d point) {
            x = (int) Math.round(point.x);
            y = (int) Math.round(point.y);
            cursor.setPos(new Point3d(x, y, 0));
            cursor.setColor((world.getLand().getRoadNetwork().getPoint(x, y) == null) ? new Color3f(0.5F, 0.5F, 0.5F) : new Color3f(1, 0, 0));
            cursor.setSize(0.5, 0.5);
            cursor.refresh();
        }
    }
    private UserAction selectedAction = null;

    public void selectAction(UserAction action) {
        this.selectedAction = action;
        System.out.println(action + " selected");
    }

    public void selectBuildingBuilding(Building.Type buildingType) {
        selectAction(new BuildFactoryAction(buildingType));
    }

    public void selectBuildingRoad() {
        selectAction(new BuildRoadAction());
    }

// Create a simple scene and attach it to the virtual universe
    public ChaosFrame(Game game, final World world) {
        this.game = game;
        this.world = world;
//        this.player = new Player("Kotuc", Color.yellow, 1000000, world);
//        this.player = game.player;//new Player("Kotuc", Color.yellow, 1000000, world);

        this.controllerEnvironment = ControllerEnvironment.getDefaultEnvironment();

//        super("ChaosFrame");

//        setLayout(new BorderLayout());
        {
//            canvas3D = new Canvas3D(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());

            canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

//            canvas3D.setPreferredSize(new Dimension(800, 800));
            canvas3D.setSize(256, 256);
            universe = new SimpleUniverse(canvas3D);
            JFrame frame = new JFrame("Trade World");
            frame.setLayout(new BorderLayout());
//        GameMenuPanel panel = new GameMenuPanel();
            //            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
//            splitPane.setRightComponent(canvas3D);
            {
//                JPanel greatPanel = new JPanel();
//                frame.getRootPane().add(canvas3D);

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
//            splitPane.setLeftComponent(new MainPanel(this));

//            splitPane.setLeftComponent(new MainPanel2(this));
            frame.add(new MainPanel2(this), BorderLayout.NORTH);

            statusPanel = new StatusPanel();
            frame.add(statusPanel, BorderLayout.SOUTH);

//            frame.add(canvas3D);
//            frame.add(splitPane);

            frame.setSize(700, 500);
//            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

//            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

        }//        {
//            GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
//            GraphicsConfiguration config2 = SimpleUniverse.getPreferredConfiguration();
//            System.out.println("config1: " + config);
//            System.out.println("config2: " + config2);
//            canvas3D = new Canvas3D(config2, true);
////            add(canvas3D);
//            universe = new ConfiguredUniverse(canvas3D);
//        }
//        {
//            universe = new ConfiguredUniverse(getClass().getResource("/data/viewsetup.txt"));
//            canvas3D = universe.getCanvas();
//        }

        System.out.println("canvas3D: " + canvas3D);
        canvas3D.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
//					System.out.println("EVENT: "+e);
                switch (e.getKeyCode()) {
//                    case KeyEvent.VK_R:
//                        selectAction(Action.BUILD_ROAD);
//                        break;
//                    case KeyEvent.VK_F:
//                        selectAction(Action.BUILD_FACTORY);
//                        selectedBuildingType = Building.Type.LUMBERJACK_CAMP;
//                        break;
//                    case KeyEvent.VK_S:
//                        selectAction(Action.BUILD_FACTORY);
//                        selectedBuildingType = Building.Type.STORAGE;
//                        System.out.println("Storage selected");
//                        break;
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

        this.world3d = world.getWorld3d();
        this.cursor = world3d.getCursor();

        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(vpTrans);
        keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000000.0));
        this.world3d.getRoot().addChild(keyNavBeh);

        final View view = universe.getViewer().getView();
        view.setLocalEyeLightingEnable(true);
        view.setBackClipDistance(1000000);

        universe.addBranchGraph(this.world3d.getRoot());

        setEye(new Vector3d(10, 10, 30));

        MouseAdapter mouseAd = new MouseHandler();

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

    public void updatePanels() {
        synchronized (superPanels) {
            for (SuperPanel panel : superPanels) {
                panel.update();
            }
        }
        statusPanel.setCash(game.getCurrentPlayer().getCash());
        statusPanel.setStatus(game.getCurrentPlayer().getName());
    }

    public void scheduleAction(GameAction action) {
        this.game.scheduleAction(action);
    }

    void showPanel(final SuperPanel panel) {
        synchronized (superPanels) {
            panel.chaosFrame = this;
            superPanels.add(panel);

            JInternalFrame frame = new JInternalFrame(panel.getName());
            //        frame.setBorder(null);
            frame.setClosable(true);
            frame.add(panel);
//        panel.getP
            frame.pack();
            frame.addComponentListener(new ComponentAdapter() {

                @Override
                public void componentHidden(ComponentEvent e) {
                    synchronized (superPanels) {
                        superPanels.remove(panel);
                    }

                }
            });
            //        frame.setDefaultCloseOperation(JFrame.);
            //        canvas3D.add(frame);
            layeredPane.add(frame, new Integer(0));
            frame.setVisible(true);
            //        layeredPane.add(panel, new Integer(0));
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
//        ChaosFrame frame = new ChaosFrame(new World());
//        frame.add(new ChaosFrame().canvas3D);
//        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
//        frame.setSize(640, 480);
//        frame.setVisible(true);
//        System.out.println("Visible");
//        frame.exportImage();
//        frame.exportImage();
//        frame.exportImage();
    } // end of main (method of HelloJava3Db)

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            Vector3d eye = getEye();
            eye.z *= 1 + (e.getWheelRotation()) / 5.0;
            eye.z = Math.max(1.0, eye.z);
            setEye(eye);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            System.out.println("dragged " + e.getPoint());
            mouseMoved(e);
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

            if (selectedAction != null) {
                // actions
                PickRay ray = createPickRay(canvas3D, e.getX(), e.getY());
                Point3d pick = world.getLand().pick(ray);
                selectedAction.update(pick);
                selectedAction.update(ray);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (selectedAction != null) {
                if (e.getButton() == MouseEvent.BUTTON1) { // left
                    selectedAction.submit();


                }
                if (e.getButton() == MouseEvent.BUTTON3) { // right
                    selectedAction.cancel();
                    selectedAction = null; // erase
                }
            } else {
                System.out.println("click " + e.getPoint());
                PickRay ray = createPickRay(canvas3D, e.getX(), e.getY());
                Point3d pick = world.getLand().pick(ray);
                System.out.println("Land pick: " + pick);
                {
                    Building pickBuilding = world3d.pickBuilding(ray);
                    if (pickBuilding != null) {
                        System.out.println("Picked building " + pickBuilding);
                        showPanel(pickBuilding.getPanel());
                    }
                }
                {
                    Vehicle pickVehicle = world3d.pickVehicle(ray);
                    if (pickVehicle != null) {
                        System.out.println("Picked vehicle " + pickVehicle);
                        showPanel(new VehiclePanel(pickVehicle));
                    }
                    System.out.println("Picked vehicle " + pickVehicle);
                }
            }
        }
    }
} // end of class ChaosFrame

