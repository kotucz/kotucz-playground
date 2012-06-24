package kotucz.village.build;

import kotucz.village.tiles.GenericGrid;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Pos;
import kotucz.village.tiles.TileGrid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Kotuc
 */
public class Buildings extends GenericGrid<Building> implements Iterable<Building> {


    public final Map<String, Building> map = new HashMap<String, Building>();


    public Buildings(LinearGrid lingrid) {
        super(lingrid);

    }

    public void putBuilding(Building building) {
        Set<Pos> occupiedPosses = building.getOccupiedPosses();
        for (Pos occupiedPoss : occupiedPosses) {
            set(occupiedPoss, building);
        }
        map.put(building.getId(), building);

    }


    @Override
    public Iterator<Building> iterator() {
        return map.values().iterator();
    }



}
