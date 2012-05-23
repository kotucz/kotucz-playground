package pathpoint;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tkotula
 */
public class PathPoint {

//    final static int SIDE_LEFT = 1;
//    final static int SIDE_RIGHT = -1;
    enum Result {

        LEFT,
        RIGHT,
        ON_ROAD,
        BEFORE,
        AFTER;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        {
            Point2D p1 = new Point2D(0, 0);
            Point2D p2 = new Point2D(1, 0);
            Point2D p3 = new Point2D(1, 1);

            System.out.println("Test Side " + side(p1, p2, p3));
        }


        List<Point2D> path = new ArrayList<Point2D>();
        path.add(new Point2D(0, 0));
        path.add(new Point2D(3, 0));
        path.add(new Point2D(6, 3));
        path.add(new Point2D(9, 3));


        test(path, new Point2D(0, 0), Result.BEFORE); // could be ON_ROAD, is corner case
        
        test(path, new Point2D(1, -2), Result.RIGHT); // 1
        test(path, new Point2D(1, 1), Result.LEFT); // 2
        test(path, new Point2D(4, -2), Result.RIGHT); // 3
        test(path, new Point2D(5, 1), Result.RIGHT); // 
        test(path, new Point2D(7, 1), Result.RIGHT); // 
        test(path, new Point2D(7, -1), Result.RIGHT); // 6
        
        test(path, new Point2D(5, 5), Result.LEFT); // 8
        test(path, new Point2D(8, 5), Result.LEFT); // 9
        test(path, new Point2D(11, 5), Result.AFTER); // 10
        
        test(path, new Point2D(-1, 1), Result.BEFORE); // 11
        test(path, new Point2D(-1, 5), Result.BEFORE); // 12
        test(path, new Point2D(-1, 12), Result.LEFT); // 13

        test(path, new Point2D(10, -15), Result.RIGHT); // 14
        

    }

    static void test(List<Point2D> path, Point2D p, Result expect) {
        System.out.println("\nTest: " + p);
        Closest cst = findClosest(path, p);
        System.out.println(cst+" expect "+expect);
    }
    
    

    static Result toSide(double d) {
        switch ((int) Math.signum(d)) {
            case -1:
                return Result.RIGHT;
            case 1:
                return Result.LEFT;
            default:
                return Result.ON_ROAD;
        }
    }
    
    public static Result findClosestSide(List<Point2D> path, Point2D p) {
        return findClosest(path, p).res;
    }
    
    /**
     * the point must not be u < 0 of the first segment nor 1 < u from last segment
     * @param path
     * @param p
     * @return 
     */
    static Closest findClosest(List<Point2D> path, Point2D p) {
        Segment seg = new Segment();

        Closest best = new Closest();
        best.point = path.get(0);
        best.dist = best.point.distance(p);
//        best.side = 0;
        best.u = -1;
        best.segment = -1;
        best.res = Result.BEFORE;

        for (int i = 0; i < path.size() - 1; i++) {
            seg.p1 = path.get(i);
            seg.p2 = path.get(i + 1);

            double u = seg.getU(p);
            if (u < 0) {
                // processed before
                // closestPoint = p1;  
            } else if (u > 1) {
                // closestPoint = p2;
                // possibly at turn around p2
                // assume point is on outer angle where this u > 1 and next u < 0 
                Closest closest = new Closest();
                closest.point = seg.p2;
                closest.dist = closest.point.distance(p);
                if (i < path.size() - 2) {
                    closest.res = toSide( -seg.side(path.get(i + 2)));
                } else {
//                    after whole path
                    closest.res = Result.AFTER;
                }
                closest.u = u;
                closest.segment = i;
                
                if (closest.dist < best.dist) {
                    best = closest;
                }
            } else {
                Closest closest = new Closest();
                closest.point = seg.pointAt(u);
                closest.dist = closest.point.distance(p);
//                closest.side = (int) Math.signum(seg.side(p));
                closest.res = toSide(seg.side(p));
                closest.u = u;
                closest.segment = i;
                // closestPoint = new Point2D.Double(p1.getX() + u * xDelta, p1.getY() + u * yDelta);
                if (closest.dist < best.dist) {
                    best = closest;
                }
            }
        }

        return best;

    }

    static class Closest {

        Point2D point;
        double dist = Double.MAX_VALUE;
//        int side;
        int segment;
        double u;
        Result res;

        
        
        @Override
        public String toString() {
            return "Closest{" + "point=" + point + ", dist=" + dist + ", segment=" + segment + ", u=" + u + ", res=" + res + '}';
        }
    }

    static class Segment {

        Point2D p1;
        Point2D p2;

        double side(Point2D p3) {
            return PathPoint.side(p1, p2, p3);
        }

        double getU(Point2D p3) {
            final double xDelta = p2.getX() - p1.getX();
            final double yDelta = p2.getY() - p1.getY();

            if ((xDelta == 0) && (yDelta == 0)) {
                throw new IllegalArgumentException("p1 and p2 cannot be the same point");
            }

            final double u = ((p3.getX() - p1.getX()) * xDelta + (p3.getY() - p1.getY()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);
            return u;
        }

        Point2D pointAt(double u) {

            final double xDelta = p2.getX() - p1.getX();
            final double yDelta = p2.getY() - p1.getY();

            if ((xDelta == 0) && (yDelta == 0)) {
                throw new IllegalArgumentException("p1 and p2 cannot be the same point");
            }

            final Point2D closestPoint;
            if (u < 0) {
                closestPoint = p1;
            } else if (u > 1) {
                closestPoint = p2;
            } else {
                closestPoint = new Point2D(p1.getX() + u * xDelta, p1.getY() + u * yDelta);
            }

            return closestPoint;
        }
    }

    /**
     * Returns the distance of p3 to the segment defined by p1,p2;
     * 
     * @param p1
     *                First point of the segment
     * @param p2
     *                Second point of the segment
     * @param p3
     *                Point to which we want to know the distance of the segment
     *                defined by p1,p2
     * @return The distance of p3 to the segment defined by p1,p2
     */
    public static double distanceToSegment(Point2D p1, Point2D p2, Point2D p3) {

        final double xDelta = p2.getX() - p1.getX();
        final double yDelta = p2.getY() - p1.getY();

        if ((xDelta == 0) && (yDelta == 0)) {
            throw new IllegalArgumentException("p1 and p2 cannot be the same point");
        }

        final double u = ((p3.getX() - p1.getX()) * xDelta + (p3.getY() - p1.getY()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

        final Point2D closestPoint;
        if (u < 0) {
            closestPoint = p1;
        } else if (u > 1) {
            closestPoint = p2;
        } else {
            closestPoint = new Point2D(p1.getX() + u * xDelta, p1.getY() + u * yDelta);
        }

        return closestPoint.distance(p3);
    }

    /**
     * 
     * @param p1
     * @param p2
     * @param p3
     * @return 0 collinear points
     * @return positive for p3 left to p1 to p2 vector
     * @return negative for p3 right to p1 to p2 vector
     */
    public static double side(Point2D p1, Point2D p2, Point2D p3) {
        return (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p2.getY() - p1.getY()) * (p3.getX() - p1.getX());
    }

    static class Point2D {

        double x;
        double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double distance(Point2D other) {


            return Math.hypot(other.getX() - this.getX(), other.getY() - this.getY());
        }
    }
}
