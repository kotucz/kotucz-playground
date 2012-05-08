package tradeworld;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.vecmath.Point3d;
import tradeworld.actions.SpawnSoldier;
import tradeworld.graphics.Land3D;
import tradeworld.graphics.Model3D;
import tradeworld.graphics.Notification3D;
import tradeworld.graphics.World3D;
import tradeworld.war.Soldier;

/**
 *
 * @author Kotuc
 */
public class World {

    private final Tile[][] tiles = new Tile[10][10];
    private World3D world3d;
    private RoadNetwork roadNetwork;
    private TrainNetwork trainNetwork;
    private final Game game;
    private final Map<BuildingId, Building> buildings = new HashMap<BuildingId, Building>();
    private final List<Vehicle> vehicles = new LinkedList<Vehicle>();
    private final Map<PlayerId, Player> players = new HashMap<PlayerId, Player>();
    private final Map<ObjectId<? extends Identifiable>, Identifiable> identifiables = new HashMap<ObjectId<? extends Identifiable>, Identifiable>();
    private final Set<WorldObject> objects = new HashSet<WorldObject>();
    private final Set<WorldObject> locals = new HashSet<WorldObject>();

    public Player getPlayer(PlayerId id) {
        if (!players.containsKey(id)) {
            throw new AssertionError("player with id " + id + " not found");
        }
        return players.get(id);
    }

    public Building getBuilding(BuildingId id) {
        if (!buildings.containsKey(id)) {
            throw new AssertionError("building with id " + id + " not found");
        }
        return buildings.get(id);
    }

    public <T> T getObject(ObjectId<T> id) {
        if (!identifiables.containsKey(id)) {
            throw new IllegalStateException("Unknown id: "+id);
        }
        return (T) identifiables.get(id);
    }

    public Collection<Building> getBuildings() {
        return buildings.values();
    }

    public World(Game game) {
        this.game = game;
        this.world3d = new World3D(this);
        createObjects();
        System.out.println("World created");
    }

    void moveAll(Time time) {

//        synchronized (objects) {
//            Set<WorldObject> copy = new HashSet<WorldObject>(objects);
//        }

        for (WorldObject worldObject : objects) {
            worldObject.act(time);
        }

//        for (WorldObject worldObject : locals) {
//            worldObject.act(time);
//        }

        for (WorldObject worldObject : toRemove) {
            world3d.removeModel(worldObject.getModel());
        }

//        locals.removeAll(toRemove);
        objects.removeAll(toRemove);
        toRemove.clear();


//        for (Vehicle vehicle : vehicles) {
//            vehicle.act(time);
//        }
//        for (Building building : buildings.values()) {
////            building.act(time);
//        }

    }
    private Set<WorldObject> toRemove = new HashSet<WorldObject>();

    public Game getGame() {
        return game;
    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public void putObject(WorldObject object) {

        object.setWorld(this);

        objects.add(object);

        if (object instanceof Identifiable<?>) {
            identifiables.put(((Identifiable<? extends Identifiable>) object).getId(), (Identifiable) object);
        }

        Model3D model = object.getModel();
        world3d.addModel(model);
        world3d.associateObject(model, object);


    }

    public Set<WorldObject> getObjects() {
        return objects;
    }

    public void putBuilding(Building building) {
//        Building3D building3D = new Building3D(building);
//        building.model = building3D;
//        world3d.addBuilding(building3D);
//        world3d.associateBuilding(building3D, building);
//        world3d.associateBuilding(building.getModel(), building);

//        building.setWorld(this);

        putObject(building);

        buildings.put(building.getBuildingId(), building);


//        world3d.addBuilding(building.getModel());


    }

    public void putVehicle(Vehicle vehicle) {

        putObject(vehicle);

//        vehicle.setWorld(this);
        vehicles.add(vehicle);
//        Vehicle3D vehicle3D = new Vehicle3D(vehicle);
//        world3d.addVehicle(vehicle.getModel());
//        world3d.addVehicle(vehicle3D);
//        world3d.associateVehicle(vehicle3D, vehicle);
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

        this.land = new Land3D(64, 64);
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
        for (int i = 0; i < 0; i++) {
            System.out.print(i + ", ");
            final Vehicle vehicle = new Truck(null, Vehicle.Type.SKODA120, roadNetwork.randomRoadPoint().getPos());
            this.putVehicle(vehicle);
//            vehicleThread.add(vehicle);
        }
        System.out.println();

        System.out.print("Trains: ");
        for (int i = 0; i < 0; i++) {
            System.out.print(i + ", ");
            final Vehicle vehicle = new Truck(null, Vehicle.Type.TRAIN1, trainNetwork.randomRoadPoint().getPos());
            this.putVehicle(vehicle);
//            vehicleThread.add(vehicle);
        }
        System.out.println();

        System.out.print("Soldiers ");
        for (int i = 0; i < 1; i++) {
            System.out.print(i + ", ");
//            final Soldier sold = new Soldier(new Point3d(Math.random() * 20, Math.random() * 20, 0));
//            sold.id = new ObjectId<Soldier>(sold, i);
//            this.putObject(sold);

//            getGame().scheduleAction(new SpawnSoldier(new Point3d(Math.random() * 20, Math.random() * 20, 0)));

//            vehicleThread.add(vehicle);
        }
        System.out.println();

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

    public void remove(WorldObject object) {
        toRemove.add(object);
//        objects.remove(object);
    }
}
