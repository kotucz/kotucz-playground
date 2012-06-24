package kotucz.village.transport;

import kotucz.village.build.Building;
import kotucz.village.build.Buildings;

import java.util.Random;

/**
 * @author Kotuc
 */
public class TransporterBehavior extends BlockingVehicleBehavior {

    Depot srcDepot;
    RoadPoint srcRoadPoint;
    Depot destDepot;
    RoadPoint destRoadPoint;
    State state = State.WAIT;
    private Buildings buildings;

    public TransporterBehavior(Vehicle vehicle, BlockingTraffic network, Buildings buildings) {
        super(vehicle, network);
        this.buildings = buildings;
    }

    public void setSrcDepot(Depot srcDepot) {
        this.srcDepot = srcDepot;
        this.srcRoadPoint = network.getRoadPoint(srcDepot.getEntrancePos());
    }

    public void setDestDepot(Depot destDepot) {
        this.destDepot = destDepot;
        this.destRoadPoint = network.getRoadPoint(destDepot.getEntrancePos());
        path = null;
        setState(State.GO_FOR_LOAD);
    }


    public void act(float time) {
        //  System.out.println("" + this + " " + state);
        switch (state) {
//            case RANDOM:
//                if (path == null) {
//                    destLong = network.randomRoadPoint(new Random());
//                }
//                if (travelTo(destLong, time)) {
//                    path = null;
//                }
//                break;
            case WAIT:
                // TODO uncomment/fix
                for (Building building : buildings) {
                    if (building instanceof Depot) {
                        if (srcDepot == null) {
                            setSrcDepot((Depot) building);
                        } else {
                            // second depot
                            setDestDepot((Depot) building);
                        }
                    }

//                        if (srcDepot == null) {
//                            setSrcDepot(building);
//                        } else {
//                            // second depot
//                            setDestDepot(building);
//                        }

                }
                if (destDepot != null) {
                    setState(State.GO_FOR_LOAD);
                    System.out.println("Depos found " + srcRoadPoint + " -> " + destRoadPoint);
                } else {
                    srcDepot = null;
                }
                break;
            case GO_FOR_LOAD:
                if (travelTo(srcRoadPoint, time)) {
                    path = null;
                    // reset path
                    setState(State.LOADING);
                    srcDepot.addVehicle(vehicle);
                }
                break;
            case LOADING:
                if (srcDepot.requestLoadVehicle(vehicle, Goods.Type.WOOD, srcDepot.getOwner())) {
//                    destLong = null; // dest depot
                    setState(State.GO_FOR_UNLOAD);
                    srcDepot.removeVehicle(vehicle);
                }
                break;
            case GO_FOR_UNLOAD:
                if (travelTo(destRoadPoint, time)) {
                    path = null;
                    // reset path
                    setState(State.UNLOADING);
                    destDepot.addVehicle(vehicle);
                }
                break;
            case UNLOADING:
                if (destDepot.requestUnloadVehicle(vehicle, Goods.Type.WOOD, destDepot.getOwner())) {
//                    destLong = null; // next depot
                    setState(State.GO_FOR_LOAD);
                    destDepot.removeVehicle(vehicle);
                }
                break;
        }
//        if (travelTo(destLong)) {
//            destLong = getWorld().getRoadNetwork().randomRoadPoint();
//            path = null;
//        }
//        model.refresh();
        updateModel();
    }

    public String getStatusMessage() {
        switch (state) {
            case GO_FOR_LOAD:
                return "Heading to src " + srcDepot;
            case GO_FOR_UNLOAD:
                return "Heading to dest " + destDepot;
            default:
                return state.toString();
        }
    }

    void setState(State state) {
        this.state = state;
        System.out.println("" + vehicle + " " + state);
    }

    enum State {

        //        RANDOM,
        WAIT,
        GO_FOR_LOAD,
        LOADING,
        GO_FOR_UNLOAD,
        UNLOADING;
    }

    @Override
    public String toString() {
        return "TranspBeh{" +
                ", state=" + state +
//                "srcDepot=" + srcDepot +
                ", src=" + srcRoadPoint +
//                ", destDepot=" + destDepot +
                ", dest=" + destRoadPoint +
//                ", buildings=" + buildings +
                '}';
    }
}
