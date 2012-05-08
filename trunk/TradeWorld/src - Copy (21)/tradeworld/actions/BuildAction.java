package tradeworld.actions;

import javax.vecmath.Point3d;
import tradeworld.Building;
import tradeworld.Building.Type;
import tradeworld.Depot;
import tradeworld.Factory;
import tradeworld.Player;
import tradeworld.PlayerId;
import tradeworld.Storage;
import tradeworld.World;

/**
 *
 * @author Kotuc
 */
public class BuildAction extends AbstractAction {

    private static final long serialVersionUID = 5781753891L;
    private Point3d center;
    private Building.Type selectedBuildingType;
    private PlayerId playerid;

    public BuildAction(Point3d center, Type selectedBuildingType, Player player) {
        this.center = center;
        this.selectedBuildingType = selectedBuildingType;
        this.playerid = player.getId();
    }

    @Override
    public boolean validate(World world) {
        if (world.getGame().getPlayer(playerid).getCash() < selectedBuildingType.getPrice()) {
            System.err.println("Not enough money!:");
            return false;
        }
        return true;
    }

    public void perform(World world) {
        Player player = world.getGame().getPlayer(playerid);
//            SuperPanel panel;
        switch (selectedBuildingType) {
            case LUMBERJACK_CAMP:
            case FURNITURE_FACTORY:
            case WOODMILL:
                final Factory fact = new Factory(
                        selectedBuildingType, player, center);
                world.putBuilding(fact);

                for (Building building : world.getBuildings()) {
                    if ((building instanceof Storage) && (fact.isInRange(building))) {
                        fact.addStorage((Storage) building);
                    }
                }
//                    panel = fact.getPanel();
//                    showPanel(panel);
                break;
            case TRUCK_DEPOT1:
                final Depot depo = new Depot(
                        selectedBuildingType, player, center);
                world.putBuilding(depo);

                for (Building building : world.getBuildings()) {
                    if ((building instanceof Storage) && (depo.isInRange(building))) {
                        depo.addStorage((Storage) building);
                    }
                }
                Point3d point = depo.getEntrance();

                try {
                    int enx = (int) Math.round(point.x);
                    int eny = (int) Math.round(point.y);
                    world.getRoadNetwork().addPoint(enx, eny);
                } catch (Exception eff) {
                    System.err.println("EEEEEEEEEE" + eff.getMessage());
                }

//                                    panel = fact.getPanel();
//                                    showPanel(panel);
                break;
            case STORAGE:
                final Storage stor = new Storage(
                        player, center, 111);
                world.putBuilding(stor);

                for (Building building : world.getBuildings()) {
                    if (stor.isInRange(building)) {
                        if (stor.getOwner().equals(building.getOwner())) {
                            if (building instanceof Factory) {
                                ((Factory) building).addStorage(stor);
                            }
                            if (building instanceof Depot) {
                                ((Depot) building).addStorage(stor);
                            }
                        }
                    }
                }
//                    panel = stor.getPanel();
//                    showPanel(panel);
                break;
        }
        long price = selectedBuildingType.getPrice();
        player.pay(price);

    }
}
