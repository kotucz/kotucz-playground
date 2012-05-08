package tradeworld;

import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.PathNetwork.RoadPoint;
import tradeworld.graphics.Notification3D;
import tradeworld.graphics.Vehicle3D;

/**
 *
 * @author Kotuc
 */
public abstract class Vehicle {

    private final Vehicle3D model;
    private RoadPoint destLong;
    private final Pos pos = new Pos();
//    private Point3d destShort = new Point3d();
    private LinkedList<RoadPoint> path = null;// new LinkedList<RoadPoint>();
    private World world;
    private long fuel;
    protected final Payload payload;
    private final Type type;
    private String name;
    private Player owner;
    private State state = State.RANDOM;
    private Depot srcDepot;
    private RoadPoint srcRoadPoint;
    private Depot destDepot;
    private RoadPoint destRoadPoint;
    private Trajectory trajectory;
    private PathNetwork network;

    public void setPos(Point3d pos) {
        this.pos.set(pos);
        this.model.setPos(pos);
        this.model.refresh();
    }

    public Vehicle3D getModel() {
        return model;
    }

    void setState(State state) {
        this.state = state;
        System.out.println("" + this + " " + state);
    }

    void act() {
        //  System.out.println("" + this + " " + state);
        switch (state) {
            case RANDOM:
                destLong = network.randomRoadPoint();
                if (travelTo(destLong)) {
                    path = null;
                }
                break;
            case WAIT:
                for (Building building : world.getBuildings()) {
                    if (building instanceof Depot) {
                        if (srcDepot == null) {
                            srcDepot = (Depot) building;
                            srcRoadPoint = getNetwork().getPoint(srcDepot.getEntrance());
                        } else {
                            // second depot
                            destDepot = (Depot) building;
                            destRoadPoint = getNetwork().getPoint(destDepot.getEntrance());
                        }
                    }
                }
                if (destDepot != null) {
                    setState(State.GO_FOR_LOAD);
                    System.out.println("Depos found " + srcRoadPoint + " -> " + destRoadPoint);

                } else {
                    srcDepot = null;
                }
                break;
            case GO_FOR_LOAD:
                if (travelTo(srcRoadPoint)) {
                    this.path = null; // reset path
                    setState(State.LOADING);
                    srcDepot.addVehicle(this);
                }
                break;
            case LOADING:
                if (srcDepot.requestLoadVehicle(this, Goods.Type.WOOD, srcDepot.getOwner())) {
//                    destLong = null; // dest depot
                    setState(State.GO_FOR_UNLOAD);
                    srcDepot.removeVehicle(this);
                }
                break;
            case GO_FOR_UNLOAD:
                if (travelTo(destRoadPoint)) {
                    this.path = null; // reset path
                    setState(State.UNLOADING);
                    destDepot.addVehicle(this);
                }
                break;
            case UNLOADING:
                if (destDepot.requestUnloadVehicle(this, Goods.Type.WOOD, destDepot.getOwner())) {
//                    destLong = null; // next depot
                    setState(State.GO_FOR_LOAD);
                    destDepot.removeVehicle(this);
                }
                break;


        }

//        if (travelTo(destLong)) {
//            destLong = getWorld().getRoadNetwork().randomRoadPoint();
//            path = null;
//        }

        model.refresh();
//        if (!navigate(destShort, 0.01)) {
//            refresh();
//        } else {
//            destShort.x = Math.random() * 20;
//            destShort.y = Math.random() * 20;
//            getWorld().add(new Notification3D(destShort));
//        }

    }

    /**
     *
     * @param point
     * @return true when reached, false during way
     */
    public boolean travelTo(RoadPoint point) {
//        if (point.getPos().distance(pos) < type.getSpeed()) {
//            return true;
//        }
        if (path == null) {
            // create new path
//            destLong = getWorld().getRoadNetwork().randomRoadPoint();
            List<RoadPoint> findPath = findPath(point);
            if (findPath != null) {
                int i = 0;
                for (RoadPoint roadPoint : findPath) {
                    getWorld().showNotification(
                            Notification3D.createTextNotification("R" + i, new Color3f(1, 0, 0)),
                            roadPoint.getPos());
                    i++;
                }
                this.path = new LinkedList<RoadPoint>();
                this.path.addAll(findPath);
                this.trajectory = new Trajectory(path, (Element.ROAD.equals(type.element)?0.25:0));
                this.t = 0;
            } else {
                System.err.println("Path not found !");
                return false;
            }
        }
        return followTrajectory();
//        return followPath();
    }

    public List<RoadPoint> findPath(RoadPoint target) {
        PathFinding pathFinding = new PathFinding(network);
//        RoadPoint curr = roadNetwork.getPoint(
//                (int) Math.round(pos.x),
//                (int) Math.round(pos.y));
        RoadPoint curr = network.getPoint(this.pos);
        return pathFinding.aStar(curr, target);
    }
    private double t;

    public boolean followTrajectory() {
        if (t > trajectory.length()) {
            return true;
        }

        t += 0.02;

        setPos(trajectory.getPoint(t));
        Vector3d vec = trajectory.getVector(t);

        double angle = Math.atan2(vec.y, vec.x);
        model.setAngle(angle);

        return false;
    }

    public boolean followPath() {
        if (path.isEmpty()) {
            return true;
        }

        double lookAhead = 1;

        Point3d tgt = new Point3d(path.peek().getPos());

//        if (pos.distance(tgt) < lookAhead && path.size() > 1) {
//            if (pos.distance(tgt) > 0.5) {
//                tgt.interpolate(path.get(1).getPos(), (pos.distance(tgt) / lookAhead));
//            }
//        }

        if (navigateLocal(tgt, 0.02)) {
            RoadPoint poll = path.poll(); // remove
        }

        return false;
    }

    /**
     * Goes to dest point not faster than speed (per one call)
     *
     * @param dest
     * @param speed
     * @return true if reached destination; false otherwise
     */
    public boolean navigateLocal(Point3d dest, double speed) {
        if (dest == null) {
            return true;
        }

        Vector3d vec = new Vector3d(dest);

        vec.sub(pos);
        if (vec.lengthSquared() <= speed * speed) {
            pos.set(dest);
            model.setPos(pos);
            return true;
        }

        double atan2 = Math.atan2(vec.y, vec.x);
        double angle = model.getAngle();

        double diff = atan2 - angle;
        while (diff > Math.PI) {
            diff -= 2 * Math.PI;
        }

        while (diff < Math.PI) {
            diff += 2 * Math.PI;
        }

        angle += diff;

        model.setAngle(angle);

        vec.set(Math.cos(angle), Math.sin(angle), 0);

        vec.normalize();
        vec.scale(speed);
        pos.add(vec);
        model.setPos(pos);

        spentFuel();

        return false;
    }

    public Vehicle(Player owner, Type type, Point3d point) {
        this.type = type;
        this.name = type.toString();
        this.owner = owner;

        this.fuel = 2000;

        this.model = new Vehicle3D(type.modelName);
        this.setPos(point);

//        payload = new Payload(type.maxPayload);
        payload =
                new Payload(1); //TODO: To be changed - depends on truck type
    }

    public void spentFuel() {

        fuel -= 1;
    }

    public void addFuel(long fuel) {

        this.fuel += fuel;
    }

    public long getFuel() {

        return fuel;
    }

    public Goods.Type getFuelType() {

        return type.fuelType;
    }

    public long getMaxFuel() {

        return type.maxFuel;
    }

    public void load(Goods goods) {

        payload.addGoods(goods);
    }

    public void unload(Goods goods) {

        payload.removeGoods(goods);
    }

    public String getName() {

        return name;
    }

    public Player getOwner() {

        return owner;
    }

    public Payload getPayload() {

        return payload;
    }

    public World getWorld() {
        return world;
    }

    void setWorld(World world) {
        this.world = world;
        switch (type.element) {
            case ROAD:
                this.network = world.getRoadNetwork();
                break;
            case TRAIN:
                this.network = world.getTrainNetwork();
                break;
            default:
                throw new UnsupportedOperationException(" " + type.element);
        }
    }

    private PathNetwork getNetwork() {
        return network;
    }

    @Override
    public String toString() {
        return type + " (" + owner + ")";
    }

    public enum Type {

        SKODA120(Goods.Type.PETROL, 0.2, 4000, Element.ROAD, "Car1"),
        LIGHT_TRUCK(Goods.Type.PETROL, 0.2, 4000, Element.TRAIN, "Train1"),
        TRAIN1(Goods.Type.PETROL, 0.2, 4000, Element.TRAIN, "Train1"),
        STEAM_TRUCK(Goods.Type.PETROL, 0.2, 4000, Element.ROAD, "Car1");
        private final Goods.Type fuelType;
        private final double speed;
        private final long maxFuel;
        private final Element element;
        private final String modelName;

        public double getSpeed() {
            return speed;
        }

        private Type(Goods.Type fuelType, double speed, long maxFuel, Element element, String modelName) {
            this.fuelType = fuelType;
            this.speed = speed;
            this.maxFuel = maxFuel;
            this.element = element;
            this.modelName = modelName;
        }
    }

    public enum Element {
        ROAD, TRAIN, WATTER, AIR;
    }

    enum State {
        RANDOM,
        WAIT,
        GO_FOR_LOAD,
        LOADING,
        GO_FOR_UNLOAD,
        UNLOADING;
    }
}
