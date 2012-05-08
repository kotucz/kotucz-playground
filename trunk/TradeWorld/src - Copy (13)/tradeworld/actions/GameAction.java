package tradeworld.actions;

import java.io.Serializable;
import tradeworld.World;

/**
 *
 * @author Kotuc
 */
public interface GameAction extends Serializable {

    boolean validate(World world);

    void perform(World world);
}
