package kotuc.chaos;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;

/**
 *
 * @author Kotuc
 */
public class Box extends Entity {

    public Box() {
        addChild(new com.sun.j3d.utils.geometry.Box());
    }

    public Box(float halfWidth, float halfHeigth, float halfDepth) {
        Appearance appear = new Appearance();
        appear.setMaterial(new Material());
        addChild(new com.sun.j3d.utils.geometry.Box(halfWidth, halfHeigth, halfDepth, appear));
    }
}
