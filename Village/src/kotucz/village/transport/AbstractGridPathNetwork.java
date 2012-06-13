package kotucz.village.transport;

import com.jme3.math.Vector3f;
import kotucz.village.tiles.*;

/**
 * @author Kotuc
 */
public class AbstractGridPathNetwork {
    /*
    if true - road is shifted by 0.5 0.5 so that it is in the middle of tiles
    */
    final boolean centeroffset = true;
    protected final GenericGrid<RoadPoint> roadpoints;
    protected final TileGrid tilegrid;
    LinearGrid lingrid;

    public AbstractGridPathNetwork(TileGrid grid) {
        this.tilegrid = grid;
        this.lingrid = tilegrid.getLingrid();
        this.roadpoints = new GenericGrid<RoadPoint>(lingrid);
    }

    RoadPoint createRoadPoint(Pos pos) {
        if (centeroffset) {
            return new RoadPoint(pos, new Vector3f(pos.x + 0.5f, pos.y + 0.5f, 0));
        } else {
            return new RoadPoint(pos, new Vector3f(pos.x, pos.y, 0));
        }
    }

    public void updateTextures() {
//        for (Tile t : lingrid) {
//            tilegrid.setTexture(t.x, t.y, getRoadTileHash(t.x, t.y));
//        }
//        tilegrid.updateTexture();

        tilegrid.updateGrid(new AbstractSetGrid(tilegrid, 0) {
            @Override
            public boolean contains(Pos pos) {
                return getPoint(pos) != null;
            }
        });

    }

    public RoadPoint getPoint(Vector3f point) {

        if (centeroffset) {
            return getPoint((int) Math.floor(point.x), (int) Math.floor(point.y));
        } else {
            return getPoint((int) Math.round(point.x), (int) Math.round(point.y));
        }
    }

    public RoadPoint getPoint(Pos pos) {
        if (lingrid.isOutOfBounds(pos)) {
            return null;
        }
        return getPoint(pos.x, pos.y);
    }

    public RoadPoint getPoint(int x, int y) {
        return getPoint(new Pos(x, y));
    }

    public void removePoint(int x, int y) {
        RoadPoint roadPoint = roadpoints.get(x, y);
        for (RoadPoint otherPoint : roadPoint.incidents) {
            otherPoint.incidents.remove(roadPoint);
        }
        roadpoints.set(x, y, null);
//        correctRoadTile(x, y);
//        correctRoadTile(x - 1, y);
//        correctRoadTile(x - 1, y - 1);
//        correctRoadTile(x, y - 1);
    }
}
