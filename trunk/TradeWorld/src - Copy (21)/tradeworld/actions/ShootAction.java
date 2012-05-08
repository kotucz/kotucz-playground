package tradeworld.actions;

import tradeworld.PlayerId;
import tradeworld.World;
import tradeworld.war.Laser;
import tradeworld.war.TimePos3D;

/**
 *
 * @author Kotuc
 */
public class ShootAction extends AbstractAction {

    private final PlayerId playerid;
    private final TimePos3D timepos;

    public ShootAction(PlayerId playerid, TimePos3D timepos) {
        if (playerid==null) {
            throw new NullPointerException("playerid");
        }
        this.playerid = playerid;
        this.timepos = timepos;
    }

    public void perform(World world) {
//        Bullet bullet = new Bullet(playerid, timepos);
//        world.putObject(bullet);
        Laser laser = new Laser(playerid, timepos);
        world.putObject(laser);
    }

    @Override
    public String toString() {
        return "SHOOT \t" + id + "\t" + playerid + "\t" + timepos;
    }
}
