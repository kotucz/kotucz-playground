package tradeworld;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kotuc
 */
public class World {

    private Tile[][] tiles = new Tile[10][10];
    private List<Building> buildings = new LinkedList<Building>();
    private List<Vehicle> vehicles = new LinkedList<Vehicle>();

    public void put(Building building) {
        buildings.add(building);
    }

}
