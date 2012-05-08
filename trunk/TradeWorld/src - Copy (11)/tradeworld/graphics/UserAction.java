package tradeworld.graphics;

import javax.vecmath.Point3d;
import tradeworld.GameAction;

public abstract class UserAction implements GameAction {
    
    public void update(Point3d point) {}

    public void cancel() {}
}
