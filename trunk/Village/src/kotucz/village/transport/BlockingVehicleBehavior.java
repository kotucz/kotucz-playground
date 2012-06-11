package kotucz.village.transport;

import kotucz.village.tiles.Pos;

import java.util.HashSet;
import java.util.Set;

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
        vehicle.requestPos = nextpos;
        Vehicle occupier = traffic.getOccupier(nextpos);
        if (occupier != null && occupier != this.vehicle) {
            // stop
            System.out.println("Vehicle "+vehicle.getName()+" "+vehicle.getPos() + " holds "+vehicle.reservedPos +" waits for "+occupier.getName()+" "+occupier.getPos());

            // simple deadlock of 2 cars
//            boolean deadlock = (occupier.requestPos == vehicle.reservedPos);

            boolean deadlock = isDeadlock(new HashSet<Vehicle>(), vehicle.requestPos);

            if (deadlock) {
                System.out.println("DEADLOCK");
                path = null; // choose another path
                return true;
            }
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

    boolean isDeadlock(Set<Vehicle> vehs, Pos request) {
        Vehicle occupier = traffic.getOccupier(request);
        if (occupier == null) {
            return false;  // this one has free way
        } else {
            return vehs.add(occupier) || isDeadlock(vehs, occupier.requestPos);

        }

    }


}
