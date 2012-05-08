package tradeworld.actions;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import tradeworld.ObjectId;
import tradeworld.Time;
import tradeworld.World;
import tradeworld.war.Soldier;

/**
 *
 * @author Kotuc
 */
public class UpdatePosAction extends AbstractAction {

    final ObjectId<Soldier> soldier;
    final Point3d pos;
    final Vector3d vector;

    public UpdatePosAction(Soldier sold, Point3d pos, Vector3d vector, Time time) {
        this.soldier = sold.getId();
        this.pos = pos;
        this.vector = vector;
        this.time = time;
    }

    public void perform(World world) {
        Soldier sold = world.getObject(soldier);
        sold.setPos(pos);
        sold.postime = time;
        sold.velocity = vector;
    }

    @Override
    public String toString() {
        return "update " +id+ " " + soldier + " " + pos + " + " + vector + " * " + time.getSeconds();
    }
}
