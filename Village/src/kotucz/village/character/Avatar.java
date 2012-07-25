package kotucz.village.character;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import kotucz.village.common.Entities;
import kotucz.village.common.MyBox;
import kotucz.village.game.Player;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Multitexture1;
import kotucz.village.transport.VehicleBehavior;

/**
 * @author Kotuc
 */
public class Avatar {

    private final String id = Entities.acquireUniqueKey(this);
    private String name;

    private Player owner;

    private Vector3f posVector;

    private float heading;

    final Node node = new Node("Avatar");
    private VehicleBehavior behavior;
    private final CharacterControl physics;


    public Avatar(Material mat, Vector3f pos) {

        this.name = "Jarda";


        this.posVector = pos;



        final float halfSize = 1.25f;

        Geometry spatial;
        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));

            MyBox box = new MyBox(new Vector3f(-halfSize, -halfSize,-halfSize), new Vector3f(halfSize, halfSize, halfSize));
            spatial = new Geometry("kapota", box);

            spatial.setMaterial(mat);
            spatial.setLocalTranslation(new Vector3f(0, 0, 0));
            Multitexture1 mtex = new Multitexture1(new LinearGrid(16, 16));
            final int off = 16;
            box.setTexture(MyBox.FACE_FRONT, mtex.getTex(off +3));
            box.setTexture(MyBox.FACE_RIGHT, mtex.getTex(off +0));
            box.setTexture(MyBox.FACE_BACK, mtex.getTex(off +4));
            box.setTexture(MyBox.FACE_BOTTOM, mtex.getTex(off +5));
            box.setTexture(MyBox.FACE_TOP, mtex.getTex(off +2));
            box.setTexture(MyBox.FACE_LEFT, mtex.getTex(off +1));
            spatial.setQueueBucket(RenderQueue.Bucket.Transparent);


        }

        spatial.setUserData(Entities.ID_KEY, id);

        physics = new CharacterControl(new BoxCollisionShape(new Vector3f(halfSize, halfSize, halfSize)), 0.01f);
//        physics = new CharacterControl(new CapsuleCollisionShape(0.5f, 1.8f), 1);
        physics.setUpAxis(2);
//        physics.setJumpSpeed(0f);
//        physics.setFallSpeed(0.01f);
//        physics.setMaxSlope(1.5f);
        physics.setGravity(1f);
        physics.setWalkDirection(new Vector3f(0.f, 0f, 0f));
//        physics.setKinematic(true);
        spatial.setLocalTranslation(posVector);
        physics.setPhysicsLocation(posVector);

        spatial.addControl(physics);

        node.attachChild(spatial);

    }

    public PhysicsControl getPhysics() {
        return physics;
    }

    public void setDir(Vector3f dir) {
        physics.setWalkDirection(dir);

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
