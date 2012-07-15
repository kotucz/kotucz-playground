package kotucz.village.transport;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import kotucz.village.common.MyBox;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Multitexture1;
import kotucz.village.tiles.Pos;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kotuc
 */
public class Mineral {

    public static final String ID_KEY = "MineralUserDataIdKey";

    //    private Vector3f destShort = new Vector3f();

    static final AtomicInteger idGen = new AtomicInteger();


    private final String id;

    final GoodsType type;

    Vector3f posVector;
    Pos pos;

    Spatial spatial;



    //    public Vehicle(Player owner, Type type, RoadPoint roadPoint, , AbstractGridPathNetwork network) {
    public Mineral(GoodsType goodsType, Vector3f posVector, Material mat) {

        this.type = goodsType;

        this.posVector = posVector;

//        mat = mat.clone();
//        mat.setColor("Color", new ColorRGBA((float)Math.random(), (float)Math.random(),(float)Math.random(), 1f));

        id = "M"+idGen.incrementAndGet();
        final float halfSize = 0.125f;

        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            final float halfSize = 1.25f;

            MyBox box = new MyBox(new Vector3f(-halfSize, -halfSize, -halfSize), new Vector3f(halfSize, halfSize, halfSize));
            Geometry reBoxg = new Geometry("kapota", box);
            reBoxg.setUserData(ID_KEY, id);
            reBoxg.setMaterial(mat);
            reBoxg.setLocalTranslation(new Vector3f(0, 0, 0));
            Multitexture1 mtex = new Multitexture1(new LinearGrid(16, 16));
            final int off = 16*3 + type.ordinal();

            box.setTexture(MyBox.FACE_FRONT, mtex.getTex(off));
            box.setTexture(MyBox.FACE_RIGHT, mtex.getTex(off));
            box.setTexture(MyBox.FACE_BACK, mtex.getTex(off));
            box.setTexture(MyBox.FACE_BOTTOM, mtex.getTex(off));
            box.setTexture(MyBox.FACE_TOP, mtex.getTex(off));
            box.setTexture(MyBox.FACE_LEFT, mtex.getTex(off));


//            box.setTexture(MyBox.FACE_FRONT, mtex.getTex(off +3));
//            box.setTexture(MyBox.FACE_RIGHT, mtex.getTex(off +0));
//            box.setTexture(MyBox.FACE_BACK, mtex.getTex(off +4));
//            box.setTexture(MyBox.FACE_BOTTOM, mtex.getTex(off +5));
//            box.setTexture(MyBox.FACE_TOP, mtex.getTex(off +2));
//            box.setTexture(MyBox.FACE_LEFT, mtex.getTex(off +1));

//            reBoxg.setQueueBucket(RenderQueue.Bucket.Transparent);

            this.spatial = reBoxg;
        }


        RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(halfSize, halfSize, halfSize)), 1);
        spatial.addControl(control);
        control.setPhysicsLocation(posVector);

        spatial.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        updateModel();



//        this.model = new Vehicle3D(this);
//        this.setPos(point);

//        type = new Payload(type.maxPayload);
//        type = new Payload(1); //TODO: To be changed - depends on truck type
    }



    public Pos getPos() {
//         return network.getRoadPoint(posVector).getPos();
         return pos;
    }



    public Vector3f getPosVector() {
         return posVector;
    }



    public GoodsType getType() {
        return type;
    }





    public void updateModel() {
        spatial.setLocalTranslation(this.getPosVector());
    }


    @Override
    public String toString() {
        return type + " (Mineral)";
    }


    public Spatial getSpatial() {
        return spatial;
    }
}

