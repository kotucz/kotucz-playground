package tradeworld.graphics;

import javax.vecmath.Point3d;

public abstract class Action {

    public abstract void perform();

    public abstract void update(Point3d point);

    public abstract void cancel();
}
