package hypergame.platformer;

import hypergame.Game;
import hypergame.GameBehavior;
import org.jbox2d.common.Vec2;
import robot.input.KeyboardDriving;

import java.util.ArrayList;
import java.util.List;

/**
 * Time loop behavior
 *
 * @author Kotuc
 */
public class TimeLoopBehavior extends GameBehavior {


    final List<TimeFrame> frames = new ArrayList<TimeFrame>();

    int frameid = 0;

    protected final Player player0;
    private final Player player2;
    private final KeyboardDriving keyboard;

    public TimeLoopBehavior(Game game, Player player0, Player player2) {
        super(game);
        keyboard = new KeyboardDriving();
        this.player0 = player0;
        this.player2 = player2;
    }


    @Override
    public void update(float timestep) {

        Vec2 goDir = keyboard.actPlayer();

        TimeFrame timeFrame = new TimeFrame(frameid, goDir, player0.body.getPosition());

        frames.add(timeFrame);

        player0.setTimeFrame(timeFrame);

        // paralelly
//        player2.setTimeFrame(timeFrame);

        { // later by 10 sec
            int histFrameid = frameid - 1000;
            if (0 <= histFrameid && histFrameid < frames.size()) {
                player2.setTimeFrame(frames.get(histFrameid));
            }
        }

        frameid++;
    }

}
