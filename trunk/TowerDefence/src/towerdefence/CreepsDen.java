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
public class CreepsDen extends Unit {

    public CreepsDen() {
        img = getImage("creepsden1_32.png");
    }
    
    private double reloading = 0.02;
    
    @Override
    public void doRound() {
        if (Math.random()<reloading) {
//        if (reloading>400) {
            Creep creep = (Creep)Templates.CREEP1.clone();
            creep.setPos(new Point3d(pos));
            board.put(creep);
//            reloading = 0;
            reloading+=0.0001;
        } else {
//            reloading++;
        }
    }

    
    
}
