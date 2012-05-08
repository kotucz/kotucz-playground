package tradeworld;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    private Queue<RoadPoint> path = new LinkedList<RoadPoint>();
    private World world;
    private double fuel;
    protected final Payload payload;
    private final Type type;
    private String name;
    private Player owner;

    public void setPos(Point3d pos) {
        this.pos.set(pos);
        this.model.setPos(pos);
        this.model.refresh();
    }

    public Vehicle3D getModel() {
        return model;
    }

    void act() {
        if (followPath()) {
            // create new path
            destLong = getWorld().getRoadNetwork().randomRoadPoint();
            List<RoadPoint> findPath = findPath(destLong);
            if (findPath != null) {
                for (RoadPoint roadPoint : findPath) {
                    getWorld().showNotification(new Notification3D(roadPoint.getPos()));
                }
                path.addAll(findPath);
            } else {
                System.out.println("Path not found !");
            }

        }
        model.refresh();
//        if (!navigate(destShort, 0.01)) {
//            refresh();
//        } else {
//            destShort.x = Math.random() * 20;
//            destShort.y = Math.random() * 20;
//            getWorld().add(new Notification3D(destShort));
//        }
    }

    public List<RoadPoint> findPath(RoadPoint target) {
        RoadNetwork roadNetwork = getWorld().getRoadNetwork();
        PathFinding pathFinding = new PathFinding(roadNetwork);
        RoadPoint curr = roadNetwork.getPoint(
                (int) Math.round(pos.x),
                (int) Math.round(pos.y));
        return pathFinding.aStar(curr, target);
    }

    public boolean followPath() {
        if (path.isEmpty()) {
            return true;
        }
        if (navigate(path.peek().getPos(), 0.02)) {
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
    public boolean navigate(Point3d dest, double speed) {
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
        return false;
    }

    public Vehicle(Player owner, Type type, Point3d point) {
        name = toString();
        this.owner = owner;
        this.type = type;
        this.setPos(point);
//        payload = new Payload(type.maxPayload);
        payload = new Payload(10);
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

        SKODA120, LIGHT_TRUCK, STEAM_TRUCK;
        private Goods.Type fuelType;
    }
}
