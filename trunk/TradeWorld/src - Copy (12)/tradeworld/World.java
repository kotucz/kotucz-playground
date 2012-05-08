package tradeworld;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.vecmath.Point3d;
import tradeworld.graphics.Land3D;
import tradeworld.graphics.Notification3D;
import tradeworld.graphics.World3D;

/**
 *
 * @author Kotuc
 */
public class World {

    private final Tile[][] tiles = new Tile[10][10];
    private final List<Building> buildings = new LinkedList<Building>();
    private final List<Vehicle> vehicles = new LinkedList<Vehicle>();
    private World3D world3d;
    private RoadNetwork roadNetwork;
    private TrainNetwork trainNetwork;
    private final Game game;
    private final Map<PlayerId, Player> players = new HashMap<PlayerId, Player>();

    public Player getPlayer(PlayerId id) {
        return players.get(id);
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public World(Game game) {
        this.game = game;
        this.world3d = new World3D();
        createObjects();
        System.out.println("World created");
    }

    void moveAll() {

        for (Vehicle vehicle : vehicles) {
            vehicle.act();
        }
        for (Building building : buildings) {
            building.act();
        }


    }

    public Game getGame() {
        return game;
    }

    public void putBuilding(Building building) {
        building.setWorld(this);
        buildings.add(building);
        world3d.addBuilding(building.getModel());
        world3d.associateBuilding(building);
    }

    public void putVehicle(Vehicle vehicle) {
        vehicle.setWorld(this);
        vehicles.add(vehicle);
        world3d.addVehicle(vehicle.getModel());
        world3d.associateVehicle(vehicle);
    }

    public void showNotification(Notification3D notification, Point3d pos) {
        notification.setPos(pos);
        notification.refresh();
        world3d.addNotification(notification);
    }
    private Land3D land;

    public Land3D getLand() {
        return land;
    }

    public void createObjects() {

        this.land = new Land3D(20, 20);
        this.roadNetwork = land.getRoadNetwork();
        this.trainNetwork = land.getTrainNetwork();

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

        System.out.print("Cars: ");
        // cars
        for (int i = 0; i < 0; i++) {
            System.out.print(i + ", ");
            final Vehicle vehicle = new Truck(null, Vehicle.Type.SKODA120, roadNetwork.randomRoadPoint().getPos());
            this.putVehicle(vehicle);
//            vehicleThread.add(vehicle);
        }
        System.out.println();
        System.out.print("Trains: ");
        // trains
        for (int i = 0; i < 0; i++) {
            System.out.print(i + ", ");
            final Vehicle vehicle = new Truck(null, Vehicle.Type.TRAIN1, trainNetwork.randomRoadPoint().getPos());
            this.putVehicle(vehicle);
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

    public TrainNetwork getTrainNetwork() {
        return trainNetwork;
    }

    public World3D getWorld3d() {
        return world3d;
    }
}
