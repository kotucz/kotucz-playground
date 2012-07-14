package kotucz.village.transport;

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
import kotucz.village.common.MyBox;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Multitexture;
import kotucz.village.tiles.Multitexture1;
import kotucz.village.tiles.Pos;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kotuc
 */
public class Conveyor {

    public static final String ID_KEY = "ConveyorUserDataIdKey";

    //    private Vector3f destShort = new Vector3f();

    static final AtomicInteger idGen = new AtomicInteger();


    private final String id;


    Vector3f posVector;
    Pos pos;

    Spatial spatial;
    private final MyBox box;


    //    public Vehicle(Player owner, Type type, RoadPoint roadPoint, , AbstractGridPathNetwork network) {
    public Conveyor(Vector3f posVector, Material mat) {


        this.posVector = posVector;

//        mat = mat.clone();
//        mat.setColor("Color", new ColorRGBA((float)Math.random(), (float)Math.random(),(float)Math.random(), 1f));

        id = "M" + idGen.incrementAndGet();

        final float halfSize = 0.5f;
        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            final float halfSize = 1.25f;

            box = new MyBox(new Vector3f(-halfSize, -halfSize, -halfSize), new Vector3f(halfSize, halfSize, halfSize));
            Geometry reBoxg = new Geometry("kapota", box);
            reBoxg.setUserData(ID_KEY, id);
            reBoxg.setMaterial(mat);
            reBoxg.setLocalTranslation(new Vector3f(0, 0, 0));
            Multitexture1 mtex = new Multitexture1(new LinearGrid(16, 16));
            final int off = 16 * 3;
            box.setTexture(MyBox.FACE_FRONT, mtex.getTex(off + 3));
            box.setTexture(MyBox.FACE_RIGHT, mtex.getTex(off + 0));
            box.setTexture(MyBox.FACE_BACK, mtex.getTex(off + 4));
            box.setTexture(MyBox.FACE_BOTTOM, mtex.getTex(off + 5));
            box.setTexture(MyBox.FACE_TOP, mtex.getTex(off + 2));
            box.setTexture(MyBox.FACE_LEFT, mtex.getTex(off + 1));
            reBoxg.setQueueBucket(RenderQueue.Bucket.Transparent);

//            Multitexture mtex2 = new Multitexture(256, 256);
//            box.setTexture(MyBox.FACE_TOP, mtex2.createRealSubtexture(16*(1), 11*16, 16*(1+1f), 12*16));

            this.spatial = reBoxg;
        }


        spatial.addControl(new AbstractControl() {

            float offset = 0;

            @Override
            protected void controlUpdate(float tpf) {
                offset += tpf;
                Multitexture mtex = new Multitexture(256, 256);
                float x = -(offset % 1f);
                System.out.println("offset " + offset + " " + x);
                box.setTexture(MyBox.FACE_TOP, mtex.createRealSubtexture(16 * (1+x), 5 * 16, 16 * (x + 2f), 6 * 16));


                updateModel();
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Control cloneForSpatial(Spatial spatial) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(halfSize, halfSize, halfSize)), 0);
        control.setLinearVelocity(new Vector3f(1, 0, 0));;
        spatial.addControl(control);
        control.setPhysicsLocation(posVector);

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


    public void updateModel() {
        spatial.setLocalTranslation(this.getPosVector());
    }


    @Override
    public String toString() {
        return " (Conveyor)";
    }


    public Spatial getSpatial() {
        return spatial;
    }
}

