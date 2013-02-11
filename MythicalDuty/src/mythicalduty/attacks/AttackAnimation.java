package mythicalduty.attacks;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.*;

public abstract class AttackAnimation extends Animation {
    protected Attack attack;
    
    AttackAnimation (Attack a) {
        this.attack = a;       
    }

    public void finished() {
        attack.proceed();
    }    
}

