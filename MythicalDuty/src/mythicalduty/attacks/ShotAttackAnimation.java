/*
 * ShotAttackAnimation.java
 *
 * Created on 25. èerven 2006, 13:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mythicalduty.attacks;

import kotuc.midp.*;

/**
 *
 * @author PC
 */
public class ShotAttackAnimation extends AttackAnimation {
    
    /** Creates a new instance of ShotAttackAnimation */
    public ShotAttackAnimation(Attack a) {
        super(a);
    }

//    public void finished() {
////        attack.target.getAnim().flashing = 10;//flashes 3 times
//        attack.proceed();
//    }    
//    
    public void act() {
        this.pos = attack.attacker.getPos().inter(attack.target.getPos(), (int)(MySystem.getTime()-startt), (int)(tout));
        pos.y-=20;
    }    
    
}
