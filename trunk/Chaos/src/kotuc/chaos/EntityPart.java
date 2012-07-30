package kotuc.chaos;

/**
 *	Entity v 0.01;
 *
 *	Tomas Kotula as Kotuc (=disc)
 */
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;

public class EntityPart {

    BranchGroup objRoot = new BranchGroup();
    private TransformGroup objTrans = new TransformGroup();
    private Node objPart;

    public EntityPart() {
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        objRoot.addChild(objTrans);
        objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        objRoot.setCapability(BranchGroup.ALLOW_DETACH);
    }

    public EntityPart(Node node) {
        objPart = node;

        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        objTrans.addChild(objPart);
        objRoot.addChild(objTrans);
        objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        objRoot.setCapability(BranchGroup.ALLOW_DETACH);

    }

    public EntityPart(Node node, Transform3D transform) {
        this(node);
        objTrans.setTransform(transform);
    }

    /*	final public Bounds getBounds () {
    Bounds thisBounds = this.objPart.getBounds();
    thisBounds.transform(this.getTransform());
    return thisBounds;
    }
     */
    public static Node createDefaultPart() {
        return new ColorCube();
    }

    public void set(Node node) {
//		this.objPart = node;
        objTrans.addChild(node);
    }

    public void setTransform(Transform3D t) {
        this.objTrans.setTransform(t);
    }
}
	
