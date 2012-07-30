package kotuc.chaos;

import com.sun.j3d.utils.geometry.Primitive;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

/**
 *
 * @author Kotuc
 */
public class Box extends Entity {

    private Material material;
    private Primitive primitive;

    public Box() {
        this(0.9f, 0.9f, 0.9f);
    }
    Appearance appear = new Appearance();

    public Box(float halfWidth, float halfHeigth, float halfDepth) {
        appear.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        material = new Material();
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);
        appear.setMaterial(material);
        primitive = new com.sun.j3d.utils.geometry.Box(halfWidth, halfHeigth, halfDepth, appear);
        addChild(primitive);
    }

    public void setColor(Color3f color) {
        material.setDiffuseColor(color);
//        appear.setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.FASTEST));
//        primitive.setAppearance(appear);
    }
}
