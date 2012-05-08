/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefence;

import java.awt.Graphics;

/**
 *
 * @author Kotuc
 */
public class Tower extends Unit {

    protected Tower() {
        
    }
    
    int reload = 0;
    
    @Override
    public void doRound() {
        reload++;
        if (reload > Templates.BULLET1.buildTime) {

            for (Unit unit : board.getUnits()) {
                if (unit instanceof Creep) {
                    if (unit.fakeHealth > 0) {
                        if (dist(unit) < shootRange) {
                            Bullet bullet = (Bullet)Templates.BULLET1.clone();
                            bullet.setParent(this);
                            bullet.setTarget(unit);
                            board.put(bullet);
                            reload = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
//        g.drawOval((int)(pos.x-shootRange), (int)(pos.y-shootRange), (int)(2*shootRange), (int)(2*shootRange));
        super.paint(g);
    }

    
    
    @Override
    public String toString() {
        return "Tower " + super.toString();
    }
}
