package mythicalduty.figures;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.*;
import mythicalduty.attacks.*;

public class Hero extends GoodFigure {

   
    public Hero() {
    
        getAnim().loadImages("/figures/heroback", 2);
        
        addAttack(Attack.getFireball());
        addAttack(Attack.getMudSpit());
        addAttack(Attack.getHeal());
        
//      load(1);
               
        
    }
    
    

}
	