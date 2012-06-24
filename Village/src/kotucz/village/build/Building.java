package kotucz.village.build;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import kotucz.village.common.Dir4;
import kotucz.village.common.Dir8;
import kotucz.village.common.MyBox;
import kotucz.village.game.Player;
import kotucz.village.tiles.Multitexture;
import kotucz.village.tiles.Pos;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Kotuc
 */
public class Building {

    public static final String ID_KEY = "BuildingUserDataIdKey";

    final Pos pos;

    String name;

    final Node node = new Node("Budova");

    static final AtomicInteger idGen = new AtomicInteger();
    private final String id;
    private final Material mat;

    Player owner;

    public Building(Pos pos, Material mat16, Player owner) {
        this.pos = pos;
        this.owner = owner;

        Set<Pos> occupyPosses = getOccupyPosses(pos);

        Multitexture multitexture = new Multitexture(256, 256);

        MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(3, 2, 1));
        box.setTexture(MyBox.FACE_STATIC_S, multitexture.createSubtexture(0, 32, 48, 48));
        box.setTexture(MyBox.FACE_STATIC_E, multitexture.createSubtexture(0, 32, 48, 48));
        box.setTexture(MyBox.FACE_STATIC_N, multitexture.createSubtexture(0, 32, 48, 48));
        box.setTexture(MyBox.FACE_STATIC_W, multitexture.createSubtexture(0, 32, 48, 48));
        box.setTexture(MyBox.FACE_STATIC_TOP, multitexture.createSubtexture(64, 32, 80, 48));

        id = "B"+idGen.incrementAndGet();

        Geometry reBoxg = new Geometry("brick3", box);
        reBoxg.setUserData(ID_KEY, id);
        mat = mat16.clone();
        reBoxg.setMaterial(mat);
        reBoxg.setLocalTranslation(new Vector3f(pos.x-1, pos.y, 0));

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

    }

    public Node getNode() {
        return node;
    }


    public Player getOwner() {
        return owner;
    }

    public Set<Pos> getOccupiedPosses() {
        return getOccupyPosses(pos);
    }

    public static Set<Pos> getOccupyPosses(Pos pos) {
        HashSet<Pos> poses = new HashSet<Pos>();
        poses.add(pos);
        poses.add(pos.inDir(Dir8.E));
        poses.add(pos.inDir(Dir8.NE));
        poses.add(pos.inDir(Dir8.N));
        poses.add(pos.inDir(Dir8.NW));
        poses.add(pos.inDir(Dir8.W));
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
        return this.getName() +"("+  super.toString()+")";
    }

    public Pos getEntrancePos() {
        return pos.inDir(Dir4.S);
    }

}
