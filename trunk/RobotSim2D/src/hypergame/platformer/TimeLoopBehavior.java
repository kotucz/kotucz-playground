package hypergame.platformer;

import hypergame.Game;
import hypergame.GameBehavior;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Time loop behavior
 *
 * @author Kotuc
 */
public class TimeLoopBehavior extends GameBehavior {


    final List<TimeFrame> frames = new ArrayList<TimeFrame>();

    public TimeLoopBehavior(Game game) {
        super(game);
    }



    @Override
    public void update(float timestep) {

    }

}
