package tradeworld.actions;

import tradeworld.Identifiable;
import tradeworld.ObjectId;
import tradeworld.World;
import tradeworld.WorldObject;

/**
 *
 * @author Kotuc
 */
public class RemoveObject extends AbstractAction {

    private ObjectId<? extends WorldObject> id;

    public RemoveObject(Identifiable<? extends WorldObject> object) {
        this.id = object.getId();
    }

    public void perform(World world) {
        world.remove(world.getObject(id));
    }
}
