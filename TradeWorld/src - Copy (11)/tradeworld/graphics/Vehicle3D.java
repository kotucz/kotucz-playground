package tradeworld.graphics;

import javax.media.j3d.BranchGroup;

/**
 *
 * @author Kotuc
 */
public class Vehicle3D extends Model3D {

   
    public Vehicle3D(String model) {
//        getRoot().setName(textureName);
        getRoot().setCapability(BranchGroup.ENABLE_PICK_REPORTING);

//        try {
//            this.addChild(loadVehicle());
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Vehicle3D.class.getName()).log(Level.SEVERE, null, ex);
//        }
        this.addChild(Models.getInstance().createLink(model));
//        this.setPos(new Point3d(5, 4, 0));
//        this.setAngle(Math.PI / 4);
//        refresh();

    }

    
}
