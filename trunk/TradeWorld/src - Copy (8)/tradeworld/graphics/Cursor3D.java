package tradeworld.graphics;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

/**
 *
 * @author Kotuc
 */
public class Cursor3D extends Model3D {

    private Appearance appear;
    private TextureAttributes texAttrs;
    private ColoringAttributes colorAttrs;
    private Shape3D shape;

    public Cursor3D() {
        appear = new Appearance();

        texAttrs = new TextureAttributes();
        texAttrs.setTextureMode(TextureAttributes.DECAL);
        appear.setTextureAttributes(texAttrs);

        colorAttrs = new ColoringAttributes(new Color3f(1, 1, 1), ColoringAttributes.FASTEST);
        colorAttrs.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
        appear.setColoringAttributes(colorAttrs);

        appear.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.2f));

        this.shape = new Shape3D(Tools.createRectanglePlane(1, 1), appear);
        shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        addChild(shape);
    }

    void setColor(Color3f color) {
        colorAttrs.setColor(color);
    }

    void setSize(double wx, double wy) {
        shape.setGeometry(Tools.createRectanglePlane(wx, wy));

    }
}
