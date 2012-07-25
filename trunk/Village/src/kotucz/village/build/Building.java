package kotucz.village.build;

import com.google.common.collect.EnumMultiset;
import com.google.common.collect.Multiset;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import kotucz.village.common.Dir4;
import kotucz.village.common.Dir8;
import kotucz.village.common.Entities;
import kotucz.village.common.MyBox;
import kotucz.village.game.Player;
import kotucz.village.tiles.Multitexture;
import kotucz.village.tiles.Pos;
import kotucz.village.transport.Depot;
import kotucz.village.transport.GoodsType;
import kotucz.village.transport.Vehicle;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kotuc
 */
public class Building implements Depot {

//    public static final String ID_KEY = "BuildingUserDataIdKey";
    private static final int LOADING_SPEED = 200;
    private final String id = Entities.acquireUniqueKey(this);

    final Pos pos;

    String name;

    final Node node = new Node("Budova");

    static final AtomicInteger idGen = new AtomicInteger();
//    private final String id;
    private final Material mat;

    Player owner;

    final Type type;
    //    private final List<Storage> storages = new LinkedList<Storage>();
//    private final List<Vehicle> vehicles = new LinkedList<Vehicle>();
    protected int progress;
    protected WorkingState workingState;
    final Multiset<GoodsType> goods = EnumMultiset.create(GoodsType.class);

    public Building(Pos pos, Material mat16, Type type, Player owner) {
        this.pos = pos;
        this.owner = owner;
        this.type = type;

        Set<Pos> occupyPosses = getOccupyPosses(pos, type);

        Multitexture multitexture = new Multitexture(256, 256);

        MyBox box;
        switch (type) {
            case FACTORY:
                box = new MyBox(new Vector3f(-1, 0, 0), new Vector3f(2, 2, 1));
                box.setTexture(MyBox.FACE_STATIC_S, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_E, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_N, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_W, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_TOP, multitexture.createSubtexture(64, 32, 80, 48));
                break;
            case MINE:
                box = new MyBox(new Vector3f(-1, 0, 0), new Vector3f(2, 1, 1));
                box.setTexture(MyBox.FACE_STATIC_S, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_E, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_N, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_W, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_TOP, multitexture.createSubtexture(64, 32, 80, 48));
                break;
            case HOUSE:
                box = new MyBox(new Vector3f(0, 0, 0), new Vector3f(2, 2, 1));
                box.setTexture(MyBox.FACE_STATIC_S, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_E, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_N, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_W, multitexture.createSubtexture(0, 32, 48, 48));
                box.setTexture(MyBox.FACE_STATIC_TOP, multitexture.createSubtexture(64, 32, 80, 48));
                break;
            default:
                // error
                box = null;
        }
//        id = "B" + idGen.incrementAndGet();

        Geometry reBoxg = new Geometry("brick3", box);
        reBoxg.setUserData(Entities.ID_KEY, id);
        mat = mat16.clone();
        reBoxg.setMaterial(mat);
        reBoxg.setLocalTranslation(new Vector3f(pos.x, pos.y, 0));

        node.attachChild(reBoxg);

//        for (Pos occupyPoss : occupyPosses) {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//
//            Geometry reBoxg = new Geometry("brick3", box);
//            reBoxg.setUserData("test", "budova13654");
//            reBoxg.setMaterial(mat16);
//            reBoxg.setLocalTranslation(new Vector3f(occupyPoss.x, occupyPoss.y, 0));
//
//            node.attachChild(reBoxg);
//        }


//        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            Geometry reBoxg = new Geometry("brick3", box);
//            reBoxg.setUserData("test", "budova13654");
//            reBoxg.setMaterial(mat16);
//            reBoxg.setLocalTranslation(new Vector3f(0, 0, 0));
//
//            node.attachChild(reBoxg);
//        }
//
//        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            Geometry reBoxg = new Geometry("brick3", box);
//            reBoxg.setMaterial(mat16);
//            reBoxg.setLocalTranslation(new Vector3f(1, 0, 0));
//
//
//            node.attachChild(reBoxg);
//        }
//        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            Geometry reBoxg = new Geometry("brick3", box);
//            reBoxg.setMaterial(mat16);
//            reBoxg.setLocalTranslation(new Vector3f(0, 1, 0));
//
//
//            node.attachChild(reBoxg);
//        }

//        node.setLocalTranslation(pos.x, pos.y, 0);

        progress = 0;
        workingState = WorkingState.WAITING_FOR_REQUEST;

        goods.add(GoodsType.WOOD, 20);
        goods.add(GoodsType.PETROL, 20);

    }

    public Node getNode() {
        return node;
    }


    public Player getOwner() {
        return owner;
    }

    public Set<Pos> getOccupiedPosses() {
        return getOccupyPosses(pos, type);
    }

    public static Set<Pos> getOccupyPosses(Pos pos, Type type) {
        HashSet<Pos> poses = new HashSet<Pos>();
        poses.add(pos);
        switch (type) {
            case FACTORY:
                poses.add(pos.inDir(Dir8.E));
                poses.add(pos.inDir(Dir8.NE));
                poses.add(pos.inDir(Dir8.N));
                poses.add(pos.inDir(Dir8.NW));
                poses.add(pos.inDir(Dir8.W));
                break;
            case MINE:
                poses.add(pos.inDir(Dir8.E));
//                poses.add(pos.inDir(Dir8.NE));
//                poses.add(pos.inDir(Dir8.N));
//                poses.add(pos.inDir(Dir8.NW));
                poses.add(pos.inDir(Dir8.W));
                break;
            case HOUSE:
                poses.add(pos.inDir(Dir8.E));
                poses.add(pos.inDir(Dir8.NE));
                poses.add(pos.inDir(Dir8.N));
//                poses.add(pos.inDir(Dir8.NW));
//                poses.add(pos.inDir(Dir8.W));
                break;
        }
        return poses;
    }

    public void mark(ColorRGBA color) {
        mat.setColor("Color", color);
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName() + "(" + super.toString() + ")";
    }

    public Pos getEntrancePos() {
        return pos.inDir(Dir4.S);
    }

    private void doStep() {

        if (WorkingState.WORKING.equals(workingState)) {
            if (progress >= LOADING_SPEED) {
                workingState = WorkingState.DONE;
            } else {
                progress++;
                System.out.println("" + progress);
            }
        }
    }

    @Override
    public boolean requestLoadVehicle(Vehicle vehicle, GoodsType type) {

        // TODO remove hack
        doStep();


        if (WorkingState.WAITING_FOR_REQUEST.equals(workingState)) {

            if (goods.contains(type)) {
//                operatedVehicle = vehicle;

                goods.remove(type);
                vehicle.setPayload(type);

                progress = 0;
                workingState = WorkingState.WORKING;

            }

            return false;
        }
        if (WorkingState.DONE.equals(workingState)) {

//            if (vehicle.equals(operatedVehicle)) {
            workingState = WorkingState.WAITING_FOR_REQUEST;
//                operatedVehicle = null;
            return true;
        }


        return false;
    }

    @Override
    public boolean requestUnloadVehicle(Vehicle vehicle, GoodsType type) {

        // TODO remove hack
        doStep();

//        if (vehicles.contains(vehicle)) {

        if (WorkingState.WAITING_FOR_REQUEST.equals(workingState)) {


//                Storage storage = findFreeStorage(1);

//                    operatedVehicle = vehicle;
//                    Goods targetGoods = new Goods(type, 1, owner);
            goods.add(type);
            vehicle.setPayload(null);

            progress = 0;
            workingState = WorkingState.WORKING;
        }
//            }
        if (WorkingState.DONE.equals(workingState)) {

//                if (vehicle.equals(operatedVehicle)) {
            workingState = WorkingState.WAITING_FOR_REQUEST;
//                    operatedVehicle = null;
            return true;

        }


        return false;
    }

    public WorkingState getWorkingState() {
        return workingState;
    }

    public enum Type {
        FACTORY,
        MINE,
        HOUSE
    }


    public enum WorkingState {

        WORKING,
        WAITING_FOR_REQUEST,
        DONE;
    }
}
