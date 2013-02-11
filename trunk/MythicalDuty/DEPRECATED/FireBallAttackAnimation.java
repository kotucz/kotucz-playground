package mythicalduty.attacks;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.*;

public class FireBallAttackAnimation extends AttackAnimation {
   
    Pos startpos, endpos;
    
    FireBallAttackAnimation (Attack a) {
        super(a);
      
        startpos = attack.attacker.getPos();      
        
        endpos = attack.target.getPos();
        
        setTimeout(700);
        
        image = Game.createImage("/attacks/firearrowback0.png");
    }

    public void finished() {
//        attack.target.getAnim().flashing = 10;//flashes 3 times
        attack.proceed();
        Game.fight.addAnim(new Bang(attack.target.getPos()));
    }    
    
    public void act() {
        this.pos = startpos.inter(endpos, (int)(MySystem.getTime()-startt), (int)(tout));
        pos.y-=20;
    }    
}
