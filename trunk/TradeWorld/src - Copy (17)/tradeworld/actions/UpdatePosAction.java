package tradeworld.actions;

import tradeworld.ObjectId;
import tradeworld.World;
import tradeworld.war.Soldier;
import tradeworld.war.TimePos3D;

/**
 *
 * @author Kotuc
 */
public class UpdatePosAction extends AbstractAction {

    final ObjectId<Soldier> soldierid;
    final TimePos3D timepos;

    public UpdatePosAction(Soldier sold, TimePos3D timepos) {
        this.soldierid = sold.getId();
        this.timepos = timepos;
    }

    public void perform(World world) {
        Soldier sold = world.getObject(soldierid);

        if (timepos.time.passed(sold.timepos.time)) { // only least recent action update no back in time
            sold.timepos = this.timepos;
        }

//        sold.setPos(pos);
//        sold.postime = time;
//        sold.velocity = vector;
    }

    @Override
    public String toString() {
        return "UPDATE\t" +id+ "\t" + soldierid +"\t"+ timepos.time;//.getSeconds();
    }
}
