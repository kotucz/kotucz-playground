package tradeworld.actions;

import tradeworld.BuildingId;
import tradeworld.Depot;
import tradeworld.Vehicle;
import tradeworld.World;

public class BuildVehicleAction implements GameAction {

    private BuildingId depotid;
    private Vehicle.Type selectedVeicleType;

    public BuildVehicleAction(Depot depot, Vehicle.Type selectedVeicleType) {
        super();
        this.depotid = depot.getBuildingId();
        this.selectedVeicleType = selectedVeicleType;
    }

    public boolean validate(World world) {
        if (((Depot) world.getBuilding(depotid)).getOwner().getCash() < 4566) {
            System.err.println("Not enough money!:");
            return false;
        }
        return true;
    }

    @Override
    public void perform(World world) {
//            world.putVehicle(new Truck(player, selectedVeicleType, pos));
//            depotid.buildVehicle(selectedVeicleType);
        ((Depot) world.getBuilding(depotid)).getOwner().pay(4566);
        ((Depot) world.getBuilding(depotid)).buildVehicle(selectedVeicleType);
    }
}
