package mythicalduty.figures;

import javax.microedition.lcdui.*;
import kotuc.midp.*;
import mythicalduty.*;

public class Nightmare extends EvilFigure {
//    Image frontImage = Game.createImage("/nightmarefront1.png");

    /** Creates a new instance of Figure */
    public Nightmare() {
//        anim.image = frontImage;
        anim.loadImages("/figures/nightmarefront", 2);
        
//        anim.setFrameSequence(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 });
        
//        addAttack(Attack.getFireball());
    }
               
}
	