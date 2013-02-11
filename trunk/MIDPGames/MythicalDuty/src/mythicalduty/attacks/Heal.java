package mythicalduty.attacks;

import java.lang.*;
import kotuc.midp.*;
import mythicalduty.*;
import mythicalduty.figures.*;

public class Heal extends Attack {
    
    public Heal () {
//        super(f);
        targetType = SINGLE|Figure.GOOD;
        name = "Heal";
    }
 
    public void animate() {
        Game.fight.addAnim(new HealAnimation(this));    
    }
    
    protected void calculate() {
        damageToEnemy = -40;
        experienceBonus = 100; 
        energyUsed = 8000;
    }
    
}
	