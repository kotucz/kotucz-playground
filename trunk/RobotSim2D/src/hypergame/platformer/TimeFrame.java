package hypergame.platformer;

import org.jbox2d.common.Vec2;

/**
 * @author Kotuc
 */
public class TimeFrame {

    final int frame;

    final Vec2 goVec;

    final Vec2 pos;

    public TimeFrame(int frame, Vec2 goVec, Vec2 pos) {
        this.frame = frame;
        this.goVec = goVec;
        this.pos = pos;
    }
}
