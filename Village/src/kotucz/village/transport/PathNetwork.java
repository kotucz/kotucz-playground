package kotucz.village.transport;


import com.jme3.math.Vector3f;
import kotucz.village.common.Dir;
import kotucz.village.common.Neighbouring;
import kotucz.village.tiles.*;

import java.util.Random;


/**
 * @author Kotuc
 */
public class PathNetwork {

    final TextureSelect[] selects = new TextureSelect[]{
            new TextureSelect(0, 0, 8, 1, 0, 0),
            new TextureSelect(1, 0, 8, 1, 2, 0),
            new TextureSelect(1, 0, 8, 1, 1, 0),
            new TextureSelect(2, 0, 8, 1, 2, 0),
            new TextureSelect(1, 0, 8, 1, 0, 0),
            new TextureSelect(3, 0, 8, 1, 1, 0),
            new TextureSelect(2, 0, 8, 1, 1, 0),
            new TextureSelect(4, 0, 8, 1, 0, 0),
            new TextureSelect(1, 0, 8, 1, 3, 0),
            new TextureSelect(2, 0, 8, 1, 3, 0),
            new TextureSelect(3, 0, 8, 1, 0, 0),
            new TextureSelect(4, 0, 8, 1, 1, 0),
            new TextureSelect(2, 0, 8, 1, 0, 0),
            new TextureSelect(4, 0, 8, 1, 2, 0),
            new TextureSelect(4, 0, 8, 1, 3, 0),
            new TextureSelect(5, 0, 8, 1, 0, 0)};

    /*
     if true - road is shifted by 0.5 0.5 so that it is in the middle of tiles
     */
    final boolean centeroffset = true;


    private final GenericGrid<RoadPoint> roadpoints;

    private final TileGrid tilegrid;

    private final int widthx;
    private final int widthy;

    LinearGrid lingrid;

    Neighbouring neighbouring = Neighbouring.N4;


//    private Land3D land;
//    private TileLayer layer;


//    public PathNetwork(Land3D land) {
//        this.land = land;
//        this.widthx = land.getTilesx() + 1;
//        this.widthy = land.getTilesy() + 1;
//        this.roadpoints = new RoadPoint[widthx][widthy];
////        this.layer = layer;
////        roadTextures();
//    }

    public PathNetwork(TileGrid grid) {
//        this.land = land;
        this.tilegrid = grid;
        this.lingrid = tilegrid.getLingrid();
        this.widthx = lingrid.getSizeX();
        this.widthy = lingrid.getSizeY();
        this.roadpoints = new GenericGrid<RoadPoint>(lingrid);
//        this.layer = layer;
//        roadTextures();


//        generateRandomWalk(random);
    }

    public void randomlySelect(int s) {
        final Random random = new Random();
        for (int i = 0; i < s; i++) {

            int index = random.nextInt(lingrid.getTotalNum());

            roadpoints.set(index, createRoadPoint(lingrid.getPos(index)));

//            TextureSelect textureSelect = selects[i];

        }
    }

    RoadPoint createRoadPoint(Pos pos) {
        if (centeroffset) {
            return new RoadPoint(pos, new Vector3f(pos.x + 0.5f, pos.y + 0.5f, 0));
        } else {
            return new RoadPoint(pos, new Vector3f(pos.x, pos.y, 0));
        }
    }

    public void addPoint(int x, int y) {
        System.out.println("add point " + x + ", " + y);
//        RoadPoint roadPoint = new RoadPoint(land.new Point3d(x, y, 0));
//        RoadPoint roadPoint = new RoadPoint(land.getHeighmap().get(x, y));
        RoadPoint roadPoint = createRoadPoint(new Pos(x, y));
        roadpoints.set(x, y, roadPoint);
        for (Dir dir : neighbouring.getDirections()) {
            RoadPoint point = getPoint(x + dir.dx(), y + dir.dy());
            if (point != null) {
                point.incidents.add(roadPoint);
                roadPoint.incidents.add(point);
            }
        }
        correctRoadTile(x, y);
        correctRoadTile(x - 1, y);
        correctRoadTile(x - 1, y - 1);
        correctRoadTile(x, y - 1);

    }

    public void updateTextures() {
        for (Tile t : lingrid) {
            tilegrid.setTexture(t.x, t.y, getRoadTileHash(t.x, t.y));
        }
        tilegrid.updateTexture();
    }


    /**
     * works on vertices
     *
     * @param x
     * @param y
     */
    public void correctRoadTile(int x, int y) {
//        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
//            return;
//        }
        int hash = ((getPoint(x, y) != null) ? 8 : 0) + ((getPoint(x + 1, y) != null) ? 4 : 0) + ((getPoint(x + 1, y + 1) != null) ? 2 : 0) + ((getPoint(x, y + 1) != null) ? 1 : 0);
//        layer.selectTexture(x, y, selects[hash]);
    }

    public int getFlatNodeTileHash(int x, int y) {
//        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
//            return;
//        }
        int hash = ((contains(x, y)) ? 1 : 0)
                + ((contains(x + 1, y)) ? 2 : 0)
                + ((contains(x + 1, y + 1)) ? 4 : 0)
                + ((contains(x, y + 1)) ? 8 : 0);
//        layer.selectTexture(x, y, selects[hash]);
        return hash;
    }

    public int getRoadTileHash(int x, int y) {
//        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
//            return;
//        }


        if (!contains(x, y)) {
            return 0;
        }

        int hash = ((contains(x + 1, y)) ? 1 : 0) +
                ((contains(x, y + 1)) ? 2 : 0) +
                ((contains(x - 1, y)) ? 4 : 0) +
                ((contains(x, y - 1)) ? 8 : 0);
        return hash;
    }

             boolean contains(int x, int y) {
                 return getPoint(x, y )!=null;
             }

    public void generateRandomWalk(Random random) {
//        Random random = new Random();
        int x = random.nextInt(widthx);
        int y = random.nextInt(widthy);
//        for (int i = 0; i < random.nextInt(20); i++) {
        for (int i = 0; i < widthx; i++) {
//            Dir8 dir = Dir8.values()[random.nextInt(8)];
            Dir dir = neighbouring.randomDir(random);


            final int nextInt = random.nextInt(20);
            for (int j = 0; j < nextInt; j++) {
//            for (int j = 0; j < 10; j++) {
                try {
                    addPoint(x, y);
                } catch (Exception e) {
                    x = random.nextInt(widthx);
                    y = random.nextInt(widthy);
                }
                x += dir.dx();
                y += dir.dy();
            }
        }
    }

    public RoadPoint getPoint(Vector3f point) {

        if (centeroffset) {
            return getPoint((int) Math.floor(point.x), (int) Math.floor(point.y));
        } else {
            return getPoint((int) Math.round(point.x), (int) Math.round(point.y));
        }
    }

    public RoadPoint getPoint(Pos pos) {
        return getPoint(pos.x, pos.y);
    }

    public RoadPoint getPoint(int x, int y) {
        if ((x < 0) || (y < 0) || (widthx <= x) || (widthy <= y)) {
            return null;
        }
        return roadpoints.get(x, y);
    }

    public boolean isAreaFree(double cx, double cy, double wx, double wy) {
        for (int i = (int) Math.round(cx - 0.5 * wy); i <= (int) Math.round(cx + 0.5 * wx); i++) {
            for (int j = (int) Math.round(cy - 0.5 * wy); j <= (int) Math.round(cy + 0.5 * wy); j++) {
                if (getPoint(i, j) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public RoadPoint randomRoadPoint() {
        Random random = new Random();
        while (true) {
            RoadPoint rp = getPoint(random.nextInt(widthx), random.nextInt(widthy));
            if (rp != null) {
                return rp;
            }
        }
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

//    void roadTextures() {
////        int[][] roads = new int[widthx + 1][widthy + 1];
////
////        for (int x = 0; x < widthx + 1; x++) {
////            for (int y = 0; y < widthy + 1; y++) {
////                if (Math.random() < 0.3) {
////                    roads[x][y] = 1;
////                }
////            }
////        }
//        for (int x = 0; x < widthx; x++) {
//            for (int y = 0; y < widthy; y++) {
////                int hash = (roads[x][y] * 8) +
////                (roads[x + 1][y] * 4) +
////                        (roads[x + 1][y + 1] * 2) +
////                (roads[x][y + 1] * 1);
//                correctRoadTile(x, y);
//            }
//        }
//    }


}
