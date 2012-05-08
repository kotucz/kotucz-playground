package tradeworld.graphics;

import com.sun.j3d.utils.geometry.Text2D;
import java.awt.Font;
import java.util.Enumeration;
import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Color3f;
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

    protected Notification3D(OrientedShape3D shape) {
        this.orientedShape3D = shape;

        transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.5f);
        transparencyAttributes.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
        shape.getAppearance().setTransparencyAttributes(transparencyAttributes);

//        orientedShape3D.setCapability(OrientedShape3D.ALLOW_POINT_WRITE);
//        final OrientedShape3D orientedShape3D = new OrientedShape3D();
//        orientedShape3D.setGeometry(geometry);
        addChild(orientedShape3D);
//        addChild(
//                new Shape3D(geometry, appear));
//        addChild(new ColorCube());
        NotificationBehavior notificationBehavior = new NotificationBehavior();
        notificationBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000));

        addChild(notificationBehavior);

    }

    public static Notification3D createTextNotification(String text, Color3f color) {
        Text2D text2d = new Text2D(
                text,
                color, "Arial", 12, Font.BOLD);
        text2d.setRectangleScaleFactor(0.02f);
        return new Notification3D(new OrientedShape3D(text2d.getGeometry(), text2d.getAppearance(), OrientedShape3D.ROTATE_NONE, new Point3f()));

    }

    public static Notification3D createNotificationIcon(String textureFilename) {
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

        appear.setTexture(Tools.loadTexture(textureFilename));
//            appear.setTexture(Tools.loadTexture("mega4.png"));
//        appear.setTexture(Tools.loadTexture("road1.png"));
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.MODULATE);
        appear.setTextureAttributes(textureAttributes);

        return new Notification3D(new OrientedShape3D(geometry, appear, OrientedShape3D.ROTATE_NONE, new Point3f()));
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
            //            transparencyAttributes.setTransparency((float) pos.z);
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
