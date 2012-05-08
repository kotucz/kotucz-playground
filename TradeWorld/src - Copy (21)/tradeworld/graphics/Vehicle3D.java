package tradeworld.graphics;

import javax.media.j3d.BranchGroup;
import tradeworld.Goods;
import tradeworld.Vehicle;

/**
 *
 * @author Kotuc
 */
public class Vehicle3D extends Model3D {

    final Vehicle vehicle;

    public Vehicle3D(Vehicle vehicle) {
        this.vehicle = vehicle;
//        getRoot().setName(textureName);
        getRoot().setCapability(BranchGroup.ENABLE_PICK_REPORTING);

//        try {
//            this.addChild(loadVehicle());
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Vehicle3D.class.getName()).log(Level.SEVERE, null, ex);
//        }
        this.addChild(Models.getInstance().createLink(vehicle.getModelName()));
//        this.setPos(new Point3d(5, 4, 0));
//        this.setAngle(Math.PI / 4);
//        refresh();

    }

    @Override
    public void refresh() {
        setTransform(vehicle.getPos(), vehicle.getAngle());
//        for (Goods goods : vehicle.getPayload().getGoods()) {
//            Notification3D icon = Notification3D.createGoodsIcon(goods);
//            icon.setPos(vehicle.getPos());
//            icon.getPos().z = 1.485;
//            getWorld3D().addNotification(icon);
//        }

    }
}
