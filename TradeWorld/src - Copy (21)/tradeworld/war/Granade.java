package tradeworld.war;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import tradeworld.WorldObject;
import tradeworld.graphics.Granade3D;
import tradeworld.physics.PObject;
import tradeworld.physics.PhysicsWorld;

/**
 *
 * @author Kotuc
 */
public class Granade extends WorldObject {

    private final PObject pobject;

    public Granade(PhysicsWorld pworld, Point3d pos, Vector3d dir) {
//        this.pobject = pworld.createSphere(0.1f, 0.2f, pos);
        this.pobject = pworld.createSphere(0.3f, 0.2f, pos);
        pobject.setVelocity(new Vector3f(dir));
        pobject.body.setDamping(0.1f, 1);
        pworld.add(pobject);
        this.model = new Granade3D(pobject);
    }

    public PObject getPobject() {
        return pobject;
    }

    

}
