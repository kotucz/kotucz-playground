package tradeworld.graphics;

import tradeworld.war.Soldier;

/**
 *
 * @author Kotuc
 */
public class Soldier3D extends Model3D {

    private final Soldier soldier;

    public Soldier3D(Soldier soldier) {
        this.soldier = soldier;
        addChild(Tools.superBox(1, 1, 2, "sold-duck1.png"));
    }

    @Override
    public void refresh() {
       setPos(soldier.getPos(getWorld3D().getCurrentTime()), 0);
//        System.out.println("ref "+soldier.getPos());
    }
}
