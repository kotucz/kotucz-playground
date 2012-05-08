package tradeworld.graphics;

import com.microcrowd.loader.java3d.max3ds.Loader3DS;
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.QuadArray;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TextureUnitState;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;

/**
 *
 * @author Kotuc
 */
public class Tools {

    /**
     * In memory textures.
     * Filename->Texture
     */
    private static Map<String, Texture2D> textures = new HashMap<String, Texture2D>();

//    public static ImageComponent2D loadImageComponent(String fileName) {
//        TextureLoader loader =
//                new TextureLoader("res/images/" + fileName, TextureLoader.GENERATE_MIPMAP, null);
//        System.out.println("Loaded floor texture: images/" + fileName);
//
//        return loader.getImage();
//    }
    public static Texture2D loadTexture(String fileName) {
        Texture2D tex = textures.get(fileName);
        if (tex == null) {
            TextureLoader loader =
                    new TextureLoader("res/images/" + fileName, TextureLoader.GENERATE_MIPMAP, null);
            System.out.println("Loaded floor texture: images/" + fileName);

            tex = (Texture2D) loader.getTexture();
            tex.setMinFilter(Texture2D.MULTI_LEVEL_LINEAR);



            textures.put(fileName, tex);
        }

        return tex;
    }

    /** Create a texture unit state by combining a loaded texture
    and texture attributes. Mipmaps are generated for the texture.
     * @param fileName 
     * @param texAttr 
     * @return
     */
    public static TextureUnitState loadTextureUnit(String fileName, int texAttr) {
        Texture2D tex = loadTexture(fileName);

        tex.setBoundaryModeS(Texture2D.CLAMP_TO_EDGE);
        tex.setBoundaryModeT(Texture2D.CLAMP_TO_EDGE);
//            tex.setBoundaryModeS(Texture2D.CLAMP_TO_BOUNDARY);
//            tex.setBoundaryModeT(Texture2D.CLAMP_TO_BOUNDARY);

        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(texAttr);

        TextureUnitState tus = new TextureUnitState(tex, ta, null);
        return tus;
    }  // end of loadTextureUnit()

    public static Geometry createRectanglePlane(double widthx, double widthy) {
        GeometryArray geometry =
                new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);

        widthx *= 0.5;
        widthy *= 0.5;

        Point3d[] coords = new Point3d[]{
            new Point3d(-widthx, -widthy, 0),
            new Point3d(widthx, -widthy, 0),
            new Point3d(widthx, widthy, 0),
            new Point3d(-widthx, widthy, 0)
        };
        geometry.setCoordinates(0, coords);

        TexCoord2f[] texs = new TexCoord2f[]{
            new TexCoord2f(0, 0),
            new TexCoord2f(1, 0),
            new TexCoord2f(1, 1),
            new TexCoord2f(0, 1)};
        geometry.setTextureCoordinates(0, 0, texs);

        return geometry;
    }

    public static Node superBox(double widthx, double widthy, double height, String textureFile) {

        Appearance appear = new Appearance();

        { // appearance

            // material
            Material ma = new Material();
            ma.setAmbientColor(new Color3f(0.32f, 0.32f, 0.32f));
            ma.setSpecularColor(new Color3f(0.2f, 0.2f, 0.2f));
            ma.setEmissiveColor(new Color3f(0.41f, 0.41f, 0.41f));
            ma.setDiffuseColor(new Color3f(1.0f, 1.0f, 1.0f));
            appear.setMaterial(ma);
//            ma.setShininess(1.0f);

            // texture
            Texture2D texture = Tools.loadTexture(textureFile);
            appear.setTexture(texture);

            // mapping
            TexCoordGeneration tcg = new TexCoordGeneration(
                    TexCoordGeneration.OBJECT_LINEAR,
                    TexCoordGeneration.TEXTURE_COORDINATE_2,
                    new Vector4f(1f / (float) widthx, 0f, 0f, -0.5f),
                    new Vector4f(0f, 1f / (float) widthy, 0f, -0.5f));
            appear.setTexCoordGeneration(tcg);
        }

        Primitive box = new Box((float) widthx / 2, (float) widthy / 2, (float) height / 2, appear);
        box.setName("Box " + textureFile);
        return box;


    }

    public static Node loadModel3DS(String modelName) throws FileNotFoundException {

        System.out.println("Loading " + modelName + " (3DS)");
//            Lw3dLoader loader = new Lw3dLoader();
//            Scene scene = loader.load("res/models/" + "Dodge Ram pick-up.lwo");
//            this.branchGroup = scene.getSceneGroup();
        Loader loader = new Loader3DS();
//            Scene scene = loader.load("res/models/" + "War.max");
        Scene scene = loader.load("res/models/" + modelName);

        Transform3D t3d = new Transform3D();
//            t3d.rotX(Math.PI/2);
//            t3d.set(0.01);
//            t3d.set(new Matrix3d(1, 0, 0, 0.25), new Vector3d(), 0.01);

        Quat4d quat4d = new Quat4d();
        quat4d.set(new AxisAngle4d(1, 0, 0, Math.PI / 2));
        Quat4d quat2 = new Quat4d();
        quat2.set(new AxisAngle4d(0, 0, 1, Math.PI / 2));
        quat2.mul(quat4d);
        t3d.set(quat2, new Vector3d(0, 0, 0.0), 0.2);

        TransformGroup tg = new TransformGroup(t3d);
        tg.addChild(scene.getSceneGroup());

        System.out.println("loading vehicle DONE");
        return tg;

    }
}
