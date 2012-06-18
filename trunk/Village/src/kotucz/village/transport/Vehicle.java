package kotucz.village.transport;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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

/**
 * @author Kotuc
 */
public class Vehicle {


    //    private Vector3f destShort = new Vector3f();

    private long fuel;
    protected final Payload payload;
//    private AbstractGridPathNetwork network;
    protected final Type type;
    private Player owner;
    private String name;

    Pos requestPos;
    Pos reservedPos;

    Vector3f posVector;
    Pos pos;

    float heading;

    final Node node = new Node("Vozidlo");
    private VehicleBehavior behavior;

//    public Vehicle(Player owner, Type type, RoadPoint roadPoint, Material mat, AbstractGridPathNetwork network) {
    public Vehicle(Player owner, Type type, RoadPoint roadPoint, Material mat) {
        this.type = type;
        this.name = type.toString();
        this.owner = owner;
//        this.network = network;

        this.fuel = 2000;

        this.reservedPos = roadPoint.getPos();
        this.posVector = roadPoint.getPosVector();

        mat = mat.clone();
        mat.setColor("Color", new ColorRGBA((float)Math.random(), (float)Math.random(),(float)Math.random(), 1f));

        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
            final float halfSize = 0.25f;

            MyBox box = new MyBox(new Vector3f(-halfSize, -halfSize, 0), new Vector3f(halfSize, halfSize, 2*halfSize));
            Geometry reBoxg = new Geometry("kapota", box);
            reBoxg.setUserData("test", "auto13654");
            reBoxg.setMaterial(mat);
            reBoxg.setLocalTranslation(new Vector3f(0, 0, 0));
            Multitexture1 mtex = new Multitexture1(new LinearGrid(16, 16));
            final int off = 16;
            box.setTexture(MyBox.FACE_FRONT, mtex.getTex(off +3));
            box.setTexture(MyBox.FACE_RIGHT, mtex.getTex(off +0));
            box.setTexture(MyBox.FACE_BACK, mtex.getTex(off +4));
            box.setTexture(MyBox.FACE_BOTTOM, mtex.getTex(off +5));
            box.setTexture(MyBox.FACE_TOP, mtex.getTex(off +2));
            box.setTexture(MyBox.FACE_LEFT, mtex.getTex(off +1));
            reBoxg.setQueueBucket(RenderQueue.Bucket.Transparent);

            node.attachChild(reBoxg);
        }


//        updateModel();

//        this.model = new Vehicle3D(this);
//        this.setPos(point);

//        payload = new Payload(type.maxPayload);
        payload = new Payload(1); //TODO: To be changed - depends on truck type
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

    public void setBehavior(VehicleBehavior behavior) {
        this.behavior = behavior;
    }


    public Pos getPos() {
//         return network.getPoint(posVector).getPos();
         return pos;
    }



    public Vector3f getPosVector() {
        if (behavior.trajectory == null) {
            return posVector;
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

//    public AbstractGridPathNetwork getNetwork() {
//        return network;
//    }

    public String getStatusMessage() {
        return behavior.getStatusMessage();
    }

//    public void updateModel() {
//
//        if (behavior.trajectory != null) {
////            this.posVector = trajectory.contains(t);
////            node.setLocalTranslation(posVector);
//            Vector3f point = behavior.trajectory.getPoint(behavior.t);
//            this.posVector = point;
////            System.out.println(""+this.name+" "+ behavior.t +" "+point);
//            node.setLocalTranslation(point);
//
//        }
//
//
//        float heading = 0;
//
//        Quaternion rotation = new Quaternion();
////        rotation.fromAngleAxis(heading, MyGame.UP);
//
////        rotation
//
////        tra
//
//
//        if (behavior.trajectory != null) {
//            Vector3f vec = behavior.trajectory.getVector(behavior.t);
////
//
//
////        double angle = Math.atan2(vec.y, vec.x);
////        model.setAngle(angle);
//
////           rotation.lookAt(vec, MyGame.UP);
////            node.setLocalRotation(rotation.fromAxes(vec, vec.cross(MyGame.UP), MyGame.UP));
//            node.setLocalRotation(rotation.fromAxes(vec, MyGame.UP.cross(vec), MyGame.UP));
//        }
//
////        node.setLocalRotation(rotation);
//    }


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

