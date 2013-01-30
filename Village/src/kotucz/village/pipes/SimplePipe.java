package kotucz.village.pipes;

import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import kotucz.village.common.Entities;

/**
 * @author Kotuc
 */
public class SimplePipe {


    public static final String ID_KEY = "CubeUserDataIdKey";

    //    private Vector3f destShort = new Vector3f();

//    static final AtomicInteger idGen = new AtomicInteger();


    private final String id = Entities.acquireUniqueKey(this);


    Vector3f posVector;


    Spatial spatial;

    private final RigidBodyControl control;
    private Vector3f velocity = new Vector3f();
    private final Geometry geometry;
    private Vector3f endVector;
    private Material mat;


    //    public Vehicle(Player owner, Type type, RoadPoint roadPoint, , AbstractGridPathNetwork network) {
    public SimplePipe(Vector3f posVector, Vector3f endVector, Material mat) {
        this.endVector = endVector;
        this.mat = mat;


        final float radius = 0.125f;

        this.posVector = posVector;
//        this.dir = dir;

//        mat = mat.clone();
//        mat.setColor("Color", new ColorRGBA((float)Math.random(), (float)Math.random(),(float)Math.random(), 1f));

//        id = "Cube" + idGen.incrementAndGet();


        final Vector3f dir = endVector.subtract(posVector);
        final float length = dir.length();
        final float halfLen = 0.5f * length;

        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            final float halfSize = 1.25f;


            Cylinder cylinder = new Cylinder(4, 8, radius, length);
            geometry = new Geometry("kapota", cylinder);
            geometry.setUserData(Entities.ID_KEY, id);
            geometry.setMaterial(mat);


            // set center of mass in the middle of the point
            geometry.setLocalTranslation(posVector.add(dir.mult(.5f)));


            Quaternion quaternion = new Quaternion();
            quaternion.lookAt(dir, new Vector3f(1, 1, 1));
            geometry.setLocalRotation(quaternion);


//            Multitexture1 mtex = new Multitexture1(new LinearGrid(16, 16));
//            int off = 16 * 10;
//
//            box.setTexture(MyBox.FACE_STATIC_S, mtex.getTex(off + 0));
//            box.setTexture(MyBox.FACE_STATIC_E, mtex.getTex(off + 1));
//            box.setTexture(MyBox.FACE_STATIC_N, mtex.getTex(off + 2));
//            box.setTexture(MyBox.FACE_STATIC_W, mtex.getTex(off + 3));
//            box.setTexture(MyBox.FACE_STATIC_TOP, mtex.getTex(off - 16));
//            box.setTexture(MyBox.FACE_STATIC_BOTTOM, mtex.getTex(off + 16));
//            reBoxg.setQueueBucket(RenderQueue.Bucket.Transparent);

//            Multitexture mtex2 = new Multitexture(256, 256);
//            box.setTexture(MyBox.FACE_TOP, mtex2.createRealSubtexture(16*(1), 11*16, 16*(1+1f), 12*16));

            this.spatial = geometry;

            spatial.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

            control = new RigidBodyControl(new CylinderCollisionShape(new Vector3f(radius, radius, halfLen), 2), 0);

            spatial.addControl(control);

//        spatial.setLocalTranslation(posVector);
//            control.setPhysicsLocation(posVector);

        }

    }


    public SimplePipe extend(Vector3f localVector) {
        Quaternion localRotation = geometry.getLocalRotation();
        Vector3f mult = localRotation.mult(localVector);
        return new SimplePipe(this.endVector, this.endVector.add(mult), this.mat);
    }


    public RigidBodyControl getPhysics() {
        return control;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }


    public Spatial getSpatial() {
        return spatial;
    }



}
