/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefence;

import javax.vecmath.Point3d;

/**
 *
 * @author Kotuc
 */
public class Creep extends Unit {

    public Creep() {
        this.speed = 50.0;
    }

    @Override
    public void doRound() {
        if (Math.random()<0.01) {
        for (Unit unit : board.getUnits()) {
            if (unit instanceof Tower) {
                if (dist(unit) < 20) {
                    unit.health -= 40;
                    if (unit.health<=0) {
                        unit.destroy();
                    }
                    this.destroy();
                    break;
                }
            }
        }
        } else {
            goTo(new Point3d(600, 240, 0));
        }
    }

    @Override
    public String toString() {
        return "Creep " + super.toString();
    }
}
