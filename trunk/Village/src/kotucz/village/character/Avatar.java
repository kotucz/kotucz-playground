package kotucz.village.character;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import kotucz.village.common.Entities;
import kotucz.village.common.MyBox;
import kotucz.village.game.MyGame;
import kotucz.village.game.Player;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Multitexture1;
import kotucz.village.tiles.Pos;
import kotucz.village.transport.GoodsType;
import kotucz.village.transport.Mineral;
import kotucz.village.transport.RoadPoint;
import kotucz.village.transport.VehicleBehavior;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kotuc
 */
public class Avatar {

    private final String id = Entities.acquireUniqueKey(this);
    private String name;

    private Player owner;

    private Vector3f posVector;

    private float heading;

    final Node node = new Node("Vozidlo");
    private VehicleBehavior behavior;


    public Avatar(Material mat, Vector3f pos) {

        this.name = "Jarda";


        this.posVector = pos;



        final float halfSize = 0.25f;

        Geometry reBoxg;
        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));

            MyBox box = new MyBox(new Vector3f(-halfSize, -halfSize, 0), new Vector3f(halfSize, halfSize, 2*halfSize));
            reBoxg = new Geometry("kapota", box);
            reBoxg.setUserData(Entities.ID_KEY, id);
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

        RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(halfSize, halfSize, halfSize)), 0);
        reBoxg.addControl(control);
//        control.setKinematic(true);
        control.setPhysicsLocation(posVector);


    }



    public Spatial getSpatial() {
        return node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

}
