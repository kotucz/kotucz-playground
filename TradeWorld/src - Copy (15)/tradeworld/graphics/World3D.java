package tradeworld.graphics;

import tradeworld.gui.ChaosFrame;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DecalGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Light;
import javax.media.j3d.Node;
import javax.media.j3d.PickInfo;
import javax.media.j3d.PickRay;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import tradeworld.Building;
import tradeworld.Time;
import tradeworld.Vehicle;
import tradeworld.World;
import tradeworld.WorldObject;

/**
 *
 * @author Kotuc
 */
public class World3D {

    // logic
    private Land3D land;
    private BranchGroup sceneRoot = new BranchGroup();
    final BranchGroup objects = new BranchGroup();
    final BranchGroup buildings = new BranchGroup();
    final BranchGroup vehicles = new BranchGroup();
    private BranchGroup notifications = new BranchGroup();
    private DecalGroup decalGroup = new DecalGroup();
    private Cursor3D cursor = new Cursor3D();
    // visualization
    private final Canvas3D canvas3D;
    private final SimpleUniverse universe;
    private final TransformGroup vpTrans;

    private final World world;

    public World3D(World world) {
        this.world = world;
        {

            sceneRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            buildings.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            vehicles.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            objects.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            notifications.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            decalGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);

            objects.setCapability(Group.ALLOW_CHILDREN_WRITE);
            notifications.setCapability(Group.ALLOW_CHILDREN_WRITE);
            decalGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);



//        sceneRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);

//        AudioDevice audioDev = universe.getViewer().createAudioDevice();


            objects.addChild(buildings);
            objects.addChild(vehicles);
            sceneRoot.addChild(objects);
            sceneRoot.addChild(notifications);
            sceneRoot.addChild(decalGroup);
        }
        {
            //            canvas3D = new Canvas3D(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());

            canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

//            canvas3D.setPreferredSize(new Dimension(800, 800));
            canvas3D.setSize(256, 256);
            universe = new SimpleUniverse(canvas3D);

            final ViewingPlatform viewingPlatform = universe.getViewingPlatform();

            this.vpTrans = viewingPlatform.getViewPlatformTransform();

            KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(vpTrans);
            keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000000.0));
            getRoot().addChild(keyNavBeh);

            final View view = universe.getViewer().getView();
            view.setLocalEyeLightingEnable(true);
            view.setBackClipDistance(1000000);

            universe.addBranchGraph(getRoot());

            setEye(new Vector3d(10, 10, 30));

        }
    }

    public Time getCurrentTime() {
        return world.getGame().getCurrentTime();
    }

    public void setEye(Transform3D transform) {
        vpTrans.setTransform(transform);
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

    public Vehicle pickVehicle(PickRay pickRay) {
        PickInfo info = vehicles.pickClosest(PickInfo.PICK_BOUNDS, PickInfo.NODE | PickInfo.SCENEGRAPHPATH, pickRay);
        if (info == null) {
            return null;
        }
        SceneGraphPath closest = info.getSceneGraphPath();

        System.out.println("Pick " + pickRay + " node " + info.getNode());
        for (int i = 0; i < closest.nodeCount(); i++) {

            System.out.println("pick: " + i + ": " + closest.getNode(i));

        }

//        return vehiclesMap.get(closest.getNode(0));
        return (Vehicle) objectsMap.get(closest.getNode(0));
    }

    public Building pickBuilding(PickRay pickRay) {
        SceneGraphPath closest = buildings.pickClosest(pickRay);
//        pickClosest.
        if (closest == null) {
            return null;
        }
        System.out.println("Pick " + pickRay + " node " + closest.getObject());
        for (int i = 0; i < closest.nodeCount(); i++) {

            System.out.println("pick: " + i + ": " + closest.getNode(i));

        }
        return (Building) objectsMap.get(closest.getNode(0));
//        return buildingsMap.get(closest.getNode(0));
    }
//    private Map<Node, Building> buildingsMap = new HashMap<Node, Building>();
//
//    public void associateBuilding(Building3D model, Building building) {
//        buildingsMap.put(model.getRoot(), building);
//    }
//    private Map<Node, Vehicle> vehiclesMap = new HashMap<Node, Vehicle>();
//
//    public void associateVehicle(Vehicle3D model, Vehicle vehicle) {
//        vehiclesMap.put(model.getRoot(), vehicle);
//    }
    private Map<Node, WorldObject> objectsMap = new HashMap<Node, WorldObject>();

    public void associateObject(Model3D model, WorldObject object) {
        objectsMap.put(model.getRoot(), object);
    }

    public void setLand(Land3D land) {
        this.land = land;
        land.setWorld(this);
        decalGroup.addChild(land.getRoot());
        decalGroup.addChild(cursor.getRoot());
    }

    public Cursor3D getCursor() {
        return cursor;
    }

    public Land3D getLand() {
        return land;
    }

    public BranchGroup getRoot() {
        return sceneRoot;
    }

    private BranchGroup getAppropriateGroup(Model3D model) {
        if (model instanceof Building3D) {
            return buildings;
        }
        if (model instanceof Notification3D) {
            return notifications;
        }
        if (model instanceof Vehicle3D) {
            return vehicles;
        }
        return objects;
    }

    public void addModel(Model3D model) {
        model.setWorld(this);
        getAppropriateGroup(model).addChild(model.getRoot());
    }

    public void addBuilding(Model3D model) {
        addModel(model);
//        model.setWorld(this);
//        this.buildings.addChild(model.getRoot());
    }

    public void addNotification(Notification3D model) {
        addModel(model);
//        model.setWorld(this);
//        this.notifications.addChild(model.getRoot());
    }

    public void addVehicle(Model3D model) {
        addModel(model);
//        model.setWorld(this);
//        this.vehicles.addChild(model.getRoot());
    }

    public void removeModel(Model3D model) {
        model.setWorld(null);
        getAppropriateGroup(model).removeChild(model.getRoot());
    }

    public void removeBuilding(Building3D model) {
        removeModel(model);
//        model.setWorld(null);
//        this.buildings.removeChild(model.getRoot());
    }

    public void removeVehicle(Vehicle3D model) {
        removeModel(model);
//        model.setWorld(null);
//        this.vehicles.removeChild(model.getRoot());
    }

    public void removeNotification(Notification3D model) {
        removeModel(model);
//        model.setWorld(null);
//        this.notifications.removeChild(model.getRoot());
    }

    public void createLights() {
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

        this.sceneRoot.addChild(lights);


    }

    public Canvas3D getCanvas3D() {
        return canvas3D;
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
}
