package tradeworld.graphics;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture2D;
import javax.vecmath.Color3f;
import javax.vecmath.Vector4f;

/**
 *
 * @author Kotuc
 */
public class Model {

    private double widthx, widthy;
    private final int SIDE = 1;
    private BranchGroup objModel = new BranchGroup();
    private GeometryArray landGeometry;

    public BranchGroup getObjModel() {
        return objModel;
    }

    public Model(double widthx, double widthy, double height) {
        this.widthx = widthx;
        this.widthy = widthy;
        Appearance appear = createAppearance();
        Primitive box = new Box((float) widthx / 2, (float) widthy / 2, (float) height / 2, appear);
        objModel.addChild(box);
    }

    private Appearance createAppearance() {
        Appearance appear = new Appearance();

//        landAppearance.setColoringAttributes (new ColoringAttributes (new Color3f (0.0f, 0.0f, 1.0f),1));
//        landAppearance.setPolygonAttributes(new PolygonAttributes);

        Material ma = new Material();

        ma.setAmbientColor(new Color3f(0.32f, 0.32f, 0.32f));
        ma.setSpecularColor(new Color3f(0.2f, 0.2f, 0.2f));
        ma.setEmissiveColor(new Color3f(0.41f, 0.41f, 0.41f));
        ma.setDiffuseColor(new Color3f(1.0f, 1.0f, 1.0f));
        appear.setMaterial(ma);
//        ma.setShininess(1.0f);


        Texture2D texture = Tools.loadTexture("factory2.png");
        appear.setTexture(texture);

        TexCoordGeneration tcg = new TexCoordGeneration(
                TexCoordGeneration.OBJECT_LINEAR,
                TexCoordGeneration.TEXTURE_COORDINATE_2,
                new Vector4f(1/(float)widthx, 0, 0, -0.5f),
                new Vector4f(0, 1/(float)widthy, 0, -0.5f));
        appear.setTexCoordGeneration(tcg);

        return appear;

    }
}
