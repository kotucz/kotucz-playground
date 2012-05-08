package tradeworld.war;

import tradeworld.PlayerId;
import tradeworld.Time;
import tradeworld.World;
import tradeworld.WorldObject;
import tradeworld.actions.ShootAction;
import tradeworld.actions.ThrowAction;

/**
 *
 * @author Kotuc
 */
public class Gun extends WorldObject {

    private PlayerId playerid;
    private Time nextShoot;

    public void fire(TimePos3D timepos) {
        if (nextShoot == null || timepos.time.passed(nextShoot)) {
//            shoot(dir, when);
//            this.nextShoot = when.addSec(0.1);
            granade(timepos);
            this.nextShoot = timepos.time.addSec(3);
        }
    }

    private void shoot(TimePos3D timepos) {
        getWorld().getGame().sendToAll(new ShootAction(playerid, timepos));
//        playSound("gunshot.wav");
    }

    private void granade(TimePos3D timepos) {
        getWorld().getGame().sendToAll(new ThrowAction(playerid, timepos));
    }

}
