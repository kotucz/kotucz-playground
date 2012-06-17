package kotucz.village.transport;

import com.jme3.math.Vector3f;
import kotucz.village.tiles.*;

import java.util.Random;

/**
 * @author Kotuc
 */
public abstract class AbstractGridPathNetwork {
    /*
    if true - road is shifted by 0.5 0.5 so that it is in the middle of tiles
    */
    final boolean centeroffset = true;
    protected final GenericGrid<RoadPoint> roadpoints;
//    protected final TileGrid tilegrid;
    LinearGrid lingrid;

    public AbstractGridPathNetwork(LinearGrid lingrid) {
//        this.tilegrid = grid;
        this.lingrid = lingrid;
        this.roadpoints = new GenericGrid<RoadPoint>(lingrid);
    }

    RoadPoint createRoadPoint(Pos pos) {
        if (centeroffset) {
            return new RoadPoint(pos, new Vector3f(pos.x + 0.5f, pos.y + 0.5f, 0));
        } else {
            return new RoadPoint(pos, new Vector3f(pos.x, pos.y, 0));
        }
    }



    public RoadPoint getPoint(Vector3f point) {

        if (centeroffset) {
            return getPoint((int) Math.floor(point.x), (int) Math.floor(point.y));
        } else {
            return getPoint(Math.round(point.x), Math.round(point.y));
        }
    }

    public RoadPoint getPoint(Pos pos) {
//        if (lingrid.isOutOfBounds(pos)) {
//            return null;
//        } else {
            return roadpoints.get(pos);
//        }
    }

    public RoadPoint getPoint(int x, int y) {
        return getPoint(new Pos(x, y));
    }

    public void removePoint(int x, int y) {
        RoadPoint roadPoint = roadpoints.get(x, y);
        roadPoint.unlinkAll();
//        for (RoadPoint otherPoint : roadPoint.incidents) {
//            otherPoint.incidents.remove(roadPoint);
//        }
        roadpoints.set(x, y, null);
//        correctRoadTile(x, y);
//        correctRoadTile(x - 1, y);
//        correctRoadTile(x - 1, y - 1);
//        correctRoadTile(x, y - 1);
    }

    public RoadPoint randomRoadPoint(Random random) {
        while (true) {
            RoadPoint rp = getPoint(lingrid.randomPos(random));
            if (rp != null) {
                return rp;
            }
        }
    }
}
