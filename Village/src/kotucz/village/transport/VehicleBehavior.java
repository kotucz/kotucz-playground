package kotucz.village.transport;

import com.jme3.math.Vector3f;

import java.util.LinkedList;
import java.util.List;

public  class VehicleBehavior {
    final Vehicle vehicle;
    Trajectory trajectory;
    float t;
    RoadPoint destLong;


    LinkedList<RoadPoint> path = null;// new LinkedList<RoadPoint>();

    public VehicleBehavior(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void act(float time) {

        if (path == null) {
            destLong = vehicle.getNetwork().randomRoadPoint();
        }
        if (travelTo(destLong, time)) {
            path = null;
        }
        vehicle.updateModel();
    }

    /**
     * @param point
     * @return true when reached, false during way
     */
    public boolean travelTo(RoadPoint point, float time) {
//        if (point.getPosVector().distance(vector) < type.getSpeed()) {
//            return true;
//        }
        if (path == null) {
            // create new path
//            destLong = getWorld().getRoadNetwork().randomRoadPoint();
            List<RoadPoint> findPath = findPath(point);
            if (findPath != null && findPath.size()>1) {
                int i = 0;
                // debug trace path
//                for (RoadPoint roadPoint : findPath) {
//                    getWorld().showNotification(
//                            Notification3D.createTextNotification("R" + i, new Color3f(1, 0, 0)),
//                            roadPoint.getPosVector());
//                    i++;
//                }
                path = new LinkedList<RoadPoint>();
                path.addAll(findPath);
//                this.trajectory = new SplineTrajectory(path, (Element.ROAD.equals(type.element) ? 0.25 : 0));
                this.trajectory = new Trajectory(path);
                this.t = 0;
            } else {
                System.err.println("Path not found !");
                return false;
            }
        }
        return followTrajectory(time);
//        return followPath();
    }

    public List<RoadPoint> findPath(RoadPoint target) {
        PathFinding pathFinding = new PathFinding(vehicle.getNetwork());
//        RoadPoint curr = roadNetwork.contains(
//                (int) Math.round(vector.x),
//                (int) Math.round(vector.y));
        RoadPoint curr = vehicle.getNetwork().getPoint(vehicle.getPosVector());
        return pathFinding.aStar(curr, target);
    }

    public boolean followTrajectory(float time) {
        if (t > trajectory.length()) {
            return true;
        }

        t += time;

        vehicle.setPos(trajectory.getPoint(t));

//        Vector3d vec = trajectory.getVector(t);
//        double angle = Math.atan2(vec.y, vec.x);
//        model.setAngle(angle);

        return false;
    }

    public boolean followPath() {
        if (path.isEmpty()) {
            return true;
        }

        double lookAhead = 1;

        Vector3f tgt = new Vector3f(path.peek().getPosVector());

//        if (vector.distance(tgt) < lookAhead && path.size() > 1) {
//            if (vector.distance(tgt) > 0.5) {
//                tgt.interpolate(path.get(1).getPosVector(), (vector.distance(tgt) / lookAhead));
//            }
//        }

        if (navigateLocal(tgt, 0.02)) {
            RoadPoint poll = path.poll(); // remove
        }

        return false;
    }

    /**
     * Goes to dest point not faster than speed (per one call)
     *
     * @param dest
     * @param speed
     * @return true if reached destination; false otherwise
     */
    public boolean navigateLocal(Vector3f dest, double speed) {
//        if (dest == null) {
//            return true;
//        }
//
//        Vector3d vec = new Vector3d(dest);
//
//        vec.sub(vector);
//        if (vec.lengthSquared() <= speed * speed) {
//            vector.set(dest);
//            model.setPos(vector);
//            return true;
//        }
//
//        double atan2 = Math.atan2(vec.y, vec.x);
//        double angle = model.getAngle();
//
//        double diff = atan2 - angle;
//        while (diff > Math.PI) {
//            diff -= 2 * Math.PI;
//        }
//
//        while (diff < Math.PI) {
//            diff += 2 * Math.PI;
//        }
//
//        angle += diff;
//
//        model.setAngle(angle);
//
//        vec.set(Math.cos(angle), Math.sin(angle), 0);
//
//        vec.normalize();
//        vec.scale(speed);
//        vector.add(vec);
//        model.setPos(vector);
//
//        spentFuel();
//
//        return false;
        throw new UnsupportedOperationException("obsollette or uncomment");
    }

    public String getStatusMessage() {
        return "Doing something";
    }

}