package kotucz.village.plants;

import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import java.util.Arrays;
import kotucz.village.common.Entities;

/**
 * @author Kotuc
 */
public class Stem implements BeingStem {


    public static final String ID_KEY = "CubeUserDataIdKey";

    //    private Vector3f destShort = new Vector3f();

//    static final AtomicInteger idGen = new AtomicInteger();


    private final String id = Entities.acquireUniqueKey(this);


    Vector3f posVector;


    Spatial spatial;

    private final RigidBodyControl control;
    private final Geometry geometry;
    private Vector3f endVector;
    private Material mat;
    private float length;


    //    public Vehicle(Player owner, Type type, RoadPoint roadPoint, , AbstractGridPathNetwork network) {
    public Stem(Vector3f posVector, Vector3f endVector, Material mat) {
        this.endVector = endVector;
        this.mat = mat;


        final float radius = 0.125f;

        this.posVector = posVector;
//        this.dir = dir;

//        mat = mat.clone();
//        mat.setColor("Color", new ColorRGBA((float)Math.random(), (float)Math.random(),(float)Math.random(), 1f));

//        id = "Cube" + idGen.incrementAndGet();


        final Vector3f dir = endVector.subtract(posVector);
        this.length = dir.length();
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


    public Stem(Transform transform, float length, Material mat) {
        this.length = length;


        // grow in z direction
        final Vector3f dir = transform.getRotation().mult(new Vector3f(0, 0, length));

        // TODO
        this.endVector = new Vector3f(transform.getTranslation()).add(dir);
        this.posVector = new Vector3f(transform.getTranslation());


        this.mat = mat;


        final float radius = 0.125f;

//        this.dir = dir;

//        mat = mat.clone();
//        mat.setColor("Color", new ColorRGBA((float)Math.random(), (float)Math.random(),(float)Math.random(), 1f));

//        id = "Cube" + idGen.incrementAndGet();


//        final Vector3f dir = endVector.subtract(posVector);

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


//            Quaternion quaternion = new Quaternion();
//            quaternion.lookAt(dir, new Vector3f(1, 1, 1));
            geometry.setLocalRotation(transform.getRotation());


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


//    public Stem extend(Vector3f localVector) {
//        Quaternion localRotation = geometry.getLocalRotation();
//        Vector3f mult = localRotation.mult(localVector);
//        return new Stem(this.endVector, this.endVector.add(mult), this.mat);
//    }

    public Stem extend(Vector3f localVector) {
        Quaternion localRotation = geometry.getLocalRotation();
        final Vector3f dir = new Vector3f(0, 0, length*0.5f);
        Vector3f dirTrans = geometry.getLocalRotation().mult(dir);


        // TODO
        Vector3f endVector = new Vector3f(geometry.getLocalTranslation()).add(dirTrans);
//        Vector3f mult = localRotation.mult(localVector);
        Quaternion rot = new Quaternion();
//        rot.fromAngleNormalAxis(0.5f,new Vector3f(0, 1, 1) );
        rot.fromAngles(localVector.x, localVector.y, localVector.z);
        Transform transform = new Transform(endVector, localRotation.mult(rot));
        return new Stem(transform, length, this.mat);
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

    
    int genomePosition;
    int[] register = new int[5];
    
    public void setGenomePosition(int position) {
        genomePosition = position;
    }

    public int getGenomePosition() {
        return genomePosition;
    }

    public void addToRegister(int registerNumber, int value) {
        register[registerNumber] += value;
    }

    public void subFromRegister(int registerNumber, int value) {
        addToRegister(registerNumber, -value);
    }

    public int getRegisterValue(int registerNumber) {
        return register[registerNumber];
    }
    
    public int[] getRegister() {
        return Arrays.copyOf(register, register.length);
    }
    
    public void setRegister(int[] register) {
        this.register = register;
    }



}