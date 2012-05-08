package tradeworld.actions;

import tradeworld.ObjectId;
import tradeworld.World;
import tradeworld.war.Bullet;
import tradeworld.war.Soldier;
import tradeworld.war.TimePos3D;

/**
 *
 * @author Kotuc
 */
public class ShootAction extends AbstractAction {

    private final ObjectId<Soldier> soldier;
    private final TimePos3D timepos;

    public ShootAction(Soldier soldier, TimePos3D timepos) {
        this.soldier = soldier.getId();
        this.timepos = timepos;
    }

    public void perform(World world) {
        Bullet bullet = new Bullet(world.getObject(soldier), timepos);
        world.putObject(bullet);
    }

    @Override
    public String toString() {
        return "SHOOT \t" + id + "\t" + soldier + "\t" + timepos;
    }
}
