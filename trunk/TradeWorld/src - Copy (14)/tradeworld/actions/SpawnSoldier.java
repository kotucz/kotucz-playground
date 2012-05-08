package tradeworld.actions;

import javax.vecmath.Point3d;
import tradeworld.ObjectId;
import tradeworld.World;
import tradeworld.war.Soldier;

public class SpawnSoldier extends AbstractAction {

    private Point3d pos;

    public SpawnSoldier(Point3d pos) {
        this.pos = pos;
    }
    
    @Override
    public void perform(World world) {
        Soldier soldier = new Soldier(pos);
        soldier.id = new ObjectId<Soldier>(soldier, this.id); // action id is unique
        soldier.clientid = clientid;
        world.putObject(soldier);        
    }
}
