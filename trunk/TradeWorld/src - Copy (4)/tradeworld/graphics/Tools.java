package tradeworld.graphics;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.Appearance;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TextureUnitState;

import javax.vecmath.Color3f;
import javax.vecmath.Vector4f;

/**
 *
 * @author Kotuc
 */
public class Tools {

    public static ImageComponent2D loadImageComponent(String fileName) {
        TextureLoader loader =
                new TextureLoader("res/images/" + fileName, TextureLoader.GENERATE_MIPMAP, null);
        System.out.println("Loaded floor texture: images/" + fileName);

        return loader.getImage();
    }

    public static Texture2D loadTexture(String fileName) {
        TextureLoader loader =
                new TextureLoader("res/images/" + fileName, TextureLoader.GENERATE_MIPMAP, null);
        System.out.println("Loaded floor texture: images/" + fileName);

        Texture2D tex = (Texture2D) loader.getTexture();
        tex.setMinFilter(Texture2D.MULTI_LEVEL_LINEAR);
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

        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(texAttr);

        TextureUnitState tus = new TextureUnitState(tex, ta, null);
        return tus;
    }  // end of loadTextureUnit()
    private double widthx, widthy, height;

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
                    new Vector4f(1 / (float) widthx, 0, 0, -0.5f),
                    new Vector4f(0, 1 / (float) widthy, 0, -0.5f));
            appear.setTexCoordGeneration(tcg);
        }

        Primitive box = new Box((float) widthx / 2, (float) widthy / 2, (float) height / 2, appear);
        return box;


    }
}
