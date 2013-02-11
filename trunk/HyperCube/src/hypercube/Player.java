package hypercube;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Kotuc
 */
public class Player extends Entity {

    int time;
    private TimeLine timeline = new TimeLine();

    public Player(Room room) {
        super(room);
    }

    void addAction(int time, TimeAction action) {
        timeline.addAction(time, action);
    }

    @Override
    public void act() {
        for (TimeAction action : timeline.getActions(time)) {
            action.execute(this);
        }
        time++;
        super.act();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.drawString("" + time, x + 5, y + 15);
    }

    @Override
    Player createCopy() {
        final Player copy = new Player(this.room);
        copy.timeline = this.timeline;
        copy.x = this.x;
        copy.y = this.y;
        copy.color = this.color;

//        copy.actions = this.actions; // the timeline is equal
        return copy;

    }
}


