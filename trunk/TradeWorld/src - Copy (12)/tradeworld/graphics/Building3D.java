package tradeworld.graphics;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.BranchGroup;

/**
 *
 * @author Kotuc
 */
public class Building3D extends Model3D {

    private double widthx, widthy, height;

    public Building3D(double widthx, double widthy, double height, String textureName) {
        getRoot().setName(textureName);
//        getRoot().setPickable(true);
        getRoot().setCapability(BranchGroup.ENABLE_PICK_REPORTING);
        System.out.println("Building "+getRoot());
        this.addChild(Tools.superBox(widthx, widthy, height, textureName));
    }

    public Building3D() {
//        getRoot().setPickable(true);
        getRoot().setCapability(BranchGroup.ENABLE_PICK_REPORTING);
        System.out.println("Building "+getRoot());
        try {
            this.addChild(Tools.loadModel3DS("Simpsonpowerplant.max"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Building3D.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



}
