package tradeworld.graphics;

import java.util.HashMap;
import java.util.Map;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DecalGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.Light;
import javax.media.j3d.Node;
import javax.media.j3d.PickInfo;
import javax.media.j3d.PickRay;
import javax.media.j3d.SceneGraphPath;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import tradeworld.Building;
import tradeworld.Vehicle;

/**
 *
 * @author Kotuc
 */
public class World3D {

    private Land3D land;
    private BranchGroup sceneRoot = new BranchGroup();
    final BranchGroup buildings = new BranchGroup();
    final BranchGroup vehicles = new BranchGroup();
    private BranchGroup notifications = new BranchGroup();
    private DecalGroup decalGroup = new DecalGroup();
    private Cursor3D cursor = new Cursor3D();

    public World3D() {

        sceneRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        buildings.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        vehicles.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        notifications.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        decalGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);

        notifications.setCapability(Group.ALLOW_CHILDREN_WRITE);
        decalGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);



//        sceneRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);

//        AudioDevice audioDev = universe.getViewer().createAudioDevice();

        sceneRoot.addChild(buildings);
        sceneRoot.addChild(vehicles);
        sceneRoot.addChild(notifications);
        sceneRoot.addChild(decalGroup);

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

        return vehiclesMap.get(closest.getNode(0));
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
        return buildingsMap.get(closest.getNode(0));
    }
    private Map<Node, Building> buildingsMap = new HashMap<Node, Building>();

    public void associateBuilding(Building building) {
        buildingsMap.put(building.getModel().getRoot(), building);
    }

    private Map<Node, Vehicle> vehiclesMap = new HashMap<Node, Vehicle>();

    public void associateVehicle(Vehicle vehicle) {
        vehiclesMap.put(vehicle.getModel().getRoot(), vehicle);
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

    public void addBuilding(Building3D model) {
        model.setWorld(this);
        this.buildings.addChild(model.getRoot());
    }

    public void addNotification(Notification3D model) {
        model.setWorld(this);
        this.notifications.addChild(model.getRoot());
    }

    public void addVehicle(Vehicle3D model) {
        model.setWorld(this);
        this.vehicles.addChild(model.getRoot());
    }

    public void removeBuilding(Building3D model) {
        model.setWorld(null);
        this.buildings.removeChild(model.getRoot());
    }

    public void removeVehicle(Vehicle3D model) {
        model.setWorld(null);
        this.vehicles.removeChild(model.getRoot());
    }

    public void removeNotification(Notification3D model) {
        model.setWorld(null);
        this.notifications.removeChild(model.getRoot());
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
}
