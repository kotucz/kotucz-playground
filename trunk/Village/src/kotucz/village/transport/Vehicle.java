package kotucz.village.transport;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import kotucz.village.common.MyBox;
import kotucz.village.game.MyGame;
import kotucz.village.game.Player;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Multitexture1;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Kotuc
 */
public class Vehicle {

    private RoadPoint destLong;
    //    private Vector3f destShort = new Vector3f();
    private LinkedList<RoadPoint> path = null;// new LinkedList<RoadPoint>();
    private long fuel;
    protected final Payload payload;
    private State state = State.RANDOM;
    private Depot srcDepot;
    private RoadPoint srcRoadPoint;
    private Depot destDepot;
    private RoadPoint destRoadPoint;
    private Trajectory trajectory;
    private PathNetwork network;
    protected final Type type;
    private Player owner;
    private String name;

    Vector3f pos;
    float heading;

    final Node node = new Node("Vozidlo");

    public Vehicle(Player owner, Type type, RoadPoint pos, Material mat, PathNetwork network) {
        this.type = type;
        this.name = type.toString();
        this.owner = owner;
        this.network = network;

        this.fuel = 2000;

        this.pos = pos.getPos();

        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
            MyBox box = new MyBox(new Vector3f(-0.5f, -0.5f, 0), new Vector3f(0.5f, 0.5f, 1));
            Geometry reBoxg = new Geometry("kapota", box);
            reBoxg.setUserData("test", "auto13654");
            reBoxg.setMaterial(mat);
            reBoxg.setLocalTranslation(new Vector3f(0, 0, 0));
            Multitexture1 mtex = new Multitexture1(new LinearGrid(4, 4));
            box.setTexture(MyBox.FACE_FRONT, mtex.getTex(3));
            box.setTexture(MyBox.FACE_RIGHT, mtex.getTex(0));
            box.setTexture(MyBox.FACE_BACK, mtex.getTex(4));
            box.setTexture(MyBox.FACE_BOTTOM, mtex.getTex(5));
            box.setTexture(MyBox.FACE_TOP, mtex.getTex(2));
            box.setTexture(MyBox.FACE_LEFT, mtex.getTex(1));
            reBoxg.setQueueBucket(RenderQueue.Bucket.Transparent);

            node.attachChild(reBoxg);
        }


        updateModel();

//        this.model = new Vehicle3D(this);
//        this.setPos(point);

//        payload = new Payload(type.maxPayload);
        payload = new Payload(1); //TODO: To be changed - depends on truck type
    }

    public void setSrcDepot(Depot srcDepot) {
        this.srcDepot = srcDepot;
        this.srcRoadPoint = getNetwork().getPoint(srcDepot.getEntrance());
    }

    public void setDestDepot(Depot destDepot) {
        this.destDepot = destDepot;
        this.destRoadPoint = getNetwork().getPoint(destDepot.getEntrance());
        this.path = null;
        setState(State.GO_FOR_LOAD);
    }

    void setState(State state) {
        this.state = state;
        System.out.println("" + this + " " + state);
    }

//    @Override
//    public void addingTo(World world) {
//        switch (type.element) {
//            case ROAD:
//                this.network = world.getRoadNetwork();
//                break;
//            case TRAIN:
//                this.network = world.getTrainNetwork();
//                break;
//            default:
//                throw new UnsupportedOperationException(" " + type.element);
//        }
//    }


    public void act(float time) {
        //  System.out.println("" + this + " " + state);
        switch (state) {
            case RANDOM:
                if (path == null) {
                    destLong = network.randomRoadPoint();
                }
                if (travelTo(destLong, time)) {
                    path = null;
                }
                break;
            case WAIT:
                // TODO uncomment/fix
//                for (Building building : getWorld().getBuildings()) {
//                    if (building instanceof Depot) {
//                        if (srcDepot == null) {
//                            setSrcDepot((Depot) building);
//                        } else {
//                            // second depot
//                            setDestDepot((Depot) building);
//                        }
//                    }
//                }
                if (destDepot != null) {
                    setState(State.GO_FOR_LOAD);
                    System.out.println("Depos found " + srcRoadPoint + " -> " + destRoadPoint);
                } else {
                    srcDepot = null;
                }
                break;
            case GO_FOR_LOAD:
                if (travelTo(srcRoadPoint, time)) {
                    this.path = null;
                    // reset path
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
                if (travelTo(destRoadPoint, time)) {
                    this.path = null;
                    // reset path
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
//        model.refresh();
        updateModel();
    }

    /**
     * @param point
     * @return true when reached, false during way
     */
    public boolean travelTo(RoadPoint point, float time) {
//        if (point.getPos().distance(pos) < type.getSpeed()) {
//            return true;
//        }
        if (path == null) {
            // create new path
//            destLong = getWorld().getRoadNetwork().randomRoadPoint();
            List<RoadPoint> findPath = findPath(point);
            if (findPath != null) {
                int i = 0;
                // debug trace path
//                for (RoadPoint roadPoint : findPath) {
//                    getWorld().showNotification(
//                            Notification3D.createTextNotification("R" + i, new Color3f(1, 0, 0)),
//                            roadPoint.getPos());
//                    i++;
//                }
                this.path = new LinkedList<RoadPoint>();
                this.path.addAll(findPath);
//                this.trajectory = new Trajectory(path, (Element.ROAD.equals(type.element) ? 0.25 : 0));
                this.trajectory = new Trajectory(path);
                this.t = 0;
            } else {
                System.err.println("Path not found !");
                return false;
            }
        }
        return followTrajectory(time);
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

    private float t;

    public boolean followTrajectory(float time) {
        if (t > trajectory.length()) {
            return true;
        }

        t += time;

        this.pos = trajectory.getPoint(t);

//        Vector3d vec = trajectory.getVector(t);
//        double angle = Math.atan2(vec.y, vec.x);
//        model.setAngle(angle);

        return false;
    }

    public boolean followPath() {
        if (path.isEmpty()) {
            return true;
        }

        double lookAhead = 1;

        Vector3f tgt = new Vector3f(path.peek().getPos());

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
    public boolean navigateLocal(Vector3f dest, double speed) {
//        if (dest == null) {
//            return true;
//        }
//
//        Vector3d vec = new Vector3d(dest);
//
//        vec.sub(pos);
//        if (vec.lengthSquared() <= speed * speed) {
//            pos.set(dest);
//            model.setPos(pos);
//            return true;
//        }
//
//        double atan2 = Math.atan2(vec.y, vec.x);
//        double angle = model.getAngle();
//
//        double diff = atan2 - angle;
//        while (diff > Math.PI) {
//            diff -= 2 * Math.PI;
//        }
//
//        while (diff < Math.PI) {
//            diff += 2 * Math.PI;
//        }
//
//        angle += diff;
//
//        model.setAngle(angle);
//
//        vec.set(Math.cos(angle), Math.sin(angle), 0);
//
//        vec.normalize();
//        vec.scale(speed);
//        pos.add(vec);
//        model.setPos(pos);
//
//        spentFuel();
//
//        return false;
        throw new UnsupportedOperationException("obsollette or uncomment");
    }

    public Vector3f getPos() {
        if (trajectory == null) {
            return pos;
        }
        return trajectory.getPoint(t);
    }

//    public float getAngle() {
//        if (trajectory == null) {
//            return 0;
//        }
////        Vector3f vec = trajectory.getVector(t);
////        return Math.atan2(vec.y, vec.x);
//    }

    public String getModelName() {
        return type.modelName;
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

    public Payload getPayload() {

        return payload;
    }

    private PathNetwork getNetwork() {
        return network;
    }

    public String getStatusMessage() {
        switch (state) {
            case GO_FOR_LOAD:
                return "Heading to src " + srcDepot;
            case GO_FOR_UNLOAD:
                return "Heading to dest " + destDepot;
            default:
                return state.toString();
        }
    }

    public void updateModel() {

        if (trajectory != null) {
        this.pos = trajectory.getPoint(t);
        }
        node.setLocalTranslation(pos);

        float heading = 0;

        Quaternion rotation = new Quaternion();
//        rotation.fromAngleAxis(heading, MyGame.UP);

//        rotation

//        tra


        if (trajectory != null) {
            Vector3f vec = trajectory.getVector(t);



//        double angle = Math.atan2(vec.y, vec.x);
//        model.setAngle(angle);

//           rotation.lookAt(vec, MyGame.UP);
//            node.setLocalRotation(rotation.fromAxes(vec, vec.cross(MyGame.UP), MyGame.UP));
            node.setLocalRotation(rotation.fromAxes(vec, MyGame.UP.cross(vec), MyGame.UP));
        }

//        node.setLocalRotation(rotation);
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

    public Node getNode() {
        return node;
    }
}
