package kotucz.village.transport;



import java.util.Random;

import com.jme3.math.Vector3f;
import kotucz.village.common.Dir;
import kotucz.village.common.Dir4;
import kotucz.village.tiles.*;


/**
 *
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
    
    
    private final GenericGrid<RoadPoint> roadpoints;
    
    private final TileGrid tilegrid;
    
    private final int widthx;
    private final int widthy;

    LinearGrid lingrid;


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

            roadpoints.set(random.nextInt(lingrid.getTotalNum()), new RoadPoint(null));
            
//            TextureSelect textureSelect = selects[i];
            
        }
    }
    
    public void addPoint(int x, int y) {
        System.out.println("add point "+x+", "+y);
//        RoadPoint roadPoint = new RoadPoint(land.new Point3d(x, y, 0));
//        RoadPoint roadPoint = new RoadPoint(land.getHeighmap().get(x, y));
        RoadPoint roadPoint = new RoadPoint(null);
        roadpoints.set(x, y, roadPoint);
        for (Dir dir : Dir.values()) {
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
        for (Tile t:lingrid) {
            tilegrid.setTexture(t.x, t.y, getRoadTileHash(t.x, t.y));
        }
        tilegrid.updateTexture();
    }
    
    
    /**
     * works on vertices
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
        int hash = ((getPoint(x, y) != null) ? 1 : 0)
                + ((getPoint(x + 1, y) != null) ? 2 : 0)
                + ((getPoint(x + 1, y + 1) != null) ? 4 : 0)
                + ((getPoint(x, y + 1) != null) ? 8 : 0);
//        layer.selectTexture(x, y, selects[hash]);
        return hash;
    }           
    
    public int getRoadTileHash(int x, int y) {
//        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
//            return;
//        }
        
                
        if (getPoint(x, y) == null) {
            return 0;
        }
        
        int hash = ((getPoint(x + 1, y) != null) ? 1 : 0) +
                   ((getPoint(x , y + 1) != null) ? 2 : 0) +
                   ((getPoint(x - 1, y) != null) ? 4 : 0) + 
                   ((getPoint(x, y - 1) != null) ? 8 : 0);
        return hash;
    }
    
    public void generateRandomWalk(Random random) {
//        Random random = new Random();
        int x = random.nextInt(widthx);
        int y = random.nextInt(widthy);
//        for (int i = 0; i < random.nextInt(20); i++) {
        for (int i = 0; i < widthx; i++) {
//            Dir dir = Dir.values()[random.nextInt(8)];
            Dir4 dir = Dir4.values()[random.nextInt(4)];
            
            
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
        return getPoint((int) Math.round(point.x), (int) Math.round(point.y));
    }

    public RoadPoint getPoint(Pos pos) {
        return getPoint(pos.x , pos.y);
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

    RoadPoint randomRoadPoint() {
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
