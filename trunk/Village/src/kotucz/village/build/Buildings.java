package kotucz.village.build;

import kotucz.village.tiles.GenericGrid;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Pos;
import kotucz.village.tiles.TileGrid;

import java.util.Set;

/**
 * @author Kotuc
 */
public class Buildings extends GenericGrid<Building> {





    public Buildings(LinearGrid lingrid) {
        super(lingrid);

    }

    public void put(Building building) {
        Set<Pos> occupiedPosses = building.getOccupiedPosses();
        for (Pos occupiedPoss : occupiedPosses) {
            set(occupiedPoss, building);
        }


    }




}
