package mythicalduty.figures;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.*;

public class LevelUpAnimation extends Animation {
    Figure figure;
    
    LevelUpAnimation (Figure f) {
      
        this.figure = f;
        
        this.pos = figure.getPos();
        
        setTimeout(2000);
        
        image = Game.createImage("/figures/levelup.png");
    }

    public void act() {
        pos.y-=2;
    }
    
    public void finished() {
//        figure.anim.flashing = -1;
        
//        Game.fight.anims.addAnimation(new FloatingString("LEVEL UP", 0x00FFFFFF, figure.getPos()));
    }    
        
}
