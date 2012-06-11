package kotucz.village.transport;

import kotucz.village.tiles.Pos;

/**
 * @author Kotuc
 */
public class BlockingVehicleBehavior extends VehicleBehavior {

    final BlockingTraffic traffic;

    public BlockingVehicleBehavior(Vehicle vehicle, BlockingTraffic traffic) {
        super(vehicle);
        this.traffic = traffic;
    }



    @Override
    public boolean followTrajectory(float time) {
        if (t > trajectory.length()) {
            return true;
        }



        Pos nextpos = trajectory.getNextRoadPoint(t).getPos();
        Vehicle occupier = traffic.getOccupier(nextpos);
        if (occupier != null && occupier != this.vehicle) {
            // stop
        } else {

            traffic.occupiers.set(vehicle.reservedPos, null); // release

            t += time;

            vehicle.setPos(trajectory.getPoint(t));

//            vehicle.reservedPos = vehicle.getPos();
            vehicle.reservedPos = nextpos;

            traffic.occupiers.set(vehicle.reservedPos, vehicle);

        }
//        Vector3d vec = trajectory.getVector(t);
//        double angle = Math.atan2(vec.y, vec.x);
//        model.setAngle(angle);

        return false;
    }


}
