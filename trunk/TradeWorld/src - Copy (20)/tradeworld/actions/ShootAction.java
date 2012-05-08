package tradeworld.actions;

import tradeworld.ObjectId;
import tradeworld.PlayerId;
import tradeworld.World;
import tradeworld.war.Bullet;
import tradeworld.war.TimePos3D;

/**
 *
 * @author Kotuc
 */
public class ShootAction extends AbstractAction {

    private final PlayerId playerid;
    private final TimePos3D timepos;

    public ShootAction(PlayerId playerid, TimePos3D timepos) {
        this.playerid = playerid;
        this.timepos = timepos;
    }

    public void perform(World world) {
        Bullet bullet = new Bullet(playerid, timepos);
        world.putObject(bullet);
    }

    @Override
    public String toString() {
        return "SHOOT \t" + id + "\t" + playerid + "\t" + timepos;
    }
}
