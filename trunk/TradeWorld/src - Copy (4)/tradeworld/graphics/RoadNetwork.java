package tradeworld.graphics;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.vecmath.Point3d;

/**
 *
 * @author Kotuc
 */
public class RoadNetwork {

    private final RoadPoint[][] roadpoints;
    private int widthx;
    private int widthy;

    public RoadNetwork(int widthx, int widthy) {
        this.widthx = widthx;
        this.widthy = widthy;
        this.roadpoints = new RoadPoint[widthx][widthy];
        generateRandomWalk();
    }

    class RoadPoint {

        final Point3d pos;
        Set<RoadPoint> incidents = new HashSet<RoadPoint>();

        public RoadPoint(Point3d pos) {
            this.pos = pos;
        }

        public Point3d getPos() {
            return pos;
        }
    }

    RoadPoint getPoint(int x, int y) {
        if ((x < 0) || (y < 0) || (widthx <= x) || (widthy <= y)) {
            return null;
        }
        return roadpoints[x][y];
    }

    void addPoint(int x, int y) {
        RoadPoint roadPoint = new RoadPoint(new Point3d(x, y, 0));
        roadpoints[x][y] = roadPoint;
        for (Dir dir : Dir.values()) {
            RoadPoint point = getPoint(x + dir.dx, y + dir.dy);
            if (point != null) {
                point.incidents.add(roadPoint);
                roadPoint.incidents.add(point);
            }
        }
    }

    void removePoint(int x, int y) {
        RoadPoint roadPoint = roadpoints[x][y];
        for (RoadPoint roadPoint1 : roadPoint.incidents) {
            roadPoint1.incidents.remove(roadPoint1);
        }
        roadpoints[x][y] = null;
    }

    void generateRandomWalk() {
        Random random = new Random();

        int x = random.nextInt(widthx);
        int y = random.nextInt(widthy);

//        for (int i = 0; i < random.nextInt(20); i++) {
        for (int i = 0; i < 20; i++) {

            Dir dir = Dir.values()[random.nextInt(8)];
//            for (int j = 0; j < random.nextInt(20); j++) {
            for (int j = 0; j < 10; j++) {
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