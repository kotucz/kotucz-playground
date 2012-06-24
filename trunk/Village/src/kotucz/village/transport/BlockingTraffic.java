package kotucz.village.transport;

import com.jme3.scene.Node;
import kotucz.village.game.MyGame;
import kotucz.village.tiles.GenericGrid;
import kotucz.village.tiles.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kotuc
 *         Date: 10.6.12
 *         Time: 19:52
 */
public class BlockingTraffic {

    final UnidirectionalPathNetwork network;
    private MyGame game;

    final GenericGrid<Vehicle> occupiers;

     List<Vehicle> cars = new ArrayList<Vehicle>();

    public final Map<String, Vehicle> vehicleFindById = new HashMap<String, Vehicle>();

    public final Node node = new Node("Cars");


    public BlockingTraffic(UnidirectionalPathNetwork network, MyGame game) {
        this.network = network;
        this.game = game;

        this.occupiers = new GenericGrid<Vehicle>(network.lingrid);



    }

    public void addVehicle(Vehicle car) {



//        BlockingVehicleBehavior blockingVehicleBehavior = new BlockingVehicleBehavior(car, this);
        BlockingVehicleBehavior blockingVehicleBehavior = new TransporterBehavior(car, this, game.map.buildings);
        car.setBehavior(blockingVehicleBehavior);

        car.setName("Car "+cars.size());

        vehicleFindById.put(car.getId(), car);

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
