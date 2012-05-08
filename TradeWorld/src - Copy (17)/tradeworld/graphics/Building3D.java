package tradeworld.graphics;

import javax.media.j3d.BranchGroup;
import tradeworld.Building;

/**
 *
 * @author Kotuc
 */
public class Building3D extends Model3D {

//    private double widthx, widthy, height;
    private final Building building;

    public Building3D(Building building) {
        this.building = building;
        getRoot().setName(building.getType().getIconName());
//        getRoot().setPickable(true);
        getRoot().setCapability(BranchGroup.ENABLE_PICK_REPORTING);
        System.out.println("Building " + getRoot());
        this.addChild(Tools.superBox(
                building.getType().getWidthx(), building.getType().getWidthy(),
                1, "factory2.png"));
        setPos(building.getPos());
    }

//    public Building3D(double widthx, double widthy, double height, String textureName) {
//        getRoot().setName(textureName);
////        getRoot().setPickable(true);
//        getRoot().setCapability(BranchGroup.ENABLE_PICK_REPORTING);
//        System.out.println("Building " + getRoot());
//        this.addChild(Tools.superBox(widthx, widthy, height, textureName));
//    }
//
//    public Building3D() {
////        getRoot().setPickable(true);
//        getRoot().setCapability(BranchGroup.ENABLE_PICK_REPORTING);
//        System.out.println("Building " + getRoot());
//        try {
//            this.addChild(Tools.loadModel3DS("Simpsonpowerplant.max"));
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Building3D.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
