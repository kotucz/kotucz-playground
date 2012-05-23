package hypergame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Kotuc
 */
public class ScoringArea extends Entity {

    Team team;
    Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, 0.35f, 0.35f);
    boolean bonus = false;

    public ScoringArea(Team team) {
        this.team = team;
    }

    boolean contains(Pawn pawn) {
        Vec2 position = pawn.body.getPosition();
        // TODO implement
        return false;
    }

    @Override
    void paint(Graphics g) {
        switch (team) {
            case RED:
                g.setColor(Color.RED);
                g.setColor(new Color(1f, 0.8f, 0.8f));
                break;
            case BLUE:
                g.setColor(Color.BLUE);
                g.setColor(new Color(0.8f, 0.8f, 1f));
                break;
            default:
                g.setColor(Color.GRAY);
        }
        Point xy = toPoint(new Vec2(rect.x, rect.y));
//        Point xy = toPoint(new Vec2(rect.w, rect.getMaxX()));
        g.fillRect(xy.x, xy.y, (int) (rect.width * scale), (int) (rect.height * scale));
        g.setColor(Color.BLACK);
        g.drawRect(xy.x, xy.y, (int) (rect.width * scale), (int) (rect.height * scale));
    }
}
