package tradeworld;

import java.io.Serializable;

/**
 *
 * @author Kotuc
 */
public interface GameAction extends Serializable {

     void perform(World world);

}
