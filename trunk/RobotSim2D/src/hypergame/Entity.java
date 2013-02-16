package hypergame;

import org.jbox2d.dynamics.Body;

import java.awt.*;

/**
 * @author Kotuc
 */
public class Entity {

    protected Game game;
    public Body body;
    public Color color = Color.gray;

    public Entity() {
    }

    public Entity(Body body) {
        this.body = body;
    }

    public void update(float timestep) {
    }




    //    final int scale = 200;
//    final int xoff = 350;
//    final int yoff = 450;
//    final int scale = 1;
//    final int xoff = 0;
//    final int yoff = 0;
//
//    Point toPoint(Vec2 vec) {
//        return new Point((int) (vec.x * scale) + xoff, (int) (-vec.y * scale) + yoff);
//    }


    public void paint(Displayer g) {
        g.setColor(color);
        g.paintBody(body);
    }
}
