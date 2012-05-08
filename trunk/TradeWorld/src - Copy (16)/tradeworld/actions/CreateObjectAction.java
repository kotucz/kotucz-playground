package tradeworld.actions;

import tradeworld.World;
import tradeworld.WorldObject;

/**
 *
 * @author Kotuc
 */
public class CreateObjectAction extends AbstractAction {

    private final WorldObject object;

    public CreateObjectAction(WorldObject object) {
        this.object = object;
    }
        
    public void perform(World world) {        
//        Soldier soldier = (Soldier)object;
//        soldier.id = new ObjectId<Soldier>(soldier, this.id); // action id is unique
        world.putObject(object);
    }
}
