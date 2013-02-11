package mythicalduty.attacks;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.*;

public class HealAnimation extends AttackAnimation {
   
    HealAnimation (Attack a) {
        super(a);
        this.pos = a.target.getPos();
        pos.y-=75;
        
        setTimeout(1000);
        
        loadImages("/attacks/healshower", 2);
        
        setFrameSequence(new int[] {0, 0, 0, 0 ,0, 1, 1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1});
    }
    
    public void act() {
        nextFrame();
        this.pos.y+=3;
    }
    
}

