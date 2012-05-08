package tradeworld.actions;

import tradeworld.PlayerId;
import tradeworld.World;
import tradeworld.war.Granade;
import tradeworld.war.TimePos3D;

/**
 *
 * @author Kotuc
 */
public class ThrowAction extends AbstractAction {

    private final PlayerId playerId;
    private final TimePos3D timepos;

    public ThrowAction(PlayerId pleyerId, TimePos3D timepos) {
        this.playerId = pleyerId;
        this.timepos = timepos;
    }

    public void perform(World world) {
        Granade granade = new Granade(world.getPworld(), timepos.position, timepos.velocity);
        world.getPworld().add(granade.getPobject());
        world.putObject(granade);
    }

    @Override
    public String toString() {
        return "GRANADE \t" + id + "\t" + playerId + "\t" + timepos;
    }
}
