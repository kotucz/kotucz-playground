package kotucz.village.tiles;

/**
 *
 * @author Kotuc
 */
public class Subtexture {

    public float startU;   //     X
    public float endU;
    public float startV;   //     Y 
    public float endV;

    public Subtexture(float startU, float startV, float endU, float endV) {
        this.startU = startU;
        this.endU = endU;
        this.startV = startV;
        this.endV = endV;
    }
}
