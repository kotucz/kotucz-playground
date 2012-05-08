package tradeworld;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.RoadNetwork.RoadPoint;
import tradeworld.graphics.Notification3D;
import tradeworld.graphics.Vehicle3D;

/**
 *
 * @author Kotuc
 */
public abstract class Vehicle {

    private final Vehicle3D model = new Vehicle3D();
    private RoadPoint destLong;
    private final Pos pos = new Pos();
//    private Point3d destShort = new Point3d();
    private Queue<RoadPoint> path = null;// new LinkedList<RoadPoint>();
    private World world;
    private long fuel;
    protected final Payload payload;
    private final Type type;
    private String name;
    private Player owner;
    private State state = State.WAIT;
    private Depot srcDepot;
    private RoadPoint srcRoadPoint;
    private Depot destDepot;
    private RoadPoint destRoadPoint;

    public void setPos(Point3d pos) {
        this.pos.set(pos);
        this.model.setPos(pos);
        this.model.refresh();
    }

    public Vehicle3D getModel() {
        return model;
    }

    public void setState(State state) {
        this.state = state;
        System.out.println("" + this + " " + state);
    }

    void act() {
        //  System.out.println("" + this + " " + state);
        switch (state) {
            case WAIT:
                for (Building building : world.getBuildings()) {
                    if (building instanceof Depot) {
                        if (srcDepot == null) {
                            srcDepot = (Depot) building;
                            srcRoadPoint = getRoadNetwork().getPoint(srcDepot.getEntrance());
                        } else {
                            // second depot
                            destDepot = (Depot) building;
                            destRoadPoint = getRoadNetwork().getPoint(destDepot.getEntrance());
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
                path = new LinkedList<RoadPoint>();
                path.addAll(findPath);
            } else {
                System.err.println("Path not found !");
                return false;
            }
        }
        return followPath();
    }

    public List<RoadPoint> findPath(RoadPoint target) {
        RoadNetwork roadNetwork = getWorld().getRoadNetwork();
        PathFinding pathFinding = new PathFinding(roadNetwork);
//        RoadPoint curr = roadNetwork.getPoint(
//                (int) Math.round(pos.x),
//                (int) Math.round(pos.y));
        RoadPoint curr = roadNetwork.getPoint(this.pos);
        return pathFinding.aStar(curr, target);
    }

    public boolean followPath() {
        if (path.isEmpty()) {
            return true;
        }

        if (navigateLocal(path.peek().getPos(), 0.02)) {
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

        model.setAngle(Math.atan2(vec.y, vec.x));
        vec.normalize();
        vec.scale(speed);
        pos.add(vec);
        model.setPos(pos);

        spentFuel();

        return false;
    }

    public Vehicle(Player owner, Type type, Point3d point) {
        name = toString();
        this.owner = owner;
        this.type = type;
        this.setPos(point);
        fuel = 2000;


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
    }

    RoadNetwork getRoadNetwork() {
        return world.getRoadNetwork();
    }

    public enum Type {

        SKODA120(0.2, Goods.Type.PETROL, 4000),
        LIGHT_TRUCK(0.2, Goods.Type.PETROL, 4000),
        STEAM_TRUCK(0.2, Goods.Type.PETROL, 4000);
        private final Goods.Type fuelType;
        private final double speed;
        private final long maxFuel;

        public double getSpeed() {
            return speed;
        }

        private Type(double speed, Goods.Type fuelType, long maxFuel) {
//            this.fuelType = fuelType;
            this.speed = speed;
            this.fuelType = fuelType;
            this.maxFuel = maxFuel;
        }
    }

    enum State {

        WAIT,
        GO_FOR_LOAD,
        LOADING,
        GO_FOR_UNLOAD,
        UNLOADING;
    }
}
