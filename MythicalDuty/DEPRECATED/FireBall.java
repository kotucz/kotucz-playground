package mythicalduty.attacks;

import java.lang.*;
import kotuc.midp.*;
import mythicalduty.*;
import mythicalduty.figures.*;

public class FireBall extends Attack {
    
    public FireBall () {
        targetType = SINGLE|Figure.EVIL;
        name = "Fireball";
    }
    
/*    public FireBall (Figure f) {
        super(f);
        targetType = SINGLE|Figure.EVIL;
        name = "Fireball";
    }*/
 
    public void animate() {
        Game.fight.addAnim(new FireBallAttackAnimation(this));    
    }
    
    protected void calculate() {
        int tw = (target.weak())?1:0;
        int aw = (target.weak())?1:0;
        damageToEnemy = (MySystem.random(5+3*tw-2*aw+attacker.skilllevel-target.skilllevel)==0)?(0):(attacker.skilllevel*2+MySystem.random(6)+3*tw);
        experienceBonus = 10*(damageToEnemy);//target.skilllevel; 
        energyUsed = 4000+500*MySystem.random(4);
    }
    
}
	