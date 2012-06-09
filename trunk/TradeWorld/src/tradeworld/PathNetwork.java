package tradeworld;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.vecmath.Point3d;
import tradeworld.graphics.Land3D;
import tradeworld.graphics.Land3D.TileLayer;
import tradeworld.graphics.TextureSelect;

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
    private final RoadPoint[][] roadpoints;
    private int widthx;
    private int widthy;
    private Land3D land;
    private TileLayer layer;

    public PathNetwork(Land3D land, TileLayer layer) {
        this.land = land;
        this.widthx = land.getTilesx() + 1;
        this.widthy = land.getTilesy() + 1;
        this.roadpoints = new RoadPoint[widthx][widthy];
        this.layer = layer;
//        roadTextures();
    }

    public void addPoint(int x, int y) {
//        RoadPoint roadPoint = new RoadPoint(land.new Point3d(x, y, 0));
        RoadPoint roadPoint = new RoadPoint(land.getHeighmap().get(x, y));
        roadpoints[x][y] = roadPoint;
        for (Dir dir : Dir.values()) {
            RoadPoint point = getPoint(x + dir.dx, y + dir.dy);
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

    public void correctRoadTile(int x, int y) {
//        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
//            return;
//        }
        int hash = ((getPoint(x, y) != null) ? 8 : 0) + ((getPoint(x + 1, y) != null) ? 4 : 0) + ((getPoint(x + 1, y + 1) != null) ? 2 : 0) + ((getPoint(x, y + 1) != null) ? 1 : 0);
        layer.selectTexture(x, y, selects[hash]);
    }

    void generateRandomWalk(Random random) {
//        Random random = new Random();
        int x = random.nextInt(widthx);
        int y = random.nextInt(widthy);
//        for (int i = 0; i < random.nextInt(20); i++) {
        for (int i = 0; i < widthx; i++) {
            Dir dir = Dir.values()[random.nextInt(8)];
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

    public RoadPoint getPoint(Point3d point) {
        return getPoint((int) Math.round(point.x), (int) Math.round(point.y));
    }

    public RoadPoint getPoint(int x, int y) {
        if ((x < 0) || (y < 0) || (widthx <= x) || (widthy <= y)) {
            return null;
        }
        return roadpoints[x][y];
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
        RoadPoint roadPoint = roadpoints[x][y];
        for (RoadPoint roadPoint1 : roadPoint.incidents) {
            roadPoint1.incidents.remove(roadPoint1);
        }
        roadpoints[x][y] = null;
        correctRoadTile(x, y);
        correctRoadTile(x - 1, y);
        correctRoadTile(x - 1, y - 1);
        correctRoadTile(x, y - 1);
    }

    void roadTextures() {
//        int[][] roads = new int[widthx + 1][widthy + 1];
//
//        for (int x = 0; x < widthx + 1; x++) {
//            for (int y = 0; y < widthy + 1; y++) {
//                if (Math.random() < 0.3) {
//                    roads[x][y] = 1;
//                }
//            }
//        }
        for (int x = 0; x < land.getTilesx(); x++) {
            for (int y = 0; y < land.getTilesy(); y++) {
//                int hash = (roads[x][y] * 8) +
//                (roads[x + 1][y] * 4) +
//                        (roads[x + 1][y + 1] * 2) +
//                (roads[x][y + 1] * 1);
                correctRoadTile(x, y);
            }
        }
    }

    public class RoadPoint {

        final Point3d pos;
        Set<RoadPoint> incidents = new HashSet<RoadPoint>();

        public RoadPoint(Point3d pos) {
            this.pos = pos;
        }

        public Point3d getPos() {
            return pos;
        }

        @Override
        public String toString() {
//            return this.pos + "@" + super.toString().split("@")[1];
            return "(" + pos.x + "," + pos.y + ")";

        }
    }

    enum Dir {

        E(1, 0),
        NE(1, 1),
        N(0, 1),
        NW(-1, 1),
        W(-1, 0),
        SW(-1, -1),
        S(0, -1),
        SE(1, -1);
        private final int dx, dy;

        public int dx() {
            return dx;
        }

        public int dy() {
            return dy;
        }

        private Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }
}
