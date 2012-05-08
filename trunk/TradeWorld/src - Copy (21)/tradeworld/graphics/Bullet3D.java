package tradeworld.graphics;

import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.Appearance;
import javax.media.j3d.Node;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import tradeworld.war.Bullet;

/**
 *
 * @author Kotuc
 */
public class Bullet3D extends Model3D {

    private final Bullet bullet;

    public Bullet3D(Bullet bullet) {
        this.bullet = bullet;
        addChild(new Sphere(0.1f));
        addChild(createLaser());
    }

    @Override
    public void refresh() {
        setTransform(bullet.getPos(getWorld3D().getCurrentTime()), 0);
    }

    private Node createLaser() {
        double width = 0.2;
        double length = 100;

        Appearance appear = new Appearance();
//        appear.setMaterial(new Material());

        appear.setTexture(Tools.loadTexture("lasershotred1.png"));
//    appear.setTexture(Tools.loadTexture("mega4.png"));
//        appear.setTexture(Tools.loadTexture("road1.png"));
        appear.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.5f));
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.MODULATE);
        appear.setTextureAttributes(textureAttributes);

        Transform3D transform = new Transform3D();
        transform.lookAt(new Point3d(), new Point3d(bullet.getDirection()), new Vector3d(0, 0, 1));
        transform.invert();
        TransformGroup transformGroup = new TransformGroup(transform);

        Quat4d quat = new Quat4d();
        quat.set(new AxisAngle4d(0, 1, 0, 0.5 * Math.PI));
        Transform3D t3d = new Transform3D();
        t3d.set(quat, new Vector3d(0, 0, -length / 2), 1);
        TransformGroup transformGroup2 = new TransformGroup(t3d);

        transformGroup2.addChild(new OrientedShape3D(Tools.createRectanglePlane(length, width), appear, OrientedShape3D.ROTATE_ABOUT_AXIS, new Vector3f(1, 0, 0)));
//        transformGroup2.addChild(new Shape3D(Tools.createRectanglePlane(length, width), appear));
//        transformGroup.addChild(new OrientedShape3D(Tools.createRectanglePlane(width, length), appear, OrientedShape3D.ROTATE_ABOUT_AXIS, new Vector3f(0, 0, 1)));

        transformGroup.addChild(transformGroup2);
        return transformGroup;
//        return transformGroup2;
    }
}
