package tradeworld;

import java.io.Serializable;

/**
 *
 * @author Kotuc
 */
public interface GameAction extends Serializable {

    boolean validate(World world);

    void perform(World world);
}
