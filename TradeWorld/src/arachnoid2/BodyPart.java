package arachnoid2;

import java.util.HashSet;
import java.util.Set;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;

/**
 *
 * @author Kotuc
 */
public class BodyPart {

    String name;
    // the body part joined to
    // null for root -> jointLocation == identity
    BodyPart parent;
    // static location of the joint e.g. translation
    final Transform3D jointLocation = new Transform3D();
    final Joint joint = new Joint(this);
    final Set<BodyPart> childs = new HashSet<BodyPart>();
    // transform of body (physics and graphics)
    final Transform3D bodyShift = new Transform3D();
    // body shape
    Vector3d halfBodyBox;
    Vector3d massCenter;
    double mass = 1;

    public BodyPart(String name) {
        this.name = name;
    }

    public BodyPart(String name, Transform3D jointTrans) {
        this(name);
        jointLocation.set(jointTrans);
    }

    void setJointLocation(Transform3D jointLocation) {
        this.jointLocation.set(jointLocation);
    }

    void applyTransform(Transform3D trans) {
        trans.mul(jointLocation);
        trans.mul(joint.jointRotation);
    }

    void getTransform(Transform3D trans) {
        if (parent != null) {
            parent.getTransform(trans);
        }
        applyTransform(trans);
    }

    void addPart(BodyPart part) {
        if (part.parent != null) {
            throw new IllegalArgumentException("non null parent");
        }
        childs.add(part);
        part.parent = this;
    }

    @Override
    public String toString() {
        return name;//super.toString();
    }
//    PObject pobject;



}
