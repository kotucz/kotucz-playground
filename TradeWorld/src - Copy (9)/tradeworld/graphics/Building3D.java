package tradeworld.graphics;

/**
 *
 * @author Kotuc
 */
public class Building3D extends Model3D {

    private double widthx, widthy, height;

    public Building3D(double widthx, double widthy, double height) {
        this.addChild(Tools.superBox(widthx, widthy, height, "factory2.png"));
    }
    

}
