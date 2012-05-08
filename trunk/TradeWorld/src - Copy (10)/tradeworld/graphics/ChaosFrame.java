package tradeworld.graphics;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import java.awt.Color;
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
import javax.swing.JSplitPane;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import tradeworld.Building;
import tradeworld.Building.Type;
import tradeworld.Depot;
import tradeworld.Factory;
import tradeworld.Player;
import tradeworld.Storage;
import tradeworld.Truck;
import tradeworld.Vehicle;
import tradeworld.World;
import tradeworld.gui.MainPanel2;
import tradeworld.gui.SuperPanel;
import tradeworld.gui.VehiclePanel;

public class ChaosFrame {

    private World3D world3d;
    private World world;
    private Canvas3D canvas3D;
    private SimpleUniverse universe;
    private TransformGroup vpTrans;
    private Player player;
    private JLayeredPane layeredPane;
    private Cursor3D cursor;

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

    class SelectBuildingAction extends Action {

        private Point3d center;
        private Building.Type selectedBuildingType;
        private Building selectedBuilding;

        public SelectBuildingAction(Type selectedBuildingType) {
            this.selectedBuildingType = selectedBuildingType;
        }

        @Override
        public void perform() {

//            SuperPanel panel;
            switch (selectedBuildingType) {
                case LUMBERJACK_CAMP:
                case FURNITURE_FACTORY:
                case WOODMILL:
                    final Factory fact = new Factory(
                            selectedBuildingType, player, center);
                    world.putBuilding(fact);

                    for (Building building : world.getBuildings()) {
                        if ((building instanceof Storage) && (fact.isInRange(building))) {
                            fact.addStorage((Storage) building);
                        }
                    }
//                    panel = fact.getPanel();
//                    showPanel(panel);
                    break;
                case TRUCK_DEPOT1:
                    final Depot depo = new Depot(
                            selectedBuildingType, player, center);
                    world.putBuilding(depo);

                    for (Building building : world.getBuildings()) {
                        if ((building instanceof Storage) && (depo.isInRange(building))) {
                            depo.addStorage((Storage) building);
                        }
                    }
                    Point3d point = depo.getEntrance();

                    try {
                        int enx = (int) Math.round(point.x);
                        int eny = (int) Math.round(point.y);
                        world.getRoadNetwork().addPoint(enx, eny);
                    } catch (Exception eff) {
                        System.err.println("EEEEEEEEEE" + eff.getMessage());
                    }

//                                    panel = fact.getPanel();
//                                    showPanel(panel);
                    break;
                case STORAGE:
                    final Storage stor = new Storage(
                            player, center, 111);
                    world.putBuilding(stor);

                    for (Building building : world.getBuildings()) {
                        if (stor.isInRange(building)) {
                            if (stor.getOwner().equals(building.getOwner())) {
                                if (building instanceof Factory) {
                                    ((Factory) building).addStorage(stor);
                                }
                                if (building instanceof Depot) {
                                    ((Depot) building).addStorage(stor);
                                }
                            }
                        }
                    }
//                    panel = stor.getPanel();
//                    showPanel(panel);
                    break;
            }
        }

        @Override
        public void update(Point3d point) {
//            System.out.println("click " + e.getPoint());
//            PickRay ray = createPickRay(canvas3D, e.getX(), e.getY());
//            Point3d pick = world.getLand().pick(ray);
//            System.out.println("Land pick: " + pick);
//
//            Building pickBuilding = world3d.pickBuilding(ray);
//            if (pickBuilding != null) {
//                System.out.println("Picked building " + pickBuilding);
//                showPanel(pickBuilding.getPanel());
//            }
//            this.center = new Point3d(
//                    0.5 * selectedBuildingType.getWidthx() + Math.round(point.x - 0.5 * selectedBuildingType.getWidthx()),
//                    0.5 * selectedBuildingType.getWidthy() + Math.round(point.y - 0.5 * selectedBuildingType.getWidthy()), 0);
//            cursor.setPos(center);
//            cursor.setColor(
//                    (world.getLand().isAreaFree(
//                    center.x, center.y,
//                    selectedBuildingType.getWidthx(), selectedBuildingType.getWidthy()))
//                    ? new Color3f(0.0f, 1f, 0.0f) : new Color3f(1f, 0f, 0.0f));
//            cursor.setSize(selectedBuildingType.getWidthx(), selectedBuildingType.getWidthy());
//            cursor.refresh();
        }

        @Override
        public void cancel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    class BuildFactoryAction extends Action {

        private Point3d center;
        private Building.Type selectedBuildingType;

        public BuildFactoryAction(Type selectedBuildingType) {
            this.selectedBuildingType = selectedBuildingType;
        }

        @Override
        public void perform() {

//            SuperPanel panel;
            switch (selectedBuildingType) {
                case LUMBERJACK_CAMP:
                case FURNITURE_FACTORY:
                case WOODMILL:
                    final Factory fact = new Factory(
                            selectedBuildingType, player, center);
                    world.putBuilding(fact);

                    for (Building building : world.getBuildings()) {
                        if ((building instanceof Storage) && (fact.isInRange(building))) {
                            fact.addStorage((Storage) building);
                        }
                    }
//                    panel = fact.getPanel();
//                    showPanel(panel);
                    break;
                case TRUCK_DEPOT1:
                    final Depot depo = new Depot(
                            selectedBuildingType, player, center);
                    world.putBuilding(depo);

                    for (Building building : world.getBuildings()) {
                        if ((building instanceof Storage) && (depo.isInRange(building))) {
                            depo.addStorage((Storage) building);
                        }
                    }
                    Point3d point = depo.getEntrance();

                    try {
                        int enx = (int) Math.round(point.x);
                        int eny = (int) Math.round(point.y);
                        world.getRoadNetwork().addPoint(enx, eny);
                    } catch (Exception eff) {
                        System.err.println("EEEEEEEEEE" + eff.getMessage());
                    }

//                                    panel = fact.getPanel();
//                                    showPanel(panel);
                    break;
                case STORAGE:
                    final Storage stor = new Storage(
                            player, center, 111);
                    world.putBuilding(stor);

                    for (Building building : world.getBuildings()) {
                        if (stor.isInRange(building)) {
                            if (stor.getOwner().equals(building.getOwner())) {
                                if (building instanceof Factory) {
                                    ((Factory) building).addStorage(stor);
                                }
                                if (building instanceof Depot) {
                                    ((Depot) building).addStorage(stor);
                                }
                            }
                        }
                    }
//                    panel = stor.getPanel();
//                    showPanel(panel);
                    break;
            }
        }

        @Override
        public void update(Point3d point) {
            this.center = new Point3d(
                    0.5 * selectedBuildingType.getWidthx() + Math.round(point.x - 0.5 * selectedBuildingType.getWidthx()),
                    0.5 * selectedBuildingType.getWidthy() + Math.round(point.y - 0.5 * selectedBuildingType.getWidthy()), 0);
            cursor.setPos(center);
            cursor.setColor(
                    (world.getLand().isAreaFree(
                    center.x, center.y,
                    selectedBuildingType.getWidthx(), selectedBuildingType.getWidthy()))
                    ? new Color3f(0.0f, 1f, 0.0f) : new Color3f(1f, 0f, 0.0f));
            cursor.setSize(selectedBuildingType.getWidthx(), selectedBuildingType.getWidthy());
            cursor.refresh();

        }

        @Override
        public void cancel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    

    class BuildRoadAction extends Action {

        private int x;
        private int y;

        @Override
        public void perform() {
            world.getRoadNetwork().addPoint(x, y);
        }

        @Override
        public void update(Point3d point) {
            x = (int) Math.round(point.x);
            y = (int) Math.round(point.y);
            cursor.setPos(new Point3d(x, y, 0));
            {
            }
            cursor.setColor((world.getLand().getRoadNetwork().getPoint(x, y) == null) ? new Color3f(0.5f, 0.5f, 0.5f) : new Color3f(1, 0, 0));
            cursor.setSize(0.5, 0.5);
            cursor.refresh();
        }

        @Override
        public void cancel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    private Action selectedAction = null;

    public void selectAction(Action action) {
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
    public ChaosFrame(final World world) {
        this.world = world;
        this.player = new Player("Kotuc", Color.yellow, 1000000, world);

//        super("ChaosFrame");

//        setLayout(new BorderLayout());
        {
//            canvas3D = new Canvas3D(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
            canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

//            canvas3D.setPreferredSize(new Dimension(800, 800));
            canvas3D.setSize(900, 700);
            universe = new SimpleUniverse(canvas3D);
            JFrame frame = new JFrame("Trade World");

//        GameMenuPanel panel = new GameMenuPanel();
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
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
                splitPane.setRightComponent(layeredPane);
            }
//            splitPane.setLeftComponent(new MainPanel(this));
            splitPane.setLeftComponent(new MainPanel2(this));
//            frame.add(canvas3D);
            frame.add(splitPane);
//            frame.setSize(500, 500);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);


        }
//        {
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
    private PickRay createPickRay(
            Canvas3D canvas, int x, int y) {
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
    private Set<SuperPanel> superPanels = new HashSet<SuperPanel>();

    public void updatePanels() {
        for (SuperPanel panel : superPanels) {
            panel.update();
        }
    }

    private void showPanel(final SuperPanel panel) {

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
//                superPanels.remove(panel);
            }
        });
        //        frame.setDefaultCloseOperation(JFrame.);
        //        canvas3D.add(frame);
        layeredPane.add(frame, new Integer(0));
        frame.setVisible(true);
        //        layeredPane.add(panel, new Integer(0));

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
        ChaosFrame frame = new ChaosFrame(new World());
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
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (selectedAction != null) {
                if (e.getButton() == MouseEvent.BUTTON1) { // left
//                    selectedAction.perform();
                    world.schedule(selectedAction);
                }
                if (e.getButton() == MouseEvent.BUTTON3) { // right
                    selectedAction = null; // erase
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
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

//                    System.out.println("Picked vehicle " + pickVehicle);

                }//                SceneGraphPath[] paths = universe.getLocale().pickAll(ray);
//                if (paths != null) {
//                    for (SceneGraphPath sceneGraphPath : paths) {
////                    System.out.println("sgpath: " + sceneGraphPath);
//                        System.out.println("object: " + sceneGraphPath.getObject());
//                    }
//                } else {
//                    System.err.println("no pick");
//                }
            } catch (Exception ex) {
                System.err.println("click action exception");
                ex.printStackTrace();
            }
        }
    }
} // end of class ChaosFrame

