package kotucz.village.cubes;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import kotucz.village.common.Entities;
import kotucz.village.common.MyBox;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Multitexture;
import kotucz.village.tiles.Multitexture1;
import kotucz.village.tiles.Pos;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kotuc
 */
public class Cube {

//    public static final String ID_KEY = "CubeUserDataIdKey";

    //    private Vector3f destShort = new Vector3f();

//    static final AtomicInteger idGen = new AtomicInteger();


    private final String id = Entities.acquireUniqueKey(this);


    Vector3f posVector;
    Pos3D pos;

    Spatial spatial;
    private final MyBox box;
    private final RigidBodyControl control;
    private Vector3f velocity = new Vector3f();



    //    public Vehicle(Player owner, Type type, RoadPoint roadPoint, , AbstractGridPathNetwork network) {
    public Cube(CubeType type, Pos3D pos, Vector3f posVector, Material mat) {
        this.pos = pos;

        this.posVector = posVector;
//        this.dir = dir;

//        mat = mat.clone();
//        mat.setColor("Color", new ColorRGBA((float)Math.random(), (float)Math.random(),(float)Math.random(), 1f));

//        id = "Cube" + idGen.incrementAndGet();

        final float halfSize = 0.5f;
        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            final float halfSize = 1.25f;

            box = new MyBox(new Vector3f(-halfSize, -halfSize, -halfSize), new Vector3f(halfSize, halfSize, halfSize));
            Geometry reBoxg = new Geometry("kapota", box);
            reBoxg.setUserData(Entities.ID_KEY, id);
            reBoxg.setMaterial(mat);
            reBoxg.setLocalTranslation(new Vector3f(0, 0, 0));
            Multitexture1 mtex = new Multitexture1(new LinearGrid(16, 16));
            int off = 16 * 10;
            off += type.ordinal()*4;

            box.setTexture(MyBox.FACE_STATIC_S, mtex.getTex(off + 0));
            box.setTexture(MyBox.FACE_STATIC_E, mtex.getTex(off + 1));
            box.setTexture(MyBox.FACE_STATIC_N, mtex.getTex(off + 2));
            box.setTexture(MyBox.FACE_STATIC_W, mtex.getTex(off + 3));
            box.setTexture(MyBox.FACE_STATIC_TOP, mtex.getTex(off - 16));
            box.setTexture(MyBox.FACE_STATIC_BOTTOM, mtex.getTex(off + 16));
            reBoxg.setQueueBucket(RenderQueue.Bucket.Transparent);

//            Multitexture mtex2 = new Multitexture(256, 256);
//            box.setTexture(MyBox.FACE_TOP, mtex2.createRealSubtexture(16*(1), 11*16, 16*(1+1f), 12*16));

            this.spatial = reBoxg;
        }




        spatial.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(halfSize, halfSize, halfSize)), 0);

        spatial.addControl(control);

        spatial.setLocalTranslation(posVector);
        control.setPhysicsLocation(posVector);


    }





    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id+"("+pos+")";
    }


    public Spatial getSpatial() {
        return spatial;
    }

    public Pos3D getPos() {
        return pos;
    }
}

