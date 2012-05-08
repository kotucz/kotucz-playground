package tradeworld;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.vecmath.Point3d;
import tradeworld.graphics.Land3D;

/**
 *
 * @author Kotuc
 */
public class RoadNetwork {

    private final RoadPoint[][] roadpoints;
    private int widthx;
    private int widthy;
    private Land3D land;

    public RoadNetwork(Land3D land) {
        this.land = land;
        this.widthx = land.getWidthx() + 1;
        this.widthy = land.getWidthy() + 1;
        this.roadpoints = new RoadPoint[widthx][widthy];
        generateRandomWalk();
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

    public RoadPoint getPoint(Point3d point) {
        return getPoint((int) Math.round(point.x), (int) Math.round(point.y));
    }

    public RoadPoint getPoint(int x, int y) {
        if ((x < 0) || (y < 0) || (widthx <= x) || (widthy <= y)) {
            return null;
        }
        return roadpoints[x][y];
    }

    public void addPoint(int x, int y) {
        RoadPoint roadPoint = new RoadPoint(new Point3d(x, y, 0));
        roadpoints[x][y] = roadPoint;
        for (Dir dir : Dir.values()) {
            RoadPoint point = getPoint(x + dir.dx, y + dir.dy);
            if (point != null) {
                point.incidents.add(roadPoint);
                roadPoint.incidents.add(point);
            }
        }
        land.correctRoadTile(this, x, y);
        land.correctRoadTile(this, x - 1, y);
        land.correctRoadTile(this, x - 1, y - 1);
        land.correctRoadTile(this, x, y - 1);
    }

    public void removePoint(int x, int y) {
        RoadPoint roadPoint = roadpoints[x][y];
        for (RoadPoint roadPoint1 : roadPoint.incidents) {
            roadPoint1.incidents.remove(roadPoint1);
        }
        roadpoints[x][y] = null;
        land.correctRoadTile(this, x, y);
        land.correctRoadTile(this, x - 1, y);
        land.correctRoadTile(this, x - 1, y - 1);
        land.correctRoadTile(this, x, y - 1);
    }

    void generateRandomWalk() {
        Random random = new Random();

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

    RoadPoint randomRoadPoint() {
        Random random = new Random();
        while (true) {
            RoadPoint rp = getPoint(random.nextInt(widthx), random.nextInt(widthy));
            if (rp != null) {
                return rp;
            }
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
