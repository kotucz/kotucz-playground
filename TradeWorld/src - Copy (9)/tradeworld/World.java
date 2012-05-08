package tradeworld;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.vecmath.Point3d;
import tradeworld.graphics.ChaosFrame.Action;
import tradeworld.graphics.Land3D;
import tradeworld.graphics.Notification3D;
import tradeworld.graphics.World3D;

/**
 *
 * @author Kotuc
 */
public class World {

    private Tile[][] tiles = new Tile[10][10];
    private List<Building> buildings = new LinkedList<Building>();
    private List<Vehicle> vehicles = new LinkedList<Vehicle>();
    private World3D world3d;
    private RoadNetwork roadNetwork;
    private Queue<Action> actions = new LinkedList<Action>();

    public List<Building> getBuildings() {
        return buildings;
    }

    public void schedule(Action action) {
        this.actions.add(action);
    }

    public World() {
        this.world3d = new World3D();
        createObjects();
    }

    void gameStep() {

        for (Vehicle vehicle : vehicles) {
            vehicle.act();
        }
        for (Building building : buildings) {
            building.act();
        }
        while (!actions.isEmpty()) {
            actions.poll().perform();
        }

    }

    public void put(Building building) {
        building.setWorld(this);
        buildings.add(building);
        world3d.add(building.getModel());
    }

    public void put(Vehicle vehicle) {
        vehicle.setWorld(this);
        vehicles.add(vehicle);
        world3d.add(vehicle.getModel());
    }

    public void showNotification(Notification3D notification, Point3d pos) {
        notification.setPos(pos);
        notification.refresh();
        world3d.add(notification);
    }
    private Land3D land;

    public Land3D getLand() {
        return land;
    }

    public void createObjects() {

        this.land = new Land3D(200, 200);
        this.roadNetwork = land.getRoadNetwork();

        world3d.setLand(land);

//        for (int i = 0; i < 1; i++) {
//            System.out.println("building " + i);
//            final Building building = new Building(Building.Type.STORAGE, null, );
//            building3D.setPos(new Point3d(Math.random() * 20, Math.random() * 20, 0));
//            building3D.refresh();
//            world3d.add(building3D);
//        }
        world3d.createLights();

//        VehicleThread vehicleThread = new VehicleThread();

        for (int i = 0; i < 20; i++) {
            System.out.println("" + i);
            final Vehicle vehicle = new Truck(null, Vehicle.Type.LIGHT_TRUCK, roadNetwork.randomRoadPoint().getPos());
            this.put(vehicle);
//            vehicleThread.add(vehicle);
        }

//        add(new Notification(Tools.loadTexture("mega4.png").getDetailImage()).getRoot());
//        showNotification(new Notification3D(new Point3d()));

        //this.add(new Vehicle().getBranchGroup());
//        new Thread(vehicleThread).start();
    }

    public RoadNetwork getRoadNetwork() {
        return roadNetwork;
    }

    public World3D getWorld3d() {
        return world3d;
    }
}
