package hypergame;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import java.awt.*;

/**
 * @author Kotuc
 */
public abstract class Displayer {

    public abstract void setColor(Color green);

    public abstract void drawLine(Vec2 pos, Vec2 end);

    public abstract void drawVector(Vec2 pos, Vec2 end);

    public abstract void paintBody(Body body);
}
