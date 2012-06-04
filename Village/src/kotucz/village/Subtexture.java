package kotucz.village;

/**
 *
 * @author Kotuc
 */
public class Subtexture {

    float startU;   //     X
    float endU;
    float startV;   //     Y 
    float endV;

    public Subtexture(float startU, float startV, float endU, float endV) {
        this.startU = startU;
        this.endU = endU;
        this.startV = startV;
        this.endV = endV;
    }
}
