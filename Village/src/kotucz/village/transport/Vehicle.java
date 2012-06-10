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
import kotucz.village.tiles.Pos;

import java.util.List;

/**
 * @author Kotuc
 */
public class Vehicle {


    //    private Vector3f destShort = new Vector3f();

    private long fuel;
    protected final Payload payload;
    private PathNetwork network;
    protected final Type type;
    private Player owner;
    private String name;

    Vector3f pos;
    float heading;

    final Node node = new Node("Vozidlo");
    private final VehicleBehavior behavior = new VehicleBehavior(this);

    public Vehicle(Player owner, Type type, RoadPoint pos, Material mat, PathNetwork network) {
        this.type = type;
        this.name = type.toString();
        this.owner = owner;
        this.network = network;

        this.fuel = 2000;

        this.pos = pos.getPosVector();

        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
            final float halfSize = 0.25f;

            MyBox box = new MyBox(new Vector3f(-halfSize, -halfSize, 0), new Vector3f(halfSize, halfSize, 2*halfSize));
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
        behavior.setSrcDepot(srcDepot);
    }

    public void setDestDepot(Depot destDepot) {
        behavior.setDestDepot(destDepot);
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
        //        if (travelTo(destLong)) {
//            destLong = getWorld().getRoadNetwork().randomRoadPoint();
//            path = null;
//        }
//        model.refresh();
        behavior.act(time);
    }

    /**
     * @param point
     * @return true when reached, false during way
     */
    public boolean travelTo(RoadPoint point, float time) {
//        if (point.getPosVector().distance(vector) < type.getSpeed()) {
//            return true;
//        }
        //        return followPath();
        return behavior.travelTo(point, time);
    }

    public List<RoadPoint> findPath(RoadPoint target) {
        //        RoadPoint curr = roadNetwork.getPoint(
//                (int) Math.round(vector.x),
//                (int) Math.round(vector.y));
        return behavior.findPath(target);
    }

    public boolean followTrajectory(float time) {

        //        Vector3d vec = trajectory.getVector(t);
//        double angle = Math.atan2(vec.y, vec.x);
//        model.setAngle(angle);

        return behavior.followTrajectory(time);
    }

    public Pos getP() {
         return network.getPoint(pos).getPos();
    }

    public boolean followPath() {

        //        if (vector.distance(tgt) < lookAhead && path.size() > 1) {
//            if (vector.distance(tgt) > 0.5) {
//                tgt.interpolate(path.get(1).getPosVector(), (vector.distance(tgt) / lookAhead));
//            }
//        }

        return behavior.followPath();
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
//        vec.sub(vector);
//        if (vec.lengthSquared() <= speed * speed) {
//            vector.set(dest);
//            model.setPos(vector);
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
//        vector.add(vec);
//        model.setPos(vector);
//
//        spentFuel();
//
//        return false;
        return behavior.navigateLocal(dest, speed);
    }

    public Vector3f getPos() {
        if (behavior.trajectory == null) {
            return pos;
        }
        return behavior.trajectory.getPoint(behavior.t);
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

    public PathNetwork getNetwork() {
        return network;
    }

    public String getStatusMessage() {
        return behavior.getStatusMessage();
    }

    public void updateModel() {

        if (behavior.trajectory != null) {
//            this.pos = trajectory.getPoint(t);
//            node.setLocalTranslation(pos);
            Vector3f point = behavior.trajectory.getPoint(behavior.t);
            this.pos = point;
            System.out.println(""+this.name+" "+ behavior.t +" "+point);
            node.setLocalTranslation(point);

        }


        float heading = 0;

        Quaternion rotation = new Quaternion();
//        rotation.fromAngleAxis(heading, MyGame.UP);

//        rotation

//        tra


        if (behavior.trajectory != null) {
            Vector3f vec = behavior.trajectory.getVector(behavior.t);
//


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

    public void setPos(Vector3f point) {

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

    public Node getNode() {
        return node;
    }
}
