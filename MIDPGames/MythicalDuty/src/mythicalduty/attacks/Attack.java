/*
 * Attack.java
 *
 * Created on 14. kvìten 2006, 14:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mythicalduty.attacks;

import java.lang.*;
import kotuc.midp.*;
import mythicalduty.*;
import mythicalduty.figures.Figure;

/**
 *
 * @author PC
 */
public abstract class Attack/* implements java.lang.Runnable*/ {
    
    public Figure attacker, target;
    
    public String name;
    
    /** Creates a new instance of Attack */
/*    public Attack(Figure attacker, Figure target) {
        this.attacker = attacker;
        this.target = target;
        
        this.attack();
    }
*/    
    protected Attack() {
    }

    /*   
    protected Attack(Figure atkr) {
        this.attacker = atkr;
    }
  */  
    public boolean isPossible() {
        try {
            return (!animating)&&attacker.alive()&&target.alive()&&attacker.ready();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean animating = false;
    
    public void attack() {
        if (!isPossible()) return;
        attacker.ready = false;
        //        new Thread(this).start();
        attacker.energy=0;
        animate();
        animating = (Game.slowFightModeEnabled);//  true;
    }
    
    public abstract void animate() ;
//        AttackAnimation aanim;
//        Game.fight.addAnim(aanim = new AttackAnimation(this));
//    }
    
    void proceed() {
        
// TODO  if (Game.slowFightModeEnabled) 
        animating = false;
               
        calculate();
        
        if (damageToEnemy<0) target.heal(-damageToEnemy);
        else {
            target.damage(damageToEnemy);
        }
        attacker.addExp(experienceBonus);
        attacker.energyNeeded = energyUsed;
        
        attacker.ready = true;
                
    }
   
    // fight stats
    protected int damageToEnemy, experienceBonus, energyUsed;
    
    // with attacker and target computes fight stats 
    protected void calculate() {
        damageToEnemy = attacker.skilllevel*1;
        experienceBonus = damageToEnemy*target.skilllevel; 
        energyUsed = 10000;
    } 
    
/*  public void run() {
        // TODO calculate injuries, bonuses and so on
        
        AttackAnimation aanim;
        
        Game.fight.addAnimation(aanim = new AttackAnimation(this));
        
        while (aanim.alive) Thread.yield();
        
        target.damage(5);
        attacker.addExp(60);
    }
    */
    // bits must not colide with Figure.EVIL/GOOD
    public final static int SINGLE = 4, ALL = 8;
    // ored combination of Figure.EVIL/GOOD and SINGLE/ALL
    int targetType = SINGLE|Figure.EVIL;
   
    public int getTargetType() {
        return targetType;
    }
    
    public Figure[] targets;// to do ()
    public int targetId;
    
    public void nextTarget() {
        if (targetId<targets.length-1) targetId++;
        while ((!targets[targetId].alive())&&(targetId<targets.length-1)) targetId++;
        while ((!targets[targetId].alive())&&(targetId>0)) targetId--;
        target = targets[targetId];
    }

    public void prevTarget() {
        if (targetId>0) targetId--;
        while ((!targets[targetId].alive())&&(targetId>0)) targetId--;
        while ((!targets[targetId].alive())&&(targetId<targets.length-1)) targetId++;
        target = targets[targetId];
    }
    
    public boolean useable(Figure f) {
        if (f.alive()&&f.side!=attacker.side) return true; else return false;
    }
    
    public static Attack getAttack(int id) {
        return null;
    }

    public void setTarget(Figure target) {
        if (!attacker.ready) {
            MySystem.error("Attack.setTarget() error cannot set target while already attacking");
            return;
        }
        this.target = target;
    }
    
    public static Attack getFireball() {
//        return new FireBall();
        ShotAttack fb = new ShotAttack();
        fb.name = "fireball";
        fb.setImages("fire", 1, 3);
        return fb;
    }
    
    public static Attack getHeal() {
        return new Heal();
    }

    public static Attack getMudSpit() {
        ShotAttack ms = new ShotAttack(); 
        ms.name = "mud spit";
        ms.setImages("mud", 1, 1);
        return ms;
    }
    
}





