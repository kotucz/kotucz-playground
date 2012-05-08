package tradeworld.actions;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.ObjectId;
import tradeworld.Time;
import tradeworld.World;
import tradeworld.war.Bullet;
import tradeworld.war.Soldier;

/**
 *
 * @author Kotuc
 */
public class ShootAction extends AbstractAction {

    private final ObjectId<Soldier> soldier;
    private final Vector3d vector;
    private final Point3d pos;

    public ShootAction(Soldier soldier, Point3d pos, Time when, Vector3d vector) {
        this.soldier = soldier.getId();
        this.pos = pos;
        this.time = when;
        this.vector = vector;
    }

    public void perform(World world) {
        Bullet bullet = new Bullet(world.getObject(soldier), pos, vector, time);
        world.putObject(bullet);
    }

    @Override
    public String toString() {
        return "shoot "+soldier+" "+pos+" "+vector+" "+time;
    }



}
