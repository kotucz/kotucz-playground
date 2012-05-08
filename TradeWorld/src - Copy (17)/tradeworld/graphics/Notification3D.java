package tradeworld.graphics;

import com.sun.j3d.utils.geometry.Text2D;
import java.awt.Font;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import tradeworld.Goods;
import tradeworld.war.TimePos3D;

/**
 *
 * @author Kotuc
 */
public class Notification3D extends Model3D {

    final OrientedShape3D orientedShape3D;
    private TransparencyAttributes transparencyAttributes;
    private TimePos3D timepos;

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

    }

    public static Notification3D createTextNotification(String text, Color3f color) {
        Text2D text2d = new Text2D(
                text,
                color, "Arial", 12, Font.BOLD);
        text2d.setRectangleScaleFactor(0.02f);
        return new Notification3D(new OrientedShape3D(text2d.getGeometry(), text2d.getAppearance(), OrientedShape3D.ROTATE_NONE, new Point3f()));

    }

    public static Notification3D createNotificationIcon(String textureFilename) {
//        GeometryArray geometry =
//                new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);
//
//        double a = 0.5;
//        Point3d[] coords = new Point3d[]{
//            new Point3d(-a, -a, 0),
//            new Point3d(a, -a, 0),
//            new Point3d(a, a, 0),
//            new Point3d(-a, a, 0)
//        };
//        geometry.setCoordinates(0, coords);
//
//        TexCoord2f[] texs = new TexCoord2f[]{
//            new TexCoord2f(0, 0),
//            new TexCoord2f(1, 0),
//            new TexCoord2f(1, 1),
//            new TexCoord2f(0, 1)};
//        geometry.setTextureCoordinates(0, 0, texs);

        double a = 1; // one side

        Appearance appear = new Appearance();
        appear.setMaterial(new Material());

        appear.setTexture(Tools.loadTexture(textureFilename));
//            appear.setTexture(Tools.loadTexture("mega4.png"));
//        appear.setTexture(Tools.loadTexture("road1.png"));
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.MODULATE);
        appear.setTextureAttributes(textureAttributes);

        return new Notification3D(new OrientedShape3D(Tools.createRectanglePlane(a, a), appear, OrientedShape3D.ROTATE_NONE, new Point3f()));
    }

    public static Notification3D createGoodsIcon(Goods goods) {
        return Notification3D.createNotificationIcon(goods.getType().toString() + ".png");
    }

    @Override
    public void refresh() {
//            System.out.println("stimulus");
        Point3d pos = getPos();
        pos.z += 0.01;
        if (pos.z > 1.5) {
//                remove();
            if (getWorld3D() != null) {
                getWorld3D().removeNotification(this);
            }
        }
        //            transparencyAttributes.setTransparency((float) pos.z);
        super.refresh();
    }
}
