package kotucz.village.transport;

import com.jme3.scene.Node;
import kotucz.village.tiles.GenericGrid;
import kotucz.village.tiles.Pos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kotuc
 *         Date: 10.6.12
 *         Time: 19:52
 */
public class BlockingTraffic {

    final AbstractGridPathNetwork network;

    final GenericGrid<Vehicle> occupiers;

     List<Vehicle> cars = new ArrayList<Vehicle>();

    public final Node node = new Node("Cars");


    public BlockingTraffic(AbstractGridPathNetwork network) {
        this.network = network;

        this.occupiers = new GenericGrid<Vehicle>(network.lingrid);



    }

    public void addVehicle(Vehicle car) {

        BlockingVehicleBehavior blockingVehicleBehavior = new BlockingVehicleBehavior(car, this);
        car.setBehavior(blockingVehicleBehavior);

        car.setName("Car "+cars.size());

        this.cars.add(car);
    }

    public Vehicle getOccupier(Pos pos) {
        Vehicle vehicle = occupiers.get(pos);
        if (vehicle!=null && pos.equals(vehicle.reservedPos)) {
            return vehicle;
        }
        return null;
    }



    public void update(float tpf) {
        for (Vehicle car : cars) {
            car.act(tpf);

//            selectGrid.add(car.getPos());


        }
    }





}
