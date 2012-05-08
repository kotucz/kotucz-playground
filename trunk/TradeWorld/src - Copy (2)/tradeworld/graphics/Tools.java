package tradeworld.graphics;

import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TextureUnitState;

/**
 *
 * @author Kotuc
 */
public class Tools {

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
     */
    public static TextureUnitState loadTextureUnit(String fileName, int texAttr) {
        Texture2D tex = loadTexture(fileName);

        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(texAttr);

        TextureUnitState tus = new TextureUnitState(tex, ta, null);
        return tus;
    }  // end of loadTextureUnit()
}
