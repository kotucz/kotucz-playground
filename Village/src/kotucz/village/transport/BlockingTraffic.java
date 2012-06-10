package kotucz.village.transport;

import com.jme3.scene.Node;
import kotucz.village.tiles.GenericGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kotuc
 *         Date: 10.6.12
 *         Time: 19:52
 */
public class BlockingTraffic {

    final PathNetwork network;

    final GenericGrid<Vehicle> occupiers;

    public List<Vehicle> cars = new ArrayList<Vehicle>();

    public final Node node = new Node("Cars");


    public BlockingTraffic(PathNetwork network) {
        this.network = network;

        this.occupiers = new GenericGrid<Vehicle>(network.lingrid);



    }


    public void update(float tpf) {
        for (Vehicle car : cars) {
            car.act(tpf);

//            selectGrid.add(car.getP());


        }
    }





}
