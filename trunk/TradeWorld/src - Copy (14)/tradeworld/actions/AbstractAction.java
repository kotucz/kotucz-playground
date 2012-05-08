package tradeworld.actions;

import tradeworld.Time;
import tradeworld.World;
import tradeworld.multi.ClientId;

/**
 *
 * @author Kotuc
 */
public abstract class AbstractAction implements GameAction {

    protected Time time;
    public int id;
    public ClientId clientid;

    public boolean validate(World world) {
        return true;
    }

}
