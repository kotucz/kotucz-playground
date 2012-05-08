package tradeworld.graphics;

import java.util.Enumeration;
import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;

/**
 *
 * @author Kotuc
 */
public class Notification3D extends Model3D {

    final OrientedShape3D orientedShape3D;
    private TransparencyAttributes transparencyAttributes;

    public Notification3D(Point3d pos) {
//    public Notification(ImageComponent2D image) {
//        int iw = image.getWidth();
//        int ih = image.getHeight();
//        int iw = 256;
//        int ih = 256;
//        Point3f pos = new Point3f();

        GeometryArray geometry =
                new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);

        double a = 0.5;
        Point3d[] coords = new Point3d[]{
            new Point3d(-a, -a, 0),
            new Point3d(a, -a, 0),
            new Point3d(a, a, 0),
            new Point3d(-a, a, 0)
        };
        geometry.setCoordinates(0, coords);

        TexCoord2f[] texs = new TexCoord2f[]{
            new TexCoord2f(0, 0),
            new TexCoord2f(1, 0),
            new TexCoord2f(1, 1),
            new TexCoord2f(0, 1)};
        geometry.setTextureCoordinates(0, 0, texs);

        Appearance appear = new Appearance();
        appear.setMaterial(new Material());
        transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.5f);
        transparencyAttributes.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
        appear.setTransparencyAttributes(transparencyAttributes);
        appear.setTexture(Tools.loadTexture("mega4.png"));
//        appear.setTexture(Tools.loadTexture("road1.png"));
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.MODULATE);
        appear.setTextureAttributes(textureAttributes);

        orientedShape3D = new OrientedShape3D(geometry, appear, OrientedShape3D.ROTATE_NONE, new Point3f());
        orientedShape3D.setCapability(OrientedShape3D.ALLOW_POINT_WRITE);
//        final OrientedShape3D orientedShape3D = new OrientedShape3D();
//        orientedShape3D.setGeometry(geometry);
        addChild(orientedShape3D);
//        addChild(
//                new Shape3D(geometry, appear));
//        addChild(new ColorCube());
        NotificationBehavior notificationBehavior = new NotificationBehavior();
        notificationBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000));

        addChild(notificationBehavior);

        setPos(pos);
        refresh();
    }

    class NotificationBehavior extends Behavior {

        private WakeupOnElapsedFrames w = new WakeupOnElapsedFrames(0);

        @Override
        public void initialize() {
            wakeupOn(w);
        }

        @Override
        public void processStimulus(Enumeration arg0) {
//            System.out.println("stimulus");
            Point3d pos = getPos();
            pos.z += 0.01;
            if (pos.z > 1.5) {
                remove();
            }
            transparencyAttributes.setTransparency((float)pos.z);
            refresh();
            wakeupOn(w);

//            if (isAlive()) {
//                wakeupOn(w);
//            } else {
//                kill();
//            }
        }
    }
}
