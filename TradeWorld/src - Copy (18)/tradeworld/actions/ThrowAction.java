package tradeworld.actions;

import tradeworld.ObjectId;
import tradeworld.World;
import tradeworld.war.Granade;
import tradeworld.war.Soldier;
import tradeworld.war.TimePos3D;

/**
 *
 * @author Kotuc
 */
public class ThrowAction extends AbstractAction {

    private final ObjectId<Soldier> soldier;
    private final TimePos3D timepos;

    public ThrowAction(Soldier soldier, TimePos3D timepos) {
        this.soldier = soldier.getId();
        this.timepos = timepos;
    }

    public void perform(World world) {
        Granade granade = new Granade(world.getPworld(), timepos.position, timepos.velocity);
        world.getPworld().add(granade.getPobject());
        world.putObject(granade);
    }

    @Override
    public String toString() {
        return "GRANADE \t" + id + "\t" + soldier + "\t" + timepos;
    }
}
