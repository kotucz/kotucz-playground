package kotucz.village.transport;

import com.jme3.material.Material;
import kotucz.village.common.Dir4;
import kotucz.village.game.MyGame;
import kotucz.village.tiles.Pos;
import kotucz.village.tiles.Tile;
import kotucz.village.tiles.TileGrid;

import java.util.Set;

/**
 * @author Kotuc
 */
public class UnidirectionalRoadTileGrid extends TileGrid {

    final AbstractGridPathNetwork pnet;

    public UnidirectionalRoadTileGrid(AbstractGridPathNetwork pnet, Material mat, MyGame myGame) {
        super(pnet.lingrid, mat, myGame);
        this.pnet = pnet;
    }


    @Override
    public void updateTexture() {
        for (Tile t : lingrid) {
//            tilegrid.setTexture(t.x, t.y, getRoadTileHash(t.x, t.y));
            setTexture(t.x, t.y, getRoadTileHash(t.pos));
        }

        super.updateTexture();
    }

    private int getRoadTileHash(Pos pos) {
//        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
//            return;
//        }

        RoadPoint point = pnet.getRoadPoint(pos);
        if (point == null) {
            return 0;
        }

        Set<RoadPoint> s = point.getNexts();

        int hash = (isConnected3(pos, pos.inDir(Dir4.E)) ? 1 : 0)
                + (isConnected3(pos, pos.inDir(Dir4.N)) ? 2 : 0)
                + (isConnected3(pos, pos.inDir(Dir4.W)) ? 4 : 0)
                + (isConnected3(pos, pos.inDir(Dir4.S)) ? 8 : 0);
        return hash;
    }

    private boolean isConnected3(Pos pos1, Pos pos2) {
        return isConnected(pos1, pos2) && !isConnected(pos2, pos1) ;

    }

    private boolean isConnected(Pos from, Pos to) {
        RoadPoint roadPoint = pnet.getRoadPoint(from);
        if (roadPoint == null) {
            return false;
        }
        return roadPoint.getNexts().contains(pnet.getRoadPoint(to));
    }

}
