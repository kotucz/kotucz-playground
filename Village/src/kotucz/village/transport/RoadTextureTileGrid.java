package kotucz.village.transport;

import com.jme3.material.Material;
import kotucz.village.common.Dir4;
import kotucz.village.game.MyGame;
import kotucz.village.tiles.*;

import java.util.Set;

/**
 * @author Kotuc
 */
public class RoadTextureTileGrid extends TileGrid {

    public RoadTextureTileGrid(AbstractGridPathNetwork pnet, Material mat, MyGame myGame) {
        super(pnet.lingrid, mat, myGame);
        this.pnet = pnet;
    }

    final AbstractGridPathNetwork pnet;

//    @Override
//    public void updateTexture() {
////        for (Tile t : lingrid) {
////            tilegrid.setTexture(t.x, t.y, getRoadTileHash(t.x, t.y));
////        }
////        tilegrid.updateTexture();
//
//        this.updateGrid(new AbstractSetGrid(this, 0) {
//            @Override
//            public boolean contains(Pos pos) {
//                return pnet.getRoadPoint(pos) != null;
//            }
//        });
//
////        super.updateTexture();
//    }


    @Override
    public void updateTexture() {
        for (Tile t : lingrid) {
//            tilegrid.setTexture(t.x, t.y, getRoadTileHash(t.x, t.y));
            setTexture(t.x, t.y, getRoadTileHash(t.pos));
        }

        super.updateTexture();
    }


//    private int getRoadTileHash(Pos pos) {
////        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
////            return;
////        }
//
//        RoadPoint point = pnet.getRoadPoint(pos);
//        if (point == null) {
//            return 0;
//        }
//
//        Set<RoadPoint> s = point.getNexts();
//
//        int hash = ((s.contains(pnet.getRoadPoint(pos.inDir(Dir4.E)))) ? 1 : 0)
//                + ((s.contains(pnet.getRoadPoint(pos.inDir(Dir4.N)))) ? 2 : 0)
//                + ((s.contains(pnet.getRoadPoint(pos.inDir(Dir4.W)))) ? 4 : 0)
//                + ((s.contains(pnet.getRoadPoint(pos.inDir(Dir4.S)))) ? 8 : 0);
//        return hash;
//    }

    private int getRoadTileHash(Pos pos) {
//        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
//            return;
//        }

        RoadPoint point = pnet.getRoadPoint(pos);
        if (point == null) {
            return 0;
        }

        Set<RoadPoint> s = point.getNexts();

        int hash = (isConnected2(pos, pos.inDir(Dir4.E)) ? 1 : 0)
                + (isConnected2(pos, pos.inDir(Dir4.N)) ? 2 : 0)
                + (isConnected2(pos, pos.inDir(Dir4.W)) ? 4 : 0)
                + (isConnected2(pos, pos.inDir(Dir4.S)) ? 8 : 0);
        return hash;
    }

    private boolean isConnected2(Pos pos1, Pos pos2) {
        return isConnected(pos1, pos2) || isConnected(pos2, pos1) ;

    }

    private boolean isConnected(Pos from, Pos to) {
        RoadPoint roadPoint = pnet.getRoadPoint(from);
        if (roadPoint == null) {
            return false;
        }
        return roadPoint.getNexts().contains(pnet.getRoadPoint(to));
    }

}
