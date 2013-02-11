package mythicalduty.attacks;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.*;

public class Bang extends Animation {

    Bang () {   
    }
      
    Bang (Pos p) {
        this.pos = p;
        pos.y -= 20; 
        
        setTimeout(500);
        
        loadImages("/attacks/firebang", 3);
        
    }
    
    public void act() {
        setFrame(images.length*(int)(MySystem.getTime()-startt)/(int)(tout));
    }
}
	