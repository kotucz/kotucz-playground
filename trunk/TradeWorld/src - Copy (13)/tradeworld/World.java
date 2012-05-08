package tradeworld;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    private final Map<BuildingId, Building> buildings = new HashMap<BuildingId, Building>();
    private final List<Vehicle> vehicles = new LinkedList<Vehicle>();
    private World3D world3d;
    private RoadNetwork roadNetwork;
    private TrainNetwork trainNetwork;
    private final Game game;
    private final Map<PlayerId, Player> players = new HashMap<PlayerId, Player>();
    private final Map<ObjectId<? extends Identifiable>, Identifiable> identifiables = new HashMap<ObjectId<? extends Identifiable>, Identifiable>();

    public Player getPlayer(PlayerId id) {
        if (!players.containsKey(id)) {
            throw new AssertionError("player with id " + id + " not found");
        }
        return players.get(id);
    }

    public Building getBuilding(BuildingId id) {
        if (!buildings.containsKey(id)) {
            throw new AssertionError("player with id " + id + " not found");
        }
        return buildings.get(id);
    }

    public <T> T getObject(ObjectId<T> id) {
        return null;
    }

    public Collection<Building> getBuildings() {
        return buildings.values();
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
        for (Building building : buildings.values()) {
            building.act();
        }


    }

    public Game getGame() {
        return game;
    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public void putBuilding(Building building) {
        building.setWorld(this);
        buildings.put(building.getBuildingId(), building);
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

        addPlayer(new Player("Kotuc", Color.RED, 1000000, this));
        addPlayer(new Player("Albert", Color.BLUE, 1000000, this));

        this.land = new Land3D(20, 20);
        this.roadNetwork = land.getRoadNetwork();
        roadNetwork.generateRandomWalk(new Random(57873297582975L));
        this.trainNetwork = land.getTrainNetwork();
        trainNetwork.generateRandomWalk(new Random(5328572357L));

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
